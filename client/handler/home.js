var id = localStorage.getItem("id");
var username = localStorage.getItem("username");
var role = localStorage.getItem("role").toLowerCase();
var token = localStorage.getItem("token");
var btnLogout = document.getElementById("btnLogout");

let popup = document.getElementById("popup");
let popupUpdate = document.getElementById("popupUpdate");
let btnDelete = document.getElementById("btnDelete");
let btnUpdate = document.getElementById("btnUpdate");
let btnProfile = document.getElementById("btnProfile");

//POPUP DELETE BUTTON
function openPopup() {
  popup.classList.add("open-popup");
}

function closePopup() {
  popup.classList.remove("open-popup");
}

//POPUP UPDATE BUTTON
function openPopupUpdate() {
  popupUpdate.classList.add("open-popupUpdate");
}

function closePopupUpdate() {
  popupUpdate.classList.remove("open-popupUpdate");
}

if (role) {
  document.getElementById("btnProfile").disabled = false;
  if (role == "role_admin") {
    document.getElementById("btnDelete").disabled = false;
    document.getElementById("btnUpdate").disabled = false;
  }
}

//ON DETELE
function deleteUser() {
  var txtDelete = document.getElementById("txtDelete").value;
  fetch("http://localhost:8080/api/admin/" + `${txtDelete}`, {
    method: "DELETE",
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
      window.location.href = "home.html";
    });
}

//ON UPDATE
function updateRole() {
  var txtId = document.getElementById("txtId").value;
  var selectRole = document.getElementById("selectRole").value;

  fetch("http://localhost:8080/api/admin/" + `${txtId}/` + `${selectRole}/`, {
    method: "PUT",
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
      window.location.href = "home.html";
    });
}

function logout() {
  localStorage.clear();
  window.location.href = "index.html";
}

//GET TABLE DATA
fetch("http://localhost:8080/api/users", {
  method: "GET",
  headers: {
    "Content-Type": "application/json; charset=UTF-8",
    Authorization: "Bearer " + token,
  },
})
  .then((data) => {
    return data.json();
  })
  .then((objectData) => {
    let tableData = "";

    objectData.map((values) => {
      tableData += `<tr>
            <td>${values.id}</td>
            <td>${values.username}</td>
            <td>${values.email}</td>
            <td>${JSON.stringify(values.roles)}</td>
          </tr>`;
    });
    document.getElementById("table_body").innerHTML = tableData;

    var Wellcome = `${username} - ${role}`;
    document.getElementById("identify").innerHTML = Wellcome;

    if (token) {
      var Wellcome = "Logout";
      document.getElementById("btnLogoutContent").innerHTML = Wellcome;
    }
  });

//SEARCH FUNCTION
function searchFunction() {
  var input, filter, table, tr, td, i, txtValue;
  input = document.getElementById("searchInput");
  filter = input.value.toUpperCase();
  table = document.getElementById("myTable");
  tr = table.getElementsByTagName("tr");

  for (i = 0; i < tr.length; i++) {
    td = tr[i].getElementsByTagName("td")[1];
    if (td) {
      txtValue = td.textContent || td.innerText;
      if (txtValue.toUpperCase().indexOf(filter) > -1) {
        tr[i].style.display = "";
      } else {
        tr[i].style.display = "none";
      }
    }
  }
}
