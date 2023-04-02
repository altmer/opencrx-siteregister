package ru.obcon.siteregcrm.servlet;

import java.io.IOException;

import javax.servlet.ServletException;

import org.easymock.EasyMock;
import org.opencrx.kernel.account1.jmi1.Contact;
import org.w3c.dom.Document;

import ru.obcon.siteregcrm.servlet.GetCategoryServlet;
import ru.obcon.siteregcrm.test.ContextBaseTest;
import ru.obcon.siteregcrm.utils.Const;

public class GetCategoryServletTest extends ContextBaseTest{
	private GetCategoryServlet servlet;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		servlet = new GetCategoryServlet();
	}
	
	@SuppressWarnings("deprecation")
	public void testIdNotExist() throws ServletException, IOException{
		EasyMock.expect(reqHelper.getAccountIdParameter()).andReturn(null);
		
		replay();
		
		servlet.handleRequest(null, null, context);
		
		verify();
	}

	@SuppressWarnings("deprecation")
	public void testIdEmpty() throws ServletException, IOException{
		EasyMock.expect(reqHelper.getAccountIdParameter()).andReturn("");
		
		replay();
		
		servlet.handleRequest(null, null, context);
		
		verify();		
	}
	
	@SuppressWarnings("deprecation")
	public void testContactNotExist() throws ServletException, IOException{
		
		String id = "id";		
		Contact contact = EasyMock.createMock(Contact.class);
		
		EasyMock.expect(reqHelper.getAccountIdParameter()).andReturn(id);
		EasyMock.expect(account.getContactByID(id)).andReturn(null);
		
		replay();
		EasyMock.replay(contact);
		
		servlet.handleRequest(null, null, context);
		
		verify();		
		EasyMock.verify(contact);
	}

	@SuppressWarnings("deprecation")
	public void testContactExist() throws ServletException, IOException{
		
		String id = "id";		
		Contact contact = EasyMock.createMock(Contact.class);
		
		EasyMock.expect(reqHelper.getAccountIdParameter()).andReturn(id);
		EasyMock.expect(account.getContactByID(id)).andReturn(contact);
		respHelper.returnXML("<"+Const.CATEGORIES_TAG+"></"+Const.CATEGORIES_TAG+">");
		activity.addCategoriesList(EasyMock.eq(contact), EasyMock.anyObject(Document.class));
		
		replay();
		EasyMock.replay(contact);
		
		servlet.handleRequest(null, null, context);
		
		verify();		
		EasyMock.verify(contact);
	}
}
