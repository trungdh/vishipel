function onLoadLogin() {
	if (getCookie("access_token") == "") {
		$('.login-page').show();
	} else {
		$('.login-page').hide();
	}
}

function loadNews() {
	$.ajax({
        type: 'GET',
        url: '/Vishipel/rest/news/all',
        headers: { 'Content-Type':'application/json'},
        success: function(response){
        	var ul1 = document.getElementById("news-type1");
        	var ul2 = document.getElementById("news-type2");
        	
        	if (response.length > 0) {
        		for (var i = 0; i < response.length; i++) {
        			var li = document.createElement("li");
        			var a = document.createElement("a");
        			a.appendChild(document.createTextNode(response[i].TITLE));
        			a.href = response[i].LINK;
        			li.appendChild(a);
					if (response[i].LOAITIN == "1") {
						ul1.appendChild(li);
					} else if (response[i].LOAITIN == "2") {
						ul2.appendChild(li);
					}
				}
        	}        	
        },
        error : function(response){
            console.log(response);
        }
      });
}

function onLogout() {
	setCookie("email", "", 0);
	setCookie("customer_code", "", 0);
	setCookie("access_token", "", 0);	
	location.reload();
}

function onLogin() {
	var username = $('#user_login').val();
	if (username == "") { // if username variable is empty
       $('#errormess').html ('Please Insert Your Username'); // printing error message
       return false; // stop the script
    }
    var password = $("#pass_login").val(); // define password variable
    if (password == "") { // if password variable is empty
       $('#errormess').html ('Please Insert Your Password'); // printing error message
       return false; // stop the script
    }
    
    $('#loading-image').show();
    $.ajax({
        type: 'POST',
        url: '/Vishipel/rest/user/authenticate',
        headers: { 'Content-Type':'application/json',
            'email' : username,
            'password': password,
            'type':'systems'},
        success: function(response){
        	setCookie("email", response.data.EMAIL.toString(), 7);
        	setCookie("customer_code", response.data.MAKH.toString(), 7);
        	setCookie("access_token", response.message.toString(), 7);
        	$('#loading-image').hide();
        	add_header();
        	go_invoice();
        },
        error : function(response){
            console.log(response);
            alert("Your email or password is incorrect");
            $('#loading-image').hide();
        }
      });
}

//Login facebook

function loginFB(token) {
	$('#loading-image').show();
	$.ajax({
	    type: 'POST',
	    url: '/Vishipel/rest/user/authenticate',
	    headers: { 'Content-Type':'application/json',
	        'accessToken': token,
	        'type':'facebook'},
	    success: function(response){
	    	setCookie("email", response.data.EMAIL.toString(), 7);
	    	setCookie("customer_code", response.data.MAKH.toString(), 7);
	    	setCookie("access_token", response.message.toString(), 7);
	    	
	    	$('#loading-image').hide();
	    	add_header();
	    	go_invoice();
	    },
	    error : function(response){
	        console.log(response);
	        alert("Your email not exist in system. Please make sure social email is correctly email which registered in Vishipel system");
	        $('#loading-image').hide();
	    }
	});
}

function statusChangeCallback(response) {
	
	if(response.status === 'connected'){
		console.log(response.authResponse.accessToken);
		loginFB(response.authResponse.accessToken);
		//getUserInfo();
    } else {
    	console.log('you are not logged in to Facebook');
    	FB.login(function(response) {
    		  console.log(response);
    		  if (response.status === 'connected') {
    			  loginFB(response.authResponse.accessToken);
    		  }
    	}, {scope: 'public_profile,email'});
    	
    }
}

function getUserInfo() {
	FB.api('/me', {fields: 'name, email'}, function(response) {
		console.log('Successful login for: ' + response.name + ' - ' + response.email);
    });
}

// This function is called when someone finishes with the Login
// Button.  See the onlogin handler attached to it in the sample
// code below.
function checkLoginState() {
  FB.getLoginStatus(function(response) {
    statusChangeCallback(response);
  });
}

window.fbAsyncInit = function() {
  FB.init({
    appId      : '312004822553994',
    cookie     : true,  // enable cookies to allow the server to access 
                        // the session
    xfbml      : true,  // parse social plugins on this page
    version    : 'v2.8' // use graph api version 2.8
  });

};

// Load the SDK asynchronously
(function(d, s, id) {
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "https://connect.facebook.net/en_US/sdk.js";
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

//=======================
//Login GG

function ggInit() {
	$('#loading-image').show();
    gapi.load('auth2', function() {
        auth2 = gapi.auth2.init({
            client_id: '123186829247-v8fhokg2paacqve9r78p42l5fimsvjvj.apps.googleusercontent.com',
            fetch_basic_profile: false,
            scope: 'profile'
        });
        gapi.signin2.render("btn-google", {
                scope: 'email',
                width: 131,
                height: 34,
                longtitle: false,
                theme: 'dark',
                onsuccess: onSignIn,
                onfailure: onLoadError
            });
    });
}

function onLoadError() {
	console.log("Loading google login failed");
  	$('#loading-image').hide();
}

function onSignIn(googleUser) {
      // Useful data for your client-side scripts:
  var profile = googleUser.getBasicProfile();
  var email = googleUser.w3.U3;
  
  // The ID token you need to pass to your backend:
  var id_token = googleUser.getAuthResponse().id_token;
  $('#loading-image').show();
  $.ajax({
      type: 'POST',
      url: '/Vishipel/rest/user/authenticate',
      headers: { 'Content-Type':'application/json',
          'email': email,
          'type':'google'},
      success: function(response){
      	setCookie("email", response.data.EMAIL.toString(), 7);
      	setCookie("customer_code", response.data.MAKH.toString(), 7);
      	setCookie("access_token", response.message.toString(), 7);
      	
      	$('#loading-image').hide();
      	add_header();
      	go_invoice();
      },
      error : function(response){
          console.log(response);
          alert("Your email not exist in system. Please make sure social email is correctly email which registered in Vishipel system");
          $('#loading-image').hide();
      }
    });
};

function signOut() {
  var auth2 = gapi.auth2.getAuthInstance();
  auth2.signOut().then(function () {
    console.log('User signed out.');
  });
}