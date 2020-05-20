function login() {
     //to get the username and password
     var userName = document.getElementById("name").value;
     var pswrd = document.getElementById("pass").value;

     if (userName == "" && pswrd == "") {
          document.getElementById("no-name-pass-label").removeAttribute("hidden");
          return;
     } else {
          document.getElementById("no-name-pass-label").setAttribute("hidden", "true");
     }

     //variables used to send data to the endpoint
     var xhttp = new XMLHttpRequest();
     var url = "/login";
     var urlForm = `username=${userName}&password=${pswrd}`;
     //to get the feedback about if signing up is successful
     xhttp.onreadystatechange = function () {
          var status = xhttp.status;
          if (status === 403) {
               document.getElementById("name-label5").removeAttribute("hidden");
          } else if (status === 200) {
               document.getElementById("name-label6").removeAttribute("hidden");
          }
     }
     xhttp.open('POST', url, true);
     xhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
     xhttp.send(urlForm);
}
//to call the login function when login button is clicked
$(document).ready(function () {
     document.getElementById("login").onclick = function () {
          login();
     }
});




