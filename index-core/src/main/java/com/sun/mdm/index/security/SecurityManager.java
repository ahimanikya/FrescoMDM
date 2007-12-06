/**
 * 
 */
package com.sun.mdm.index.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.ejb.SessionContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.java.hulp.i18n.Logger;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.util.Localizer;

/**
 * @author Sujit Biswas
 * 
 */
public class SecurityManager {

	private static final String EVIEW_ADMIN = "eView.Admin";
	private SessionContext context;
	private String method;
	private Document doc;

	private HashMap<String, HashSet<String>> roleMap = new HashMap<String, HashSet<String>>();

	private transient final Logger mLogger = Logger.getLogger(this.getClass()
			.getName());
	private transient Localizer mLocalizer = Localizer.get();

	/**
	 * @param context
	 */
	public SecurityManager(SessionContext context) {
		this.context = context;

		doc = getDocument(this.getClass().getClassLoader().getResourceAsStream(
				"security.xml"));

		init();

	}

	private void init() {

		if (doc != null) {

			XPath xpath = XPathFactory.newInstance().newXPath();

			try {
				NodeList elements = (NodeList) xpath.evaluate(
						"//Configuration/SecurityConfig/role", doc,
						XPathConstants.NODESET);

				for (int i = 0; i < elements.getLength(); i++) {
					Element e = (Element) elements.item(i);

					String roleName = e.getElementsByTagName("role-name").item(
							0).getTextContent();
					e = (Element) e.getElementsByTagName("operations").item(0);

					NodeList nl = e.getElementsByTagName("name");

					for (int j = 0; j < nl.getLength(); j++) {

						String method = nl.item(j).getTextContent();

						updateMap(method, roleName);

					}

				}
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private void updateMap(String method, String roleName) {
		HashSet<String> roles = roleMap.get(method);

		if (roles == null) {
			roles = new HashSet<String>();
			roleMap.put(method, roles);
		}
		roles.add(roleName);

	}

	public boolean checkAccess() throws ProcessingException {

		mLogger.fine(mLocalizer.x("MSC901: caller principal: {0}", context
				.getCallerPrincipal()));
		mLogger.fine(mLocalizer.x("MSC902: caller role is Admin? :  {0}",
				context.isCallerInRole(EVIEW_ADMIN)));
		mLogger.fine(mLocalizer.x("MSC903: current method :  {0}",
				getCurrentMethod()));

		if (isAdmin()) {
			return true;
		}

		if (checkMethodPermission()) {
			return true;
		}

		throw new ProcessingException(mLocalizer.t(
				"MSC904: invalid security credentials: {0}", context
						.getCallerPrincipal()));
	}

	private boolean checkMethodPermission() {

		HashSet<String> roles = roleMap.get(method);
		
		if(roles == null){
			return false;
		}

		Iterator<String> iter = roles.iterator();

		while (iter.hasNext()) {
			String role = (String) iter.next();
			boolean b = context.isCallerInRole(role);

			if (!b) {
				b = role.equals(context.getCallerPrincipal().getName());
			}

			if (b)
				return true;

		}

		return false;
	}

	private boolean isAdmin() {
		boolean b = context.isCallerInRole(EVIEW_ADMIN);

		if (!b) {
			b = EVIEW_ADMIN.equals(context.getCallerPrincipal().getName());
		}

		return b;
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

	public static void main(String[] args) {
		SecurityManager sm = new SecurityManager(null);

		File f = new File("trunk/index-core/src/test/resources/security.txt");

		try {
			if (f.exists()) {
				sm.doc = sm.getDocument(new FileInputStream(f));

				sm.init();

				sm.toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
