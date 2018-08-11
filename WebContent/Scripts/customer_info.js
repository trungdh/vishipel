function loadInfo() {
	var customer_code = getCookie('customer_code');
	var access_token = getCookie('access_token');
	if (access_token) {
		$.ajax({
	        type: 'GET',
	        url: '/Vishipel/rest/customer?customer_code=' + customer_code,
	        headers: { 
	        	'Content-Type':'application/json',
	        	'access_token':access_token
	        	},
	        success: function(response){
	        	$('#MAKH').html(response.data.MAKH);
	        	$('#TENKH').html(response.data.TENKH);
	        	$('#DIACHI').html(response.data.DIACHI);
	        	$('#DIENTHOAI').html(response.data.DIENTHOAI);
	        	$('#FAX').html(response.data.FAX);
	        	$('#EMAIL').html(response.data.EMAIL);
	        	
	        },
	        error : function(response){
	            console.log(response);
	            setCookie("email", "", 7);
	        	setCookie("customer_code", "", 7);
	        	setCookie("access_token", "", 7);
	            go_news();
	        }
	      });	
	} else {
		go_news();
	}
    
}