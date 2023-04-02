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
import ru.obcon.siteregcrm.manager.ActivityManager;
import ru.obcon.siteregcrm.manager.RequestHelper;
import ru.obcon.siteregcrm.manager.ResponseHelper;
import ru.obcon.siteregcrm.manager.XmlManager;
import ru.obcon.siteregcrm.objects.ApplicationContext;
import ru.obcon.siteregcrm.utils.ApplicationContextFactory;
import ru.obcon.siteregcrm.utils.ObcLogger;

@SuppressWarnings("serial")
public class GetCategoryServlet extends HttpServlet {
	
	private final Logger logger = ObcLogger.getLogger();
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {

		logger.debug("GetCategoryServlet: Got GET request...");
		
		ApplicationContext context;
		
		logger.debug("GetCategoryServlet: Retrieving application context...");
		if (testContext == null){
			try{
				context = ApplicationContextFactory.createContext(request, response);
			}catch (Exception e) {
				logger.error("GetCategoryServlet: Can't create AppContext.");
				e.printStackTrace();
				return;
			}
		}else{
			context = testContext;
		}
		
		ResponseHelper responseHelper = context.getResponseHelper();
		RequestHelper requestHelper = context.getRequestHelper();
		
		AccountManager accountManager = context.getAccountManager();
		ActivityManager activityManager = context.getActivityManager();
		XmlManager xml = context.getXmlManager();
		
		String id = requestHelper.getAccountIdParameter();
		
		if (id == null || id.isEmpty()){
			logger.error("GetCategoryServlet: empty id");
			return;
		}

		Contact contact = accountManager.getContactByID(id);
		
		if (contact == null){
			logger.error("GetCategoryServlet: no contact with id = " + id);
			return;
		}
		
		Document doc = xml.getNewCategoriesList();
		
		activityManager.addCategoriesList(contact, doc);
		
		responseHelper.returnXML(xml.xmlToString(doc.getDocumentElement()));
	}

	/**
	* @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	*/
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {}

	@Deprecated
	protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ApplicationContext context) 
		throws ServletException, IOException {
		
		this.testContext = context;
		
		doGet(request, response);
	}

	private ApplicationContext testContext;

}
