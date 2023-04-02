package ru.obcon.siteregcrm.manager;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.mail.Message;

import org.apache.log4j.Logger;
import org.opencrx.kernel.account1.jmi1.Contact;
import org.opencrx.kernel.account1.jmi1.EMailAddress;
import org.opencrx.kernel.activity1.cci2.ActivityCategoryQuery;
import org.opencrx.kernel.activity1.jmi1.ActivityCategory;
import org.opencrx.kernel.activity1.jmi1.ActivityCreator;
import org.opencrx.kernel.activity1.jmi1.ActivityGroup;
import org.opencrx.kernel.activity1.jmi1.ActivityGroupAssignment;
import org.opencrx.kernel.activity1.jmi1.ActivityProcess;
import org.opencrx.kernel.activity1.jmi1.ActivityType;
import org.opencrx.kernel.activity1.jmi1.EMail;
import org.opencrx.kernel.activity1.jmi1.Segment;

import org.opencrx.kernel.backend.Activities;

import org.opencrx.security.realm1.jmi1.PrincipalGroup;
import org.openmdx.base.exception.ServiceException;
import org.openmdx.base.text.conversion.UUIDConversion;
import org.openmdx.kernel.id.UUIDs;

import org.w3c.dom.Document;

import ru.obcon.siteregcrm.objects.User;
import ru.obcon.siteregcrm.utils.Const;
import ru.obcon.siteregcrm.utils.ObcLogger;
/*
class MyICalendar extends ICalendar{
	public MyICalendar(){}

    public BasicObject importItem(
            byte[] item,
            Activity activity,
            short locale,
            List<String> errors,
            List<String> report,
            boolean isEMailAddressLookupCaseInsensitive,
            boolean isEMailAddressLookupIgnoreDisabled,
            org.opencrx.kernel.account1.jmi1.Segment accountSegment
        ) throws ServiceException{
        try {
            InputStream is = new ByteArrayInputStream(item);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder ical = new StringBuilder();
            Map<String,String> icalFields = this.parseICal(
                reader,
                ical
            );
            SysLog.trace("ICalendar", icalFields);
            return this.importItem(
                ical.toString(),
                icalFields,
                activity,
                accountSegment,
                locale,
                errors,
                report,
                isEMailAddressLookupCaseInsensitive,
                isEMailAddressLookupIgnoreDisabled                
            );
        }
        catch(IOException e) {
        	SysLog.warning("Can not read item", e.getMessage());
        }
        return null;
	}
	
	
}
*/
/**
 * Utility class providing methods working with Activities.
 * @author ikari
 *
 */
public class ActivityManager extends Activities{
	
	protected PersistenceManager pm;
	protected Segment segment;
	protected String provider;
	protected String segmentName;
	protected AccountManager accountManager;
	protected XmlManager xml;
	
	public ActivityManager(PersistenceManager pm, Segment segment, String prov, String segmName,
								AccountManager account, XmlManager xml){
		this.pm = pm;
		this.segment = segment;
		this.provider = prov;
		this.segmentName = segmName;
		this.accountManager = account;
		this.xml = xml;
	}
	
	/* ************************************************************************************************ */
	/*							Public utility methods													*/
	/* ************************************************************************************************ */

	public void sendEmail(String requestNumber, String message, Contact contact) {
		
		ActivityType type = findActivityType(Activities.ACTIVITY_TYPE_NAME_EMAILS, segment, pm);
		
		ActivityProcess process = type.getControlledBy();
		
		ActivityCreator creator = findActivityCreator(Activities.ACTIVITY_CREATOR_NAME_EMAILS, segment);

		Transaction tx = null;
	    try {
	        tx = pm.currentTransaction();       
	        tx.begin();
	        
	        EMail newActivity = createActivity(null, creator, process, type);
	        
	        // name of activity
	        newActivity.setName("E-Mail оповещение клиенту " + contact.getLastName());
	        // save date of interaction 
	        newActivity.setSendDate(new Date());
	        // save EMailAddress
	        EMailAddress userAddress = accountManager.getBusinessEmailAddress(contact);
	        
	        // set sender
	        newActivity.setSender(accountManager.getDefaultEMailAddress(accountManager.getAdminHome()));
	        
	        // set reported to 
	        newActivity.setReportingContact(contact);
	        
	        // Scheduled duration
	        newActivity.setScheduledStart(new Date());
	        newActivity.setScheduledEnd(new Date());
	        
	        // Mail message
	        newActivity.setMessageSubject("Запрос #" + requestNumber);
	        newActivity.setMessageBody(message);
	        
	    	segment.addActivity(
	        		false,
	        		UUIDConversion.toUID(UUIDs.getGenerator().next()),
	        		newActivity
	        	);
	        
	        tx.commit();

	        addEmailRecipient(pm, newActivity, userAddress, Message.RecipientType.TO);
	        
	        sendEMail(newActivity);
	    }catch(Exception e) {
	    	if(tx != null) {
	    		try {
	    			tx.rollback();
	    		}
	    		catch(Exception e0) {}
	    	}
	        new ServiceException(e).log();
	    }
	}
	
	/**
	 * Creates new Activity based on User object.
	 * @param user
	 */
	public void createDocumentRequestActivity(User user, String documentName) {
		ActivityCategory category = initActivityCategory(Const.ACTIVITY_CATEGORY, null, pm, provider, segmentName);
		
		ActivityProcess process = findActivityProcess(Activities.ACTIVITY_PROCESS_NAME_BUG_AND_FEATURE_TRACKING,
									segment, pm);

		ActivityType type = initActivityType( Const.ACTIVITY_TYPE, Activities.ACTIVITY_CLASS_EMAIL, process, 
								pm, provider, segmentName);
		
		List <ActivityGroup> groups = new LinkedList<ActivityGroup>();
		groups.add (category);
		
		ActivityCreator creator = initActivityCreator(Const.ACTIVITY_CREATOR, type, groups, null, pm, provider, segmentName);
		
		ActivityProcess activityProcess = type.getControlledBy();
		
		Transaction tx = null;
	    try {
	        tx = pm.currentTransaction();       
	        tx.begin();
	        
	        EMail newActivity = createActivity(category, creator, activityProcess, type);
	        
	        // name of activity equal to the name of the contact
	        newActivity.setName("Запрос с сайта: " + user.getName());
	        // save date of interaction 
	        newActivity.setSendDate(new Date());
	        // save EMailAddress
	        EMailAddress userAddress = accountManager.findEmailAddress(user.getEmail());
	        
	        if (userAddress != null)
	        	newActivity.setSender(userAddress);
	        
	        // save city
	        newActivity.setLocation(user.getCity());
	        // save company
	        newActivity.setMisc1(user.getCompany());
	        // save phone
	        newActivity.setMisc2(user.getPhoneNumber());
	        // save source
	        newActivity.setDescription("Загрузка документа: " + documentName);
	        
	        // set reported to created contact
	        newActivity.setReportingContact(accountManager.getContactByID(user.getID()));
	        
	    	segment.addActivity(
	        		false,
	        		UUIDConversion.toUID(UUIDs.getGenerator().next()),
	        		newActivity
	        	);
	    	
	        tx.commit();
	    }
	    catch(Exception e) {
	    	if(tx != null) {
	    		try {
	    			tx.rollback();
	    		}
	    		catch(Exception e0) {}
	    	}
	        new ServiceException(e).log();
	    }
	}
	
	public EMail createUserRequestActivity(Contact contact,
			ActivityCategory category, String description, String name,
			short priority) {
		
		ActivityProcess process = findActivityProcess(Activities.ACTIVITY_PROCESS_NAME_BUG_AND_FEATURE_TRACKING,
														segment, pm);

		ActivityType type = initActivityType( Const.ACTIVITY_TYPE, Activities.ACTIVITY_CLASS_EMAIL, process,
								pm, provider, segmentName);
		
		List <ActivityGroup> groups = new LinkedList<ActivityGroup>();
		groups.add (category);
		
		ActivityCreator creator = initActivityCreator(Const.ACTIVITY_CREATOR, type, groups, null, pm, provider, segmentName);
		
		ActivityProcess activityProcess = type.getControlledBy();
		
		Transaction tx = null;
	    try {
	        tx = pm.currentTransaction();       
	        tx.begin();
	        
	        EMail newActivity = createActivity(category, creator, activityProcess, type);
	        
	        // name of activity equal to the name of request
	        newActivity.setName(name);
	        // save date of interaction 
	        newActivity.setSendDate(new Date());
	        // save description
	        newActivity.setDescription(description);
	        // save priority
	        newActivity.setPriority(priority);
	        
	        // save EMailAddress
	        EMailAddress userAddress = accountManager.getBusinessEmailAddress(contact);
        
	        // set sender
	        newActivity.setSender(accountManager.getDefaultEMailAddress(accountManager.getAdminHome()));
	        
	        // set reported to requesting contact
	        newActivity.setReportingContact(contact);
	        
	    	segment.addActivity(
	        		false,
	        		UUIDConversion.toUID(UUIDs.getGenerator().next()),
	        		newActivity
	        	);
	    	
	    	
	        tx.commit();

	        pm.currentTransaction().begin();
	        newActivity.setMessageSubject("#" + newActivity.getActivityNumber() + " " + name);
	        pm.currentTransaction().commit();
	        
	        addEmailRecipient(pm, newActivity, userAddress, Message.RecipientType.TO);
	        
	        return newActivity;
	    }
	    catch(Exception e) {
	    	if(tx != null) {
	    		try {
	    			tx.rollback();
	    		}
	    		catch(Exception e0) {}
	    	}
	        new ServiceException(e).log();
	        return null;
	    }
	} 

	
	public void addCategoriesList(Contact contact, Document document){
		List<ActivityCategory> categories = getAllActivityCategories();
		
		List<org.opencrx.security.realm1.cci2.PrincipalGroup> contactGroups = contact.getOwningGroup();
		
		for (ActivityCategory category : categories){
			logger.debug("Category: " + category.getName());
			List<org.opencrx.security.realm1.cci2.PrincipalGroup> categoryGroups = category.getOwningGroup();
			
			loop:
			for (org.opencrx.security.realm1.cci2.PrincipalGroup contactGroup : contactGroups){
				logger.debug("User group: " + contactGroup.getName());
				
				for (org.opencrx.security.realm1.cci2.PrincipalGroup categoryGroup : categoryGroups){
					logger.debug("Category group: " + categoryGroup.getName());
					
					if (contactGroup.getName() != null && contactGroup.getName().equals(categoryGroup.getName())){
						xml.addCategoryInfo(document, category);
						
						break loop;
					}
				}
			}
		}
	}
	
	public ActivityCategory getActivityCategoryById(String id){
	    if(id.length() > 0) {
	        ActivityCategory category = (ActivityCategory)segment.getActivityCategory(id);
	        return category;
	    }
	    else {
	        return null;
	    }    	

	}

	/* ************************************************************************************************ */
	/*							Private helper methods													*/
	/* ************************************************************************************************ */
	
	private List<ActivityCategory> getAllActivityCategories(){
		ActivityCategoryQuery query = (ActivityCategoryQuery) pm.newQuery(ActivityCategory.class);	
		return segment.getActivityCategory(query);
	}
	
	private EMail createActivity(ActivityCategory category, 
				ActivityCreator creator, ActivityProcess activityProcess, ActivityType type){
		
        
        short priority = creator.getPriority() != 0 ? creator.getPriority() : (short)2;
        short icalType = creator.getIcalType();
        
        EMail newActivity =  pm.newInstance(EMail.class);
        newActivity.refInitialize(false, false);
        
        newActivity.setPriority(new Short(priority));
        newActivity.setIcalType(new Short(icalType));
        newActivity.setActivityState(new Short((short)0));
        newActivity.setPercentComplete(new Short((short)0));
        newActivity.setProcessState(activityProcess.getStartState());

        // activity type
        newActivity.setActivityType(type);
        
        // access rights
        newActivity.setAccessLevelBrowse((short) 3);   // ACCESS_LEVEL_DEEP
        newActivity.setAccessLevelUpdate((short) 3);   // ACCESS_LEVEL_DEEP
        newActivity.setAccessLevelDelete((short) 2);   // ACCESS_LEVEL_BASIC
        
        // Replace owning groups. The owning groups of the activity is the
        // the union of all owning groups of the assigned activity groups. 
        // This way it is guaranteed that the activity can be viewed in all
        // assigned activity groups.
        List<ActivityGroup> activityGroups = creator.getActivityGroup();
        Set<PrincipalGroup> owningGroups = new HashSet<PrincipalGroup>();
        for(ActivityGroup activityGroup : activityGroups) {
            List<PrincipalGroup> activityOwningGroups = activityGroup.getOwningGroup();
            for(PrincipalGroup group : activityOwningGroups) {
                owningGroups.add(group);
            }
        }
        
        newActivity.getOwningGroup().clear();
        //newActivity.getOwningGroup().addAll(owningGroups);
        newActivity.setLastAppliedCreator(creator);
        
        // Create GroupAssignments
        // Add new group assignment
        ActivityGroupAssignment activityGroupAssignment = 
        	pm.newInstance(ActivityGroupAssignment.class);
        
        activityGroupAssignment.refInitialize(false, false);
        if (category != null)
        	activityGroupAssignment.setActivityGroup(category);
        activityGroupAssignment.getOwningGroup().addAll(owningGroups);
        
        newActivity.addAssignedGroup(
        	false,
        	Const.getNextUUID(),
        	activityGroupAssignment
        );
        return newActivity;
	}

	private static Logger logger = ObcLogger.getLogger();


}
