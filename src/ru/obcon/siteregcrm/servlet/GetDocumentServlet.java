package ru.obcon.siteregcrm.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ru.obcon.siteregcrm.manager.AccountManager;
import ru.obcon.siteregcrm.manager.ActivityManager;
import ru.obcon.siteregcrm.manager.DocumentManager;
import ru.obcon.siteregcrm.manager.RequestHelper;
import ru.obcon.siteregcrm.manager.ResponseHelper;
import ru.obcon.siteregcrm.objects.ApplicationContext;
import ru.obcon.siteregcrm.objects.DocumentInfo;
import ru.obcon.siteregcrm.objects.User;
import ru.obcon.siteregcrm.utils.ApplicationContextFactory;
import ru.obcon.siteregcrm.utils.ObcLogger;

/**
 * Servlet handling document download requests.
 */
public class GetDocumentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger logger = ObcLogger.getLogger(); 
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetDocumentServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
															throws ServletException, IOException {
	    
		logger.debug("GetDocumentServlet: Got GET request...");
		
		ApplicationContext context;
		
		logger.debug("GetDocumentServlet: Retrieving application context...");
		if (testContext == null){
			try{
				context = ApplicationContextFactory.createContext(request, response);
			}catch (Exception e) {
				logger.error("GetDocumentServlet: Can't create AppContext.");
				e.printStackTrace();
				return;
			}
		}else{
			context = testContext;
		}

		ResponseHelper responseHelper = context.getResponseHelper();
		RequestHelper requestHelper = context.getRequestHelper();

		DocumentManager documentManager = context.getDocumentManager();
		ActivityManager activityManager = context.getActivityManager();
		AccountManager accountManager = context.getAccountManager();
		
		String id = requestHelper.getDocumentIdParameter();
		String crxId = requestHelper.getCrxIdParameter();
		
		if (crxId == null || crxId.isEmpty()){
			logger.error("GetDocumentServlet: User Id was not provided.");
			
			responseHelper.printHTMLMessage("Неправильная ссылка.");
			
			return;
		}
		
		if (id == null || id.isEmpty()){
			logger.error("GetDocumentServlet: document id was not provided.");
			
			responseHelper.printHTMLMessage("Неправильная ссылка.");
			
			return;
		}
		
		User user = accountManager.getUserByID(crxId);
		
		if (user == null){
			logger.error("GetDocumentServlet: Tried to download document with non-existent Contact.");
			
			responseHelper.printHTMLMessage("Зарегистрируйтесь на сайте для скачивания информационных материалов.");
			
			return;
		}
			
		DocumentInfo docInfo = documentManager.getDocument(id);
	
		if (docInfo == null){
			logger.debug("GetDocumentServlet: Document is not found. Id: " + id);
		
			responseHelper.printHTMLMessage("Запрошенного Вами файла не существует.");
			
			return;
		}
		
		logger.debug("GetDocumentServlet: Document is found.");
		
		activityManager.createDocumentRequestActivity(user, docInfo.getDocumentName());		
		
		responseHelper.returnContent(docInfo.getContentName(), 
				docInfo.getContentMime(), docInfo.getContent());
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
