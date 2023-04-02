var HTTP = {};

HTTP.sendPost = function(info, destination, callback){
	var request = new XMLHttpRequest();
	
	request.open("POST", destination);
	
	request.setRequestHeader("Content-Type", "text/xml");
	
	request.onreadystatechange = function(){
		if (request.readyState == 4){
			if (request.status == 200){
				callback(request);
			}else{
				alert("Error " + request.status);
			}
		}
	};
	
	request.send(info);	
	
};

HTTP.sendGet = function(parameter, info, destination, callback){
	var request = new XMLHttpRequest();
	
	request.open("GET", destination + "?" + parameter + "=" + info );
	
	request.onreadystatechange = function(){
		if (request.readyState == 4){
			if (request.status == 200){
				callback(request);
			}else{
				alert("Error " + request.status);
			}
		}
	};
	
	request.send(info);	
	
};