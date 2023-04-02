package ru.obcon.siteregcrm.objects;

import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.opencrx.kernel.account1.jmi1.AccountAddress;
import org.opencrx.kernel.account1.jmi1.Contact;
import org.opencrx.kernel.account1.jmi1.EMailAddress;
import org.opencrx.kernel.account1.jmi1.PhoneNumber;
import org.opencrx.kernel.account1.jmi1.PostalAddress;
import org.opencrx.kernel.backend.Addresses;

import ru.obcon.siteregcrm.manager.AccountManager;
import ru.obcon.siteregcrm.utils.Const;

/**
 * User class which stores a registered user information
 *
 * @author OAZM (initial implementation)
 * @author WFRO (port to openCRX)
 * @author Ikari (port to Open Business Consulting)
 */
public final class User
{
	// --------------------------------------------------------------------------------------------
	// Constructor
	//---------------------------------------------------------------------------------------------
	
    public User( final String id )
    {
        this.id = id;
        newKey = false;
    }

    public User()
    {
    	id = Const.getNextUUID();
    	newKey = true;
    	generatePassword();
    }

    public User(Contact contact, AccountManager accountManager) {
    	
    	// set key
        this.id = contact.refGetPath().getBase();
        newKey = false;
        
        // set last name
        setName( contact.getLastName() );   
        
		List<AccountAddress> addresses = accountManager.getBusinessAddresses(contact);
        // set city
        this.city = "";
        PostalAddress postalAddress = null;
		
	    for (AccountAddress address : addresses){
	    	if (address instanceof PostalAddress){
		   		postalAddress = (PostalAddress)address; 
	       		setCity ( postalAddress.getPostalCity() );
		        break;
	    	}
    	}
        
        // set organization        
        company = contact.getOrganization() == null ? "" : contact.getOrganization();
        
        // set phone number        
        phoneNumber = "";
        PhoneNumber phone = null;
	    for (AccountAddress address : addresses){
	    	if (address instanceof PhoneNumber){
		   		phone = (PhoneNumber)address; 
	        	setPhoneNumber ( phone.getPhoneNumberFull() ); 
		        break;
	    	}
	    }
        
        // set e-mail
        this.email = "";
        EMailAddress mailAddress = null;
	    for (AccountAddress address : addresses){
	    	if (address instanceof EMailAddress){
		   		mailAddress = (EMailAddress)address; 
	        	setEmail( mailAddress.getEmailAddress() ); 
		        break;
	    	}
	    }
        // set source
        setSource( contact.getUserString2() );
        
        // set password
        setPassword ( contact.getUserString3() );
        
        // set subscribe
        setSubscribe( contact.isDoNotBulkPostalMail() );
    }
	
    
    // --------------------------------------------------------------------------------------------
	// Methods
	// --------------------------------------------------------------------------------------------
	
    /**
     * Saves attributes of user in the given Contact object.
     * @param contact
     * @param context
     */
    public void update(
        Contact contact,
        AccountManager accountManager
    ) {
        // set name
        contact.setLastName(
            this.getName()
        );
        
        List<AccountAddress> addresses = accountManager.getBusinessAddresses(contact);
        
        // set the city to the postal address
        PostalAddress postalAddress = null;
        // Find existing postal address
       	for (AccountAddress address : addresses){
       		if (address instanceof PostalAddress){
       			postalAddress = (PostalAddress)address;
       			break;
       		}
       	}
        // Create
        if(postalAddress == null) {
        	postalAddress = accountManager.createPostalAddress(contact);
        }
        
        postalAddress.setUsage(Addresses.USAGE_BUSINESS);
        postalAddress.setMain(true);
        postalAddress.setPostalCity(this.getCity());        
        
        // set organization
        contact.setOrganization(company);
        
        // set phone number
        PhoneNumber phone = null;       
        // Find existing phone number
        for (AccountAddress address : addresses){
        	if (address instanceof PhoneNumber){
        		phone = (PhoneNumber)address;
        		break;
        	}
        }
        
        // Create
        if(phone == null) {
        	phone = accountManager.createPhoneNumber(contact);
        }
        
        phone.setUsage(Addresses.USAGE_BUSINESS);
        phone.setMain(true);
        phone.setPhoneNumberFull(this.getPhoneNumber());

        // set e-mail
        EMailAddress mail = null;       
        // Find existing e-mail
        for (AccountAddress address : addresses){
        	if (address instanceof EMailAddress){
        		mail = (EMailAddress)address;
        		break;
        	}
        }
        
        // Create
        if(mail == null) {
        	mail = accountManager.createEmailAddress(contact);
        }
        
        mail.setUsage(Addresses.USAGE_BUSINESS);
        mail.setMain(true);
        mail.setEmailAddress( this.getEmail() );
        
        // set source
        contact.setUserString2( this.getSource() );
        
        // set password
        contact.setUserString3( this.getPassword() );
        
        // set subscribe
        contact.setDoNotBulkPostalMail( this.isSubscribe() );
    }
    
    /* ********************************************************************************************* */
    /*                                Getters and setters.                                           */
    /* ********************************************************************************************* */
    
    public final String getID()
    {
        return id;
    }
    
	public final boolean isNewKey(){
		return newKey;
	}
	
	public final String getPassword(){
		return password;
	}
	
	public final void setPassword(String pass){
        if( null == pass )
            this.password = "";
        else
            this.password = pass;
	}
	
    public final String getName(){
        return name;
    }

    public final void setName( final String name ){
        if( null == name )
            this.name = "";
        else
            this.name = name;
    }

    public final String getCity(){
        return city;
    }

    public final void setCity( final String city ){
        if( null == city )
            this.city = "";
        else
            this.city = city;
    }

	public final String getCompany() {
		return company;
	}

	public final void setCompany(final String company) {
		if (null == company)
			this.company = "";
		else
			this.company = company;
	}
	
	public final String getPhoneNumber() {
		return phoneNumber;
	}

	public final void setPhoneNumber(final String phoneNumber) {
		if (null == phoneNumber)
			this.phoneNumber = "";
		else
			this.phoneNumber = phoneNumber;
	}
	
	public final String getEmail() {
		return email;
	}

	public final void setEmail(final String email) {
		if (null == email)
			this.email = "";
		else
			this.email = email;
	}
	
	public final String getSource() {
		return source;
	}

	public final void setSource(final String source) {
		if (null == source)
			this.source = "";
		else
			this.source = source;
	}
	
    public boolean isSubscribe() {
		return subscribe;
	}

	public void setSubscribe(boolean subscribe) {
		this.subscribe = subscribe;
	}
	
	/* ************************************************************************************* */
	/*                                   Utility methods.                                    */
	/* ************************************************************************************* */

	/**
     * Returns true if all properties are valid.
     */
    public final boolean isValid()
    {
    	// null checks
    	if (getCity() == null || getEmail() == null || getName() == null || getCompany() == null 
    			|| getPhoneNumber() == null || getSource() == null)
    		return false;
    	
    	// Blank checks
        if( getCity().equals( "" ) ||  getEmail().equals( "" ) || getName().equals( "" ) 
        		|| getCompany().equals( "" ) || getPhoneNumber().equals( "" ) )
            return false;

        if (!isEmailValid())
        	return false;
        // All OK
        return true;
    }
    
    private boolean isEmailValid(){
    	Pattern mailPattern = Pattern.compile("^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", 
    								Pattern.CASE_INSENSITIVE);
    	return mailPattern.matcher(getEmail()).matches();
    }

    public final int compareTo( final Object o )
    {
        return compareTo( (User) o );
    }

    /**
     * Compares the names of the objects
     */
    public final int compareTo( final User o )
    {
        if( null == this.name ) return 1;
        return (this.name.compareTo( o.getName() ));
    }

    /**
     * If both objects has the same key then they are equal
     */
    public final boolean equals( final Object obj )
    {
        if( obj instanceof User )
        {
            final User o = (User) obj;

            if( o.getID().equals( o.getID() ) )
                return true;
            else
                return false;
        }
        else
            return false;
    }

    /**
     * Generates new password for user.
     */
    private final void generatePassword(){
    	char[] pass = new char[PASSWORD_LENGTH];
    	for (int i = 0; i < pass.length; ++i)
    		pass[i] = passwordCharacters[ rand.nextInt(passwordCharacters.length)];
    	password = new String(pass);
    }
    
    @Override
    public final String toString(){
    	return "ID: " + getID() + "\n" +
    			"Name: " + getName() + "\n" +
    			"City: " + getCity() + "\n" +
    			"Company: " + getCompany() + "\n" +
    			"Phone number: " + getPhoneNumber() + "\n" +
    			"E-mail: " + getEmail() + "\n" +
    			"Source: " + getSource() + "\n" +
    			"Subscribe: " + isSubscribe() + "\n";
    }
    
	// ---------------------------------------------------------------------------------------------
	// Members
	// ---------------------------------------------------------------------------------------------

    private static final long serialVersionUID = 4651323221910684892L;

    private String id;
    private String name = "";
    private String city = "";
    private String company = "";
    private String phoneNumber = "";
    private String email = "";
    private String source = "";
    private String password = "";
    private boolean subscribe;
    
    
    private final boolean newKey;
    
    private static final char[] passwordCharacters = 
    	"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private static final Random rand = new Random();
    private static final int PASSWORD_LENGTH = 8;
}

