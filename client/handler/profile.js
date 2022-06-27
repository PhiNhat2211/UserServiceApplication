var form = document.getElementById("form");
var id = localStorage.getItem("id");
var username = localStorage.getItem("username");
var role = localStorage.getItem("role");
var token = localStorage.getItem("token");
var email = localStorage.getItem("email");

if (role) {
  document.getElementById("btnHome").disabled = false;
  document.getElementById("btnUpdate").disabled = false;
}

if (token) {
  var Wellcome = "Logout";
  document.getElementById("btnLogoutContent").innerHTML = Wellcome;

  var lblUsername = username
  document.getElementById("lblUsername").innerHTML = lblUsername;

  var lblEmail = email
  document.getElementById("lblEmail").innerHTML = lblEmail;
  
}

function logout() {
  localStorage.clear();
  window.location.href = "index.html";
}

document.getElementById("form").addEventListener("submit", function (e) {
  e.preventDefault();

  var username = document.getElementById("username").value;
  var email = document.getElementById("email").value;
  var password = document.getElementById("password").value;

  fetch("http://localhost:8080/api/user/" + `${id}`, {
    method: "PUT",
    body: JSON.stringify({
      username: username,
      email: email,
      password: password,
    }),
    headers: {
      "Content-Type": "application/json; charset=UTF-8",
      Authorization: "Bearer " + token,
    },
  })
    .then(function (response) {
      return response.json();
    })
    .then(function (data) {
      console.log(data);
      alert(data.message);
      if (data.message == "Account updated successfully") {
        localStorage.clear();
        window.location.href = "index.html";
        alert("Please login again");
      }
    });
});
