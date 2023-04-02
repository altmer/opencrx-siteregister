package ru.obcon.siteregcrm.objects;

import ru.obcon.siteregcrm.objects.ClientRequest;
import junit.framework.TestCase;

public class ClientRequestTest extends TestCase {

	public void testClientRequest(){
		ClientRequest req = new ClientRequest();
		
		assertEquals("", req.getContactId());
		assertEquals("", req.getCategoryId());
		assertEquals("", req.getDescription());
		assertEquals("", req.getName());
		assertEquals(1, req.getPriority());
		
		req.setContactId("aid");
		req.setCategoryId("cid");
		req.setDescription("descr");
		req.setName("name");
		req.setPriority((short)2);
		
		assertEquals("aid", req.getContactId());
		assertEquals("cid", req.getCategoryId());
		assertEquals("descr", req.getDescription());
		assertEquals("name", req.getName());
		assertEquals(2, req.getPriority());
		
		req.setContactId(null);
		req.setCategoryId(null);
		req.setDescription(null);
		req.setName(null);
		req.setPriority((short)0);
		
		assertEquals("", req.getContactId());
		assertEquals("", req.getCategoryId());
		assertEquals("", req.getDescription());
		assertEquals("", req.getName());
		assertEquals(1, req.getPriority());
	}
}
