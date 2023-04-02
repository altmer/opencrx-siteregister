package ru.obcon.siteregcrm.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import ru.obcon.siteregcrm.utils.Const;
import ru.obcon.siteregcrm.utils.ObcLogger;

/**
 * Helper class working with request.
 * @author ikari
 *
 */
public class RequestHelper {

	// --------------------------------------------------------------------------------------------
	// Constructor
	//---------------------------------------------------------------------------------------------
	
	public RequestHelper(HttpServletRequest request, CodeManager codeManager){
		this.request = request;
		this.codeManager = codeManager;
		
		logger.debug("Encoding: " + request.getCharacterEncoding());
		
		// IMPORTANT !! Possible NullPointerException
		if (request.getCharacterEncoding() == null){
			try {
				request.setCharacterEncoding("cp1251");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	// --------------------------------------------------------------------------------------------
	// Methods
	// --------------------------------------------------------------------------------------------
	
	/**
	 * Returns URL of Obc Website Application
	 * @return
	 */
	public String getRequestURL() {
		return codeManager.getSettingValueShort(Const.HOST_PAGE) + request.getContextPath();
	}
	
	/**
	 * Retrieves DOCUMENT_ID_PARAMETER from request
	 * @return
	 */
	public String getDocumentIdParameter(){
		return request.getParameter(Const.DOCUMENT_ID_PARAMETER);
	}
	
	public String getCrxIdParameter() {
		return request.getParameter(Const.CRX_ID_PARAMETER);
	}
	
	public String getLoginParameter(){
		return request.getParameter(Const.LOGIN_PARAMETER);
	}
	
	public String getAccountIdParameter() {
		return request.getParameter(Const.ACCOUNT_ID_PARAMETER);
	}
	/**
	 * Returns BODY of HttpRequest.
	 * @return
	 */
	public String getRequestBody() {
		StringBuilder ret = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader( new InputStreamReader( request.getInputStream(),
					 request.getCharacterEncoding() ) );
			
			char[] buffer = new char[128];
			int bytesRead = -1;
			
			while( (bytesRead = in.read(buffer) ) > 0){
				ret.append(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			logger.error("Can't read request body.");
			e.printStackTrace();
			return null;
		}
		return ret.toString();
	}
	
	
	// ---------------------------------------------------------------------------------------------
	// Members
	// ---------------------------------------------------------------------------------------------
	
	private HttpServletRequest request;
	private CodeManager codeManager;
	private static Logger logger = ObcLogger.getLogger();
	
}
