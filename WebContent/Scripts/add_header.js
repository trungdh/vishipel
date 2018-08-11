// ******************************************************* add_header

function add_header ()
  {
    if ( document.getElementById )
      {
      var header = document.getElementById ( 'header' );

      if ( header )
        {
    	  var request = new XMLHttpRequest();

    	  let lang = getCookie("lang");
    	  if (lang == 'vi') {
    		  request.open ( 'GET', "PageContents/header_contents_vi.txt");
    	  } else {
    		  request.open ( 'GET', "PageContents/header_contents.txt");  
    	  }    	  
    	  
    	  request.setRequestHeader ( 'Content-Type', 'text/html' );
    	  request.onload = handlerHeader;
    	  request.send ();
        }
      }  
  }

function handlerHeader() {
	var header = document.getElementById ( 'header' );
	
  if(this.status == 200 && this.response != null) {
    // success!
	  document.title = "Vietnam Maritime Communication and Electronics LLC";
	  var header_contents = this.response;
	  header_contents = header_contents.replace ('{{SiteLogoTarget}}','index.html' );
	  header_contents = header_contents.replace ('{{SiteLogo}}','Images/SiteLogo.png' );
	  header_contents = header_contents.replace ('{{PageHeader}}','CÔNG TY THHH MTV THÔNG TIN ĐIỆN TỬ HÀNG HẢI VIỆT NAM' );
	  header_contents = header_contents.replace ('{{PageSubHeader}}','Vietnam Maritime Communication and Electronics LLC' );
	  header_contents = header_contents.replace ('{{PageSubHeader}}','Vietnam Maritime Communication and Electronics LLC' );
	  
	  if (getCookie("access_token") == "") {
		  header_contents = header_contents.replace ('{{SiteMenu}}','display: none' );
		  header_contents = header_contents.replace ('{{TitleLang}}','display: block' );
		  header_contents = header_contents.replace ('{{UserInfo}}','display: none' );
		} else {
			const userCode = getCookie("customer_code");
			header_contents = header_contents.replace ('{{SiteMenu}}','display: block' );
			header_contents = header_contents.replace ('{{TitleLang}}','display: none' );
			header_contents = header_contents.replace ('{{UserInfo}}','display: block' );
			header_contents = header_contents.replace ('{{UserCode}}', userCode);
			
			if (document.getElementById('header_customer')) {
	    		document.getElementById('header_customer').style.color = "#FFFFFF";	
	    	}
	    	if (document.getElementById('header_invoice')) {
	    		document.getElementById('header_invoice').style.color = "#E91D24";
	    	}
	    	if (document.getElementById('header_sms')) {
	    		document.getElementById('header_sms').style.color = "#FFFFFF";
	    	}
		}
    if(header && header.outerHTML) {
    	header.outerHTML = header_contents	
	}
  } else {

  }
}