package ru.obcon.siteregcrm.manager;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.Query;

import org.easymock.EasyMock;
import org.opencrx.kernel.account1.jmi1.Contact;
import org.opencrx.kernel.activity1.cci2.ActivityCategoryQuery;
import org.opencrx.kernel.activity1.jmi1.ActivityCategory;
import org.opencrx.security.realm1.cci2.PrincipalGroup;
import org.w3c.dom.Document;

import ru.obcon.siteregcrm.manager.ActivityManager;
import ru.obcon.siteregcrm.test.ManagerBaseTest;

public class ActivityManagerTest extends ManagerBaseTest{
	
	private ActivityManager manager;
	
	private interface TempQuery extends ActivityCategoryQuery, Query{}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		manager = new ActivityManager(pm, activitySegment, provider, segmentName, accountManagerMock, 
											xmlManagerMock);
		
	}

	public void testAddCategories(){
		// data
		Contact contact = EasyMock.createMock(Contact.class);
		Document doc = EasyMock.createMock(Document.class);
		
		TempQuery query = EasyMock.createMock(TempQuery.class);
		
		List<PrincipalGroup> contactGroups = new LinkedList<PrincipalGroup>();
		
		PrincipalGroup contactGroupEmpty = EasyMock.createMock(PrincipalGroup.class);
		EasyMock.expect(contactGroupEmpty.getName()).andReturn("").anyTimes();
		
		PrincipalGroup contactGroupNull = EasyMock.createMock(PrincipalGroup.class);
		EasyMock.expect(contactGroupNull.getName()).andReturn(null).anyTimes();
		
		PrincipalGroup contactGroupUsers = EasyMock.createMock(PrincipalGroup.class);
		EasyMock.expect(contactGroupUsers.getName()).andReturn("Users").anyTimes();
		
		contactGroups.add(contactGroupEmpty);
		contactGroups.add(contactGroupNull);
		contactGroups.add(contactGroupUsers);
		
		EasyMock.expect(contact.getOwningGroup()).andReturn(contactGroups);
		
		List<PrincipalGroup> categoryGroups1 = new LinkedList<PrincipalGroup>();

		PrincipalGroup categoryGroupEmpty = EasyMock.createMock(PrincipalGroup.class);
		EasyMock.expect(categoryGroupEmpty.getName()).andReturn("asdf").anyTimes();
		
		PrincipalGroup categoryGroupWrong = EasyMock.createMock(PrincipalGroup.class);
		EasyMock.expect(categoryGroupWrong.getName()).andReturn("Wrong").anyTimes();

		categoryGroups1.add(categoryGroupEmpty);
		categoryGroups1.add(categoryGroupWrong);
		
		List<PrincipalGroup> categoryGroups2 = new LinkedList<PrincipalGroup>();

		PrincipalGroup categoryGroupNull = EasyMock.createMock(PrincipalGroup.class);
		EasyMock.expect(categoryGroupNull.getName()).andReturn(null).anyTimes();
		
		PrincipalGroup categoryGroupUsers = EasyMock.createMock(PrincipalGroup.class);
		EasyMock.expect(categoryGroupUsers.getName()).andReturn("Users").anyTimes();

		categoryGroups2.add(categoryGroupNull);
		categoryGroups2.add(categoryGroupUsers);
		
		List<ActivityCategory> categories = new LinkedList<ActivityCategory>();
		
		ActivityCategory category1 = EasyMock.createMock(ActivityCategory.class);
		EasyMock.expect(category1.getName()).andReturn("category1").anyTimes();
		EasyMock.expect(category1.getOwningGroup()).andReturn(categoryGroups1);
		
		ActivityCategory category2 = EasyMock.createMock(ActivityCategory.class);
		EasyMock.expect(category2.getName()).andReturn("category2").anyTimes();
		EasyMock.expect(category2.getOwningGroup()).andReturn(categoryGroups2);
		
		categories.add(category1);
		categories.add(category2);
		
		// expectations
		EasyMock.expect(pm.newQuery(ActivityCategory.class)).andReturn(query);
		EasyMock.expect(activitySegment.getActivityCategory(query)).andReturn(categories);
		xmlManagerMock.addCategoryInfo(doc, category2);
		
		replay();
		EasyMock.replay(query, contact, doc, contactGroupUsers, contactGroupEmpty, contactGroupNull, 
				categoryGroupEmpty, categoryGroupWrong, categoryGroupNull, categoryGroupUsers, 
				category1, category2);
		
		manager.addCategoriesList(contact, doc);
		
		verify();
		EasyMock.verify(query, contact, doc, contactGroupUsers, contactGroupEmpty, contactGroupNull, 
				categoryGroupEmpty, categoryGroupWrong, categoryGroupNull, categoryGroupUsers, 
				category1, category2);
	}
	
	public void testCreateDocumentActivity(){
		
	}
	
}
