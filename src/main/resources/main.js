"use strict";

/* TODO
- ujednolic sposob zapytan do servera ajax -> fetch
*/

function viewModel(courses, students) {
  self = this;
  self.courses = ko.mapping.fromJS(courses);
  self.students = ko.mapping.fromJS(students);
  self.studentInfo = ko.observable();
  self.grades = ko.mapping.fromJS([]);

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

  self.addGrade = async function () {
    console.log("add grade");
    let location;
    let url =
      "http://localhost:1234/students/" +
      self.studentInfo().split(",")[0] +
      "/grades";

    let grade = {
      course: ko.toJS(self.courses()[0]),
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
    let info;
    info =
      student.index() +
      ", " +
      student.firstName() +
      ", " +
      student.lastName() +
      ", " +
      student.birthday();

    self.studentInfo(info);

    let url = "http://localhost:1234/students/" + student.index() + "/grades";

    $.ajax({
      url: url,
      dataType: "json",
    }).then((data) => {
      console.log(data);
      ko.mapping.fromJS(data, self.grades);
    });
    return true;
  };
}

let courses = [],
  students = [];

$.ajax({
  url: "http://localhost:1234/courses",
  dataType: "json",
}).then((data) => {
  courses = data;
  $.ajax({
    url: "http://localhost:1234/students",
    dataType: "json",
  }).then((data) => {
    students = data;

    var vm = new viewModel(courses, students);

    var subscription = vm.courses.subscribe(function (newValue) {
      console.log("subskrypcja");
      console.log(newValue);
    });
    ko.applyBindings(vm);
  });
});

function getDate() {
  let date = new Date();
  let day = date.getDay();
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
//});

/*
async function getData(url) {
    let response = await fetch(url, {
        headers: {
            accept: 'application/json'
        }
    });
    if (response.status == 200) {
        console.log(response)
        let json = await response.json();
        return json;
    }
    throw new Error(response.status);
}

let x = getData("http://localhost:1234/students").then(data => console.log(data)).catch(alert);
*/
