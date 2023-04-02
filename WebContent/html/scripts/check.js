var Check = {};

Check.getLogin = function(){
	var login = document.getElementById("lg").value;
	
	return login; 
};

Check.handleRequest = function(request){
	document.getElementById("mainform").style.display = "none";

	var res = request.responseText;
	
	if (res == "0")
		document.getElementById("result").appendChild(document.createTextNode("Fail :("));
	else 
		document.getElementById("result").appendChild(document.createTextNode(res));
};