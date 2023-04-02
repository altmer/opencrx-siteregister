package ru.obcon.siteregcrm.manager;

import java.util.Date;

import org.easymock.EasyMock;
import org.opencrx.kernel.activity1.jmi1.ActivityCategory;
import org.opencrx.kernel.document1.cci2.DocumentFolder;
import org.openmdx.base.naming.Path;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.obcon.siteregcrm.manager.XmlManager;
import ru.obcon.siteregcrm.objects.ClientRequest;
import ru.obcon.siteregcrm.objects.User;
import ru.obcon.siteregcrm.utils.Const;
import junit.framework.TestCase;

public class XmlManagerTest extends TestCase {

	private XmlManager xmlManager;
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		xmlManager = new XmlManager(); 
	}

	public void testNewDocumentList(){
		assertEquals("<links><result><result-code>0</result-code><error-message></error-message></result></links>",
				xmlManager.xmlToString(xmlManager.getNewDocumentList().getDocumentElement()));
		
	}
	
	public void testFindNode(){
		Document doc = xmlManager.getNewDocumentList();
		
		Node links = xmlManager.findNode(doc.getDocumentElement(), "links");
		Node result = xmlManager.findNode(doc.getDocumentElement(), "result");
		Node resultCode = xmlManager.findNode(doc.getDocumentElement(), "result-code");
		Node errorMessage = xmlManager.findNode(doc.getDocumentElement(), "error-message");
		Node notSuchNode = xmlManager.findNode(doc.getDocumentElement(), "wrong");
		
		assertNotNull( links );
		assertNotNull( result );
		assertNotNull(resultCode );
		assertNotNull(errorMessage );
		assertNull(notSuchNode);
		
		assertEquals(1, links.getChildNodes().getLength() );
		assertEquals(2, result.getChildNodes().getLength() );
		assertEquals(1, resultCode.getChildNodes().getLength() );
		assertEquals(1, errorMessage.getChildNodes().getLength() );
		
		assertEquals("0", xmlManager.getText(resultCode));
		assertEquals("", xmlManager.getText(errorMessage));
	}
	
	public void testAddNewUser(){
		Document doc = xmlManager.getNewDocumentList();
		User user = new User();
		xmlManager.addNewUserInformation(doc, user);
		
		assertEquals("<links><result><result-code>0</result-code><error-message></error-message></result>" +
				"<account><account-id>"+user.getID()+"</account-id><account-pwd>"+user.getPassword() +
				"</account-pwd></account>"+ 
				"</links>", xmlManager.xmlToString(doc.getDocumentElement()));
		
		doc = xmlManager.getNewDocumentList();
		user = new User();
		xmlManager.addNewUserInformation(doc, user);
		
		assertEquals("<links><result><result-code>0</result-code><error-message></error-message></result>" +
				"<account><account-id>"+user.getID()+"</account-id><account-pwd>"+user.getPassword() +
				"</account-pwd></account>"+ 
				"</links>", xmlManager.xmlToString(doc.getDocumentElement()));
	}
	
	public void testAddError(){
		Document doc = xmlManager.getNewDocumentList();
		xmlManager.addErrorMessage(doc, "1", "message1");
		
		assertEquals("<links><result><result-code>1</result-code><error-message>message1</error-message></result></links>",
				xmlManager.xmlToString(doc.getDocumentElement()));

		xmlManager.addErrorMessage(doc, "", "");
		
		assertEquals("<links><result><result-code></result-code><error-message></error-message></result></links>",
				xmlManager.xmlToString(doc.getDocumentElement()));
		
		
		xmlManager.addErrorMessage(doc, "0", "");
		
		assertEquals("<links><result><result-code>0</result-code><error-message></error-message></result></links>",
				xmlManager.xmlToString(doc.getDocumentElement()));
	}
	
	public void testUser(){
		String input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
				"<request>" +
				"<folder>" +
				"folder1" +
				"</folder>" +
				"<name>" +
				"name1" +
				"</name>" +
				"<company>" +
				"company1" +
				"</company>" +
				"<city>" +
				"city1" +
				"</city>" +
				"<phone>" +
				"phone1" +
				"</phone>" +
				"<email>" +
				"eww@mail.com" +
				"</email>" +
				"<source>" +
				"source" +
				"</source>" +
				"</request>";
		
		User user = xmlManager.getUser(input);
		
		assertEquals("name1", user.getName());
		assertEquals("company1", user.getCompany());
		assertEquals("city1", user.getCity());
		assertEquals("phone1", user.getPhoneNumber());
		assertEquals("eww@mail.com", user.getEmail());
		assertEquals("source", user.getSource());
		assertFalse(user.isSubscribe());
		assertTrue(user.isNewKey());
		assertTrue(user.isValid());
		
		input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
			"<request>" +
			"<name>" +
			"name1" +
			"</name>" +
			"<company>" +
			"company1" +
			"</company>" +
			"<city>" +
			"city1" +
			"</city>" +
			"<phone>" +
			"phone1" +
			"</phone>" +
			"<email>" +
			"eww@mail.com" +
			"</email>" +
			"</request>";
	
		user = xmlManager.getUser(input);
		
		assertEquals("name1", user.getName());
		assertEquals("company1", user.getCompany());
		assertEquals("city1", user.getCity());
		assertEquals("phone1", user.getPhoneNumber());
		assertEquals("eww@mail.com", user.getEmail());
		assertEquals("", user.getSource());
		assertTrue(user.isNewKey());
		assertTrue(user.isValid());
		
		input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
		"<request>" +
		"<name>" +
		"name1" +
		"</name>" +
		"<company>" +
		"company1" +
		"</company>" +
		"<city>" +
		"city1" +
		"</city>" +
		"<phone>" +
		"phone1" +
		"</phone>" +
		"<email>" +
		"eww@mail...com" +
		"</email>" +
		"</request>";

		user = xmlManager.getUser(input);
		
		assertEquals("name1", user.getName());
		assertEquals("company1", user.getCompany());
		assertEquals("city1", user.getCity());
		assertEquals("phone1", user.getPhoneNumber());
		assertEquals("eww@mail...com", user.getEmail());
		assertEquals("", user.getSource());
		assertTrue(user.isNewKey());
		assertFalse(user.isValid());
		
		input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
		"<request>" +
		"<name>" +
		"name1" +
		"</name>" +
		"<company>" +
		"company1" +
		"</company>" +
		"<phone>" +
		"phone1" +
		"</phone>" +
		"<email>" +
		"eww@mail.com" +
		"</email>" +
		"</request>";

		user = xmlManager.getUser(input);
		
		assertEquals("name1", user.getName());
		assertEquals("company1", user.getCompany());
		assertEquals("", user.getCity());
		assertEquals("phone1", user.getPhoneNumber());
		assertEquals("eww@mail.com", user.getEmail());
		assertEquals("", user.getSource());
		assertTrue(user.isNewKey());
		assertFalse(user.isValid());
		
		input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
		"<request>" +
		"<name>" +
		"name1" +
		"</name>" +
		"<company>" +
		"company1" +
		"</company>" +
		"<phone>" +
		"phone1" +
		"</phone>" +
		"<email>" +
		"eww@mail.com" +
		"</email>" +
		"<is_subscribe>" +
		"1" +
		"</is_subscribe>" +
		"</request>";

		user = xmlManager.getUser(input);
		
		assertEquals("name1", user.getName());
		assertEquals("company1", user.getCompany());
		assertEquals("", user.getCity());
		assertEquals("phone1", user.getPhoneNumber());
		assertEquals("eww@mail.com", user.getEmail());
		assertEquals("", user.getSource());
		assertTrue(user.isSubscribe());
		assertTrue(user.isNewKey());
		assertFalse(user.isValid());
		
		input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
		"<request>" +
		"<name>" +
		"name1" +
		"</name>" +
		"<company>" +
		"company1" +
		"</company>" +
		"<phone>" +
		"phone1" +
		"</phone>" +
		"<email>" +
		"eww@mail.com" +
		"</email>" +
		"<is_subscribe>" +
		"0" +
		"</is_subscribe>" +
		"</request>";

		user = xmlManager.getUser(input);
		
		assertEquals("name1", user.getName());
		assertEquals("company1", user.getCompany());
		assertEquals("", user.getCity());
		assertEquals("phone1", user.getPhoneNumber());
		assertEquals("eww@mail.com", user.getEmail());
		assertEquals("", user.getSource());
		assertFalse(user.isSubscribe());
		assertTrue(user.isNewKey());
		assertFalse(user.isValid());
		
		input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
		"<request>" +
		"<name>" +
		"name1" +
		"</name>" +
		"<company>" +
		"company1" +
		"</company>" +
		"<phone>" +
		"phone1" +
		"</phone>" +
		"<email>" +
		"eww@mail.com" +
		"</email>" +
		"<is_subscribe>" +
		"aaa" +
		"</is_subscribe>" +
		"</request>";

		user = xmlManager.getUser(input);
		
		assertEquals("name1", user.getName());
		assertEquals("company1", user.getCompany());
		assertEquals("", user.getCity());
		assertEquals("phone1", user.getPhoneNumber());
		assertEquals("eww@mail.com", user.getEmail());
		assertEquals("", user.getSource());
		assertFalse(user.isSubscribe());
		assertTrue(user.isNewKey());
		assertFalse(user.isValid());
		
		input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
		"<request>" +
		"<account-id>" +
		"1" +
		"</account-id>" +
		"<name>" +
		"name1" +
		"</name>" +
		"<company>" +
		"company1" +
		"</company>" +
		"<phone>" +
		"phone1" +
		"</phone>" +
		"<email>" +
		"e@mail.com" +
		"</email>" +
		"</request>";

		user = xmlManager.getUser(input);
		
		assertEquals("1", user.getID());
		assertFalse(user.isNewKey());
		assertFalse(user.isValid());
	}
	
	public void testGetRequestedFolder(){
		String input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
		"<request>" +
			"<" + Const.FOLDER_TAG+">" +
				"folder" +
			"</" + Const.FOLDER_TAG+">" +
			"<account-id>" +
				"1" +
			"</account-id>" +
			"<name>" +
				"name1" +
			"</name>" +
			"<company>" +
				"company1" +
			"</company>" +
			"<phone>" +
				"phone1" +
			"</phone>" +
			"<email>" +
				"e@mail.com" +
			"</email>" +
		"</request>";
 
		assertEquals("folder", xmlManager.getRequestedFolder(input));
		
		input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
		"<request>" +
			"<account-id>" +
				"1" +
			"</account-id>" +
			"<name>" +
				"name1" +
			"</name>" +
			"<company>" +
				"company1" +
			"</company>" +
			"<phone>" +
				"phone1" +
			"</phone>" +
			"<email>" +
				"e@mail.com" +
			"</email>" +
		"</request>";
 
		assertEquals("", xmlManager.getRequestedFolder(input));
		
		input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
		"<request>" +
			"<" + Const.FOLDER_TAG+">" +
			"</" + Const.FOLDER_TAG+">" +
			"<account-id>" +
				"1" +
			"</account-id>" +
			"<name>" +
				"name1" +
			"</name>" +
			"<company>" +
				"company1" +
			"</company>" +
			"<phone>" +
				"phone1" +
			"</phone>" +
			"<email>" +
				"e@mail.com" +
			"</email>" +
		"</request>";
		
		assertEquals("", xmlManager.getRequestedFolder(input));
	}
	
	public void testAddDocumentInfoNull(){
		Document doc = xmlManager.getNewDocumentList();
		
		org.opencrx.kernel.document1.jmi1.Document info = 
			EasyMock.createMock(org.opencrx.kernel.document1.jmi1.Document.class);
		
		EasyMock.replay(info);

		xmlManager.addDocumentInfo(null, "type", "url", doc);
		
		EasyMock.verify(info);
		
	}
	
	public void testAddDocumentInfoDocNull(){
		
		org.opencrx.kernel.document1.jmi1.Document info = 
			EasyMock.createMock(org.opencrx.kernel.document1.jmi1.Document.class);
		
		EasyMock.replay(info);

		xmlManager.addDocumentInfo(info, "type", "url", null);
		
		EasyMock.verify(info);
		
	}
	
	public void testAddDocumentInfoWrongDoc(){
		Document doc = xmlManager.getNewCategoriesList();
		
		org.opencrx.kernel.document1.jmi1.Document info = 
			EasyMock.createMock(org.opencrx.kernel.document1.jmi1.Document.class);
		
		EasyMock.replay(info);

		xmlManager.addDocumentInfo(info, "type", "url", doc);
		
		EasyMock.verify(info);
		
	}
	
	@SuppressWarnings("deprecation")
	public void testAddDocumentInfo(){
		Document doc = xmlManager.getNewDocumentList();
		
		org.opencrx.kernel.document1.jmi1.Document info = 
			EasyMock.createMock(org.opencrx.kernel.document1.jmi1.Document.class);
		
		Date created = new Date(109, 0, 1);
		Date modified = new Date(108, 0, 1);
		
		EasyMock.expect(info.getName()).andReturn("name");
		EasyMock.expect(info.getTitle()).andReturn("title");
		EasyMock.expect(info.getDocumentAbstract()).andReturn("abstract");
		EasyMock.expect(info.getDocumentNumber()).andReturn("number");
		EasyMock.expect(info.getCreatedAt()).andReturn(created);
		EasyMock.expect(info.getModifiedAt()).andReturn(modified);
		EasyMock.expect(info.getDescription()).andReturn("descr");
		EasyMock.expect(info.getAuthor()).andReturn("author");
		
		EasyMock.replay(info);

		xmlManager.addDocumentInfo(info, "type", "url", doc);
		
		EasyMock.verify(info);
		assertEquals("url", 
			xmlManager.getText(xmlManager.findNode(doc.getDocumentElement(), Const.URL_TAG)));
		assertEquals("type", 
				xmlManager.getText(xmlManager.findNode(doc.getDocumentElement(), Const.DOCTYPE_TAG)));
		assertEquals("name", 
				xmlManager.getText(xmlManager.findNode(doc.getDocumentElement(), Const.NAME_TAG)));
		assertEquals("title", 
				xmlManager.getText(xmlManager.findNode(doc.getDocumentElement(), Const.TITLE_TAG)));
		assertEquals("abstract", 
				xmlManager.getText(xmlManager.findNode(doc.getDocumentElement(), Const.ABSTRACT_TAG)));
		assertEquals("number", 
				xmlManager.getText(xmlManager.findNode(doc.getDocumentElement(), Const.NUMBER_TAG)));
		assertEquals("descr", 
				xmlManager.getText(xmlManager.findNode(doc.getDocumentElement(), Const.DESCRIPTION_TAG)));
		assertEquals("author", 
				xmlManager.getText(xmlManager.findNode(doc.getDocumentElement(), Const.AUTHOR_TAG)));
		assertEquals("0", 
				xmlManager.getText(xmlManager.findNode(doc.getDocumentElement(), Const.ISFOLDER_TAG)));
		
	}

	@SuppressWarnings("deprecation")
	public void testAddFolderInfo(){
		Document doc = xmlManager.getNewDocumentList();
		
		DocumentFolder info = 
			EasyMock.createMock(org.opencrx.kernel.document1.cci2.DocumentFolder.class);
		
		Date created = new Date(109, 0, 1);
		Date modified = new Date(108, 0, 1);
		
		EasyMock.expect(info.getName()).andReturn("name");
		EasyMock.expect(info.getCreatedAt()).andReturn(created);
		EasyMock.expect(info.getModifiedAt()).andReturn(modified);
		EasyMock.expect(info.getDescription()).andReturn("descr");
		
		EasyMock.replay(info);

		xmlManager.addFolderInfo(info, doc);
		
		EasyMock.verify(info);
		assertEquals("name", 
				xmlManager.getText(xmlManager.findNode(doc.getDocumentElement(), Const.NAME_TAG)));
		assertEquals("descr", 
				xmlManager.getText(xmlManager.findNode(doc.getDocumentElement(), Const.DESCRIPTION_TAG)));
		assertEquals("1", 
				xmlManager.getText(xmlManager.findNode(doc.getDocumentElement(), Const.ISFOLDER_TAG)));
		
		
	}
	
	public void testGetNewCategoriesList(){
		Document doc = xmlManager.getNewCategoriesList();
		
		assertEquals("<"+Const.CATEGORIES_TAG+"></"+Const.CATEGORIES_TAG+">", 
				xmlManager.xmlToString(doc.getDocumentElement()));
	}
	
	public void testAddNewCategoryDocumentNull(){

		ActivityCategory category = EasyMock.createMock(ActivityCategory.class);
		
		EasyMock.replay(category);
		
		xmlManager.addCategoryInfo(null, category);
		
		EasyMock.verify(category);
	}
	
	public void testAddNewCategoryNull(){
		Document doc = xmlManager.getNewCategoriesList();

		ActivityCategory category = EasyMock.createMock(ActivityCategory.class);
		
		EasyMock.replay(category);
		
		xmlManager.addCategoryInfo(doc, null);
		
		EasyMock.verify(category);
	}
	
	public void testAddNewCategoryWrongDocument(){
		Document doc = xmlManager.getNewDocumentList();

		ActivityCategory category = EasyMock.createMock(ActivityCategory.class);
		
		EasyMock.replay(category);
		
		xmlManager.addCategoryInfo(doc, category);
		
		EasyMock.verify(category);
	}
	
	public void testAddNewCategoryAllFieldsNull(){
		Document doc = xmlManager.getNewCategoriesList();

		ActivityCategory category = EasyMock.createMock(ActivityCategory.class);
		
		EasyMock.expect(category.refGetPath()).andReturn(new Path("ds/id"));
		EasyMock.expect(category.getName()).andReturn(null);
		EasyMock.expect(category.getDescription()).andReturn(null);
		
		EasyMock.replay(category);
		
		xmlManager.addCategoryInfo(doc, category);
		
		EasyMock.verify(category);
		
		assertEquals("id",xmlManager.getText(xmlManager.findNode(doc.getDocumentElement(), Const.CATEGORY_ID_TAG)));
		assertEquals("null",xmlManager.getText(xmlManager.findNode(doc.getDocumentElement(), Const.CATEGORY_NAME_TAG)));
		assertEquals("null",xmlManager.getText(xmlManager.findNode(doc.getDocumentElement(), Const.CATEGORY_DESCRIPTION_TAG)));
	}
	
	public void testAddNewCategory(){
		Document doc = xmlManager.getNewCategoriesList();

		ActivityCategory category = EasyMock.createMock(ActivityCategory.class);
		
		EasyMock.expect(category.refGetPath()).andReturn(new Path("ds/id"));
		EasyMock.expect(category.getName()).andReturn("name");
		EasyMock.expect(category.getDescription()).andReturn("some description");
		
		EasyMock.replay(category);
		
		xmlManager.addCategoryInfo(doc, category);
		
		EasyMock.verify(category);
		
		assertEquals("id",xmlManager.getText(xmlManager.findNode(doc.getDocumentElement(), Const.CATEGORY_ID_TAG)));
		assertEquals("name",xmlManager.getText(xmlManager.findNode(doc.getDocumentElement(), Const.CATEGORY_NAME_TAG)));
		assertEquals("some description",xmlManager.getText(xmlManager.findNode(doc.getDocumentElement(), Const.CATEGORY_DESCRIPTION_TAG)));
	}
	
	public void testAddNewCategoryTwice(){
		Document doc = xmlManager.getNewCategoriesList();

		ActivityCategory category = EasyMock.createMock(ActivityCategory.class);
		
		EasyMock.expect(category.refGetPath()).andReturn(new Path("ds/id"));
		EasyMock.expect(category.getName()).andReturn("name");
		EasyMock.expect(category.getDescription()).andReturn("some description");
		
		ActivityCategory category2 = EasyMock.createMock(ActivityCategory.class);
		
		EasyMock.expect(category2.refGetPath()).andReturn(new Path("ds/id2"));
		EasyMock.expect(category2.getName()).andReturn("name2");
		EasyMock.expect(category2.getDescription()).andReturn("some description2");
		
		EasyMock.replay(category, category2);
		
		xmlManager.addCategoryInfo(doc, category);
		xmlManager.addCategoryInfo(doc, category2);
		
		EasyMock.verify(category,category2);
		
		Node root = xmlManager.findNode(doc.getDocumentElement(), Const.CATEGORIES_TAG);
		
		assertEquals(2, root.getChildNodes().getLength());
		
		Node cat1 = root.getChildNodes().item(0);
		Node cat2 = root.getChildNodes().item(1);
		
		NodeList cat1list = cat1.getChildNodes(); 
		for(int i = 0; i < cat1list.getLength(); ++i){
			Node node = cat1list.item(i);
			
			if (node.getNodeName().equals(Const.CATEGORY_ID_TAG))
				assertEquals("id",xmlManager.getText(node));
			else if (node.getNodeName().equals(Const.CATEGORY_NAME_TAG))
				assertEquals("name",xmlManager.getText(node));
			else if (node.getNodeName().equals(Const.CATEGORY_DESCRIPTION_TAG))
				assertEquals("some description",xmlManager.getText(node));
		}
		
		NodeList cat2list = cat2.getChildNodes(); 
		for(int i = 0; i < cat2list.getLength(); ++i){
			Node node = cat2list.item(i);
			
			if (node.getNodeName().equals(Const.CATEGORY_ID_TAG))
				assertEquals("id2",xmlManager.getText(node));
			else if (node.getNodeName().equals(Const.CATEGORY_NAME_TAG))
				assertEquals("name2",xmlManager.getText(node));
			else if (node.getNodeName().equals(Const.CATEGORY_DESCRIPTION_TAG))
				assertEquals("some description2",xmlManager.getText(node));
		}
	}
	
	public void testGetClientRequest(){
		
		String input = 		
			"<"+Const.REQUEST_TAG+">" +
			"</"+Const.REQUEST_TAG+">"
		;
		
		ClientRequest req = xmlManager.getClientRequest(input);
		
		assertEquals("", req.getContactId());
		assertEquals("", req.getCategoryId());
		assertEquals("", req.getDescription());
		assertEquals("", req.getName());
		assertEquals(1, req.getPriority());


		input = 		
			"<"+"a"+">" +
			"</"+"a"+">"
		;
		
		req = xmlManager.getClientRequest(input);
		
		assertNull(req);
		
		input = null;
		
		req = xmlManager.getClientRequest(input);
		
		assertNull(req);
		
		input = 		
			"<"+"a"+">"
		;
		
		req = xmlManager.getClientRequest(input);
		
		assertNull(req);
		
		input = 		
			"<"+Const.REQUEST_TAG+">" +
			
				"<"+Const.NAME_TAG+">" +
					"name" + 
				"</"+Const.NAME_TAG+">" +
				
				"<"+Const.CATEGORY_ID_TAG+">" +
					"catid" + 
				"</"+Const.CATEGORY_ID_TAG+">" +
				
				"<"+Const.ACCOUNT_ID_TAG+">" +
					"accid" + 
				"</"+Const.ACCOUNT_ID_TAG+">" +
				
				"<"+Const.DESCRIPTION_TAG+">" +
					"descr" + 
				"</"+Const.DESCRIPTION_TAG+">" +
				
				"<"+Const.PRIORITY_TAG+">" +
					3 + 
				"</"+Const.PRIORITY_TAG+">" +
				
			"</"+Const.REQUEST_TAG+">"
		;
		
		req = xmlManager.getClientRequest(input);
		
		assertEquals("accid", req.getContactId());
		assertEquals("catid", req.getCategoryId());
		assertEquals("descr", req.getDescription());
		assertEquals("name", req.getName());
		assertEquals(3, req.getPriority());
		
		input = 		
			"<"+Const.REQUEST_TAG+">" +
			
				"<"+Const.NAME_TAG+">" +
					"name" + 
				"</"+Const.NAME_TAG+">" +
				
				"<"+Const.CATEGORY_ID_TAG+">" +
					"catid" + 
				"</"+Const.CATEGORY_ID_TAG+">" +
				
				"<"+Const.DESCRIPTION_TAG+">" +
					"descr" + 
				"</"+Const.DESCRIPTION_TAG+">" +
				
				"<"+Const.PRIORITY_TAG+">" +
					-1 + 
				"</"+Const.PRIORITY_TAG+">" +
				
			"</"+Const.REQUEST_TAG+">"
		;
		
		req = xmlManager.getClientRequest(input);
		
		assertEquals("", req.getContactId());
		assertEquals("catid", req.getCategoryId());
		assertEquals("descr", req.getDescription());
		assertEquals("name", req.getName());
		assertEquals(1, req.getPriority());
	}
}
