package ru.obcon.siteregcrm.manager;

import java.util.List;

import javax.jdo.PersistenceManager;

import org.apache.log4j.Logger;
import org.opencrx.kernel.code1.cci2.AbstractEntryQuery;
import org.opencrx.kernel.code1.cci2.CodeValueContainerQuery;
import org.opencrx.kernel.code1.jmi1.AbstractEntry;
import org.opencrx.kernel.code1.jmi1.CodeValueContainer;
import org.opencrx.kernel.code1.jmi1.CodeValueEntry;
import org.opencrx.kernel.code1.jmi1.Segment;

import ru.obcon.siteregcrm.utils.Const;
import ru.obcon.siteregcrm.utils.ObcLogger;

public class CodeManager {
	protected PersistenceManager pm;
	protected Segment segment;
	
	public CodeManager(PersistenceManager pm, Segment segment){
		this.pm = pm;
		this.segment = segment;
	}
	
	/**
	 * Retrieves setting specified in default setting container
	 * @param context
	 * @param setting name of the setting
	 * @return
	 */
	public String getSettingValueShort(String setting){
		CodeValueEntry entry = getEntry(getContainer(), setting);
		if (entry != null){
			return getShortEntryText(entry);
		}
		return "";
	}
	
	/**
	 * 
	 * @param setting
	 * @return
	 */
	public String getSettingValueLong(String setting){
		CodeValueEntry entry = getEntry(getContainer(), setting);
		if (entry != null){
			return getLongEntryText(entry);
		}
		return "";
	}
	
	/**
	 * Retrieves default CodeValueContainer for application (Const.CODE_VALUE_CONTAINER)
	 * @param context
	 * @return
	 */
	protected CodeValueContainer getContainer(){
		CodeValueContainerQuery query = (CodeValueContainerQuery)pm.newQuery(CodeValueContainer.class);
		query.thereExistsName().like(Const.CODE_VALUE_CONTAINER);
		List<CodeValueContainer> list = segment.getValueContainer(query);
		if (list != null && !list.isEmpty())
			return list.iterator().next();
		
		return null;
	}
	
	/**
	 * Retrieves entry from given CodeValueContainer.
	 * @param context
	 * @param container - container to search in
	 * @param name - name of the entry to search for
	 * @return
	 */
	protected CodeValueEntry getEntry(CodeValueContainer container, String name){
		if (container != null){
			AbstractEntryQuery query = (AbstractEntryQuery) pm.newQuery(AbstractEntry.class);
			query.thereExistsEntryValue().like(name);
			List<AbstractEntry> list = container.getEntry(query);
			if (list != null && list.size() > 0){
				AbstractEntry entry = list.iterator().next();
				if (entry instanceof CodeValueEntry){
					return (CodeValueEntry) entry;
				}else{
					logger.error("Entry is not CodeValue.");
				}
			}
			else{
				logger.error("Entry not found.");
			}
			
		}else{
			logger.error("Container not found.");
		}
		return null;
	}
	
	/**
	 * Retrieves ShortText from given entry
	 * @param entry
	 * @return
	 */
	protected String getShortEntryText(CodeValueEntry entry){
		List<String> lst = entry.getShortText();
		StringBuffer str = new StringBuffer();
		for (String s : lst){
			str.append(s);
		}
		return str.toString();		
		
	}
	
	protected String getLongEntryText(CodeValueEntry entry){
		List<String> lst = entry.getLongText();
		StringBuffer str = new StringBuffer();
		for (String s : lst){
			str.append("\n");
			str.append(s);
		}
		return str.toString();		
		
	}
	
	private static Logger logger = ObcLogger.getLogger();	
	
}
