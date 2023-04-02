var User = {};

User.formXML = function(){
	var res = "";
	res += "<request>"; 
	
	var accountId = document.getElementById("accountId").value;
	if (accountId!=null && accountId!=(""))
		res += "<account-id>" + accountId + "</account-id>";
	
	var folder = document.getElementById("folder").value;
	if (folder!=null&&folder!=(""))
		res += "<folder>" + folder + "</folder>";
	
	var nam = document.getElementById("name").value;
	if (nam!=null&&nam!=(""))
		res += "<name>" + nam + "</name>";

	var company = document.getElementById("company").value;
	if (company!=null&&company!=(""))
		res += "<company>" + company + "</company>";
	
	var city = document.getElementById("city").value;
	if (city!=null&&city!=(""))
		res += "<city>" + city + "</city>";
	
	var phone = document.getElementById("phone").value;
	if (phone!=null&&phone!=(""))
		res += "<phone>" + phone + "</phone>";
	
	var email = document.getElementById("email").value;
	if (email!=null&&email!=(""))
		res += "<email>" + email + "</email>";
	
	var sourc = document.getElementById("source").value;
	if (sourc!=null&&sourc!=(""))
		res += "<source>" + sourc + "</source>";
	
	var subscribe = document.getElementById("subscribe").checked;
	res += "<is_subscribe>" + ((subscribe) ? "1" : 0) + "</is_subscribe>";
	
	res += "</request>";
	return res;
};

getText = function(element){
	if (element && element.firstChild != null)
		return element.firstChild.data;
	return "";
};

User.handleRequest = function(request){
	document.getElementById("mainform").style.display = "none";
	
	var doc = request.responseXML;
	
	var doc_table = document.getElementById("document_table");
	var links = doc.getElementsByTagName("link");
	for (var i = 0; i < links.length; ++i){
		var link = links[i];
		var row = document.createElement("tr");
		
		var cell = document.createElement("td");
		var ref = document.createElement("a");
		ref.href = (getText(link.getElementsByTagName("url")[0]) + ("&crxid="));
		ref.appendChild(document.createTextNode(getText(link.getElementsByTagName("name")[0])));
		cell.appendChild(ref);
		row.appendChild(cell);
		
		cell = document.createElement("td");;
		cell.appendChild(document.createTextNode(getText(link.getElementsByTagName("title")[0])));
		row.appendChild(cell);
		
		cell = document.createElement("td");;
		cell.appendChild(document.createTextNode(getText(link.getElementsByTagName("abstract")[0])));
		row.appendChild(cell);

		cell = document.createElement("td");;
		cell.appendChild(document.createTextNode(getText(link.getElementsByTagName("number")[0])));
		row.appendChild(cell);
		
		cell = document.createElement("td");;
		cell.appendChild(document.createTextNode(getText(link.getElementsByTagName("createdDate")[0])));
		row.appendChild(cell);
		
		cell = document.createElement("td");;
		cell.appendChild(document.createTextNode(getText(link.getElementsByTagName("modifiedDate")[0])));
		row.appendChild(cell);

		cell = document.createElement("td");;
		cell.appendChild(document.createTextNode(getText(link.getElementsByTagName("description")[0])));
		row.appendChild(cell);

		cell = document.createElement("td");;
		cell.appendChild(document.createTextNode(getText(link.getElementsByTagName("author")[0])));
		row.appendChild(cell);
		
		cell = document.createElement("td");;
		cell.appendChild(document.createTextNode(getText(link.getElementsByTagName("docType")[0])));
		row.appendChild(cell);
		
		cell = document.createElement("td");;
		cell.appendChild(document.createTextNode(getText(link.getElementsByTagName("is_folder")[0])));
		row.appendChild(cell);

		doc_table.appendChild(row);
	}
	
	var res_table = document.getElementById("result_table");
	var results = doc.getElementsByTagName("result");
	
	for (var i = 0; i < results.length; ++i){
		var result = results[i];
		var row = document.createElement("tr");
		
		cell = document.createElement("td");;
		cell.appendChild(document.createTextNode(getText(result.getElementsByTagName("result-code")[0])));
		row.appendChild(cell);
		
		cell = document.createElement("td");;
		cell.appendChild(document.createTextNode(getText(result.getElementsByTagName("error-message")[0])));
		row.appendChild(cell);
		
		res_table.appendChild(row);
	}

	var acc_table = document.getElementById("account_table");
	var accounts = doc.getElementsByTagName("account");
	
	for (var i = 0; i < accounts.length; ++i){
		var account = accounts[i];
		var row = document.createElement("tr");
		
		cell = document.createElement("td");;
		cell.appendChild(document.createTextNode(getText(account.getElementsByTagName("account-id")[0])));
		row.appendChild(cell);
		
		cell = document.createElement("td");;
		cell.appendChild(document.createTextNode(getText(account.getElementsByTagName("account-pwd")[0])));
		row.appendChild(cell);
		
		acc_table.appendChild(row);
	}
	
	document.getElementById("result").style.display = "block";
};
