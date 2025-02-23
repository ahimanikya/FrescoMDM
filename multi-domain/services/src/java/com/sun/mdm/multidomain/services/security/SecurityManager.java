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
package com.sun.mdm.multidomain.services.security;

import com.sun.mdm.index.master.UserException;
//import com.sun.mdm.index.objects.metadata.ObjectFactory;
import com.sun.mdm.multidomain.services.security.util.ChildElementIterator;
import com.sun.mdm.multidomain.services.security.util.NodeUtil;
import com.sun.mdm.multidomain.services.security.UserProfile;
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
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Map.Entry;
        
/**
 * Read roles config xml
 *
 * @author rtam
 * @created October 27, 2008
 */
public class SecurityManager {
    
    public static final String OR = "or";       // OR conditional
    public static final String AND = "and";     // AND conditional
    
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.multidomain.services.security.SecurityManager");
    private transient static final Localizer mLocalizer = Localizer.get();
    
    private HashMap<String, Role> mRoleOperations;
    private HashMap<String, Role> mRoleRefs;
    private static SecurityManager instance = null;
    private DocumentBuilder builder;
    
    private static final String MDWM_SECURITY_FILENAME = "mdwm-security.xml";
    private static final String ROLES = "roles";
    private static final String ROLE = "role";
    private static final String ROLE_REF = "role-ref";
    private static final String ROLE_NAME = "role-name";
    private static final String NAME = "name";
    private static final String OPERATIONS = "operations";
    private static final String INHERITANCE = "inheritance";
    private static final String INHERITS_FROM = "inherits-from";
    private static final String EXCLUDED_OPERATIONS = "excluded-operations";
    
    

    /**
     * Constructor for the SecurityManager object
     */
    private SecurityManager() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        mRoleOperations = new HashMap<String, Role>();
        mRoleRefs = new HashMap<String, Role>();
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
                      
            try {
                instance.read();
            } catch (Exception e) {
                throw new Exception(mLocalizer.t("SRS501: Failed to read Security permissions: {0}", e.getMessage()));
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
     * Reads the MDWM_SECURITY_FILENAME file.
     *
     * @throws Exception if an error is encountered.
     */
    private void read() throws Exception {
        InputStream in;
        BufferedReader rdr;
        try {
            in = getClass().getClassLoader().getResourceAsStream(SecurityManager.MDWM_SECURITY_FILENAME);
        } catch (Exception e) {
            throw new Exception(mLocalizer.t("SRS502: Unable to open roles configuration " +
                                             "file.  File name={0}, error={1}", 
                                             SecurityManager.MDWM_SECURITY_FILENAME, 
                                             e.getMessage()));
        }
        read(in);
        in.close();
    }

    /**
     * Read and parse the permssions file.
     *
     * @param input InputStream for the MDWM_SECURITY_FILENAME file.
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
                        throw new Exception(mLocalizer.t("SRS503: Error occurred in " + 
                                                         "building role definition: {0}", 
                                                         ex.getMessage()));
                    }
                }
            }
        }
        
        Iterator<Map.Entry<String, Role>> roleRefs = mRoleRefs.entrySet().iterator(); 
        while (roleRefs.hasNext()) {
            Map.Entry<String, Role> roleRef = roleRefs.next();
            if (!mRoleOperations.containsKey(roleRef.getKey())) {
                 ArrayList<String> operations = new ArrayList<String>();
                 String[] roleNames = roleRef.getValue().getOperationsArray();
                 for (int i = 0; i < roleNames.length; ++i) {
                      ArrayList<String> opers = getOperationsforRole(roleNames[i]);
                      operations.addAll(opers);
                 }
                 mRoleOperations.put(roleRef.getKey(), new Role(roleRef.getKey(), operations));
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
        ArrayList<String> operations = new ArrayList<String>();
        ArrayList<String> roleRefs = new ArrayList<String>();
        HashMap excludedOperations = new HashMap();
        
        // parse each of the children
        while (itr.hasNext()) {
            Element element = (Element)itr.next();
            if (element.getTagName().equalsIgnoreCase(ROLE_NAME)) {
                roleName = NodeUtil.getNodeText(element);
            } else if (element.getTagName().equalsIgnoreCase(INHERITANCE)) {
                ArrayList<String> newOperations 
                        = buildInheritanceConfig(element, excludedOperations);
                // Add any inherited operations
                if (newOperations != null) {
                    Iterator iter = newOperations.iterator();
                    while (iter.hasNext()) {
                        operations.add(iter.next().toString());
                    }
                }
            } else if (element.getTagName().equalsIgnoreCase(ROLE_REF)) {
                String roleRef = NodeUtil.getNodeText(element);
                // Add any role-ref
                if (roleRef != null) {
                    roleRefs.add(roleRef);
                    // can not reliably get operations for role_ref here 
                    // as there is no gareentee that they have been read at this
                    // point.  
                    //
                    // in fact it is more likely that the role_ref is a forward 
                    // refereence and that they have not been read. 
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
                if (operations.size() > 0) {
                    Role role = new Role(roleName, operations);
                    mRoleOperations.put(roleName, role);
                }
            }
        }
        if (roleRefs.size() > 0) {
            Role role = new Role(roleName, roleRefs);
            mRoleRefs.put(roleName, role);
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
            } else if (element.getTagName().equalsIgnoreCase(EXCLUDED_OPERATIONS)) {
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
     * Retrieve all valid operations for a specified role name.
     * @param roleName Role name whose operations will be returned.
     * @throws Exception if an error is encountered.
     * @returns a String array containing all the valid
     * operations for a specified role name
     */
    public ArrayList getOperationsforRole(String roleName) throws Exception {
        
        Role role = (Role) mRoleOperations.get(roleName);
        if (role != null) {
            return role.getOperations();
        } else {
            throw new UserException(mLocalizer.t("SRS504: role name not found: {0}", roleName));
        }
    }
    
    /**
     *  Retrieve all valid operations for a specified userprofile.
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
     * Add operations as defined by a role-ref
     * @param roleName
     * @param processedRoles role refs that have already been processed 
     * @param allAvailableOperations the list of operations for a specified role 
     * @returns the updated list of operations. this is the same as the updated
      * passed in allAvailableOperations
     * operations for a specified userprofile.
     */
    private ArrayList addOperationsCheckProcessedRoles(String roleName,
            ArrayList processedRoles, ArrayList allAvailableOperations) {
        Role role = (Role) mRoleOperations.get(roleName);
        if (role != null) {
            ArrayList operations = role.getOperations();
            Iterator iter = operations.iterator();
            while (iter.hasNext()) {
                String operation = (String) iter.next();
                if (!allAvailableOperations.contains(operation)) {
                    allAvailableOperations.add(operation);
                }
            }
        }
        // process each role ref
        Role ref = (Role) mRoleRefs.get(roleName);
        if (ref != null) {
            ArrayList refs = ref.getOperations();
            Iterator iter = refs.iterator();
            while (iter.hasNext()) {
                String refRole = (String) iter.next();
                if (!processedRoles.contains(refRole)) {
                    addOperationsCheckProcessedRoles(refRole, processedRoles,
                            allAvailableOperations);
                    processedRoles.add(refRole);
                }
            }
        }
        return allAvailableOperations;
    }
    
    /**
     *  Retrieve all defined roles.
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
     * Retrieve a role with the matching role name.
     * @returns a role with the matching role name.
     */
    public Role getRole(String roleName) {
        if (roleName == null) {
            return null;
        }
        Role role = mRoleOperations.get(roleName);
        return role;
    }
    
}
