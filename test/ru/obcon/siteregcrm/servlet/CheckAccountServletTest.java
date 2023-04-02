package ru.obcon.siteregcrm.servlet;

import java.io.IOException;

import javax.servlet.ServletException;

import org.easymock.EasyMock;
import org.opencrx.kernel.account1.jmi1.Contact;
import org.openmdx.base.naming.Path;

import ru.obcon.siteregcrm.servlet.CheckAccountServlet;
import ru.obcon.siteregcrm.test.ContextBaseTest;
import ru.obcon.siteregcrm.utils.Const;

public class CheckAccountServletTest extends ContextBaseTest{

	private CheckAccountServlet servlet;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		servlet = new CheckAccountServlet();
	}

	@SuppressWarnings("deprecation")
	public void testNoLoginParameter() throws ServletException, IOException{
		
		EasyMock.expect(reqHelper.getLoginParameter()).andReturn(null);
		
		respHelper.returnPlainText(Const.AUTHENTICATION_FAIL);
		
		replay();
		
		servlet.handleRequest(null, null, context);
		
		verify();
	}
	
	@SuppressWarnings("deprecation")
	public void testLoginPresent() throws ServletException, IOException{
		String login = "login";
		Contact contact = EasyMock.createMock(Contact.class);
		
		EasyMock.expect(reqHelper.getLoginParameter()).andReturn(login);
		
		EasyMock.expect(account.getContactByEmail(login)).andReturn(contact);
		
		EasyMock.expect(contact.refGetPath()).andReturn(new Path("ff/dd/id"));
		
		respHelper.returnPlainText("id");
		
		replay();
		EasyMock.replay(contact);
		
		servlet.handleRequest(null, null, context);
		
		verify();
		EasyMock.verify(contact);
	}
	
	@SuppressWarnings("deprecation")
	public void testLoginNotPresent() throws ServletException, IOException{
		String login = "login";
		
		EasyMock.expect(reqHelper.getLoginParameter()).andReturn(login);
		
		EasyMock.expect(account.getContactByEmail(login)).andReturn(null);
		
		respHelper.returnPlainText(Const.AUTHENTICATION_FAIL);
		
		replay();
		
		servlet.handleRequest(null, null, context);
		
		verify();
	}
}
