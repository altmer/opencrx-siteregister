package ru.obcon.siteregcrm.servlet;

import java.io.IOException;
import javax.servlet.ServletException;

import org.easymock.EasyMock;
import org.opencrx.kernel.account1.jmi1.Contact;
import org.opencrx.kernel.activity1.jmi1.ActivityCategory;
import org.opencrx.kernel.activity1.jmi1.EMail;

import ru.obcon.siteregcrm.servlet.CreateRequestServlet;
import ru.obcon.siteregcrm.test.ContextBaseTest;
import ru.obcon.siteregcrm.utils.Const;

public class CreateRequestServletTest extends ContextBaseTest{
	private CreateRequestServlet servlet;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		servlet = new CreateRequestServlet();
	}

	@SuppressWarnings("deprecation")
	public void testWrongRequest() throws ServletException, IOException{
		
		String xml =
			"<"+"a"+">" +
			"</"+"a"+">"
				;

		EasyMock.expect(reqHelper.getRequestBody()).andReturn(xml);
		respHelper.returnPlainText(Const.ERROR_CODE_REQUEST_ERROR + ","
									+ Const.ERROR_MESSAGE_NOT_VALID_REQUEST);	
		
		replay();
		
		servlet.handleRequest(null, null, context);
		
		verify();
	}
	
	@SuppressWarnings("deprecation")
	public void testNoContactId() throws ServletException, IOException{
		
		String xml = 		
			"<"+Const.REQUEST_TAG+">" +
			
			"<"+Const.NAME_TAG+">" +
				"name" + 
			"</"+Const.NAME_TAG+">" +
			
			"<"+Const.CATEGORY_ID_TAG+">" +
				"catid" + 
			"</"+Const.CATEGORY_ID_TAG+">" +
			
			"<"+Const.ACCOUNT_ID_TAG+">" +
			"</"+Const.ACCOUNT_ID_TAG+">" +
			
			"<"+Const.DESCRIPTION_TAG+">" +
				"descr" + 
			"</"+Const.DESCRIPTION_TAG+">" +
			
			"<"+Const.PRIORITY_TAG+">" +
				3 + 
			"</"+Const.PRIORITY_TAG+">" +
			
		"</"+Const.REQUEST_TAG+">"
	;


		EasyMock.expect(reqHelper.getRequestBody()).andReturn(xml);
		respHelper.returnPlainText(Const.ERROR_CODE_REQUEST_ERROR + ","
									+ Const.ERROR_MESSAGE_NO_USER_ID);	
		
		replay();
		
		servlet.handleRequest(null, null, context);
		
		verify();
	}
	
	@SuppressWarnings("deprecation")
	public void testNoCategoryId() throws ServletException, IOException{
		
		String xml = 		
			"<"+Const.REQUEST_TAG+">" +
			
			"<"+Const.NAME_TAG+">" +
				"name" + 
			"</"+Const.NAME_TAG+">" +
			
			"<"+Const.CATEGORY_ID_TAG+">" +
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


		EasyMock.expect(reqHelper.getRequestBody()).andReturn(xml);
		respHelper.returnPlainText(Const.ERROR_CODE_REQUEST_ERROR + ","
									+ Const.ERROR_MESSAGE_NO_CATEGORY_ID);	
		
		replay();
		
		servlet.handleRequest(null, null, context);
		
		verify();
	}
	
	@SuppressWarnings("deprecation")
	public void testContactNotFound() throws ServletException, IOException{
		
		String xml = 		
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
		
		Contact contact = EasyMock.createMock(Contact.class);

		EasyMock.expect(reqHelper.getRequestBody()).andReturn(xml);
		EasyMock.expect(account.getContactByID("accid")).andReturn(null);
		respHelper.returnPlainText(Const.ERROR_CODE_REQUEST_ERROR + ","
									+ Const.ERROR_MESSAGE_USER_NOT_FOUND);	
		
		replay();
		EasyMock.replay(contact);
		
		servlet.handleRequest(null, null, context);
		
		verify();
		EasyMock.verify(contact);
	}
	
	@SuppressWarnings("deprecation")
	public void testCategoryNotFound() throws ServletException, IOException{
		
		String xml = 		
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
		
		Contact contact = EasyMock.createMock(Contact.class);
		ActivityCategory category = EasyMock.createMock(ActivityCategory.class);

		EasyMock.expect(reqHelper.getRequestBody()).andReturn(xml);
		EasyMock.expect(account.getContactByID("accid")).andReturn(contact);
		EasyMock.expect(activity.getActivityCategoryById("catid")).andReturn(null);
		
		respHelper.returnPlainText(Const.ERROR_CODE_REQUEST_ERROR + ","
									+ Const.ERROR_MESSAGE_CATEGORY_NOT_FOUND);	
		
		replay();
		EasyMock.replay(contact, category);
		
		servlet.handleRequest(null, null, context);
		
		verify();
		EasyMock.verify(contact, category);
	}
	
	
	@SuppressWarnings("deprecation")
	public void testActivityCreationError() throws ServletException, IOException{
		
		String xml = 		
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
		
		Contact contact = EasyMock.createMock(Contact.class);
		ActivityCategory category = EasyMock.createMock(ActivityCategory.class);

		EasyMock.expect(reqHelper.getRequestBody()).andReturn(xml);
		EasyMock.expect(account.getContactByID("accid")).andReturn(contact);
		EasyMock.expect(activity.getActivityCategoryById("catid")).andReturn(category);
		EasyMock.expect(activity.createUserRequestActivity(contact, category, "descr", "name", (short)3)).andReturn(null);
		
		respHelper.returnPlainText(Const.ERROR_CODE_REQUEST_ERROR + ","
									+ Const.ERROR_MESSAGE_CANT_CREATE_ACTIVITY);	
		
		replay();
		EasyMock.replay(contact, category);
		
		servlet.handleRequest(null, null, context);
		
		verify();
		EasyMock.verify(contact, category);
	}
	
	@SuppressWarnings("deprecation")
	public void testEmailNotSend() throws ServletException, IOException{
		String actid = "actid";
		
		String xml = 		
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
		
		Contact contact = EasyMock.createMock(Contact.class);
		ActivityCategory category = EasyMock.createMock(ActivityCategory.class);
		EMail email = EasyMock.createMock(EMail.class);

		EasyMock.expect(reqHelper.getRequestBody()).andReturn(xml);
		EasyMock.expect(account.getContactByID("accid")).andReturn(contact);
		EasyMock.expect(activity.getActivityCategoryById("catid")).andReturn(category);
		EasyMock.expect(activity.createUserRequestActivity(contact, category, "descr", "name", (short)3)).
						andReturn(email);
		EasyMock.expect(email.getActivityNumber()).andReturn(actid);
		EasyMock.expect(code.getSettingValueShort(Const.IS_EMAIL_NOTIFICATE)).andReturn("haha");

		respHelper.returnPlainText(actid);		
		
		replay();
		EasyMock.replay(contact, category, email);
		
		servlet.handleRequest(null, null, context);
		
		verify();
		EasyMock.verify(contact, category, email);
	}
	
	@SuppressWarnings("deprecation")
	public void testMessageNoReplace() throws ServletException, IOException{
		String actnum = "number";
		String name = "name";
		String descr = "descr"; 
		
		String xml = 		
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
		
		String message = "message \nmessage";
		
		Contact contact = EasyMock.createMock(Contact.class);
		ActivityCategory category = EasyMock.createMock(ActivityCategory.class);
		EMail email = EasyMock.createMock(EMail.class);

		EasyMock.expect(reqHelper.getRequestBody()).andReturn(xml);
		
		EasyMock.expect(account.getContactByID("accid")).andReturn(contact);
		
		EasyMock.expect(activity.getActivityCategoryById("catid")).andReturn(category);
		EasyMock.expect(activity.createUserRequestActivity(contact, category, "descr", "name", (short)3)).
						andReturn(email);
		activity.sendEmail(actnum, message, contact);
		
		EasyMock.expect(email.getActivityNumber()).andReturn(actnum).times(3);
		EasyMock.expect(email.getName()).andReturn(name);
		EasyMock.expect(email.getDescription()).andReturn(descr);
		
		EasyMock.expect(code.getSettingValueShort(Const.IS_EMAIL_NOTIFICATE)).andReturn("1");
		EasyMock.expect(code.getSettingValueLong(Const.IS_EMAIL_NOTIFICATE)).andReturn(message);
		
		
		respHelper.returnPlainText(actnum);		
		
		replay();
		EasyMock.replay(contact, category, email);
		
		servlet.handleRequest(null, null, context);
		
		verify();
		EasyMock.verify(contact, category, email);
	}
	
	@SuppressWarnings("deprecation")
	public void testMessageReplace() throws ServletException, IOException{
		
		String actnum = "number";
		String name = "name";
		String descr = "descr"; 
		
		String xml = 		
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
		
		String message = "Activity number: " + Const.ACTIVITY_NUMBER_PATTERN + "\n" + 
						 "Activity name: " + Const.ACTIVITY_NAME_PATTERN + "\n" +
						 "Activity description: " + Const.ACTIVITY_DESCRIPTION_PATTERN + "\n" +
						 "Activity number: " + Const.ACTIVITY_NUMBER_PATTERN + "\n";
		
		String messageExpected = "Activity number: " + actnum + "\n" + 
						 "Activity name: " + name + "\n" +
						 "Activity description: " + descr + "\n" +
						 "Activity number: " + actnum + "\n";
		
		Contact contact = EasyMock.createMock(Contact.class);
		ActivityCategory category = EasyMock.createMock(ActivityCategory.class);
		EMail email = EasyMock.createMock(EMail.class);

		EasyMock.expect(reqHelper.getRequestBody()).andReturn(xml);
		
		EasyMock.expect(account.getContactByID("accid")).andReturn(contact);
		
		EasyMock.expect(activity.getActivityCategoryById("catid")).andReturn(category);
		EasyMock.expect(activity.createUserRequestActivity(contact, category, "descr", "name", (short)3)).
						andReturn(email);
		activity.sendEmail(actnum, messageExpected, contact);
		
		EasyMock.expect(email.getActivityNumber()).andReturn(actnum).times(3);
		EasyMock.expect(email.getName()).andReturn(name);
		EasyMock.expect(email.getDescription()).andReturn(descr);
		
		EasyMock.expect(code.getSettingValueShort(Const.IS_EMAIL_NOTIFICATE)).andReturn("1");
		EasyMock.expect(code.getSettingValueLong(Const.IS_EMAIL_NOTIFICATE)).andReturn(message);
		
		
		respHelper.returnPlainText(actnum);		
		
		replay();
		EasyMock.replay(contact, category, email);
		
		servlet.handleRequest(null, null, context);
		
		verify();
		EasyMock.verify(contact, category, email);
	}

}
