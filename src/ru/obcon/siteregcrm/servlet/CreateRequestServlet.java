package ru.obcon.siteregcrm.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.opencrx.kernel.account1.jmi1.Contact;
import org.opencrx.kernel.activity1.jmi1.ActivityCategory;
import org.opencrx.kernel.activity1.jmi1.EMail;

import ru.obcon.siteregcrm.manager.AccountManager;
import ru.obcon.siteregcrm.manager.ActivityManager;
import ru.obcon.siteregcrm.manager.CodeManager;
import ru.obcon.siteregcrm.manager.RequestHelper;
import ru.obcon.siteregcrm.manager.ResponseHelper;
import ru.obcon.siteregcrm.manager.XmlManager;
import ru.obcon.siteregcrm.objects.ApplicationContext;
import ru.obcon.siteregcrm.objects.ClientRequest;
import ru.obcon.siteregcrm.utils.ApplicationContextFactory;
import ru.obcon.siteregcrm.utils.Const;
import ru.obcon.siteregcrm.utils.ObcLogger;

public class CreateRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = ObcLogger.getLogger();
	
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
														throws ServletException, IOException {
    	
    	logger.debug("CreateRequestServlet: Got GET request...");
    	
    	handleRequest(request, response);
    	
    }
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
														throws ServletException, IOException {
    	
    	logger.debug("CreateRequestServlet: Got POST request...");
		
    	handleRequest(request, response);
    }
    
    private void handleRequest(HttpServletRequest request, HttpServletResponse response) 
													throws ServletException, IOException {
		
		ApplicationContext context;
		
		if (testContext == null){
			logger.debug("CreateRequestServlet: Retrieving application context...");
			try{
				context = ApplicationContextFactory.createContext(request, response);
			}catch (Exception e) {
				logger.error("CreateRequestServlet: Can't create AppContext.");
				e.printStackTrace();
				return;
			}
		}else{
			context = testContext;
		}
		
		RequestHelper requestHelper = context.getRequestHelper();
		ResponseHelper responseHelper = context.getResponseHelper();
		
		XmlManager xmlManager = context.getXmlManager();
		
		AccountManager accountManager = context.getAccountManager();
		ActivityManager activityManager = context.getActivityManager();
		CodeManager codeManager = context.getCodeManager();
		
		String xml = requestHelper.getRequestBody();
		
		ClientRequest req = xmlManager.getClientRequest(xml);
		
		if (req == null){
			logger.error("CreateRequestServlet: wrong request format");
			responseHelper.returnPlainText(Const.ERROR_CODE_REQUEST_ERROR + "," + 
											Const.ERROR_MESSAGE_NOT_VALID_REQUEST);
			return;
		}
		
		if (req.getContactId() == null || req.getContactId().isEmpty()){
			logger.error("CreateRequestServlet: no account ID.");
			responseHelper.returnPlainText(Const.ERROR_CODE_REQUEST_ERROR + "," + 
					Const.ERROR_MESSAGE_NO_USER_ID);
			return;
		}
		
		if (req.getCategoryId() == null || req.getCategoryId().isEmpty()){
			logger.error("CreateRequestServlet: no category ID.");
			responseHelper.returnPlainText(Const.ERROR_CODE_REQUEST_ERROR + "," + 
					Const.ERROR_MESSAGE_NO_CATEGORY_ID);
			return;
		}
		
		Contact contact = accountManager.getContactByID(req.getContactId());
		
		if (contact == null){
			logger.error("CreateRequestServlet: contact with ID = "+ req.getContactId() + " not found.");
			responseHelper.returnPlainText(Const.ERROR_CODE_REQUEST_ERROR + "," + 
					Const.ERROR_MESSAGE_USER_NOT_FOUND);
			return;
		}
		
		ActivityCategory category = activityManager.getActivityCategoryById(req.getCategoryId());
		
		if (category == null){
			logger.error("CreateRequestServlet: category with ID = "+ req.getCategoryId() + " not found.");
			responseHelper.returnPlainText(Const.ERROR_CODE_REQUEST_ERROR + "," + 
					Const.ERROR_MESSAGE_CATEGORY_NOT_FOUND);
			return;
		}
		
		EMail activity  = activityManager.createUserRequestActivity(contact, category, req.getDescription(), req.getName(), req.getPriority());
		
		if (activity == null){
			logger.error("CreateRequestServlet: can't create new activity.");
			responseHelper.returnPlainText(Const.ERROR_CODE_REQUEST_ERROR + "," + 
					Const.ERROR_MESSAGE_CANT_CREATE_ACTIVITY);
			return;
		}
		
		String isEmail = codeManager.getSettingValueShort(Const.IS_EMAIL_NOTIFICATE);
		
		if (isEmail.equals("1")){
			String messageText = codeManager.getSettingValueLong(Const.IS_EMAIL_NOTIFICATE);
			
			messageText = messageText.replace(Const.ACTIVITY_NUMBER_PATTERN, activity.getActivityNumber()).
									  replace(Const.ACTIVITY_NAME_PATTERN, activity.getName()).
									  replace(Const.ACTIVITY_DESCRIPTION_PATTERN, activity.getDescription());
			
			activityManager.sendEmail(activity.getActivityNumber(), messageText, contact);
		}
		
		responseHelper.returnPlainText(activity.getActivityNumber());
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
