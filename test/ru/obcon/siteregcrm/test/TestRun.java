package ru.obcon.siteregcrm.test;

import ru.obcon.siteregcrm.manager.ActivityManagerTest;
import ru.obcon.siteregcrm.manager.XmlManagerTest;
import ru.obcon.siteregcrm.objects.ClientRequestTest;
import ru.obcon.siteregcrm.objects.UserTest;
import ru.obcon.siteregcrm.servlet.CheckAccountServletTest;
import ru.obcon.siteregcrm.servlet.CreateRequestServletTest;
import ru.obcon.siteregcrm.servlet.GetCategoryServletTest;
import ru.obcon.siteregcrm.servlet.GetDocumentServletTest;
import ru.obcon.siteregcrm.servlet.GetLinksServletTest;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class TestRun {
	public static void main(String[] args){
		TestSuite suite = new TestSuite();
		
		suite.addTestSuite(GetLinksServletTest.class);
		suite.addTestSuite(GetDocumentServletTest.class);
		suite.addTestSuite(CheckAccountServletTest.class);
		suite.addTestSuite(GetCategoryServletTest.class);
		suite.addTestSuite(CreateRequestServletTest.class);
		
		suite.addTestSuite(XmlManagerTest.class);
		suite.addTestSuite(ActivityManagerTest.class);
		
		suite.addTestSuite(UserTest.class);
		suite.addTestSuite(ClientRequestTest.class);
		
		TestRunner.run(suite);
	}
}
