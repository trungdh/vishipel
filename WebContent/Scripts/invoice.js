function loadAccessToken() {
	var access_token = getCookie('access_token');
	if (access_token) {
	} else {
		go_news();
	}
}

function closePrint () {
  document.body.removeChild(this.__container__);
}

function setPrint () {
  this.contentWindow.__container__ = this;
  this.contentWindow.onbeforeunload = closePrint;
  this.contentWindow.onafterprint = closePrint;
  this.contentWindow.focus(); // Required for IE
  this.contentWindow.print();
}

function handlePrint() {
	var oHiddFrame = document.createElement("iframe");
	oHiddFrame.onload = setPrint;
	oHiddFrame.style.visibility = "hidden";
	oHiddFrame.style.position = "fixed";
	oHiddFrame.style.right = "0";
	oHiddFrame.style.bottom = "0";
	oHiddFrame.src = _PDF_URL;
 	document.body.appendChild(oHiddFrame);
}

function handlePDF() {
	var a = document.createElement('A');
	a.href = _PDF_URL;
	a.download = _PDF_URL.substr(_PDF_URL.lastIndexOf('/') + 1);
	document.body.appendChild(a);
	a.click();
	document.body.removeChild(a);
}

var _PDF_URL = '';
function handleReport() {
	document.getElementById("invoice-content").style.display = "none";
	document.getElementById("invoice-error").innerHTML = "";
	var time = $('#input-time').val();	
	var currMonth = String(time).split("-")[0];
	var currYear = String(time).split("-")[1];
	
	var customer_code = getCookie('customer_code');
	var access_token = getCookie('access_token');

	let lang = getCookie("lang");
	if (time == "") {
		if (lang == 'vi') {
			alert("Vui lòng chọn tháng/năm để tra cứu!");
		} else {
			alert("Please select month/year to invoice!");
		}
		return;		
	}
	
	if (access_token) {
		
		var request = new XMLHttpRequest();
		request.open("GET", '/Vishipel/rest/postage?customer=' + customer_code + "&type=1&month=" + currMonth + "&year=" + currYear, true); 
		request.responseType = "blob";
		request.setRequestHeader("access_token",access_token);
		request.onload = function (e) {
			document.getElementById("invoice-content").style.display = "block";
			if (lang == 'en') {
	        	document.getElementById("titleInvoice").innerHTML = "Invoices " + time;
			} else {
				document.getElementById("titleInvoice").innerHTML = "Bảng kê tổng hợp cước tháng " + time;
			}
			$('#loading-image').hide();
		    if (this.status === 200) {
		    	if (this.response.size !== 0) {
		    		var blob = new Blob([this.response], {type: 'application/pdf'});
			        var file = window.URL.createObjectURL(blob);
			       
					console.log(file);
			        _PDF_URL = file;
			     
			        if (lang == 'en') {
			        	document.getElementById("viewInvoice").text= "View";				        
				        document.getElementById("downInvoice").text = "Download";
			    	} else {
			    		document.getElementById("viewInvoice").text= "Xem";				        
				        document.getElementById("downInvoice").text = "Tải về";
			    	}			        
			        
			        document.getElementById("viewInvoice").href=file;
			        
			        document.getElementById("downInvoice").href = file;		  
			        document.getElementById("downInvoice").download = file.substr(file.lastIndexOf('/') + 1);			       
			        
			        //renderPDF(file, document.getElementById('viewer-pdf'));
		    	} else {		    		
		    		document.getElementById("viewInvoice").text= "-";
			        document.getElementById("downInvoice").text = "-";
		    	}
		        
		    } else {		    	
		    	if (lang == 'en') {		    				    		
		    		document.getElementById("invoice-error").innerHTML = "Get invoices error";	    			
		    	} else {
		    		document.getElementById("invoice-error").innerHTML = "Lấy dữ liệu tra cứu lỗi";
		    	}		    	
		    }
		};
		request.send();	
		
		var request2 = new XMLHttpRequest();
		request2.open("GET", '/Vishipel/rest/postage?customer=' + customer_code + "&type=2&month=" + currMonth + "&year=" + currYear, true); 
		request2.responseType = "blob";
		request2.setRequestHeader("access_token",access_token);
		request2.onload = function (e) {
			document.getElementById("invoice-content").style.display = "block";
			if (lang == 'en') {
	        	document.getElementById("titleDetailInvoice").innerHTML = "Detail invoices " + time;
			} else {
				document.getElementById("titleDetailInvoice").innerHTML = "Bảng kê chi tiết cước tháng " + time;
			}
			$('#loading-image').hide();
		    if (this.status === 200) {
		    	if (this.response.size !== 0) {
			        var blob = new Blob([this.response], {type: 'application/pdf'});
			        var file = window.URL.createObjectURL(blob);
			       
					console.log(file);
			        _PDF_URL = file;
			     
			        if (lang == 'en') {
			        	document.getElementById("viewDetailInvoice").text= "View";				        
				        document.getElementById("downDetailInvoice").text = "Download";
			    	} else {
			    		document.getElementById("viewDetailInvoice").text= "Xem";				        
				        document.getElementById("downDetailInvoice").text = "Tải về";
			    	}
			        
			        document.getElementById("viewDetailInvoice").href=file;
			        document.getElementById("downDetailInvoice").href = file;	  
			        document.getElementById("downDetailInvoice").download = file.substr(file.lastIndexOf('/') + 1);			        
			        
			        //renderPDF(file, document.getElementById('viewer-pdf'));
		    	} else {		    		
		    		document.getElementById("viewDetailInvoice").text = "-";
			        document.getElementById("downDetailInvoice").text = "-";
		    	}
		    } else {
		    	if (lang == 'en') {
		    		document.getElementById("invoice-error").innerHTML = "Get invoices error";	    			
		    	} else {
		    		document.getElementById("invoice-error").innerHTML = "Lấy dữ liệu tra cứu lỗi";
		    	}
		    }
		};
		request2.send();	
		
		
		$('#loading-image').show();
	} else {
		go_news();
	}	 	
}

function renderPDF(url, canvasContainer, options) {
    var options = options || { scale: 1 };
    
    while (canvasContainer.hasChildNodes()) {
    	canvasContainer.removeChild(canvasContainer.childNodes[0]);
    }
        
    function renderPage(page) {
        var viewport = page.getViewport(options.scale);
        var canvas = document.createElement('canvas');
        var ctx = canvas.getContext('2d');
        var renderContext = {
          canvasContext: ctx,
          viewport: viewport
        };
        
        canvas.height = viewport.height;
        canvas.width = viewport.width;
        canvasContainer.appendChild(canvas);
        
        page.render(renderContext);
    }
    
    function renderPages(pdfDoc) {
        for(var num = 1; num <= pdfDoc.numPages; num++)
            pdfDoc.getPage(num).then(renderPage);
        
        $('#group-download').show();
    }
    PDFJS.disableWorker = true;
    PDFJS.getDocument(url).then(renderPages);
}


