 function login(){
            var userName = document.getElementById("name").value;
            var pswrd = document.getElementById("pass").value;
        
            if(userName == "" && pswrd == ""){
                document.getElementById("name-label4").removeAttribute("hidden");
                return;
             }
             var xhttp = new XMLHttpRequest();
             var url = "/login";
             
             var urlForm = `username=${userName}&password=${pswrd}`;
             xhttp.onreadystatechange = function(){
                 var status = xhttp.status;
                 if(status === 403){
                         
                    
                          document.getElementById("name-label5").removeAttribute("hidden");
             }else if(status === 200){
                    
                          document.getElementById("name-label6").removeAttribute("hidden");
                        
                 }
             }
             xhttp.open('POST', url, true);
 
             xhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
   
             xhttp.send(urlForm);

     
    
        }
        $( document ).ready(function(){
            document.getElementById("login").onclick = function(){
            login();
            }
        });
         

//        function signUp(){
//            document.getElementById("login").setAttribute("hidden", "true");
//            document.getElementById("new account").setAttribute("hidden", "true");
//            document.getElementById("register").removeAttribute("hidden");
//            document.getElementById("confirm-pass").removeAttribute("hidden");
//            document.getElementById("email").removeAttribute("hidden");
//        }
//
//        function register(){
//            var usernameRegex = /^[a-zA-Z0-9=^+@,._]+$/;
//            var username = document.getElementById("name").value;
//            var validUsername = usernameRegex.exec(username);
//
//            if(username.length >= 5){
//                document.getElementById("name-label1").setAttribute("hidden", "true");
//            }else{
//                document.getElementById("name-label1").removeAttribute("hidden");
//            }
//
//            if(validUsername){
//                document.getElementById("name-label2").setAttribute("hidden", "true");
//                document.getElementById("name-label3").setAttribute("hidden", "true");
//            }else{
//                document.getElementById("name-label2").removeAttribute("hidden");
//                document.getElementById("name-label3").removeAttribute("hidden");
//            }
//
//            //check validation of password
//            var password = document.getElementById("pass").value;
//            var confirmPass = document.getElementById("confirm-pass").value;
//            var validPassword = usernameRegex.exec(password);
//            var email = document.getElementById("email").value
//
//            if(password.length >= 5){
//                document.getElementById("pass-label1").setAttribute("hidden", "true");
//            }else{
//                document.getElementById("pass-label1").removeAttribute("hidden");
//            }
//            if(validPassword){
//                document.getElementById("pass-label2").setAttribute("hidden", "true");
//                document.getElementById("pass-label3").setAttribute("hidden", "true");
//            }else{
//                document.getElementById("pass-label2").removeAttribute("hidden");
//                document.getElementById("pass-label3").removeAttribute("hidden");
//            }
//            if (password.localeCompare(confirmPass) == 0) {
//                document.getElementById("pass-label4").setAttribute("hidden", "true");
//            }else{
//                document.getElementById("pass-label4").removeAttribute("hidden");
//            }
//
//         
//        }
