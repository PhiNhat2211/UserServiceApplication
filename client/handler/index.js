var form = document.getElementById("form");

document.getElementById("form").addEventListener("submit", function (e) {
  e.preventDefault();

  var username = document.getElementById("username").value;
  var password = document.getElementById("password").value;
  var check = document.getElementById("check");

  fetch("http://localhost:8080/auth/login", {
    method: "POST",
    body: JSON.stringify({
      username: username,
      password: password,
    }),
    headers: {
      "Content-Type": "application/json; charset=UTF-8",
    },
  })
    .then(function (response) {
      return response.json();
    })
    .then(function (data) {
      console.log(data);
      if (!data.token) {
        alert(data.message);
      } else {
        localStorage.setItem("token", data.token);
          localStorage.setItem("username", data.username);
          localStorage.setItem("id", data.id);
          localStorage.setItem("role", data.role);
          localStorage.setItem("email", data.email);
          window.location.href = "home.html";
      }
    });
});
