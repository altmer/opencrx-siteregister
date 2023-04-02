package ru.obcon.siteregcrm.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;

import org.easymock.EasyMock;
import org.openmdx.base.io.StringInputStream;

import ru.obcon.siteregcrm.objects.DocumentInfo;
import ru.obcon.siteregcrm.objects.User;
import ru.obcon.siteregcrm.servlet.GetDocumentServlet;
import ru.obcon.siteregcrm.test.ContextBaseTest;

public class GetDocumentServletTest extends ContextBaseTest{

	private GetDocumentServlet servlet;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		servlet = new GetDocumentServlet();
	}
	
	@SuppressWarnings("deprecation")
	public void testNoID() throws ServletException, IOException{

		String crxid = "crxid";
		
		EasyMock.expect(reqHelper.getCrxIdParameter()).andReturn(crxid);
		EasyMock.expect(reqHelper.getDocumentIdParameter()).andReturn("");
		
		respHelper.printHTMLMessage("Неправильная ссылка.");
		
		replay();
		
		servlet.handleRequest(null, null, context);
		
		verify();
	}
	
	@SuppressWarnings("deprecation")
	public void testNoCrxId() throws ServletException, IOException{
		String id = "id";		
		String crxid = "";
		
		EasyMock.expect(reqHelper.getCrxIdParameter()).andReturn(crxid);
		EasyMock.expect(reqHelper.getDocumentIdParameter()).andReturn(id);
		
		respHelper.printHTMLMessage("Неправильная ссылка.");
		
		replay();
		
		servlet.handleRequest(null, null, context);
		
		verify();
	}
	
	@SuppressWarnings("deprecation")
	public void testNoUser() throws ServletException, IOException{
		String id = "id";		
		String crxid = "crxid";
		
		EasyMock.expect(reqHelper.getCrxIdParameter()).andReturn(crxid);
		EasyMock.expect(reqHelper.getDocumentIdParameter()).andReturn(id);
		
		EasyMock.expect(account.getUserByID(crxid)).andReturn(null);
		
		respHelper.printHTMLMessage("Зарегистрируйтесь на сайте для скачивания информационных материалов.");
		
		replay();
		
		servlet.handleRequest(null, null, context);
		
		verify();
	}
	
	@SuppressWarnings("deprecation")
	public void testIdNotFound() throws ServletException, IOException{
		String id = "id";		
		String crxid = "crxid";
		
		User user = new User();
		
		EasyMock.expect(reqHelper.getCrxIdParameter()).andReturn(crxid);
		EasyMock.expect(reqHelper.getDocumentIdParameter()).andReturn(id);
		
		EasyMock.expect(account.getUserByID(crxid)).andReturn(user);
		
		EasyMock.expect(doc.getDocument(id)).andReturn(null);
		
		respHelper.printHTMLMessage("Запрошенного Вами файла не существует.");
		
		replay();
		
		servlet.handleRequest(null, null, context);
		
		verify();
	}
	
	@SuppressWarnings("deprecation")
	public void testIdFound() throws ServletException, IOException{
		String id = "id";		
		String name = "name";
		String mime = "mime";
		String crxid = "crxid";
		String docName = "document";
		
		User user = new User();
		
		InputStream content = new StringInputStream("source"); 
		
		DocumentInfo info = new DocumentInfo(content, name, mime, docName);
		
		EasyMock.expect(reqHelper.getCrxIdParameter()).andReturn(crxid);
		EasyMock.expect(reqHelper.getDocumentIdParameter()).andReturn(id);
		
		EasyMock.expect(account.getUserByID(crxid)).andReturn(user);
		
		EasyMock.expect(doc.getDocument(id)).andReturn(info);
		
		activity.createDocumentRequestActivity(user, docName);
				
		respHelper.returnContent(name, mime, content);
		
		replay();
		
		servlet.handleRequest(null, null, context);
		
		verify();
	}
}
