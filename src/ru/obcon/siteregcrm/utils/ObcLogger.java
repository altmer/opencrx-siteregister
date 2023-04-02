package ru.obcon.siteregcrm.utils;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * Wrapper for log4j Logger.
 * @author ikari
 *
 */
public class ObcLogger {
	private static Logger logger;
	
	/**
	 * Get instance of logger for Obc Website Application
	 * @return
	 */
	public static Logger getLogger(){
		if (logger == null){
			initLogger();
		}
		
		return logger;
	}
	
	private static void initLogger(){
		logger = Logger.getLogger(ObcLogger.class);
    	logger.setLevel(Level.DEBUG);
    	logger.addAppender(new ConsoleAppender(new PatternLayout("%5p [%d]: %m%n")));
	}
}
