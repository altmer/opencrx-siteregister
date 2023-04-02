package ru.obcon.siteregcrm.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.opencrx.kernel.account1.jmi1.Contact;
import org.w3c.dom.Document;

import ru.obcon.siteregcrm.manager.AccountManager;
import ru.obcon.siteregcrm.manager.DocumentManager;
import ru.obcon.siteregcrm.manager.RequestHelper;
import ru.obcon.siteregcrm.manager.ResponseHelper;
import ru.obcon.siteregcrm.manager.XmlManager;
import ru.obcon.siteregcrm.objects.ApplicationContext;
import ru.obcon.siteregcrm.objects.User;
import ru.obcon.siteregcrm.utils.ApplicationContextFactory;
import ru.obcon.siteregcrm.utils.Const;
import ru.obcon.siteregcrm.utils.ObcLogger;

/**
 * Returns list of available documents.   
 * @author ikari
 */
public class GetLinksServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = ObcLogger.getLogger();
       
    public GetLinksServlet() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
    }
    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
														throws ServletException, IOException {
    	
    	logger.debug("GetLinksServlet: Got GET request...");
    	
    	handleRequest(request, response);
    	
    }
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
														throws ServletException, IOException {
    	
    	logger.debug("GetLinksServlet: Got POST request...");
		
    	handleRequest(request, response);
    }
    
    private void handleRequest(HttpServletRequest request, HttpServletResponse response) 
													throws ServletException, IOException {
		
		ApplicationContext context;
		
		if (testContext == null){
			logger.debug("GetLinksServlet: Retrieving application context...");
			try{
				context = ApplicationContextFactory.createContext(request, response);
			}catch (Exception e) {
				logger.error("GetLinksServlet: Can't create AppContext.");
				e.printStackTrace();
				return;
			}
		}else{
			context = testContext;
		}
		
		XmlManager xmlManager = context.getXmlManager();
		
		Document doc = xmlManager.getNewDocumentList();
		
		ResponseHelper responseHelper = context.getResponseHelper();
		RequestHelper requestHelper = context.getRequestHelper();

		DocumentManager documentManager = context.getDocumentManager();
		AccountManager accountManager = context.getAccountManager();
		
		String xml = requestHelper.getRequestBody();
		
		logger.debug("Got request: " + xml);
		
		User user = xmlManager.getUser(xml);		
		String folder = xmlManager.getRequestedFolder(xml);
		
		if (user != null && ! user.isNewKey() ){
			logger.debug("GetLinksServlet: User is already logged in.");
			user = accountManager.getUserByID(user.getID());
		}
		
		if ( user == null ){
			logger.debug("GetLinksServlet: User is null.");

			xmlManager.addErrorMessage(doc, Const.ERROR_CODE_NOT_VALID, Const.ERROR_MESSAGE_NOT_VALID);
			
		}else if ( user.isNewKey() && !user.isValid() ){
			logger.debug("User:\n" + user.toString());
			logger.debug("GetLinksServlet: User is not valid.");

			xmlManager.addErrorMessage(doc, Const.ERROR_CODE_NOT_VALID, Const.ERROR_MESSAGE_NOT_VALID);
			
		}else{
			
			Contact contact = null; 

			if (user.isNewKey())
				contact = accountManager.getContactByEmail(user.getEmail());
			else
				contact = accountManager.getContactByID(user.getID());
			
			if (contact == null){
				
				logger.debug("GetLinksServlet: Create new contact - " + user.getName());
				
				contact = accountManager.create(user);
				
				xmlManager.addNewUserInformation(doc, user);
				
			}else{
				logger.debug("Find contact: " + contact.getLastName());
			}
			
			if (folder.isEmpty())
				folder = (Const.DOCUMENT_FOLDER);

			documentManager.addDocumentList(folder, contact, 
												requestHelper.getRequestURL(), doc);
		}
		
		responseHelper.returnXML(xmlManager.xmlToString(doc.getDocumentElement()));			
    }
    
    @Deprecated
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, 
    			ApplicationContext context) 
		throws ServletException, IOException {
    	
    	testContext = context;
    	
    	handleRequest(request, response);
    }

    private ApplicationContext testContext;
}
