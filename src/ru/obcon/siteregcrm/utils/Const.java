package ru.obcon.siteregcrm.utils;

import org.openmdx.base.text.conversion.UUIDConversion;
import org.openmdx.kernel.id.UUIDs;
import org.openmdx.kernel.id.cci.UUIDGenerator;

/**
 * Utility class providing global constants.
 * @author ikari
 *
 */
public class Const {
	private Const(){}
	
    private static final UUIDGenerator uuids = UUIDs.getGenerator();
    
    public static String getNextUUID()
    {
        return UUIDConversion.toUID(uuids.next());
    }

	// CodeValues
	public static final String CODE_VALUE_CONTAINER = "Obc Website Settings";
	public static final String HOST_PAGE = "CRX-SiteReqServlet";
	public static final String ERROR_PAGE = "Error Page";
	public static final String DOCUMENTLIST_PAGE = "DocumentList Page";
	public static final String IS_EMAIL_NOTIFICATE = "is_email_notificate";
	
	// HttpRequest parameters
	public static final String DOCUMENT_ID_PARAMETER = "document";
	public static final String LOGIN_PARAMETER = "login";
	public static final String ACCOUNT_ID_PARAMETER = "account-id";
	public static final String CRX_ID_PARAMETER = "crxid";
	
	// Error codes
	public static final String ERROR_CODE_OK = "0";
	public static final String ERROR_CODE_NOT_VALID = "1";
	public static final String ERROR_CODE_NOT_FOUND = "2";
	public static final String ERROR_CODE_REQUEST_ERROR = "-1";
	
	// Error messages
	public static final String ERROR_MESSAGE_NOT_FOUND = "Запрошенная папка не существует.";
	public static final String ERROR_MESSAGE_NOT_VALID = "Введены неверные данные. Возможно, Вы не заполнили обязательные поля или ввели неправильный e-mail адрес.";

	public static final String ERROR_MESSAGE_NOT_VALID_REQUEST = "Неверный формат запроса.";
	public static final String ERROR_MESSAGE_USER_NOT_FOUND = "Контакт с таким ID не найден.";
	public static final String ERROR_MESSAGE_CATEGORY_NOT_FOUND = "Категория с таким ID не найдена.";
	public static final String ERROR_MESSAGE_NO_USER_ID = "ID контакта не передан в запросе.";
	public static final String ERROR_MESSAGE_NO_CATEGORY_ID = "ID категории не передан в запросе.";
	public static final String ERROR_MESSAGE_CANT_CREATE_ACTIVITY = "Ошибка при создании активности.";
	
	// Activities
	public static final String ACTIVITY_CREATOR = "Obc Website";
	public static final String ACTIVITY_TYPE = "Website Request";
	public static final String ACTIVITY_CATEGORY = "WebSiteActivity";
	
	// replacement patterns
	public static final String ACTIVITY_NUMBER_PATTERN = "{activity_number}";
	public static final String ACTIVITY_NAME_PATTERN = "{activity_name}";
	public static final String ACTIVITY_DESCRIPTION_PATTERN = "{activity_description}";
	
	// Owning group for a created contact
	public static final String OWNING_GROUP = "Public";
	
	// Folder containing user documents 
	public static final String DOCUMENT_FOLDER = "CustomerDocuments";
	
	// Names of EMailActivity transitions
	public static final String ASSIGN_TRANSITION = "Assign";
	public static final String SEND_TRANSITION = "Send as mail";
	
	// Environment resources
	public static final String SEGMENT_RESOURCE = "Segment";
	public static final String HOST_RESOURCE = "Host";
	
	// Authentication responses
	public static final String AUTHENTICATION_SUCCESS = "1";
	public static final String AUTHENTICATION_FAIL = "0";
	
	// Folder values
	public static final String IS_FOLDER = "1";
	public static final String IS_NOT_FOLDER = "0";
	
	// Subscribe values
	public static final String DO_SUBSCRIBE = "1";
	
	// XML tags
	public static final String ACCOUNT_TAG = "account"; // used two times
	public static final String LINKS_TAG = "links";
	public static final String RESULT_TAG = "result";
	public static final String RESULTCODE_TAG = "result-code";
	public static final String ERRORMESSAGE_TAG = "error-message";
	public static final String LINK_TAG = "link";
	public static final String ISFOLDER_TAG = "is_folder";
	public static final String URL_TAG = "url";
	public static final String NAME_TAG = "name"; // used tow times
	public static final String TITLE_TAG = "title";
	public static final String ABSTRACT_TAG = "abstract";
	public static final String NUMBER_TAG = "number";
	public static final String CREATED_TAG = "createdDate";
	public static final String MODIFIED_TAG = "modifiedDate";
	public static final String DESCRIPTION_TAG = "description"; // two times
	public static final String AUTHOR_TAG = "author";
	public static final String DOCTYPE_TAG = "docType";
	public static final String ACCOUNT_ID_TAG = "account-id"; // used several times
	public static final String ACCOUNT_PWD_TAG = "account-pwd";
	public static final String FOLDER_TAG = "folder";
	public static final String REQUEST_TAG = "request"; // used two times
	public static final String ACCOUNT_NAME_TAG = "name";
	public static final String ACCOUNT_COMPANY_TAG = "company";
	public static final String ACCOUNT_CITY_TAG = "city";
	public static final String ACCOUNT_PHONE_TAG = "phone";
	public static final String ACCOUNT_EMAIL_TAG = "email";
	public static final String ACCOUNT_SOURCE_TAG = "source";
	public static final String ACCOUNT_SUBSCRIBE_TAG = "is_subscribe";
	public static final String CATEGORIES_TAG = "categories";
	public static final String CATEGORY_TAG = "category";
	public static final String CATEGORY_ID_TAG = "category-crxid"; // used several times
	public static final String CATEGORY_NAME_TAG = "name";
	public static final String CATEGORY_DESCRIPTION_TAG = "description";
	public static final String PRIORITY_TAG = "priority";
	
	public static final String TEST_PROPERTY = "test";

}
