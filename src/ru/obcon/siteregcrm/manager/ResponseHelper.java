package ru.obcon.siteregcrm.manager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ru.obcon.siteregcrm.utils.ObcLogger;

/**
 * Helper class to work with response object. 
 * @author ikari
 */
public class ResponseHelper {

	// --------------------------------------------------------------------------------------------
	// Constructor
	//---------------------------------------------------------------------------------------------

	public ResponseHelper(HttpServletResponse response) {
		this.response = response;
	}	

	// --------------------------------------------------------------------------------------------
	// Methods
	// --------------------------------------------------------------------------------------------
	
	/**
	 * Returns content of InputStream object as HttpResponse.
	 * @param content
	 * @throws IOException
	 */
	private void returnContent(InputStream content) throws IOException {
		if (content == null){
			printHTMLMessage("Error.");
			logger.error("ResponseHelper.delegateResponse: response is null !");
			return;
		}
		
		PrintWriter out = response.getWriter();
		BufferedInputStream buf = new BufferedInputStream(content);
		int bytes = 0;
		
		while( (bytes = buf.read()) != -1 )
			out.write(bytes);

		buf.close();
		content.close();
		out.close();
	}
	
	/**
	 * Sets mime-type, name of the file and returns content from InputStream.
	 * @param name
	 * @param mime
	 * @param content
	 * @throws IOException
	 */
	public void returnContent(String name, String mime, InputStream content) throws IOException{
		if (mime == null || mime.length() == 0)
			mime = "application/octet-stream";
		
		if (name == null || name.length() == 0)
			name = "content.bin";
		
		response.setContentType(mime);
		response.addHeader("Content-Disposition", "attachment; filename=" + name);

		returnContent(content);
	}
	
	/**
	 * Returns HTML content in case of an error.
	 * @param message
	 * @throws IOException
	 */
	public void printHTMLMessage(String message) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		out.println("<title>");
		out.println("Ошибка");
		out.println("</title>");	
		out.println("</head>");
		out.println("<body>");		
		out.println("<p>");
		out.println(message);
		out.println("</p>");
		out.println("<br>");
		out.println("</body>");
		out.println("</html>");
		out.close();
	}
	
	/**
	 * Returns XML file <i> text </i> as response. 
	 * @param documents
	 * @throws IOException
	 */
	public void returnXML(String text) throws IOException{
		response.setContentType("text/xml");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		out.write(text);
		out.close();
	}

	/**
	 * Returns plain text as response. 
	 * @param documents
	 * @throws IOException
	 */
	public void returnPlainText(String text) throws IOException{
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		out.write(text);
		out.close();
	}

	// ---------------------------------------------------------------------------------------------
	// Members
	// ---------------------------------------------------------------------------------------------

	private HttpServletResponse response;
	private Logger logger = ObcLogger.getLogger();

}
