// ******************************************************* add_footer

function add_footer ()
  {

  if ( document.getElementById )
    {
    var footer = document.getElementById ( 'footer' );

    if ( footer )
      {
    	var request = new XMLHttpRequest();
    	let lang = getCookie("lang");
	  	  if (lang == 'vi') {
	  		  request.open ( 'GET', "PageContents/footer_contents_vi.txt");
	  	  } else {
	  		  request.open ( 'GET', "PageContents/footer_contents.txt");  
	  	  }    	  
	  	  request.setRequestHeader ( 'Content-Type', 'text/html' );
	  	  request.onload = handlerFooter;
	  	  request.send ();
      }
    }  
  }

function handlerFooter() {
	var footer = document.getElementById ( 'footer' );
  if(this.status == 200 && this.response != null) {
    // success!
	  var footer_contents = this.response;
//	  footer_contents = footer_contents.replace ('{{FooterTitle}}','VIET NAM MARITIME COMMUNICATION AND ELECTRONICS LLC' );
//	  footer_contents = footer_contents.replace ('{{FooterAddr}}','Add: No2 Nguyen Thuong Hien, Minh Khai Ward, Hong Bang District, Hai Phong City, VietNam' );
//	  footer_contents = footer_contents.replace ('{{FooterContactStrong}}','Customercare:' );
//	  footer_contents = footer_contents.replace ('{{FooterContact}}','Tel: +84-(0)-225-3569330 *Fax: +84-(0)-225-3842073 *Email: customercare@vishipel.com.vn' );
	  
	  if(footer && footer.outerHTML) {
		  footer.outerHTML = footer_contents	
		}
  } else {

  }
}
