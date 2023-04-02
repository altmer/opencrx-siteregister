package ru.obcon.siteregcrm.manager;

import java.io.IOException;

import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;

import org.apache.log4j.Logger;

import org.opencrx.kernel.account1.jmi1.Contact;
import org.opencrx.kernel.base.cci2.SecureObject;
import org.opencrx.kernel.code1.jmi1.CodeValueEntry;
import org.opencrx.kernel.document1.jmi1.DocumentFolderEntry;
import org.opencrx.kernel.document1.cci2.DocumentFolderEntryQuery;
import org.opencrx.kernel.document1.cci2.DocumentFolderQuery;
import org.opencrx.kernel.document1.jmi1.Document;
import org.opencrx.kernel.document1.jmi1.DocumentFolder;
import org.opencrx.kernel.document1.jmi1.Media;
import org.opencrx.kernel.document1.jmi1.Segment;
import org.opencrx.security.realm1.cci2.PrincipalGroup;
import org.openmdx.base.cci2.BasicObject;
import org.openmdx.base.naming.Path;

import ru.obcon.siteregcrm.objects.DocumentInfo;
import ru.obcon.siteregcrm.utils.Const;
import ru.obcon.siteregcrm.utils.ObcLogger;

/**
 * Class handling document request. 
 * @author ikari
 */
public class DocumentManager {

	protected PersistenceManager pm;
	protected Segment segment;
	protected XmlManager xml;
	
	public DocumentManager(PersistenceManager pm, Segment segment, XmlManager xml){
		this.pm = pm;
		this.segment = segment;
		this.xml = xml;
	}
	
	/**
	 * Returns list of documents available for given Contact.
	 * @param user
	 * @param req
	 * @return
	 */
	public void addDocumentList(String fol, Contact user, String reqURL, 
												org.w3c.dom.Document document){
		String url = reqURL + "/GetDocumentServlet?" + Const.DOCUMENT_ID_PARAMETER + "=";
		
		logger.debug("Get DocSegment");
		logger.debug("DocURL: " + url);
		
		DocumentFolderQuery folderQuery = (DocumentFolderQuery)pm.newQuery(DocumentFolder.class);
		folderQuery.name().equalTo(fol);
		
		List<DocumentFolder> folders = segment.getFolder(folderQuery);

		if (folders == null || folders.isEmpty()){
			logger.debug("Folder " + fol + " not found.");
			xml.addErrorMessage(document, Const.ERROR_CODE_NOT_FOUND, Const.ERROR_MESSAGE_NOT_FOUND);
			return;
		}
		
		DocumentFolder folder = folders.iterator().next();
		
		DocumentFolderEntryQuery entryQuery = (DocumentFolderEntryQuery)pm.newQuery(DocumentFolderEntry.class);
		
		Collection<DocumentFolderEntry> entries = folder.getFolderEntry(entryQuery);

		logger.debug("GetFolder: " + folder.getName());
		for (DocumentFolderEntry entry : entries){
			
			BasicObject obj = entry.getDocument();
			
			Document doc = null;
			
			if (obj instanceof Document){
				doc = (Document) obj; 
			}else{
				continue;
			}
			logger.debug("GetDocument: " + doc.getTitle() );
			
			if (isObjectAccessibleByUser(doc, user))
				xml.addDocumentInfo(doc, decodeLiteratureType(doc.getLiteratureType()), 
						url + doc.refGetPath().getBase(), document);
			
		}
		
		for (org.opencrx.kernel.document1.cci2.DocumentFolder f : folder.getFolder()){
			
			if (isObjectAccessibleByUser(f, user)){
				xml.addFolderInfo(f, document);
			}
			
		}
	}
	
	/**
	 * Retrieves media content associated with document DOCUMENT_NAME.
	 * @return
	 */
	public DocumentInfo getDocument(String id){
		Document doc = segment.getDocument(id);
		
		if (doc == null){
			logger.error("Document not found.");
			return null;
		}
		
		if (! (doc.getHeadRevision() instanceof Media)){
			logger.error("HeadRevision is not Media.");
			return null;
		}
		
		Media con = (Media) doc.getHeadRevision();
		try {
			return new DocumentInfo(con.getContent().getContent(), con.getContentMimeType(), con.getContentName(), doc.getName());
		} catch (IOException e) {
			logger.error("DocumentManager.getDocument: IOException");
			e.printStackTrace();
			return null;
		}
	}	
	
	private String decodeLiteratureType(Short literature){
		Object entry = pm.getObjectById(
				new Path("xri://@openmdx*org.opencrx.kernel.code1/provider/CRX/segment/Root/valueContainer/literatureType/entry/" + literature)
		);
		
		String res = "N/A";
		
		if (entry instanceof CodeValueEntry){
			CodeValueEntry code = (CodeValueEntry) entry;
			res = code.getShortText().iterator().next();
		}
		
		return res;		
	}
	
	private boolean isObjectAccessibleByUser(SecureObject doc, Contact user){
		List<PrincipalGroup> groups = doc.getOwningGroup();

		for (PrincipalGroup group : groups){
			
			logger.debug("Get document/folder OwningGroup: " + group.getName());
			
			List<PrincipalGroup> userGroups = user.getOwningGroup();
			
			for (PrincipalGroup userGroup : userGroups){
				
				logger.debug("Get user OwningGroup: " + userGroup.getName());
				
				if (userGroup.getName() != null && userGroup.getName().equals(group.getName())){
					return true;
				}
			}
			
		}
		
		return false;

	}
	
	private static final Logger logger = ObcLogger.getLogger();
	
}
