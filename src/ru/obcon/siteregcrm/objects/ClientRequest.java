package ru.obcon.siteregcrm.objects;

public class ClientRequest {
	private String contactId = "";
	private String categoryId = "";
	private String name = "";
	private String description = "";
	private short priority = 1;
	
	public String getContactId() {
		return contactId;
	}
	
	public void setContactId(String contactId) {
		if (contactId == null)
			this.contactId = "";
		else
			this.contactId = contactId;
	}
	
	public String getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(String categoryId) {
		if (categoryId == null)
			this.categoryId = "";
		else
			this.categoryId = categoryId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		if (name == null)
			this.name = "";
		else
			this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		if (description == null)
			this.description = "";
		else
			this.description = description;
	}
	
	public short getPriority() {
		return priority;
	}
	
	public void setPriority(short priority) {
		if (priority < 1 || priority > 5)
			this.priority = 1;
		else
			this.priority = priority;
	}
	
	
}
