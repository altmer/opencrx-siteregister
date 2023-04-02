package ru.obcon.siteregcrm.utils;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opencrx.kernel.generic.SecurityKeys;
import org.opencrx.kernel.utils.Utils;
import org.openmdx.base.mof.cci.Model_1_0;
import org.openmdx.base.naming.Path;

import ru.obcon.siteregcrm.manager.AccountManager;
import ru.obcon.siteregcrm.manager.ActivityManager;
import ru.obcon.siteregcrm.manager.CodeManager;
import ru.obcon.siteregcrm.manager.DocumentManager;
import ru.obcon.siteregcrm.manager.RequestHelper;
import ru.obcon.siteregcrm.manager.ResponseHelper;
import ru.obcon.siteregcrm.manager.XmlManager;
import ru.obcon.siteregcrm.objects.ApplicationContext;

/**
 * Constructs ApplicationContext object.
 * @author ikari
 *
 */
public class ApplicationContextFactory {
    
    //-----------------------------------------------------------------------
    public static ApplicationContext createContext(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws Exception {
    	if (request.getSession() == null) return null;

        // Get model
        if(model == null) {
            model = Utils.getModel();
        }        
        // Get persistence manager factory
        if(persistenceManagerFactory == null) {
            persistenceManagerFactory = Utils.getPersistenceManagerProxyFactory();
        }
        
        String providerName = "CRX";
        String segmentName = (String) ((Context) new InitialContext().lookup("java:comp/env"))
        																.lookup(Const.SEGMENT_RESOURCE);
        
        PersistenceManager persistenceManager =  persistenceManagerFactory.getPersistenceManager(
                SecurityKeys.ADMIN_PRINCIPAL + SecurityKeys.ID_SEPARATOR + segmentName, 
                request.getSession().getId()
            );
        
        XmlManager xml = new XmlManager();
        
        CodeManager code = new CodeManager(persistenceManager,             
        		(org.opencrx.kernel.code1.jmi1.Segment)persistenceManager.getObjectById(
        				new Path("xri://@openmdx*org.opencrx.kernel.code1/provider/" + providerName + "/segment/" + segmentName)
        		));
        
        AccountManager account = new AccountManager(persistenceManager, 
        		(org.opencrx.kernel.account1.jmi1.Segment)persistenceManager.getObjectById(
        				new Path("xri://@openmdx*org.opencrx.kernel.account1/provider/" + providerName + "/segment/" + segmentName)
        		), providerName, segmentName);
        
        ActivityManager activity = new ActivityManager(persistenceManager, 
        		(org.opencrx.kernel.activity1.jmi1.Segment)persistenceManager.getObjectById(
        				new Path("xri://@openmdx*org.opencrx.kernel.activity1/provider/" + providerName + "/segment/" + segmentName)
        		), providerName, segmentName, account, xml);
        
        DocumentManager doc = new DocumentManager(persistenceManager,  
        		(org.opencrx.kernel.document1.jmi1.Segment)persistenceManager.getObjectById(
        				new Path("xri://@openmdx*org.opencrx.kernel.document1/provider/" + providerName + "/segment/" + segmentName)
        		), xml);
        
        RequestHelper reqHelper = new RequestHelper(request, code);
        
        ResponseHelper respHelper = new ResponseHelper(response);
        
        return new ApplicationContext(persistenceManager,account,activity,doc,code,reqHelper, respHelper, 
        									xml);        
    }
    
	public static ApplicationContext createContext(PersistenceManager pm, AccountManager account,
			ActivityManager activity, DocumentManager doc, CodeManager code,RequestHelper reqHelper,
	        ResponseHelper respHelper, XmlManager xml) {
		
		return new ApplicationContext(pm, account, activity, doc, code, reqHelper, respHelper, xml);
		
	}
    
    //-----------------------------------------------------------------------
    // Members
    //-----------------------------------------------------------------------
    private static PersistenceManagerFactory persistenceManagerFactory = null;
    private static Model_1_0 model = null;
    
}
