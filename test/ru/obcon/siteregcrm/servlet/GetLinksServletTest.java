package ru.obcon.siteregcrm.servlet;

import java.io.IOException;

import javax.servlet.ServletException;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.opencrx.kernel.account1.jmi1.Contact;
import org.w3c.dom.Document;

import ru.obcon.siteregcrm.objects.User;
import ru.obcon.siteregcrm.servlet.GetLinksServlet;
import ru.obcon.siteregcrm.test.ContextBaseTest;
import ru.obcon.siteregcrm.utils.Const;

public class GetLinksServletTest extends ContextBaseTest {

	private GetLinksServlet servlet;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		servlet = new GetLinksServlet();
	}
	
	@SuppressWarnings("deprecation")
	public void testWrongRequestNotAllFieldsPresent() throws ServletException, IOException{
		EasyMock.expect(reqHelper.getRequestBody()).andReturn("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
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
				"<email>" +
				"eww@mail.com" +
				"</email>" +
				"<source>" +
				"source" +
				"</source>" +
				"</request>");
		
		respHelper.returnXML("<links><result><result-code>"+Const.ERROR_CODE_NOT_VALID+
				"</result-code><error-message>"+Const.ERROR_MESSAGE_NOT_VALID+"</error-message></result></links>");
		
		replay();
		
		servlet.handleRequest(null, null, context);
		
		verify();
	}
	
	@SuppressWarnings("deprecation")
	public void testWrongRequestWrongEmail() throws ServletException, IOException{
		EasyMock.expect(reqHelper.getRequestBody()).andReturn("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
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
						"wrrong" +
					"</email>" +
					"<source>" +
						"source" +
					"</source>" +
				"</request>");
		
		respHelper.returnXML("<links>"+
								 "<result>"+
								 	"<result-code>"+
								 		Const.ERROR_CODE_NOT_VALID+
								 	"</result-code>" +
								 	"<error-message>"+
								 		Const.ERROR_MESSAGE_NOT_VALID+
								 	"</error-message>" +
								 "</result>" +
							 "</links>");
		
		replay();
		
		servlet.handleRequest(null, null, context);
		
		verify();
	}
	
	@SuppressWarnings("deprecation")
	public void testIDWrongContact() throws ServletException, IOException{
		String id = "id";
		
		EasyMock.expect(reqHelper.getRequestBody()).andReturn("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
				"<request>" +
					"<account-id>" +
						id +
					"</account-id>" +
					"<name>" +
						"name1" +
					"</name>" +
					"<source>" +
						"source" +
					"</source>" +
				"</request>");
		
		EasyMock.expect(account.getUserByID(id)).andReturn(null);
		
		respHelper.returnXML("<links>"+
								 "<result>"+
								 	"<result-code>"+
								 		Const.ERROR_CODE_NOT_VALID+
								 	"</result-code>" +
								 	"<error-message>"+
								 		Const.ERROR_MESSAGE_NOT_VALID+
								 	"</error-message>" +
								 "</result>" +
							 "</links>");
		
		replay();
		
		servlet.handleRequest(null, null, context);
		
		verify();
	}
	
	@SuppressWarnings("deprecation")
	public void testIDAndNoFolder() throws ServletException, IOException{
		String id = "id";
		
		User user = new User(id);
		user.setCity("city");
		user.setCompany("company");
		user.setEmail("mail@mail.com");
		user.setName("name");
		user.setPassword("pass");
		user.setPhoneNumber("1234");
		user.setSource("source");
		
		Contact contact = EasyMock.createMock(Contact.class);
		
		EasyMock.expect(reqHelper.getRequestBody()).andReturn("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
				"<request>" +
					"<account-id>" +
						id +
					"</account-id>" +
					"<name>" +
						"name1" +
					"</name>" +
					"<source>" +
						"source" +
					"</source>" +
				"</request>");
		
		EasyMock.expect(account.getUserByID(id)).andReturn(user);
		
		EasyMock.expect(account.getContactByID(id)).andReturn(contact);
		
		EasyMock.expect(contact.getLastName()).andReturn(user.getName());
		
		EasyMock.expect(reqHelper.getRequestURL()).andReturn("requestURL");
		
		doc.addDocumentList(EasyMock.eq(Const.DOCUMENT_FOLDER), EasyMock.eq(contact), 
					EasyMock.eq("requestURL"), (Document)EasyMock.anyObject());
		
		respHelper.returnXML("<links>"+
								 "<result>"+
								 	"<result-code>"+
								 		Const.ERROR_CODE_OK+
								 	"</result-code>" +
								 	"<error-message>" +
								 	"</error-message>" +
								 "</result>" +
							 "</links>");
		
		replay();
		EasyMock.replay(contact);
		
		servlet.handleRequest(null, null, context);
		
		verify();
		EasyMock.verify(contact);
	}
	
	@SuppressWarnings("deprecation")
	public void testIDAndFolder() throws ServletException, IOException{
		String id = "id";
		String folder = "folder";
		
		User user = new User(id);
		user.setCity("city");
		user.setCompany("company");
		user.setEmail("mail@mail.com");
		user.setName("name");
		user.setPassword("pass");
		user.setPhoneNumber("1234");
		user.setSource("source");
		
		Contact contact = EasyMock.createMock(Contact.class);
		
		EasyMock.expect(reqHelper.getRequestBody()).andReturn("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
				"<request>" +
					"<account-id>" +
						id +
					"</account-id>" +
					"<folder>" +
						folder +
					"</folder>" +
					"<name>" +
						"name1" +
					"</name>" +
					"<source>" +
						"source" +
					"</source>" +
				"</request>");
		
		EasyMock.expect(account.getUserByID(id)).andReturn(user);
		
		EasyMock.expect(account.getContactByID(id)).andReturn(contact);
		
		EasyMock.expect(contact.getLastName()).andReturn(user.getName());
		
		EasyMock.expect(reqHelper.getRequestURL()).andReturn("requestURL");
		
		doc.addDocumentList(EasyMock.eq(folder), EasyMock.eq(contact), 
					EasyMock.eq("requestURL"), (Document)EasyMock.anyObject());
		
		respHelper.returnXML("<links>"+
								 "<result>"+
								 	"<result-code>"+
								 		Const.ERROR_CODE_OK+
								 	"</result-code>" +
								 	"<error-message>" +
								 	"</error-message>" +
								 "</result>" +
							 "</links>");
		
		replay();
		EasyMock.replay(contact);
		
		servlet.handleRequest(null, null, context);
		
		verify();
		EasyMock.verify(contact);
	}
	
	@SuppressWarnings("deprecation")
	public void testNoIDEmailPresentNoFolder() throws ServletException, IOException{
		// data
		String input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
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
				"mail@mail.com" +
			"</email>" +
			"<source>" +
				"source" +
			"</source>" +
		"</request>";
		
		String output = "<links>"+
							 "<result>"+
							 	"<result-code>"+
							 		Const.ERROR_CODE_OK+
							 	"</result-code>" +
							 	"<error-message>" +
							 	"</error-message>" +
							 "</result>" +
						 "</links>";
		
		// expectations
		
		Contact contact = EasyMock.createMock(Contact.class);
		
		EasyMock.expect(reqHelper.getRequestBody()).andReturn(input);
		
		EasyMock.expect(account.getContactByEmail("mail@mail.com")).andReturn(contact);
		
		EasyMock.expect(contact.getLastName()).andReturn("Some random name");
		
		EasyMock.expect(reqHelper.getRequestURL()).andReturn("requestURL");
		
		doc.addDocumentList(EasyMock.eq(Const.DOCUMENT_FOLDER), EasyMock.eq(contact), 
					EasyMock.eq("requestURL"), (Document)EasyMock.anyObject());
		
		respHelper.returnXML(output);
		
		replay();
		EasyMock.replay(contact);
		
		servlet.handleRequest(null, null, context);
		
		verify();
		EasyMock.verify(contact);
	}
	
	@SuppressWarnings("deprecation")
	public void testNoIDEmailNotPresentNoFolder() throws ServletException, IOException{
		// data
		String input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
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
				"mail@mail.com" +
			"</email>" +
			"<source>" +
				"source" +
			"</source>" +
		"</request>";
		
		
		Capture<User> userCapture = new Capture<User>();
		Capture<String> outputCapture = new Capture<String>();
		
		// expectations
		
		Contact contact = EasyMock.createMock(Contact.class);
		
		EasyMock.expect(reqHelper.getRequestBody()).andReturn(input);
		
		EasyMock.expect(account.getContactByEmail("mail@mail.com")).andReturn(null);

		EasyMock.expect(account.create(EasyMock.capture(userCapture))).andReturn(contact);
		
		EasyMock.expect(reqHelper.getRequestURL()).andReturn("requestURL");
		
		doc.addDocumentList(EasyMock.eq(Const.DOCUMENT_FOLDER), EasyMock.eq(contact), 
					EasyMock.eq("requestURL"), (Document)EasyMock.anyObject());
		
		respHelper.returnXML(EasyMock.capture(outputCapture));
		
		replay();
		EasyMock.replay(contact);
		
		servlet.handleRequest(null, null, context);
		
		verify();
		EasyMock.verify(contact);
		
		User user = userCapture.getValue();
		String output = "<links>"+
							 "<result>"+
							 	"<result-code>"+
							 		Const.ERROR_CODE_OK+
							 	"</result-code>" +
							 	"<error-message>" +
							 	"</error-message>" +
							 "</result>" +
							 "<account>" +
							 	"<account-id>" +
							 		user.getID() +
							 	"</account-id>" +
							 	"<account-pwd>" +
							 		user.getPassword() + 
							 	"</account-pwd>" +
							 "</account>" +
						 "</links>";
		
		assertEquals("name1", user.getName());
		assertEquals("company1", user.getCompany());
		assertEquals("city1", user.getCity());
		assertEquals("phone1", user.getPhoneNumber());
		assertEquals("mail@mail.com", user.getEmail());
		assertEquals("source", user.getSource());
		
		assertEquals(output, outputCapture.getValue());
	}
	
	@SuppressWarnings("deprecation")
	public void testNoIDEmailPresentAndFolder() throws ServletException, IOException{
		// data
		String folder = "folder";
		String input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
		"<request>" +
			"<folder>" +
				folder +
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
				"mail@mail.com" +
			"</email>" +
			"<source>" +
				"source" +
			"</source>" +
		"</request>";
		
		String output = "<links>"+
							 "<result>"+
							 	"<result-code>"+
							 		Const.ERROR_CODE_OK+
							 	"</result-code>" +
							 	"<error-message>" +
							 	"</error-message>" +
							 "</result>" +
						 "</links>";
		
		// expectations
		
		Contact contact = EasyMock.createMock(Contact.class);
		
		EasyMock.expect(reqHelper.getRequestBody()).andReturn(input);
		
		EasyMock.expect(account.getContactByEmail("mail@mail.com")).andReturn(contact);
		
		EasyMock.expect(contact.getLastName()).andReturn("Some random name");
		
		EasyMock.expect(reqHelper.getRequestURL()).andReturn("requestURL");
		
		doc.addDocumentList(EasyMock.eq(folder), EasyMock.eq(contact), 
					EasyMock.eq("requestURL"), (Document)EasyMock.anyObject());
		
		respHelper.returnXML(output);
		
		replay();
		EasyMock.replay(contact);
		
		servlet.handleRequest(null, null, context);
		
		verify();
		EasyMock.verify(contact);
		
	}
	
	@SuppressWarnings("deprecation")
	public void testNoIDEmailNotPresentAndFolder() throws ServletException, IOException{
		// data
		String folder = "folder";
		String input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+
		"<request>" +
			"<folder>" +
				folder +
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
				"mail@mail.com" +
			"</email>" +
			"<source>" +
				"source" +
			"</source>" +
		"</request>";
		
		
		Capture<User> userCapture = new Capture<User>();
		Capture<String> outputCapture = new Capture<String>();
		Contact contact = EasyMock.createMock(Contact.class);
		
		// expectations

		EasyMock.expect(reqHelper.getRequestBody()).andReturn(input);
		
		EasyMock.expect(account.getContactByEmail("mail@mail.com")).andReturn(null);

		EasyMock.expect(account.create(EasyMock.capture(userCapture))).andReturn(contact);
		
		EasyMock.expect(reqHelper.getRequestURL()).andReturn("requestURL");
		
		doc.addDocumentList(EasyMock.eq(folder), EasyMock.eq(contact), 
					EasyMock.eq("requestURL"), (Document)EasyMock.anyObject());
		
		respHelper.returnXML(EasyMock.capture(outputCapture));
		
		replay();
		EasyMock.replay(contact);
		
		servlet.handleRequest(null, null, context);
		
		verify();
		EasyMock.verify(contact);
		
		User user = userCapture.getValue();
		String output = "<links>"+
							 "<result>"+
							 	"<result-code>"+
							 		Const.ERROR_CODE_OK+
							 	"</result-code>" +
							 	"<error-message>" +
							 	"</error-message>" +
							 "</result>" +
							 "<account>" +
							 	"<account-id>" +
							 		user.getID() +
							 	"</account-id>" +
							 	"<account-pwd>" +
							 		user.getPassword() + 
							 	"</account-pwd>" +
							 "</account>" +
						 "</links>";
		
		assertEquals("name1", user.getName());
		assertEquals("company1", user.getCompany());
		assertEquals("city1", user.getCity());
		assertEquals("phone1", user.getPhoneNumber());
		assertEquals("mail@mail.com", user.getEmail());
		assertEquals("source", user.getSource());
		
		assertEquals(output, outputCapture.getValue());
	}


}
