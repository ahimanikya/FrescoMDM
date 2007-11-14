/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * The contents of this file are subject to the terms of the Common 
 * Development and Distribution License ("CDDL")(the "License"). You 
 * may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://open-dm-mi.dev.java.net/cddl.html
 * or open-dm-mi/bootstrap/legal/license.txt. See the License for the 
 * specific language governing permissions and limitations under the  
 * License.  
 *
 * When distributing the Covered Code, include this CDDL Header Notice 
 * in each file and include the License file at
 * open-dm-mi/bootstrap/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the 
 * fields enclosed by brackets [] replaced by your own identifying 
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 */
package com.sun.mdm.index.ops;

import java.io.FileReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.StringTokenizer;

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectKey;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.ops.SBROverWriteDB;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathGroup;
import com.sun.mdm.index.objects.epath.EPathParser;
import com.sun.mdm.index.objects.epath.Filter;
import com.sun.mdm.index.util.ConnectionUtil;
import com.sun.mdm.index.util.Constants;
import com.sun.mdm.index.query.QueryManager;
import com.sun.mdm.index.query.QueryManagerFactory;
import com.sun.mdm.index.query.QueryObject;
import com.sun.mdm.index.query.QMIterator;
import com.sun.mdm.index.query.Condition;
import com.sun.mdm.index.query.AssembleDescriptor;
import com.sun.mdm.index.query.EOObjectNodeAssembler;
import com.sun.mdm.index.util.Localizer;


/** Retrieves EO from the database based on Epaths specified. Internally it 
 * uses QueryManager to retrieves the SO and SBR and it combines them using 
 * ObjectNode.combine method.
 * @author sdua
 * 
 */
public final class EORetriever {
	private QueryManager mQueryMgr;

    private transient static final Localizer mLocalizer = Localizer.get();

    /** Default constructor
     * 
     * @throws Exception if an error occurs.
     */  
    public EORetriever() throws Exception {
    	mQueryMgr = QueryManagerFactory.getInstance();
    }
    
    /**
     * Gets EO based on epaths specified.
     * @param conn JDBC Connection.
     * @param euid EUID of EO to retrieve.
     * @param epaths List of epaths on which EO is to constructed.
     * @throws Exception if any error occurs.
     * @return retrieved EnterpriseObject. 
     */
    
    public EnterpriseObject getEO(Connection conn, String euid, String[] epaths)
        throws Exception {
            
    	/**
    	 * We will use QueryMananager to retrieve EnterpriseObject.
    	 * 
    	 * High Level Algorithm:
         *        1.	split an Epath into more Epaths
         *        2.	combine into groups the epaths whose path names 
         *        are same (filters may be different)
         *        3. Create QueryObject for each group and 
         *        query using QueryManager.
         *        4. Combine (OR them) objects retrieved from QueryManager. 
         *
         *  What a split Epath means:
         *  Say we want EO and SOs whose systemcode = A
         *  And Addresses whose SO.sytemcode = A and city= Monrovia.
         *  Convert this requirement into an EPath: 
         *  Enterprise.SystemObject[systemcode=A].Person.Address[city=Monrovia].*
         *
         *  Now if we convert this into QueryObject.
         *
         *  QueryObject qo = new QueryObject();
         *  qo.setSelect(new String[] {“Enterprise.SystemObject.*”,
         *  “Enterprise.SystemObject.Person.*”, 
         *  Enterprise.SystemObject.Address.*”});
         *  qo.addCondition(“Enterprise.SystemObject.systemcode”, “A”);
         *  qo.addCondition(“Enterprise.Systemobject.Person.Address”,”Monrovia”);
         * 
         *  This QueryObject would return those SystemObjects, 
         *  whose systemcode=A and Address.city=Monrovia.
         *  Say if there is no Address such that Address.city=Monrovia, 
         *  then this query will not retrieve any data. 
         *
         *  But per the requirement we also want SOs whose systemcode=A, regardless
         *  of value of subobjects (Address.city).
         *
         *  Therefore instead of creating one QueryObject per this EPath, we will split
         *  Epath into many EPaths and combine similar Epaths into Group and create
         *  QueryObject corresponding to each Group.
    	 * 
    	 */
    	
    	
    	if (epaths == null) {
    		return null;
    	}
        
    	EnterpriseObject root = null;
    	
    	/*
    	 * splitted epaths will go into two Epaths list. One contains
    	 * EPath that contain only SBR and other contains only SO
    	 * So we can create Groups for SBR and SO epaths.
    	 * Reason being we query SBRs and SOs seperately and different way.
    	 */
    	List splitEpaths = new ArrayList();
    	List splitSBREpaths = new ArrayList();
    	
    	
    	for (int i = 0; i < epaths.length; i++ ) {
    	    EPath epath = EPathParser.parse(epaths[i]);
    	    /*
    	     * split each input Epath
    	     */
    	    EPath[] paths = EPathGroup.splitIntoQueryCombinations(epath);
    	    if (EPathGroup.isSBR(epath)) {    	    
    	       for (int j = 0; j < paths.length; j++) {
    	    	splitSBREpaths.add(paths[j]);
    	       }
    	    } else {
    	    	 for (int j = 0; j < paths.length; j++) {
        	    	splitEpaths.add(paths[j]);
        	     }
    	    }
    	}
    	    
    	/*
    	 * Form different EPathGroup from splitEpaths
    	 */
    	List groups = createGroups(splitEpaths);
    	
    	List sbrgroups = createGroups(splitSBREpaths);
    	
    	if (groups.size() > 1) {
    		/*
    		 * If epaths contains SO, then executeSO will query Enterprise table
    		 * and create EnterpriseObject, else we query and EO separately in 
    		 * createEO. 
    		 */
    		root = executeSO(euid, groups);
    	} else {
    		root = createEO(euid);
    	}
    
    	executeSBR(conn, root, euid, sbrgroups);
    
    	/*
    	 * important to invoke resetAll, to indicate that none of fields in any 
    	 * object changed.
    	 */
    	if (root != null) {
    		root.resetAll();
    	}
    	return root;
    }
    
    /**
     * Form epath groups and convert list of epaths into similar kind of epath
     * groups.
     * @param epaths Epaths that needs to be transformed into epath group
     * @return List of epath groups.
     */
    private List createGroups(List epaths) {
    	
    	
    	Iterator it = epaths.iterator();
    	
    	/*
    	 * Form different EPathGroup from splitEpaths
    	 */
    	List groups = new ArrayList();
    	
    	if (epaths == null || epaths.size() < 1 ) {
    		return groups;
    	}
    	EPathGroup group = new EPathGroup();
    	groups.add(group);
    	
    	while (it.hasNext()) {
    		EPath path = (EPath) it.next();
    		Iterator gpit = groups.iterator();
    		boolean added = false;
    		while (gpit.hasNext()) {
    		   group = (EPathGroup) gpit.next();	
    		
    	       added = group.addEPath(path);
    	       if (added == true) {
    	       	  break;
    	       }
    		}
            if (added == false) {
            	/*
            	 * which means given epath did not belong to any of the
            	 * epath group, so need to create a new EpathGroup and then
            	 * add it
            	 * 
            	 */
                group = new EPathGroup();
                group.addEPath(path);
                groups.add(group);
            }
    	    
    	}
    	return groups;
    }
    
    /**
     * Retrieve EO based on input euid. The returned EO does not contain
     * any SBR or SO. 
     * @param euid EUID to use for.
     * @return EO for the euid.
     * @throws Exception if any errors are encountered.
     */
    
    private EnterpriseObject createEO(String euid) throws Exception {
    	String[] fields = {"Enterprise.EUID"};
    	QMIterator qmIterator = null;
    	EnterpriseObject root = null;
    	try {
    		/*
    		 * Uses QueryManager to retrieve EO
    		 */
    	  QueryObject qo = createQueryObject("Enterprise.EUID", euid, fields, new ArrayList());
    	  qmIterator = mQueryMgr.executeAssemble(qo);
    	  
          while (qmIterator.hasNext()) {
        	root = (EnterpriseObject) qmIterator.next();  
          }
    	} catch (Exception ex) {
    		throw ex;
    	} finally {
    		if (qmIterator != null) {
    			qmIterator.close();
    		}
    	}
          if (root != null) {
        	root.resetAll();
          }
        return root;
    }
    
    /**
     * Return EO containing SOs only based on euid. The other sub-objects created
     * are based on search condition in groups and select fields/objects 
     * mentionted in groups.
     * @param euid EUID for processing
     * @param groups  EPath group
     * @return EO after processing
     * @throws Exception if any problems are encountered
     */
    private EnterpriseObject executeSO(String euid, List groups) throws Exception {
       
    	Iterator it = groups.iterator();
   	    
         QMIterator qmIterator = null;
         ObjectNode root = null;
       
        try {
         while (it.hasNext()) {
         	/*
         	 * iterate through every group and create a corresponding
         	 * QueryObject 
         	 */
       	  EPathGroup group = (EPathGroup) it.next();
       	  String[] fields = group.getSelectFields();
       	  List filters = group.getFilterPaths();
       	
          ObjectNode node = null;
       	  QueryObject qo =  createQueryObject("Enterprise.EUID", euid, fields, filters);
          qmIterator = mQueryMgr.executeAssemble(qo);
          while (qmIterator.hasNext()) {
        	node = (ObjectNode) qmIterator.next(); 
        	/*
        	 * for every new root retrieved using a QO, combine it with first 
        	 * root EO.
        	 */
        	if (root == null) {
        		root = node;
        	} else {
        	    combine(root, node);
        	}
          }
       	
        }
       } catch (Exception ex) {
       	 throw ex;
       } finally {
       	 if (qmIterator != null) {
       	 	qmIterator.close();
       	 }
       }
       
       if (root != null) {
       	  root.resetAll();
       }
       return (EnterpriseObject) root;
    	
    }
    
    /**
     * Return EO containing SBRs only based on euid. The other sub-objects created
     * are based on search condition in groups and select fields/objects 
     * mentionted in groups.
     * @param conn JDBC Connection.
     * @param root  EO containing the SBR.
     * @param euid  EUID for the EO.
     * @param groups EPath group.
     * @return EO after processing.
     * @throws Exception if any errors are encountered.
     */
    
    private EnterpriseObject executeSBR(Connection conn, EnterpriseObject root, 
                                        String euid, List groups) 
            throws Exception {
        Iterator it = groups.iterator();
    	
        SBR sbr = null;
        QMIterator qmIterator = null;
        try {
         boolean primaryObjectRetrieved = false;
         while (it.hasNext()) {
        	EPathGroup group = (EPathGroup) it.next();
        	String[] fields = group.getSelectFields();
        	List filters = group.getFilterPaths();
        	
            SBR node = null;
            /*
             * For SBR, we don't query from Enterpise object (table) rather do it from Enterprise.SystemSBR. 
             * Two reasons for this, one being unnecessary hit to Enterprise table when EUID is stored in SBR and
             * other being if we query Enterprise for EUID, it can retreive duplicate rows each refering to different
             * Systemcode and LID for same EUID.
             */
        	QueryObject qo =  createQueryObject("Enterprise.SystemSBR.EUID", euid, fields, filters);
            qmIterator = mQueryMgr.executeAssemble(qo);
            while (qmIterator.hasNext()) {
         	   node = (SBR) qmIterator.next();
         	   if (sbr == null) {
         	   	//  first Node retrieved is SBR
         	   	 sbr = node;
         	   } else {
         	   	  combine(sbr, node);
         	   	
         	   }
         	   
            }
        	
        }
       } catch (Exception ex) {
       	  throw ex;
       } finally {
       	  if (qmIterator != null) {
       	  	qmIterator.close();
       	  }
       }
       
       /**
        * A business logic to attach SBR with SBROverWriteDB
        */
        if (conn != null && sbr != null) {
           SBROverWriteDB owdb = new SBROverWriteDB();
           List childlist = owdb.get(conn, null, euid);
           if (null != childlist) {
              sbr.addOverWrites(childlist);
           }
        }
        if (sbr != null) {
           sbr.resetAll();
           root.setSBR(sbr);
        }
        return root;
     	
     }
    
    /**
     * Form QueryObject from euid, select fields and filter list  
     * @param euidField will be either Enterprise.EUID or Enterprise.SBR.EUID
     * @param euid EUID.
     * @param fields Fields to include in the query object.
     * @param filterList List of filter conditions.
     * @return QueryObject
     */
    private QueryObject createQueryObject(String euidField, String euid, 
                                          String[] fields, List filterList) {
    	
    	QueryObject qo = new QueryObject();
    	qo.setSelect(fields);
    	List conditionList = new ArrayList();
    	
    	
    	Condition condition = new Condition(euidField, "=", euid, true);
    	Iterator it = filterList.iterator(); 
    	while(it.hasNext()) {
    		/*
    		 *  initializes condition with EUID for each Union condition
    		 */
    	  condition = new Condition(euidField, "=", euid, true);
    	  Condition cond = null;
    	  Filter[][] filters = (Filter[][]) it.next();
    	  for (int i = 0; i < filters.length; i++) {
    	  	for (int j = 0; filters[i] != null && j < filters[i].length; j++) {
    	  		Filter filter = filters[i][j];
    	  		cond = createCondition(filter.getField(), filter.getValue());
    	  		condition.addConditions("AND", cond);
    	  	}
    	  }
    	  /*
    	   * found a filter condition
    	   */
    	  if (cond != null) {
    	     conditionList.add(condition);
    	  }
    	}
    	
    	int len = conditionList.size();
    	if (len > 0) {
    	   Condition[][] conditionUnion = new Condition[len][1];
    	   it = conditionList.iterator();
    	   for ( int i = 0; it.hasNext(); i++) {
    	      Condition cond = (Condition)it.next();
    	      conditionUnion[i][0] = cond;
    	    
    	   }
    	   qo.setCondition(conditionUnion);
    	} else {
    	   qo.addCondition(condition);   
    	}
    	AssembleDescriptor assDesc = new AssembleDescriptor();
    	assDesc.setAssembler(new EOObjectNodeAssembler());
    	qo.setAssembleDescriptor(assDesc);
    	qo.setQueryOption(QueryObject.MULTIPLE_QUERY);
    	
    	return qo;
    }
    
    /**
     * Create condition from field and value.
     * @param field  Field to use.  
     * @param value  Value for the field.
     * @return Condition object.
     */
    private Condition createCondition(String field, String value) {
    	
         Condition cond = null;
    	 if (value.equalsIgnoreCase("null")) {
    	   cond = new Condition(field, null); 	
    	 } else {
    	   cond = new Condition(field, "=", value, true);
    	 }
    	 return cond;
    }
    
    /**
     * Combine two objectNodes.
     * Called after executing query to combine two objectNodes retrieved from database.
     * Hence copying of any kind of flags are not considered here.
     * Dependency: THis is similar to ObjectNode.addChild method. So if there
     * is any main change in ObjectNode.addChild then this method should be
     * revisited.
     * @param node1 First ObjectNode to combine.
     * @param node2 Second ObjectNode to combine.
     * @throws ObjectException if an error occurs.
     */
    private static void combine(ObjectNode node1, ObjectNode node2) throws ObjectException {
        try {
            if (node2.pGetTag().equals(node1.pGetTag())) {
                ArrayList names = node2.pGetFieldNames();

                for (int i = 0; i < names.size(); i++) {
                    String name = (String) names.get(i);

                    if (!node2.isNull(name) && node1.isNull(name)) {
                        node1.setValue(name, node2.getValue(name));                       
                    }
                }
                
                if (node2 instanceof SBR) {
                	 ObjectNode n = ((SBR)node2).getObject();
                     if (n != null) {
                     	  ObjectNode n2 = ((SBR)node1).getObject();
                     	  if (n2 != null) {
                     	  	combine(n2, n);
                     	  } else {
                     	  	((SBR)node1).setObject(n);
                     	  }
                     }
                	
                } else if (node2 instanceof SystemObject) {
                	 ObjectNode n = ((SystemObject)node2).getObject();
                     if (n != null) {
                     	  ObjectNode n2 = ((SystemObject)node1).getObject();
                     	  if (n2 != null) {
                     	  	combine(n2, n);
                     	  } else {
                     	  	((SystemObject)node1).setObject(n);
                     	  }
                     }
                	
                } else {


                /* if incoming children list has new child, then add it
                 * 
                 */
                  ArrayList childList = node2.pGetChildren();

                  if (childList != null) {
                    for (int i = 0; i < childList.size(); i++) {
                        ObjectNode n = (ObjectNode) childList.get(i);
                        String t = n.pGetTag();
                        ObjectKey k = n.pGetKey();
                        boolean removed = n.isRemoved();
                        ObjectNode n2 = node1.getChild(t, k, removed);

                        if (n2 != null) {
                            combine(n2, n);
                        } else {
                              node1.addChild(n);                           
                        }
                    }
                  }
                }
            } else {
                throw new ObjectException(mLocalizer.t("OPS520: Could not combine " + 
                                        "two object nodes.  The type of node1 ({0}) " + 
                                        "does not match the type of node2 ({1})",
                                        node1.pGetTag(), node2.pGetTag()));
            }
        } catch (ObjectException e) {
            throw new ObjectException(mLocalizer.t("OPS521: Could not combine " + 
                                        "two object nodes: {0}", e));
        }
    }
}
