//switch to register page
function signUp() {
    //show the elements that were hidden when sign up button is clicked
    document.getElementById("login").setAttribute("hidden", "true");
    document.getElementById("new account").setAttribute("hidden", "true");
    document.getElementById("register").removeAttribute("hidden");
    document.getElementById("confirm-pass").removeAttribute("hidden");
    document.getElementById("email").removeAttribute("hidden");
    document.getElementById("age-check").removeAttribute("hidden");
    document.getElementById("back").removeAttribute("hidden");
    $("u-label[id*='-label']").attr("hidden", "true");
}
//to call the signUp function when sign up button is clicked as well as the back button
$(document).ready(function () {
    document.getElementById("new account").onclick = function () {
        signUp();
    }

    document.getElementById("back").onclick = function () {
        goBack();
    }
});
//switch back to login page
function goBack() {
    document.getElementById("login").removeAttribute("hidden");
    document.getElementById("new account").removeAttribute("hidden");
    document.getElementById("register").setAttribute("hidden", "true");
    document.getElementById("confirm-pass").setAttribute("hidden", "true");
    document.getElementById("email").setAttribute("hidden", "true");
    document.getElementById("age-check").setAttribute("hidden", "true");
    document.getElementById("back").setAttribute("hidden", "true");
    $("u-label[id*='-label']").attr("hidden", "true");
}

function register() {
    var usernameRegex = /^[a-zA-Z0-9=^+@,._]+$/;
    var username = document.getElementById("name").value;
    var validUsername = usernameRegex.exec(username);

    //variables used to check the validity of the password and email
    var password = document.getElementById("pass").value;
    var confirmPass = document.getElementById("confirm-pass").value;
    var passwordRegex = /^[a-zA-Z0-9=^+@,._ ]+$/;
    var validPassword = passwordRegex.exec(password);
    var email = document.getElementById("email").value;
    var err = false;

    //hide labels
    $("u-label[id*='-label']").attr("hidden", "true");

    //check age
    if (!document.getElementById("age-check").value) {
        document.getElementById("age-label").removeAttribute("hidden");
        err = true;
    }

    //check username and password. If it's not valid, show the message. otherwise, it stays hidden
    if (username.length < 5) {
        document.getElementById("name-len-label").removeAttribute("hidden");
        err = true;
    }

    if (password.length < 5) {
        document.getElementById("pass-len-label").removeAttribute("hidden");
        err = true;
    }

    if (!validUsername) {
        document.getElementById("name-chars-label1").removeAttribute("hidden");
        document.getElementById("name-chars-label2").removeAttribute("hidden");
        err = true;
    }

    if (!validPassword) {
        document.getElementById("pass-chars-label1").removeAttribute("hidden");
        document.getElementById("pass-chars-label2").removeAttribute("hidden");
        err = true;
    }

    //check if the two entries of password matches each other
    if (!(password.localeCompare(confirmPass) === 0)) {
        document.getElementById("pass-no-match-label").removeAttribute("hidden");
        err = true;
    }

    if (email === "") {
        document.getElementById("no-email-label").removeAttribute("hidden");
        err = true;
    }

    if (err) return;

    //sent data to the server and get the callback message
    var xhttp = new XMLHttpRequest();
    var url = "/register";
    var urlForm = `username=${username}&password=${password}&email=${email}`;

    xhttp.onreadystatechange = function () {
        var status = xhttp.status;
        var response = xhttp.response;

        console.log(response);

        if (status === 403) {
            document.getElementById("signup-fail-label").removeAttribute("hidden");
        } else if (status === 200) {
            goBack();
            document.getElementById("signup-success-label").removeAttribute("hidden");
        }
    }
    xhttp.open('POST', url, true);
    xhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhttp.send(urlForm);
}
$(document).ready(function () {
    document.getElementById("register").onclick = function () {
        register();
    }
});