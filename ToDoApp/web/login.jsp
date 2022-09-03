<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<style>
.btn {
  border: none;
  background-color: inherit;
  padding: 14px 28px;
  font-size: 16px;
  cursor: pointer;
  display: inline-block;
}

.btn:hover {background: #eee;}

</style>
</head>
<body>
  
    <form id="loginForm">
  
        <h3>Enter Your details</h3>
  		USERNAME : <input type="text" name="username" id="u"/><br><br>
  		PASSWORD : <input type="password" name="password" id="p" /><br><br>
  		<div class="btn" onclick="doLogin()">Submit</div></form>
    <script>
    function doLogin(){
    	const xhttp = new XMLHttpRequest();
    	xhttp.onload = function() {
    		let list = [];
			list = JSON.parse(this.responseText);
			console.log(list);
			const d = new Date();
			d.setTime(d.getTime() + (1 * 24 * 60 * 60 * 1000));
			let expires = "expires="+d.toUTCString();
			document.cookie = "userid" + "=" + list.userid + ";" + expires;
			let x = document.cookie;
			console.log(x);
			window.location.replace('index.html');
    	}
    	var user = document.querySelector("#u").value;
    	var pass =document.querySelector("#p").value;
    	xhttp.open("POST", "login?username="+user+"&password="+pass, true);
    //	const FD = new FormData(document.querySelector("#loginForm"));
    	xhttp.send();
    }
    </script>
</body>
</html>