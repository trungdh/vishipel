function add_body() {
	let lang = getCookie("lang"); 
	if (lang == "") {
		setCookie("lang", 'en', 7);
	}
	
	if (getCookie("access_token") == "") {		
		go_news();		
	} else {
		go_invoice();		
	}
}

function loadUserMenu() {
	if (getCookie("access_token") == "") {
		if (document.getElementById('header-menu-content')) {
			document.getElementById('header-menu-content').style.display = "none";	
		}		
	} else {
		if (document.getElementById('header-menu-content')) {
			document.getElementById('header-menu-content').style.display = "block";	
		}
	}
} 

function change_language () {
	let lang = getCookie("lang");
	if (lang == 'en') {
		setCookie("lang", 'vi', 7);
	} else {
		setCookie("lang", 'en', 7);
	}
	
	location.reload();
}

function go_customer_info () {
	let url = "PageContents/customer_info.txt";
	let lang = getCookie("lang");
	if (lang == 'en') {
		url = "PageContents/customer_info.txt";
	} else {
		url = "PageContents/customer_info_vi.txt";
	}
	
	if (document.getElementById('header_customer')) {
		document.getElementById('header_customer').style.color = "#E91D24";	
	}
	if (document.getElementById('header_invoice')) {
		document.getElementById('header_invoice').style.color = "#FFFFFF";	
	}
	if (document.getElementById('header_sms')) {
		document.getElementById('header_sms').style.color = "#FFFFFF";
	}

	$("#content").load(url, function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success")
            console.log("Load customer info success");
        	loadInfo();        	
        if(statusTxt == "error")
        	console.log("Load customer fail: " + xhr.status + ": " + xhr.statusText);
    });
	
}
	
function go_invoice () {
	
	let url = "PageContents/invoice.txt";
	let lang = getCookie("lang");
	if (lang == 'en') {
		url = "PageContents/invoice.txt";
	} else {
		url = "PageContents/invoice_vi.txt";
	}
	
	if (document.getElementById('header_customer')) {
		document.getElementById('header_customer').style.color = "#FFFFFF";	
	}
	if (document.getElementById('header_invoice')) {
		document.getElementById('header_invoice').style.color = "#E91D24";
	}
	if (document.getElementById('header_sms')) {
		document.getElementById('header_sms').style.color = "#FFFFFF";
	}
	
	$("#content").load(url, function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success") {
        	console.log("Load invoice success");
	        $('#loading-image').hide();
	        loadAccessToken();	        
        } else {
        	console.log("Error: " + xhr.status + ": " + xhr.statusText);
        }
            
    });
}

function go_sat_sms () {
	if ( document.getElementById )
    {
    var content = document.getElementById ( 'content' );
    if (document.getElementById('header_customer')) {
		document.getElementById('header_customer').style.color = "#FFFFFF";	
	}
	if (document.getElementById('header_invoice')) {
		document.getElementById('header_invoice').style.color = "#FFFFFF";
	}
	if (document.getElementById('header_sms')) {
		document.getElementById('header_sms').style.color = "#E91D24";
	}
	
	//location.replace("https://google.com");

//    if ( content )
//      {
//    	var request = new createXMLHTTPObject();
//
//	   	  request.open ( 'GET', "PageContents/sat_sms.txt");
//	   	  request.setRequestHeader ( 'Content-Type', 'text/html' );
//	   	  request.onload = handlerIndex;
//	   	  request.send ();
//      }
    }
}

function go_news () {
	let url = "PageContents/login_news.txt";
	let lang = getCookie("lang");
	if (lang == 'en') {
		url = "PageContents/login_news.txt";
	} else {
		url = "PageContents/login_news_vi.txt";
	}
	$("#content").load(url, function(responseTxt, statusTxt, xhr){
        if(statusTxt == "success")
        	console.log("Load news success");
        	$('#loading-image').hide();
        	onLoadLogin();
        if(statusTxt == "error")
        	console.log("Error: " + xhr.status + ": " + xhr.statusText);
    });
}


function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function checkCookie() {
    var user = getCookie("username");
    if (user != "") {
        alert("Welcome again " + user);
    } else {
        user = prompt("Please enter your name:", "");
        if (user != "" && user != null) {
            setCookie("username", user, 365);
        }
    }
}