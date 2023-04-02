package ru.obcon.siteregcrm.objects;

import javax.jdo.PersistenceManager;

import ru.obcon.siteregcrm.manager.AccountManager;
import ru.obcon.siteregcrm.manager.ActivityManager;
import ru.obcon.siteregcrm.manager.CodeManager;
import ru.obcon.siteregcrm.manager.DocumentManager;
import ru.obcon.siteregcrm.manager.RequestHelper;
import ru.obcon.siteregcrm.manager.ResponseHelper;
import ru.obcon.siteregcrm.manager.XmlManager;

/**
 * Contains segment classes, PersistenceManager, default segment, provider.
 * @author ikari
 */

public class ApplicationContext {

    public ApplicationContext(
        PersistenceManager persistenceManager,
        AccountManager account,
        ActivityManager activity,
        DocumentManager doc,
        CodeManager code,
        RequestHelper reqHelper,
        ResponseHelper respHelper,
        XmlManager xml
    ) {
        this.persistenceManager = persistenceManager;
        this.providerName = "";
        this.segmentName = "";
        
        this.account = account;  
        this.activity = activity;
        this.doc = doc;
        this.code = code;
        
        this.reqHelper = reqHelper;
        this.respHelper = respHelper;  
        this.xml = xml;
    }
    
    public PersistenceManager getPersistenceManager(
    ) {
        return this.persistenceManager;
    }
    
    public String getProviderName(
    ) {
        return this.providerName;
    }
    
    public String getSegmentName(
    ) {
        return this.segmentName;
    }
    
    public AccountManager getAccountManager() {
        return this.account;
    }

    public ActivityManager getActivityManager() {
        return this.activity;
    }
    
    public DocumentManager getDocumentManager() {
        return this.doc;
    }
    
    public CodeManager getCodeManager() {
        return this.code;
    }
    
    public RequestHelper getRequestHelper() {
        return this.reqHelper;
    }
    
    public ResponseHelper getResponseHelper() {
        return this.respHelper;
    }
    
    public XmlManager getXmlManager() {
        return this.xml;
    }
    //-----------------------------------------------------------------------
    // Members
    //-----------------------------------------------------------------------
    private final PersistenceManager persistenceManager;
    private final String providerName;
    private final String segmentName;
    private final AccountManager account;
    private final ActivityManager activity;
    private final DocumentManager doc;
    private final CodeManager code;
    private final RequestHelper reqHelper;
    private final ResponseHelper respHelper;
    private final XmlManager xml;

    
}
