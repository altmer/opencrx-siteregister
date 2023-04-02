package ru.obcon.siteregcrm.objects;

import java.io.InputStream;

public class DocumentInfo {
	
	private InputStream content;
	private String content_name;
	private String content_mime;
	private String name;
	
	public DocumentInfo(InputStream content, String contentName,
			String contentMime, String name) {
		this.content = content;
		content_name = contentName;
		content_mime = contentMime;
		this.name = name;
	}
	
	/**
	 * Returns content found during previous invocation of <i>getDocument</i> method
	 * @return
	 */
	public InputStream getContent(){
		return content;
	}
	
	/**
	 * Returns content name found during previous invocation of <i>getDocument</i> method
	 * @return
	 */
	public String getContentName(){
		return content_name;
	}
	
	/**
	 * Returns content mime found during previous invocation of <i>getDocument</i> method
	 * @return
	 */	
	public String getContentMime(){
		return content_mime;
	}
	
	public String getDocumentName() {
		return name;
	}

}
