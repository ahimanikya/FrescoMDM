/**
 * 
 */
package com.sun.mdm.index.security;

import java.io.IOException;
import java.io.InputStream;

import javax.ejb.SessionContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.util.Localizer;

/**
 * @author Sujit Biswas
 * 
 */
public class SecurityManager {

	private SessionContext context;
	private String method;
	private Document doc;

	private transient final Logger mLogger = Logger.getLogger(this.getClass()
			.getName());
	private transient Localizer mLocalizer = Localizer.get();

	/**
	 * @param context
	 */
	public SecurityManager(SessionContext context) {
		this.context = context;

		doc = getDocument(this.getClass().getClassLoader().getResourceAsStream("security.xml"));

	}

	public boolean checkAccess() throws ProcessingException {

		mLogger.fine(mLocalizer.x("MSC901: caller principal: {0}", context
				.getCallerPrincipal()));
		mLogger.fine(mLocalizer.x("MSC902: caller role is Admin? :  {0}",
				context.isCallerInRole("eView.Admin")));
		mLogger.fine(mLocalizer.x("MSC903: current method :  {0}",
				getCurrentMethod()));

		return true;
	}

	public void setCurrentMethod(String method) {
		this.method = method;
	}

	public String getCurrentMethod() {
		return method;
	}

	private Document getDocument(InputStream filename) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			// docFactory.setValidating(true);

			DocumentBuilder builder = docFactory.newDocumentBuilder();

			return builder.parse(filename);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
