<%@ page contentType = "text/html; charset=UTF-8" %>

<html>
   <head>  	  
      <title>main page</title>
      
      <script src="scripts/jquery-3.3.1.min.js"></script>
   	  <script src="scripts/bootstrap.js"></script>
   	
   	<script>  
   	  $(document).ready(function() {
   		//TODO Day2 針對訊息內容作對應的消毒
		var msg = "${requestScope.msg}";
		if (!(msg === "")) {			
			alert(msg);
			//TODO Day2 針對 msg 內容作 HTML 消毒
			$('body').append(msg);
		}
	  });
   	 </script>
            
   </head>
   
   <body>
   
	   	<form>
	   		<!-- TODO Day2 針對 account,name 內容作 HTML消毒  -->	   		
	  		帳號 : ${sessionScope.account} <br/>
	  		姓名 : ${sessionScope.name} <br/>	  		
	   		<input type="button" onclick="location.href='edit.jsp'" value="修改"/> 
	   		<input type="button" onclick="location.href='logout'" value="登出"></input>
		</form>
		
		SQL Debug Info:${requestScope.sql} <br/>
		 
   </body>
    
</html>