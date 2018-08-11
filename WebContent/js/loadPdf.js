function getPdf(){
	var request = new XMLHttpRequest();
	request.open("GET", "/path/to/pdf", true); 
	request.responseType = "blob";
	request.onload = function (e) {
	    if (this.status === 200) {
	        // `blob` response
	        console.log(this.response);
	        // create `objectURL` of `this.response` : `.pdf` as `Blob`
	        var file = window.URL.createObjectURL(this.response);
	        var a = document.createElement("a");
	        a.href = file;
	        a.download = this.response.name || "detailPDF";
	        document.body.appendChild(a);
	        a.click();
	        // remove `a` following `Save As` dialog, 
	        // `window` regains `focus`
	        window.onfocus = function () {                     
	          document.body.removeChild(a)
	        }
	    };
	};
	request.send();
}

