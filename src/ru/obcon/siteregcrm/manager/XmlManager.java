package ru.obcon.siteregcrm.manager;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.opencrx.kernel.activity1.jmi1.ActivityCategory;
import org.opencrx.kernel.document1.cci2.DocumentFolder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.obcon.siteregcrm.objects.ClientRequest;
import ru.obcon.siteregcrm.objects.User;
import ru.obcon.siteregcrm.utils.Const;
import ru.obcon.siteregcrm.utils.ObcLogger;

public class XmlManager {

	private final static Logger logger = ObcLogger.getLogger();
	
	public XmlManager(){}

	public Document getNewDocumentList(){
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			logger.error("Wrong XML parser config! Aborting");
			e.printStackTrace();
			return null;
		}
		Node root = doc.createElement(Const.LINKS_TAG);
		doc.appendChild(root);
		Node result = doc.createElement(Const.RESULT_TAG);
		root.appendChild(result);
		Node resultCode = doc.createElement(Const.RESULTCODE_TAG);
		result.appendChild(resultCode);
		resultCode.appendChild(doc.createTextNode(Const.ERROR_CODE_OK));
		Node errorMessage = doc.createElement(Const.ERRORMESSAGE_TAG);
		result.appendChild(errorMessage);
		errorMessage.appendChild(doc.createTextNode(""));
		return doc;
	}
	
    public void addDocumentInfo(
    		org.opencrx.kernel.document1.jmi1.Document info, 
    		String documentType,
    		String url,
    		Document doc){
    	
    	if (info == null){
    		logger.error("org.opencrx.kernel.document1.jmi1.Document is null.");
    		return;
    	}
    	
    	if (doc == null){
    		logger.error("Document is null.");
    		return;
    	}
    	
    	Node root = findNode(doc.getDocumentElement(), Const.LINKS_TAG);
    	
    	if (root == null){
    		logger.warn("Document is not LinksList");
    		return;
    	}
    	
    	Node link = doc.createElement(Const.LINK_TAG);
    	root.appendChild(link);
    	
    	Node urlNode = doc.createElement(Const.URL_TAG);
    	urlNode.appendChild(doc.createTextNode(url));
    	link.appendChild(urlNode);

    	Node name = doc.createElement(Const.NAME_TAG);
    	name.appendChild(doc.createTextNode(info.getName()));
    	link.appendChild(name);

    	Node title = doc.createElement(Const.TITLE_TAG);
    	title.appendChild(doc.createTextNode(info.getTitle()));
    	link.appendChild(title);

    	Node abstr = doc.createElement(Const.ABSTRACT_TAG);
    	abstr.appendChild(doc.createTextNode(info.getDocumentAbstract()));
    	link.appendChild(abstr);
    	
    	Node number = doc.createElement(Const.NUMBER_TAG);
    	number.appendChild(doc.createTextNode(info.getDocumentNumber()));
    	link.appendChild(number);
    	
    	Node created = doc.createElement(Const.CREATED_TAG);
    	created.appendChild(doc.createTextNode(info.getCreatedAt().toString()));
    	link.appendChild(created);
    	
    	Node modified = doc.createElement(Const.MODIFIED_TAG);
    	modified.appendChild(doc.createTextNode(info.getModifiedAt().toString()));
    	link.appendChild(modified);
    	
    	Node description = doc.createElement(Const.DESCRIPTION_TAG);
    	description.appendChild(doc.createTextNode(info.getDescription()));
    	link.appendChild(description);
    	
    	Node author = doc.createElement(Const.AUTHOR_TAG);
    	author.appendChild(doc.createTextNode(info.getAuthor()));
    	link.appendChild(author);
    	
    	Node docType = doc.createElement(Const.DOCTYPE_TAG);
    	docType.appendChild(doc.createTextNode(documentType));
    	link.appendChild(docType);
    	
    	Node isFolder = doc.createElement(Const.ISFOLDER_TAG);
    	isFolder.appendChild(doc.createTextNode(Const.IS_NOT_FOLDER));
    	link.appendChild(isFolder);
    }
    
	public void addFolderInfo(DocumentFolder info, Document doc) {
		
    	if (info == null){
    		logger.error("DocumentFolder is null.");
    		return;
    	}
    	
    	if (doc == null){
    		logger.error("Document is null.");
    		return;
    	}
    	
    	Node root = findNode(doc.getDocumentElement(), Const.LINKS_TAG);
    	
    	if (root == null){
    		logger.warn("Document is not LinksList");
    		return;
    	}
    	
    	Node link = doc.createElement(Const.LINK_TAG);
    	root.appendChild(link);
    	
    	Node name = doc.createElement(Const.NAME_TAG);
    	name.appendChild(doc.createTextNode(info.getName()));
    	link.appendChild(name);

    	Node created = doc.createElement(Const.CREATED_TAG);
    	created.appendChild(doc.createTextNode(info.getCreatedAt().toString()));
    	link.appendChild(created);
    	
    	Node modified = doc.createElement(Const.MODIFIED_TAG);
    	modified.appendChild(doc.createTextNode(info.getModifiedAt().toString()));
    	link.appendChild(modified);
    	
    	Node description = doc.createElement(Const.DESCRIPTION_TAG);
    	description.appendChild(doc.createTextNode(info.getDescription()));
    	link.appendChild(description);

    	Node isFolder = doc.createElement(Const.ISFOLDER_TAG);
    	isFolder.appendChild(doc.createTextNode(Const.IS_FOLDER));
    	link.appendChild(isFolder);
		
	}
    
    public void addNewUserInformation(Document doc, User user){
    	Node root = findNode(doc.getDocumentElement(), Const.LINKS_TAG);
    	
    	if (root == null){
    		logger.warn("Document is not LinksList");
    		return;
    	}
    	
    	Node account = doc.createElement(Const.ACCOUNT_TAG);
    	root.appendChild(account);
    	
    	Node accountId = doc.createElement(Const.ACCOUNT_ID_TAG);
    	accountId.appendChild(doc.createTextNode(user.getID()));
    	account.appendChild(accountId);
    	
    	Node accountPwd = doc.createElement(Const.ACCOUNT_PWD_TAG);
    	accountPwd.appendChild(doc.createTextNode(user.getPassword()));
    	account.appendChild(accountPwd);
    }
    
    public void addErrorMessage(Document doc, String errorCode, String errorMessage){
    	Node resultCode = findNode(doc.getDocumentElement(), Const.RESULTCODE_TAG);
    	Node message = findNode(doc.getDocumentElement(), Const.ERRORMESSAGE_TAG);
    	
    	if (resultCode == null || message == null){
    		logger.warn("Document is not LinksList");
    		return;
    	}
    	
    	if (resultCode.getFirstChild() == null || message.getFirstChild() == null){
    		logger.warn("Wrong LinksList document format");
    		return;
    	}

    	resultCode.getFirstChild().setNodeValue(errorCode);
    	message.getFirstChild().setNodeValue(errorMessage);
    }
    
    public User getUser(String inputXML){
		Document doc = stringToXML(inputXML);
		
		if (doc == null){
			logger.warn("Request XML is wrong.");
			return null;
		}
		
		if (!doc.getDocumentElement().getNodeName().equals(Const.REQUEST_TAG)){
			logger.warn("Request XML root tag is not " + Const.REQUEST_TAG);
			return null;
		}
		
		User ret = null;
		
		if (findNode(doc.getDocumentElement(), Const.ACCOUNT_ID_TAG) != null){
			ret = new User( getText( findNode(doc.getDocumentElement(), Const.ACCOUNT_ID_TAG) ) );
		}else{
			ret = new User();
		}
		
		ret.setName(getText(findNode(doc.getDocumentElement(), Const.ACCOUNT_NAME_TAG)));
		ret.setCompany(getText(findNode(doc.getDocumentElement(), Const.ACCOUNT_COMPANY_TAG)));
		ret.setCity(getText(findNode(doc.getDocumentElement(), Const.ACCOUNT_CITY_TAG)));
		ret.setPhoneNumber(getText(findNode(doc.getDocumentElement(), Const.ACCOUNT_PHONE_TAG)));
		ret.setEmail(getText(findNode(doc.getDocumentElement(), Const.ACCOUNT_EMAIL_TAG)));
		ret.setSource(getText(findNode(doc.getDocumentElement(), Const.ACCOUNT_SOURCE_TAG)));
		
		String isSubscribe = getText(findNode(doc.getDocumentElement(), Const.ACCOUNT_SUBSCRIBE_TAG));

		ret.setSubscribe( isSubscribe.equals(Const.DO_SUBSCRIBE) );
		
    	return ret;
    }
    
    public String getRequestedFolder(String inputXML){
		Document doc = stringToXML(inputXML);
		
		if (doc == null){
			logger.warn("Request XML is wrong.");
			return "";
		}
		
		return getText(findNode(doc.getDocumentElement(), Const.FOLDER_TAG));
    }
    
    public Document getNewCategoriesList(){
		Document doc = null;
		
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			logger.error("Wrong XML parser config! Aborting");
			e.printStackTrace();
			return null;
		}
		Node root = doc.createElement(Const.CATEGORIES_TAG);
		doc.appendChild(root);

		return doc;
    }
    
	public void addCategoryInfo(Document document, ActivityCategory category) {
		if (document == null){
			logger.error("Document is null.");
			return;
		}
		
		if (category == null){
			logger.error("Category is null.");
			return;
		}
		
		Node root = findNode(document.getDocumentElement(), Const.CATEGORIES_TAG);		
		
		if (root == null){
			logger.error("Document is not CategoriesList");
			return;
		}
		
		Node container = document.createElement(Const.CATEGORY_TAG);
		
		Node categoryId = document.createElement(Const.CATEGORY_ID_TAG);
		categoryId.appendChild(document.createTextNode(category.refGetPath().getBase()));
		
		Node categoryName = document.createElement(Const.CATEGORY_NAME_TAG);
		categoryName.appendChild(document.createTextNode(category.getName()));

		Node categoryDescription = document.createElement(Const.CATEGORY_DESCRIPTION_TAG);
		categoryDescription.appendChild(document.createTextNode(category.getDescription()));
		
		container.appendChild(categoryId);
		container.appendChild(categoryDescription);
		container.appendChild(categoryName);
		
		root.appendChild(container);
	}
	
	public ClientRequest getClientRequest(String xml){
		
		Document doc = stringToXML(xml);
		
		if (doc == null){
			logger.warn("Request XML is wrong.");
			return null;
		}
		
		if (!doc.getDocumentElement().getNodeName().equals(Const.REQUEST_TAG)){
			logger.warn("Request XML root tag is not " + Const.REQUEST_TAG);
			return null;
		}

		ClientRequest req = new ClientRequest();
		
		req.setContactId(getText(findNode(doc.getDocumentElement(), Const.ACCOUNT_ID_TAG)));
		req.setCategoryId(getText(findNode(doc.getDocumentElement(), Const.CATEGORY_ID_TAG)));
		req.setDescription(getText(findNode(doc.getDocumentElement(), Const.DESCRIPTION_TAG)));
		req.setName(getText(findNode(doc.getDocumentElement(), Const.NAME_TAG)));
		
		short priority = 1;
		
		try{
			short temp = Short.valueOf(getText(findNode(doc.getDocumentElement(), Const.PRIORITY_TAG)));
			priority = temp;
		}catch(NumberFormatException ex){
			logger.error("Wrong priority in request.");
		}
		
		req.setPriority(priority);
		
		return req;
	}
	
    public String xmlToString(Node node) {
    	StringBuffer ret = new StringBuffer();
    	ret.append("<"+node.getNodeName()+">");
    	ret.append(getText(node));
		NodeList list = node.getChildNodes();
   	    for (int i=0; i < list.getLength(); i++) {
		    Node subnode = list.item(i);
		    if (subnode.getNodeType() == Node.ELEMENT_NODE) {
		    	ret.append(xmlToString(subnode));
		    }
		}
    	ret.append("</"+node.getNodeName()+">");
        return ret.toString();
    }
    
    
    protected Document stringToXML(String input){
		if (input == null){
			logger.warn("Null xml string.");
			return null;
		}
		
		try {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			return docBuilder.parse(new ByteArrayInputStream(input.getBytes("UTF-8")) );
		} catch (ParserConfigurationException e) {
			logger.error("Wrong XML parser config! Aborting");
			e.printStackTrace();
		}catch (SAXException e) {
			logger.error("XmlUtils: SAXException");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("XmlUtils: IOException");
			e.printStackTrace();
		}
		return null;
  	
    }
    
    /**
     * Find the named subnode in a node's sublist.
     * <li>Ignores comments and processing instructions.
     * <li>Ignores TEXT nodes (likely to exist and contain
     *     ignorable whitespace, if not validating.
     * <li>Ignores CDATA nodes and EntityRef nodes.
     * <li>Examines element nodes to find one with
     *    the specified name.
     * </ul>
     * @param name  the tag name for the element to find
     * @param node  the element node to start searching from
     * @return the Node found
     */
    protected Node findNode(Node node, String name){
		  if (node == null || node.getNodeType() != Node.ELEMENT_NODE) {
			    logger.warn("Error: Search node not of element type");
			    return null;
		  }
		  
		  if (node.getNodeName().equals(name)) return node;

		  if (! node.hasChildNodes()) return null;

		  NodeList list = node.getChildNodes();
		  for (int i=0; i < list.getLength(); i++) {
		    Node subnode = list.item(i);
		    if (subnode.getNodeType() == Node.ELEMENT_NODE) {
		      if (subnode.getNodeName().equals(name)) return subnode;
		      Node rec = findNode(subnode, name);
		      if (rec != null) return rec;
		    }
		  }
		  return null;
    }
    
    /**
     * Return the text that a node contains. This routine:<ul>
     * <li>Ignores comments and processing instructions.
     * <li>Concatenates TEXT nodes, CDATA nodes, and the results of
     *     recursively processing EntityRef nodes.
     * <li>Ignores any element nodes in the sublist.
     *     (Other possible options are to recurse into element 
     *      sublists or throw an exception.)
     * </ul>
     * @param    node  a  DOM node
     * @return   a String representing its contents
     */
   protected String getText(Node node) {
	   if (node == null)
		   return "";
	   
     StringBuffer result = new StringBuffer();
     if (! node.hasChildNodes()) return "";

     NodeList list = node.getChildNodes();
     for (int i=0; i < list.getLength(); i++) {
       Node subnode = list.item(i);
       if (subnode.getNodeType() == Node.TEXT_NODE) {
         result.append(subnode.getNodeValue());
       }
       else if (subnode.getNodeType() ==
           Node.CDATA_SECTION_NODE) 
       {
         result.append(subnode.getNodeValue());
       }
       else if (subnode.getNodeType() ==
           Node.ENTITY_REFERENCE_NODE) 
       {
         // Recurse into the subtree for text
         // (and ignore comments)
         result.append(getText(subnode));
       }
     }
     return result.toString().trim();
   }


}
