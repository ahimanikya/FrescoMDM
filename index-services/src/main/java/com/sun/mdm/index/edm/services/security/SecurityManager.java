/*
 * BEGIN_HEADER - DO NOT EDIT
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * If applicable add the following below this CDDL HEADER,
 * with the fields enclosed by brackets "[]" replaced with
 * your own identifying information: Portions Copyright
 * [year] [name of copyright owner]
 */

/*
 * @(#)SecurityManager.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.index.edm.services.security;

import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.objects.metadata.ObjectFactory;
import com.sun.mdm.index.edm.services.configuration.ChildElementIterator;
import com.sun.mdm.index.edm.services.configuration.util.NodeUtil;
import com.sun.mdm.index.edm.control.UserProfile;
import com.sun.mdm.index.util.Localizer;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;

/**
 * Read roles config xml
 *
 * @author rtam
 * @created October 3, 2007
 */
public class SecurityManager {
    
    public static final String OR = "or";       // OR conditional
    public static final String AND = "and";     // AND conditional
    
    // logger
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.service.security.SecurityManager");
    private transient static final Localizer mLocalizer = Localizer.get();
    
    // key = operation name, value = ArrayList of allowable operations
    private HashMap mRoleOperations;
    private static SecurityManager instance = null;
    private DocumentBuilder builder;
    
    private static final String ROLES_FILENAME = "roles.xml";
    private static final String ROLES = "roles";
    private static final String ROLE = "role";
    private static final String ROLE_NAME = "role-name";
    private static final String NAME = "name";
    private static final String OPERATIONS = "operations";
    private static final String INHERITANCE = "inheritance";
    private static final String INHERITS_FROM = "inherits-from";
    private static final String EXCLUDED_PERMISSSIONS = "excluded-permissions";
    
    

    /**
     * Constructor for the SecurityManager object
     */
    private SecurityManager() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        mRoleOperations = new HashMap();
    }

    /**
     *  Logon 
     *
     * @param userid  This is the user's ID for logging in.
     * @param password  This is the user's password for logging in.
     * @throws Exception if any errors are encountered.
     */
    public static void logon(String userid, String password) 
            throws UserException{
        if (userid == null) {
            throw new UserException(mLocalizer.t("SRS513: Login unsuccessful. " +
                                                 "User ID is may not be null"));
        }
        if (userid.length() == 0) {
            throw new UserException(mLocalizer.t("SRS514: Login unsuccessful. " + 
                                                 "User ID is may not be an empty string"));
        }
        if (password == null) {
            throw new UserException(mLocalizer.t("SRS515: Login unsuccessful. " + 
                                                 "password is may not be null"));
        }
        if (password.length() == 0) {
            throw new UserException(mLocalizer.t("SRS516: Login unsuccessful. " + 
                                                 "password is may not be an empty string"));
        }
        try {
            Logon.execute(userid, password);
        } catch (Exception e) {
            throw new UserException(mLocalizer.t("SRS510: Login unsuccessful: {0}", e.getMessage()));
        }
    }
    
    
    /**
     * Initialize the Security Manager.
     *
     * @throws Exception if obtaining/initializing the instance failed
     */
    public static SecurityManager init() throws Exception {

        synchronized (SecurityManager.class) {
            if (instance != null) {
                return instance;
            }
            instance = new SecurityManager();
          
            ObjectFactory.init();
            
            try {
                instance.read();
            } catch (Exception e) {
                throw new Exception(mLocalizer.t("SRS511: Failed to read Security permissions: {0}", e.getMessage()));
            }
            mLogger.info(mLocalizer.x("SRS001: Parsed Security permissions file successfully."));
        }
        return instance;
    }
        
    /**
     * Gets the instance attribute of the SecurityManager class.
     *
     * @return The instance value
     */
    public static SecurityManager getInstance() {
      return instance;
    }
    
    /**
     * Reads the ROLES_FILENAME file.
     *
     * @throws Exception if an error is encountered.
     */
    private void read() throws Exception {
        InputStream in;
        BufferedReader rdr;
        try {
            in = getClass().getClassLoader().getResourceAsStream(SecurityManager.ROLES_FILENAME);
        } catch (Exception e) {
            throw new Exception(mLocalizer.t("SRS512: Unable to open roles configuration " +
                                             "file.  File name={0}, error={1}", 
                                             SecurityManager.ROLES_FILENAME, 
                                             e.getMessage()));
        }
        read(in);
        in.close();
    }

    /**
     * Read and parse the permssions file.
     *
     * @param input InputStream for ROLES_FILENAME file.
     * @throws Exception if an error is encountered.
     */
    private void read(InputStream input) throws Exception {        
        Document doc = builder.parse(input);
        Element root = doc.getDocumentElement();

        ChildElementIterator itr = new ChildElementIterator(root);

        while ( itr.hasNext() ) {
            Element element = (Element)itr.next();

            if (element.getTagName().equalsIgnoreCase(ROLES)) {
                ChildElementIterator roleItr 
                    = new ChildElementIterator(element);

                while ( roleItr.hasNext() ) {
                    Element roleElement = (Element)roleItr.next();
                    try {
                        buildRoleConfig(roleElement);
                    } catch (Exception ex) {
                        throw new Exception(mLocalizer.t("SRS518: Error occurred in " + 
                                                         "building role definition: {0}", 
                                                         ex.getMessage()));
                    }
                }
            }
        }
    }
    
    /**
     * Parses a ROLE block.  The name of the role and associated operations
     * are stored in the mRoleOperations HashMap.
     *
     * @param element Element where throws ROLE block is located.
     * @throws Exception if an error is encountered.
     */
    private void buildRoleConfig(Element roleElement) throws Exception {
        
        ChildElementIterator itr = new ChildElementIterator(roleElement);
        
        String roleName = null;
        ArrayList operations = new ArrayList();
        HashMap excludedOperations = new HashMap();
        
        // parse each of the children
        while (itr.hasNext()) {
            Element element = (Element)itr.next();
            if (element.getTagName().equalsIgnoreCase(ROLE_NAME)) {
                roleName = NodeUtil.getNodeText(element);
            } else if (element.getTagName().equalsIgnoreCase(INHERITANCE)) {
                ArrayList newOperations 
                        = buildInheritanceConfig(element, excludedOperations);
                // Add any inherited operations
                if (newOperations != null) {
                    Iterator iter = newOperations.iterator();
                    while (iter.hasNext()) {
                        operations.add(iter.next().toString());
                    }
                }
            } else if (element.getTagName().equalsIgnoreCase(OPERATIONS)) {
                ArrayList newOperations 
                        = buildOperationsConfig(element, excludedOperations);
                // Add newly-defined operations to any inherited operations
                if (newOperations != null) {
                    Iterator iter = newOperations.iterator();
                    while (iter.hasNext()) {
                        operations.add(iter.next().toString());
                    }
                }
                Role role = new Role(roleName, operations);
                mRoleOperations.put(roleName, role);
            }
        }
    }

    /**
     * Parses an INHERITANCE block and returns an ArrayList of inherited
     * operations.  Excluded operations are omitted.
     *
     * @param inElement Element where the INHERITANCE block is located.
     * @param excludedOperations This HashMap stores the excluded operations.
     * @throws Exception if an error is encountered.
     * @returns an ArrayList of inherited operations.
     */
    private ArrayList buildInheritanceConfig(Element inElement, 
                                             HashMap excludedOperations) 
            throws Exception {
        
        ChildElementIterator itr = new ChildElementIterator(inElement);
        ArrayList operations = new ArrayList();
        
        // parse each of the children
        while (itr.hasNext()) {
            Element element = (Element)itr.next();
            if (element.getTagName().equalsIgnoreCase(INHERITS_FROM)) {
                String roleName = NodeUtil.getNodeText(element);
                // get all operations for this role
                ArrayList newOperations = getOperationsforRole(roleName);
                if (newOperations != null) {
                    Iterator opIter = newOperations.iterator();
                    while (opIter.hasNext()) {
                        operations.add(opIter.next().toString());
                    }
                }
            } else if (element.getTagName().equalsIgnoreCase(EXCLUDED_PERMISSSIONS)) {
                ChildElementIterator exclPermItr = new ChildElementIterator(element);
                while (exclPermItr.hasNext()) {
                    while (exclPermItr.hasNext()) {
                        Element exclElement = (Element)exclPermItr.next();
                        String opName = NodeUtil.getNodeText(exclElement);
                        // Put a dummy value for the opName key.  Only the
                        // opName key is important.
                        excludedOperations.put(opName, opName);
                    }
                }
            } 
        }
        // remove excluded operations
        Iterator operationsIter = operations.iterator();
        while (operationsIter.hasNext()) {
            String opName = operationsIter.next().toString();
            if (excludedOperations.containsKey(opName) == true) {
                operationsIter.remove();
            }
        }
        return operations;
    }
    
    /**
     * Parses an OPERATIONS block.  
     *
     * @param opElement Element where the OPERATIONS block is located.
     * @param excludedOperations This HashMap stores the excluded operations.
     * @throws Exception if an error is encountered.
     */
    private ArrayList buildOperationsConfig(Element opElement,
                                            HashMap excludedOperations) 
        throws Exception {
        
        ChildElementIterator itr = new ChildElementIterator(opElement);
        ArrayList operations = new ArrayList();
        
        // parse each of the children
        while (itr.hasNext()) {
            Element element = (Element)itr.next();
            String opName = NodeUtil.getNodeText(element);
            // If the new operations include operations that were excluded
            // in the INHERITANCE block, they are also excluded in the
            // OPERATIONS block.
            if (excludedOperations.containsKey(opName) == false) {
                operations.add(opName);
            }
        }
        return operations;
    }

    /**
     *  Retrieve all valid operations for a specified role name.
     *
     * @param roleName Role name whose operations will be returned.
     * @throws Exception if an error is encountered.
     * @returns a String array containing all the valid
     * operations for a specified role name
     */
    public ArrayList getOperationsforRole(String roleName) throws Exception {
        
        ArrayList allAvailableRoles = new ArrayList();
        
        Role role = (Role) mRoleOperations.get(roleName);
        if (role != null) {
            return role.getOperations();
        } else {
            throw new UserException(mLocalizer.t("SRS517: role name not found: {0}", roleName));
        }
    }
    
    /**
     *  Retrieve all valid operations for a specified userprofile.
     *
     * @param userprofile  User profile for the current user.
     * @returns a String array containing all the valid
     * operations for a specified userprofile.
     */
    public String[] getOperations(UserProfile userprofile) {
        
        // Retrieve all roles for this UserProfile and then
        // retrieve all available operations for each role.
        ArrayList allAvailableOperations = new ArrayList();
        String rolesList[] = userprofile.getRoles(); // to be defined
        if (rolesList.length > 0) {
            for (int i = 0; i < rolesList.length; i++) {
                Role role = (Role) mRoleOperations.get(rolesList[i]);
                ArrayList operations = role.getOperations();
                Iterator iter = operations.iterator();
                while (iter.hasNext()) {
                    allAvailableOperations.add((String) iter.next());
                }
            }
            String operationsList[] = new String[allAvailableOperations.size()];
            for (int i = 0; i < allAvailableOperations.size(); i++) {
                operationsList[i] = allAvailableOperations.get(i).toString();
            }
            return operationsList;
        } else {
            return null;
        }
    }
    
    /**
     *  Retrieve all defined roles.
     *
     * @returns a String array containing all the defined roles.
     */
    public String[] getAllRoles() {
        
        Set roleOperationSet = mRoleOperations.entrySet();
        String[] roleList = new String[roleOperationSet.size()];
        int i = 0;
        Iterator roleOpSetIter = roleOperationSet.iterator();
        while (roleOpSetIter.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) roleOpSetIter.next();
            String roleName = (String) mapEntry.getKey();
            roleList[i] = roleName;
            i++;
        }
        return roleList;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here      
        String userid = "eview";
        String password = "eview";
        try {
            logon(userid, password);
        } catch (UserException e) {
            System.out.println("Login unsuccessful for user: " + userid);
        }
        SecurityManager.init();
        System.out.println("All done.  Bye Bye.");
 
    }
    
}
