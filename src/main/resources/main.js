"use strict";

/* TODO
- ujednolic sposob zapytan do servera ajax -> fetch
- pozbyc sie duplikacji w subscribe
*/

function getDate() {
  let date = new Date();
  let day = date.getDate();
  let month = date.getMonth() + 1;
  let year = date.getFullYear();

  day = day < 10 ? "0" + day : day;
  month = month < 10 ? "0" + month : month;

  return year + "-" + month + "-" + day;
}

async function getData(url) {
  let response = await fetch(url, {
    method: "GET",
    headers: {
      accept: "application/json",
    },
  });

  if (response.status == 200) {
    return await response.json();
  }

  throw new Error(response.status);
}

async function postData(url, data) {
  let response = await fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
    body: JSON.stringify(data),
  });

  if (response.status == 201) {
    return response.headers.get("Location");
  }

  throw new Error(response.status);
}

function event(a) {
  let data = ko.mapping.toJS(this);

  let url = "http://localhost:1234" + data.link[0].href;
  $.ajax({
    type: "PUT",
    url: url,
    data: JSON.stringify(data),
    contentType: "application/json",
    dataType: "json",
  }).then((data) => {
    console.log("updated");
  });
  console.log("Change !!");
}
/******************************************************
 *
 * VIEW MODEL
 */

function viewModel() {
  self = this;

  self.courses = ko.observableArray();
  self.students = ko.observableArray();
  self.grades = ko.observableArray();

  self.studentInfo = ko.observable();

  self.filterCourseName = ko.observable("");
  self.filterCourseLecturer = ko.observable("");
  self.filterFirstName = ko.observable("");
  self.filterLastName = ko.observable("");


  self.filterCourseName.subscribe(function(courseName){
    let lecturer = "lecturer=" + self.filterCourseLecturer();
    let course = "name=" + courseName;

    let url = "http://localhost:1234/courses?" + course + "&" + lecturer;

    getData(url).then((data) => {
      self.courses.removeAll();
      data.forEach((course) => {
        self.courses.push(ko.mapping.fromJS(course));
      });
    });
  });

  self.filterCourseLecturer.subscribe(function(lecturerName){
    let course = "name=" + self.filterCourseName();
    let lecturer = "lecturer=" + lecturerName;

    let url = "http://localhost:1234/courses?" + course + "&" + lecturer;

    getData(url).then((data) => {
      self.courses.removeAll();
      data.forEach((course) => {
        self.courses.push(ko.mapping.fromJS(course));
      });
    });
  });

  self.filterFirstName.subscribe(function(firstNameVal){
    let firstName = "firstName=" + firstNameVal;
    let lastName  = "lastName=" + self.filterLastName();

    let url = "http://localhost:1234/students?" + firstName + "&" + lastName;

    getData(url).then((data) => {
      self.students.removeAll();
      data.forEach((student) => {
        self.students.push(ko.mapping.fromJS(student));
      });
    });
  });

  self.filterLastName.subscribe(function(lastNameVal){
      let firstName  = "firstName=" + self.filterFirstName();
      let lastName = "lastName=" + lastNameVal;

      let url = "http://localhost:1234/students?" + firstName + "&" + lastName;

      getData(url).then((data) => {
        self.students.removeAll();
        data.forEach((student) => {
          self.students.push(ko.mapping.fromJS(student));
        });
      });
    });

  self.coursesSub = new Map();
  self.studentsSub = new Map();
  self.gradesSub = new Map();
  /******************************************************
   *
   * SUBSCRIBE
   */

  self.courses.subscribe(
    function (changes) {
      changes.forEach(function (change) {
        if (change.status === "added") {
          console.log("new course sub !!");

          let sub = ko
            .computed(function () {
              return ko.toJSON(change.value);
            })
            .subscribe(event.bind(change.value));
          self.coursesSub.set(change.value.id(), sub);
        } else if (change.status === "deleted") {
          console.log("deleted  course sub !!");

          let sub = self.coursesSub.get(change.value.id());
          sub.dispose();
          self.coursesSub.delete(change.value.id());
        }
      });
    },
    null,
    "arrayChange"
  );

  self.students.subscribe(
    function (changes) {
      changes.forEach(function (change) {
        if (change.status === "added") {
          console.log("new student sub !!");

          let sub = ko
            .computed(function () {
              return ko.toJSON(change.value);
            })
            .subscribe(event.bind(change.value));
          self.studentsSub.set(change.value.index(), sub);
        } else if (change.status === "deleted") {
          console.log("deleted student sub !!");

          let sub = self.studentsSub.get(change.value.index());
          sub.dispose();
          self.studentsSub.delete(change.value.index());
        }
      });
    },
    null,
    "arrayChange"
  );

  self.grades.subscribe(
    function (changes) {
      changes.forEach(function (change) {
        if (change.status === "added") {
          console.log("new grade sub !!");

          let sub = ko
            .computed(function () {
              return ko.toJSON(change.value);
            })
            .subscribe(event.bind(change.value));
          self.gradesSub.set(change.value.id(), sub);
        } else if (change.status === "deleted") {
          console.log("deleted grade sub !!");
          let sub = self.gradesSub.get(change.value.id());
          sub.dispose();
          self.gradesSub.delete(change.value.id());
        }
      });
    },
    null,
    "arrayChange"
  );

  /******************************************************
   *
   * COURSES
   */

  self.removeCourse = function (course) {
    console.log("removed course:");
    console.log(ko.toJS(course));

    let url = "http://localhost:1234/courses/" + course.id();

    $.ajax({
      type: "DELETE",
      url: url,
      contentType: "application/json",
      dataType: "json",
    }).then((data) => {
      self.courses.remove(course);
    });
  };

  self.addCourse = async function () {
    console.log("add course");
    let location;
    let url = "http://localhost:1234/courses";

    let course = {
      name: "new course",
      lecturer: "lecturer",
    };

    location = await postData(url, course);
    console.log(location);
    course = await getData(location);

    self.courses.push(ko.mapping.fromJS(course));
  };

  /******************************************************
   *
   * STUDENTS
   */

  self.removeStudent = function (student) {
    console.log("removed student");
    console.log(ko.toJS(student));

    let url = "http://localhost:1234/students/" + student.index();
    $.ajax({
      type: "DELETE",
      url: url,
      contentType: "application/json",
      dataType: "json",
    }).then((data) => {
      self.students.remove(student);
    });
  };

  self.addStudent = async function () {
    console.log("add student");
    let location;
    let url = "http://localhost:1234/students";

    let student = {
      firstName: "name",
      lastName: "lastname",
      birthday: getDate(),
    };

    location = await postData(url, student);
    console.log(location);
    student = await getData(location);

    self.students.push(ko.mapping.fromJS(student));
  };

  /******************************************************
   *
   * GRADES
   */
  self.addGrade = async function () {
    console.log("add grade");
    let location;
    let url =
      "http://localhost:1234/students/" +
      self.studentInfo().split(",")[0] +
      "/grades";
    /*
    var unmapped = ko.mapping.toJS(self.courses()[0]);
    console.log(unmapped);
*/
    let grade = {
      course: {
        id: self.courses()[0].id(),
        name: self.courses()[0].name(),
        lecturer: self.courses()[0].lecturer(),
      },
      date: getDate(),
      value: 5,
    };

    location = await postData(url, grade);
    console.log(location);
    grade = await getData(location);

    self.grades.push(ko.mapping.fromJS(grade));
  };

  self.removeGrade = async function (grade) {
    console.log("remove grade");
    console.log(ko.toJS(grade));

    let url =
      "http://localhost:1234/students/" +
      self.studentInfo().split(",")[0] +
      "/grades/" +
      grade.id();

    $.ajax({
      type: "DELETE",
      url: url,
      contentType: "application/json",
      dataType: "json",
    }).then((data) => {
      self.grades.remove(grade);
    });
  };

  self.showGrades = function (student) {
    let info =
      student.index() +
      ", " +
      student.firstName() +
      ", " +
      student.lastName() +
      ", " +
      student.birthday();

    self.studentInfo(info);

    let url = "http://localhost:1234/students/" + student.index() + "/grades";

    getData(url).then((data) => {
      self.grades.removeAll();
      data.forEach((grade) => {
        self.grades.push(ko.mapping.fromJS(grade));
      });
    });

    return true;
  };
}

let vm = new viewModel();

getData("http://localhost:1234/courses").then((data) => {
  data.forEach((course) => {
    vm.courses.push(ko.mapping.fromJS(course));
  });
});

getData("http://localhost:1234/students").then((data) => {
  data.forEach((student) => {
    vm.students.push(ko.mapping.fromJS(student));
  });
});

ko.applyBindings(vm);
