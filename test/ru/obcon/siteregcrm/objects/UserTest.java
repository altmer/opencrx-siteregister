package ru.obcon.siteregcrm.objects;

import java.util.LinkedList;
import java.util.List;

import org.easymock.EasyMock;
import org.opencrx.kernel.account1.jmi1.AccountAddress;
import org.opencrx.kernel.account1.jmi1.Contact;
import org.opencrx.kernel.account1.jmi1.EMailAddress;
import org.opencrx.kernel.account1.jmi1.PhoneNumber;
import org.opencrx.kernel.account1.jmi1.PostalAddress;
import org.opencrx.kernel.backend.Addresses;
import org.openmdx.base.naming.Path;

import ru.obcon.siteregcrm.objects.User;
import ru.obcon.siteregcrm.test.ContextBaseTest;
import ru.obcon.siteregcrm.utils.Const;

public class UserTest extends ContextBaseTest {
	

	public void testBean(){
		User user = new User();
		String cityTemp = "city";
		String nameTemp = "name";
		String emailTemp = "e-mail";
		String organizationTemp = "organization";
		String sourceTemp = "source";
		String phoneTemp = "123456789";
		
		user.setCity(cityTemp);
		user.setName(nameTemp);
		user.setEmail(emailTemp);
		user.setCompany(organizationTemp);
		user.setSource(sourceTemp);
		user.setPhoneNumber(phoneTemp);
		
		assertTrue(user.getCity().equals(new String(cityTemp)));
		assertTrue(user.getName().equals(new String(nameTemp)));
		assertTrue(user.getEmail().equals(new String(emailTemp)));
		assertTrue(user.getCompany().equals(new String(organizationTemp)));
		assertTrue(user.getSource().equals(new String(sourceTemp)));
		assertTrue(user.getPhoneNumber().equals(new String(phoneTemp)));
		
		user.setCity(null);
		user.setName(null);
		user.setEmail(null);
		user.setCompany(null);
		user.setSource(null);
		user.setPhoneNumber(null);
		
		assertEquals(user.getCity(), "");
		assertEquals(user.getName(), "");
		assertEquals(user.getEmail(), "");
		assertEquals(user.getCompany(), "");
		assertEquals(user.getSource(), "");
		assertEquals(user.getPhoneNumber(), "");
	}
	
	public void testValidity(){
		User user = new User();
		
		assertFalse(user.isValid());
		user.setName("aaa");
		user.setCity("aaa");
		user.setCompany("aaa");
		user.setPhoneNumber("aaa");
		user.setEmail(".@aaa.com");
		assertFalse(user.isValid());
		user.setEmail("wqe.@aaa.com");
		assertFalse(user.isValid());
		user.setEmail("wqe@aaa.com");
		assertTrue(user.isValid());
		user.setEmail("wqe.wqe@aaa.com");
		assertTrue(user.isValid());
		user.setEmail(".wqe.wqe@aaa.com");
		assertFalse(user.isValid());
		user.setEmail("wWe.q1_@aaa.com");
		assertTrue(user.isValid());
	}
	
	public void testPasswordUniqueness(){		
		List<String> passList = new LinkedList<String>();		
		
		for (int i = 0; i < 1000; ++i){
			User user = new User();
			for (String pass : passList){
				assertFalse(pass.equals(user.getPassword()));
			}
			passList.add(user.getPassword());
		}
	}
	
	public void testNewKey(){
		User user = new User();
		assertTrue(user.isNewKey());
		
		user = new User(Const.getNextUUID());
		assertFalse(user.isNewKey());
	}
	
	public void testConstructionFromContact(){
		Contact contact = EasyMock.createMock(Contact.class);
		
		PostalAddress postal = EasyMock.createMock(PostalAddress.class);
		PhoneNumber number = EasyMock.createMock(PhoneNumber.class);
		EMailAddress email = EasyMock.createMock(EMailAddress.class);
		
		List<AccountAddress> list = new LinkedList<AccountAddress>();
		list.add(postal);
		list.add(number);
		list.add(email);
		
		EasyMock.expect(contact.refGetPath()).andReturn(new Path("dsafds/fsdfsd/id"));
		EasyMock.expect(contact.getLastName()).andReturn("lastName");
		EasyMock.expect(contact.getOrganization()).andReturn("organization").times(2);
		EasyMock.expect(contact.getUserString2()).andReturn("source");
		EasyMock.expect(contact.getUserString3()).andReturn("password");
		EasyMock.expect(contact.isDoNotBulkPostalMail()).andReturn(false);
		
		EasyMock.expect(account.getBusinessAddresses(contact)).andReturn(list);
		
		EasyMock.expect(postal.getPostalCity()).andReturn("city");
		EasyMock.expect(number.getPhoneNumberFull()).andReturn("12345");
		EasyMock.expect(email.getEmailAddress()).andReturn("mail@mail.com");
		
		EasyMock.replay(contact, postal, number, email);
		replay();
		
		User user = new User(contact, account);
		
		assertEquals( "id",user.getID() );
		assertEquals( "city",user.getCity() );
		assertEquals( "organization",user.getCompany() );
		assertEquals( "mail@mail.com",user.getEmail() );
		assertEquals( "lastName",user.getName() );
		assertEquals( "password",user.getPassword() );
		assertEquals( "12345",user.getPhoneNumber() );
		assertEquals( "source",user.getSource() );
		assertEquals( false, user.isSubscribe() );
						
		EasyMock.verify(contact, postal, number, email);
		verify();
	}
	
	public void testUpdate(){
		Contact contact = EasyMock.createMock(Contact.class);
		
		PostalAddress postal = EasyMock.createMock(PostalAddress.class);
		PhoneNumber number = EasyMock.createMock(PhoneNumber.class);
		EMailAddress email = EasyMock.createMock(EMailAddress.class);
		
		User user = new User();
		
		user.setCity("city");
		user.setCompany("company");
		user.setEmail("mail@mail.com");
		user.setName("name");
		user.setPassword("pass");
		user.setPhoneNumber("12345");
		user.setSource("source");
		user.setSubscribe(true);
		
		// expectations
		EasyMock.expect(account.getBusinessAddresses(contact)).andReturn(new LinkedList<AccountAddress>());
		EasyMock.expect(account.createPostalAddress(contact)).andReturn(postal);
		EasyMock.expect(account.createEmailAddress(contact)).andReturn(email);
		EasyMock.expect(account.createPhoneNumber(contact)).andReturn(number);
		
		contact.setLastName("name");
		contact.setOrganization("company");
		contact.setUserString2("source");
		contact.setUserString3("pass");
		contact.setDoNotBulkPostalMail(true);
		
		postal.setUsage(Addresses.USAGE_BUSINESS);
		number.setUsage(Addresses.USAGE_BUSINESS);
		email.setUsage(Addresses.USAGE_BUSINESS);
		
		postal.setMain(true);
		number.setMain(true);
		email.setMain(true);
		
		postal.setPostalCity("city");
		email.setEmailAddress("mail@mail.com");
		number.setPhoneNumberFull("12345");
		
		EasyMock.replay(contact, postal, number, email);
		replay();
		
		user.update(contact, account);
		
		EasyMock.verify(contact, postal, number, email);
		verify();
	}
	
}
