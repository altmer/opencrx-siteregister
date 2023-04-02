package ru.obcon.siteregcrm.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.opencrx.kernel.account1.jmi1.Contact;

import ru.obcon.siteregcrm.manager.RequestHelper;
import ru.obcon.siteregcrm.manager.ResponseHelper;
import ru.obcon.siteregcrm.objects.ApplicationContext;
import ru.obcon.siteregcrm.utils.ApplicationContextFactory;
import ru.obcon.siteregcrm.utils.Const;
import ru.obcon.siteregcrm.utils.ObcLogger;

/**
 * Checks user registration.
 */
public class CheckAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckAccountServlet() {
        super();
        
        logger = ObcLogger.getLogger();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException, IOException {
		logger.debug("CheckAccountServlet: GET request");
		
		handleRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException, IOException {
		logger.debug("CheckAccountServlet: POST request");
		
		handleRequest(request, response);
	}

	private void handleRequest(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException, IOException {

		logger.debug("CheckAccountServlet: creating AppContext...");
		ApplicationContext context;
		
		if (testContext == null ){
			try{
				context = ApplicationContextFactory.createContext(request, response);
			}catch (Exception e) {
				logger.error("CheckAccountServlet: can't create AppContext");
				e.printStackTrace();
				return;
			}
		}else{
			context = testContext;
		}
		
		ResponseHelper responseHelper = context.getResponseHelper();
		RequestHelper requestHelper = context.getRequestHelper();
		
		logger.debug("CheckAccountServlet: reading request parameter...");
		String login = requestHelper.getLoginParameter();
		logger.debug("Got request: " + login);

		if (login == null || login.isEmpty()){
			logger.warn("CheckAccountServlet: Login empty.");
			responseHelper.returnPlainText(Const.AUTHENTICATION_FAIL);
			return;
		}

		Contact user = context.getAccountManager().getContactByEmail(login);
		
		if (user != null)
			responseHelper.returnPlainText(user.refGetPath().getBase());
		else
			responseHelper.returnPlainText(Const.AUTHENTICATION_FAIL);
		
		
	}
	
	@Deprecated
	protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ApplicationContext context) 
		throws ServletException, IOException {
		
		this.testContext = context;
		
		handleRequest(request, response);
	}
	
	private ApplicationContext testContext;
}
