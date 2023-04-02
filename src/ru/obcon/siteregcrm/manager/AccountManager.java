package ru.obcon.siteregcrm.manager;

import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.log4j.Logger;
import org.opencrx.kernel.account1.cci2.AccountAddressQuery;
import org.opencrx.kernel.account1.cci2.ContactQuery;
import org.opencrx.kernel.account1.cci2.EMailAddressQuery;
import org.opencrx.kernel.account1.jmi1.AccountAddress;
import org.opencrx.kernel.account1.jmi1.Contact;
import org.opencrx.kernel.account1.jmi1.EMailAddress;
import org.opencrx.kernel.account1.jmi1.PhoneNumber;
import org.opencrx.kernel.account1.jmi1.PostalAddress;
import org.opencrx.kernel.account1.jmi1.Segment;
import org.opencrx.kernel.backend.Addresses;
import org.opencrx.kernel.home1.jmi1.EMailAccount;
import org.opencrx.kernel.home1.jmi1.UserHome;
import org.opencrx.security.realm1.jmi1.PrincipalGroup;

import org.openmdx.base.exception.ServiceException;
import org.openmdx.base.naming.Path;
import org.openmdx.base.persistence.cci.UserObjects;
import org.openmdx.kernel.id.UUIDs;
import org.openmdx.kernel.id.cci.UUIDGenerator;
import org.openmdx.security.realm1.jmi1.Principal;
import org.openmdx.security.realm1.jmi1.Realm;

import ru.obcon.siteregcrm.objects.User;
import ru.obcon.siteregcrm.utils.Const;
import ru.obcon.siteregcrm.utils.ObcLogger;

/**
 * Utility class providing methods working with Contacts, Addresses, Principals, SecurityRealms 
 * and UserHomes.
 * @author ikari
 *
 */
public class AccountManager {
	
	protected PersistenceManager pm;
	protected Segment segment;
	protected String provider;
	protected String segmentName;
	
	public AccountManager(PersistenceManager pm, Segment segment, String prov, String segmName){
		this.pm = pm;
		this.segment = segment;
		this.provider = prov;
		this.segmentName = segmName;
	}
	
	/* ************************************************************************************************ */
	/*							Public utility methods													*/
	/* ************************************************************************************************ */
	
	public PostalAddress createPostalAddress(Contact contact){
    	UUIDGenerator uuids = UUIDs.getGenerator();
        
		PostalAddress postalAddress = pm.newInstance(PostalAddress.class);
        postalAddress.refInitialize(false, false);
        contact.addAddress(
            false,
            uuids.next().toString(),
            postalAddress
        );
        
        return postalAddress;
	}
	
	public PhoneNumber createPhoneNumber(Contact contact){
    	UUIDGenerator uuids = UUIDs.getGenerator();
        
    	PhoneNumber res = pm.newInstance(PhoneNumber.class);
		res.refInitialize(false, false);
        contact.addAddress(
            false,
            uuids.next().toString(),
            res
        );
        
        return res;
	}
	
	public EMailAddress createEmailAddress(Contact contact){
    	UUIDGenerator uuids = UUIDs.getGenerator();
        
    	EMailAddress res = pm.newInstance(EMailAddress.class);
		res.refInitialize(false, false);
        contact.addAddress(
            false,
            uuids.next().toString(),
            res
        );
        
        return res;
	}	
	/**
	 * Returns all business addresses of the given contact.
	 * @param contact
	 * @param pm
	 * @return
	 */
	public List<AccountAddress> getBusinessAddresses(Contact contact){
	    AccountAddressQuery businessAddressQuery = 
	    		(AccountAddressQuery)pm.newQuery(AccountAddress.class); 
		businessAddressQuery.thereExistsUsage().equalTo(Addresses.USAGE_BUSINESS);
		businessAddressQuery.forAllDisabled().isFalse();
		
		return contact.getAddress(businessAddressQuery);
	}
	
	public EMailAddress getBusinessEmailAddress(Contact contact){
		List<AccountAddress> addresses = getBusinessAddresses(contact);
		
		for (AccountAddress address : addresses){
			if (address instanceof EMailAddress)
				return (EMailAddress) address;
		}
		
		return null;
	}
    
    /**
     * Finds EMailAddress by name.
     * @param context
     * @param address
     * @return
     */
    public EMailAddress findEmailAddress(String address){
        EMailAddressQuery query = (EMailAddressQuery) 
        						pm.newQuery(EMailAddress.class);
        query.thereExistsEmailAddress().like(address);
        query.thereExistsUsage().equalTo(Addresses.USAGE_BUSINESS);
        query.forAllDisabled().isFalse();
        List<EMailAddress> list = segment.getAddress(query);
        if(list != null && list.size() > 0)
        	return list.get(0);
        else
        	return null;    	
    }
    
    /**
     * Returns UserHome of admin of default segment.
     * @param context
     * @return
     * @throws ServiceException
     */
    public UserHome getAdminHome() throws ServiceException {
  	    	List<String> principalChain = UserObjects.getPrincipalChain(pm);
  	    	return (UserHome)pm.getObjectById(
  	    		new Path(new String[]{
  	              "org:opencrx:kernel:home1",
  	              "provider",
  	              provider,
  	              "segment",
  	              segmentName,
  	              "userHome",
  	              principalChain.get(0)
  	            })    		
  	    	);
  	    }
    
    public EMailAddress getDefaultEMailAddress (UserHome home){
        Collection<EMailAccount> eMailAccounts = home.getEMailAccount();
        EMailAccount eMailAccountUser = null;
        
        for(EMailAccount obj: eMailAccounts) {
            if((obj.isDefault() != null) && obj.isDefault().booleanValue()) {
               eMailAccountUser = obj;
               break;
            }
        }
        
        if (eMailAccountUser == null){
        	logger.error("Admin doesn't have default EMailAccount! Can't send e-mail.");
        	return null;
        }
        
        return findEmailAddress(eMailAccountUser.getReplyEMailAddress() );
    	
    }
    

	/**
	 * Creates new Contact record based on User object
	 */
	public Contact create(User newValue){
		Transaction tx = null;
	    try {
	        tx = pm.currentTransaction();       
	        tx.begin();
	        Contact contact = pm.newInstance(Contact.class);
	        contact.refInitialize(false, false);
	        
	        newValue.update(contact, this);
	        
	        // access rights
	        contact.setAccessLevelBrowse((short) 3);   // ACCESS_LEVEL_DEEP
	        contact.setAccessLevelUpdate((short) 3);   // ACCESS_LEVEL_DEEP
	        contact.setAccessLevelDelete((short) 2);   // ACCESS_LEVEL_BASIC
	        
	        segment.addAccount(
	            false,
	            newValue.getID(),
	            contact
	        );
	        
	        tx.commit();
	        
	        tx = pm.currentTransaction();       
	        tx.begin();
	        
	        // set owning group "Users"
	        Realm realm = getRealm();
	        
	        PrincipalGroup principalGroup = (PrincipalGroup) findPrincipal(Const.OWNING_GROUP, realm);
	        
	        Contact contact2 = this.getContactByID(contact.refGetPath().getBase());
	        contact2.getOwningGroup().clear();
	        contact2.getOwningGroup().add(principalGroup);
	        
	        tx.commit();
	        
	        return contact2;
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

	/**
	 * Retrieves User object by its primary key.  
	 */
	public User getUserByID(
	    String id
	) {
		Contact contact = this.getContactByID(id);
		if (contact == null){
			logger.warn("Contact with ID " + id + " is not found.");
			return null;
		}
		return new User(contact, this);
	}

	/**
	 * Retrieves Contact object by its primary key.
	 * @param key
	 * @return
	 */
	public Contact getContactByID(String id){
	    if(id.length() > 0) {
	        Contact contact = (Contact)segment.getAccount(id);
	        return contact;
	    }
	    else {
	        return null;
	    }    	
	}

	/**
	 * Returns contact with given e-mail
	 * @param email
	 * @return
	 */
	public Contact getContactByEmail(String email) {
		List<Contact> contacts = getAllContacts();
		
		for (Contact contact : contacts){
			List<AccountAddress> addresses = getBusinessAddresses(contact);
			for (AccountAddress address : addresses){
				if (address instanceof EMailAddress){
					EMailAddress mail = (EMailAddress)address;
					if (mail != null && mail.getEmailAddress().equals(email)){
						return contact;
					}
				}
			}
			
		}
		return null;
	}

	/* ************************************************************************************************ */
	/*							Private helper methods													*/
	/* ************************************************************************************************ */

	/**
	 * Returns all registered contacts.
	 * @param context
	 * @return
	 */
	private List<Contact> getAllContacts(){
        ContactQuery query = (ContactQuery)pm.newQuery(Contact.class);
        return segment.getAccount(query);		
	}

	/**
	 * Returns principal by name. 
	 * @param name
	 * @param realm
	 * @param pm
	 * @return
	 */
    private Principal findPrincipal(
            String name,
            Realm realm
        ) {
            try {
                return (Principal)pm.getObjectById(realm.refGetPath().getDescendant
                											(new String[]{"principal", name})
                );
            }
            catch(Exception e) {
                return null;
            }
        }

    /**
     * Returns security realm by name.
     * @param pm
     * @param providerName
     * @param segmentName
     * @return
     */
    private Realm getRealm() {
            return (Realm)pm.getObjectById(
                new Path("xri:@openmdx:org.openmdx.security.realm1/provider/" + provider + 
                					"/segment/Root/realm/" + segmentName)
            );
        }
	
    private static Logger logger = ObcLogger.getLogger();
	
}
