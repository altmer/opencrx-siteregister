package ru.obcon.siteregcrm.test;

import javax.jdo.PersistenceManager;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import ru.obcon.siteregcrm.manager.AccountManager;
import ru.obcon.siteregcrm.manager.XmlManager;

public class ManagerBaseTest extends TestCase{
	
	protected PersistenceManager pm;
	
	protected String provider = "provider";
	protected String segmentName = "segment";
	
	protected org.opencrx.kernel.activity1.jmi1.Segment activitySegment;
	
	protected AccountManager accountManagerMock;
	protected XmlManager xmlManagerMock;
		
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		pm = EasyMock.createMock(PersistenceManager.class);
		activitySegment = EasyMock.createMock(org.opencrx.kernel.activity1.jmi1.Segment.class);
		accountManagerMock = EasyMock.createMock(AccountManager.class);
		xmlManagerMock = EasyMock.createMock(XmlManager.class);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
		
	protected void replay(){
		EasyMock.replay(pm, activitySegment, accountManagerMock, xmlManagerMock);
	}
	
	protected void verify(){
		EasyMock.verify(pm, activitySegment, accountManagerMock, xmlManagerMock);
	}
}
