var Req = {};

Req.formXML = function(){
	var res = "";
	res+="<request>";
	
	var contactId = document.getElementById("contactId").value;
	if (contactId != null && contactId != (""))
		res += "<account-id>" + contactId + "</account-id>";
	
	var catId = document.getElementById("categoryId").value;
	if (catId != null && catId != (""))
		res += "<category-crxid>" + catId + "</category-crxid>";

	var descr = document.getElementById("descr").value;
	if (descr != null && descr != (""))
		res += "<description>" + descr + "</description>";

	var name = document.getElementById("name").value;
	if (name != null && name != (""))
		res += "<name>" + name + "</name>";

	var priority = document.getElementById("priority").value;
	if (priority != null && priority != (""))
		res += "<priority>" + priority + "</priority>";
	
	res+="</request>";
	return res;
};

Req.handleRequest = function(request){
	document.getElementById("mainform").style.display = "none";
	
	document.getElementById("result").appendChild(document.createTextNode(request.responseText));
	
	document.getElementById("result").style.display = "block";

};