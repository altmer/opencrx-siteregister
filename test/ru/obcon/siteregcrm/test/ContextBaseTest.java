package ru.obcon.siteregcrm.test;

import javax.jdo.PersistenceManager;

import org.easymock.EasyMock;

import ru.obcon.siteregcrm.manager.AccountManager;
import ru.obcon.siteregcrm.manager.ActivityManager;
import ru.obcon.siteregcrm.manager.CodeManager;
import ru.obcon.siteregcrm.manager.DocumentManager;
import ru.obcon.siteregcrm.manager.RequestHelper;
import ru.obcon.siteregcrm.manager.ResponseHelper;
import ru.obcon.siteregcrm.manager.XmlManager;
import ru.obcon.siteregcrm.objects.ApplicationContext;
import ru.obcon.siteregcrm.utils.ApplicationContextFactory;
import junit.framework.TestCase;

public class ContextBaseTest extends TestCase{

	protected PersistenceManager pm;
	
	protected AccountManager account;
	protected ActivityManager activity;
	protected DocumentManager doc;
	protected CodeManager code;	
	
	protected RequestHelper reqHelper;
	protected ResponseHelper respHelper;
	
	protected XmlManager xmlManager;
	
	protected ApplicationContext context;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		pm = EasyMock.createMock(PersistenceManager.class);
		
		account = EasyMock.createMock(AccountManager.class);
		activity = EasyMock.createMock(ActivityManager.class);
		doc = EasyMock.createMock(DocumentManager.class);
		code = EasyMock.createMock(CodeManager.class);
		
		reqHelper = EasyMock.createMock(RequestHelper.class);
		respHelper = EasyMock.createMock(ResponseHelper.class);
		
		xmlManager = new XmlManager();
		
		context = ApplicationContextFactory.createContext(pm, account, activity, doc, code, reqHelper, respHelper, xmlManager);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	protected void replay(){
		EasyMock.replay(account, activity, code, doc, pm, reqHelper, respHelper);
	}
	
	protected void verify(){
		EasyMock.verify(account, activity, code, doc, pm, reqHelper, respHelper);
	}
	
}
