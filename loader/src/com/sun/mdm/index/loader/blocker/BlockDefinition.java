/**
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
package com.sun.mdm.index.loader.blocker;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.Collection;

import com.sun.mdm.index.dataobject.DataObject;
import com.sun.mdm.index.dataobject.epath.DOEpath;
import com.sun.mdm.index.dataobject.objectdef.Lookup;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.epath.EPathParser;
import java.util.HashMap;
import com.sun.mdm.index.filter.ExclusionListLookup;
import com.sun.mdm.index.filter.FilterConstants;

/**
 * BlockDefinition
 * Represents Master Index block definition.
 * 
 * Most of the apis are used to create and setup BLockDefinition and called  
 * based upon Block Defintion configuration file.
 * 
 * After BlockDefintion has been initialized in the loader, 
 * getBlockIDs() is called by BlockDistributor to get BlockID for each
 * input DataObject.
 * 
 * BlockDefintion contains Rule class that can be set up as either
 * in OR or AND state.
 * 
 * AND for two field values leads to matrix values muliplication.
		      Say field1 values= {"1", "2", "3"} 
		    *  field2 values = {"a", "b"}
		    *  Then "AND" operator would give:
		    *  {"1a", "1b","2a", "2b", "3a", "3b"}
		      
  "OR" leads to addition of field values.
          So it would result in :
		      {"1", "2", "3", "a", "b"}
		   
 * @author Swaranjit Dua
 *
 */
public class BlockDefinition {
	private static Logger logger = Logger.getLogger(BlockDefinition.class
			.getName());
	private String id;
	private List<Rule> rules_ = new ArrayList<Rule>();
	public enum Operator {AND, OR};
	private Operator operator_ = Operator.AND;
	
	/*
	 * configuration api
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/*
	 * configuration api
	 */
	public void addRule(Rule rule) {
    	rules_.add(rule);
    }
	/*
	 * configuration api
	 */
	public void addRule(String epath) throws Exception {
    	Rule rule = new Rule(epath);
    	rules_.add(rule);
    }
	
	public void addRule(String epath,String source) throws Exception {
    	Rule rule = new Rule(epath,source);
    	rules_.add(rule);
    }
	/**
	 * merges List of block defintions into one using passed operator operator.
	 * @param bdef BlockDefinitions to merge
	 * @param operator the operator in the merged block definition
	 * @return merged BlockDefinition
	 */
	public static BlockDefinition merge(List<BlockDefinition> bdefs,
			                        Operator operator) {
		BlockDefinition targetdef = new BlockDefinition();
				
		for (BlockDefinition def: bdefs) {
			Rule rule = new Rule();
			rule.children = def.rules_;
			rule.operator_ = def.operator_;
			targetdef.addRule(rule);
			if (targetdef.id == null) {
				targetdef.id = def.id;
			}
		}
		targetdef.operator_ = operator;		
		return targetdef;
		
	}
	
	
	 /*
		 * Invoked by BlockDistributor to get BlockIDs for each dataObject.
		 */
	 
	List<String> getBlockIds(DataObject dataObject, Lookup lk) throws Exception {
	   List<String> blockids = new ArrayList<String>();
	   for (int i = 0; i < rules_.size(); i++) {
		   List<String> values = rules_.get(i).getValue(dataObject, lk);
		   if (values != null) {
		     blockids = add(blockids, values, operator_);
		   }
	   }
	   return insertIds(blockids);	   
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer("Block Definition:");
		for (int i = 0; i < rules_.size(); i++) {
			Rule rule = rules_.get(i);
			String str = rule.toString();
			buf.append(str);
		}
		return buf.toString();
	}
	
	
	public static class Rule {
		private List<Rule> children = new ArrayList<Rule>();
		
		private boolean isLeaf = false;
		private EPath epath;
		private String qname;
		private String source;
		private Operator operator_ = Operator.AND;  // AND default operator
	    private BlockIDGenerator blockIDGenerator = null;
	   
	    public Rule() {
	    	
	    }
	    public Rule(String epath) throws Exception {
	    	isLeaf = true;
	    	parseEpath(epath);
	    }
	    
	    public Rule(String epath,String source) throws Exception {
	    	isLeaf = true;
	    	parseEpath(epath);
	    	this.source=source;
	    }

	    
	    public Rule(List<String> epaths) throws Exception {
	    	for (int i = 0; i < epaths.size(); i++) {
	    	  children.add(new Rule(epaths.get(i)));
	    	}
	    	//operator = Operator.AND;
	    }
	    
	    public Rule(String[] epaths) throws Exception  {
	    	for (int i = 0; i < epaths.length; i++) {
	    	  children.add(new Rule(epaths[i]));
	    	}
	    	//operator = Operator.AND;
	    }
	    
	    /*
		 * configuration api
		 */
	    public void addChild(Rule rule) {
	    	children.add(rule);
	    }
	    /*
		 * configuration api
		 */
	    public void setLeaf(boolean leaf) {
	       isLeaf = leaf;	
	    }
	    /*
		 * configuration api
		 */
	    public void setOperator(Operator oper) {
	    	operator_ = oper;
	    }
	  /*  
	    void setEpath(String epath) throws Exception {
	    	isLeaf = true;
	    	parseEpath(epath);
	    	
	    }
	    */
	    
	    
		private List<String> getValue(DataObject dataObject, Lookup lk) throws Exception {
			List<String> allvalues = new ArrayList<String>();
			if (blockIDGenerator != null) {
				allvalues = blockIDGenerator.computeBlockID(epath, dataObject, lk);
			}
			else if (isLeaf) {								
				 try {
					List<String> values = DOEpath.getFieldValueList(epath, dataObject, lk);
					values = filter(qname, values);
					if (values != null) {
					  allvalues.addAll(values);
					}
				 }  catch (EPathException ex)  {
					 logger.info(ex.getMessage());
					 throw ex;					 
				 }
				 
				//} else {
					//String value = DOEpath.getFieldValue(epath, dataObject, lk);
				    //allvalues.add(value);
			    //}

			} else {
				for (int i = 0; i < children.size(); i++) {
					Rule rule = children.get(i);
					List<String> values = rule.getValue(dataObject, lk);
					allvalues = add(allvalues, values, operator_);
				}				
			}
			return allvalues;
		}
		
		private List<String> filter(String epath, List<String> values) {
			if (values == null) {
				return null;
			}
			//List<String> newValues = new ArrayList<String>();
			for (int i = 0; i < values.size(); i++) {
				String  value = values.get(i);
				if (isFieldValueFiltered(epath, value)) {
					values.set(i, null);
					//newValues.add(value);
				}
			}						
			return values;
		}
		
		private boolean isFieldValueFiltered(String field, String value) {
			ExclusionListLookup exlookup = new ExclusionListLookup();
			return exlookup.isFieldValueInExclusion(value, field, FilterConstants.BLOCK_EXCLUSION_TYPE);			
			/*
			if (field.equals("Person.FirstName")) {
				
				if ( value != null && value.equals("John")) {
				  return true;
				}
			}
			*/
			//return false;
		}
		
		private void parseEpath(String path) throws Exception {
			String e = path;
			int index = path.indexOf('+');
			if (index > 0) {
			  e = path.substring(0, index);
			  if (index + 1 < path.length()) {
			    String blockIDGclass = path.substring(index+1);
			    
			    Class c = Class.forName(blockIDGclass);
			    blockIDGenerator = (BlockIDGenerator)c.newInstance();
			  }
			}
			qname = e;
			e = convertStarEpath(e);
			this.epath = EPathParser.parse(e);
			
		}
		
		public String toString() {
			StringBuffer buf = new StringBuffer("Rule:");
		   if (isLeaf) {
			   buf.append( "field:" + epath.toString());
		   } else {
			   
			   buf.append("Operator:" + operator_);
				for (int i = 0; i < children.size(); i++) {
						Rule rule = children.get(i);
						String str = rule.toString();
						buf.append(str);
						
				}
		   }
		   return buf.toString();
		}
		public String getSource() {
			return source;
		}
		public boolean isLeaf() {
			return isLeaf;
		}
		public List<Rule> getChildren() {
			return children;
		}			
					
  }
	
	private static List<String> add(List<String> values, List<String> source, Operator operator) {
	    List<String> retvalues = new ArrayList<String>();
	    if (values.size() == 0) {
			   retvalues = source;
		} else if (source.size() == 0) {
			   retvalues = values;
		} else if (operator.equals(Operator.AND)) {
		   /*
		    *  AND leads to matrix values muliplication.
		    *  So say values= {"1", "2", "3"}
		    *  source = {"a", "b"}
		    *  Then "AND" operator would result in:
		    *  {"1a", "1b","2a", "2b", "3a", "3b"}
		    *  
		    *  "OR" would result in :
		    *  {"1", "2", "3", "a", "b"}
		    *  
		    */
		   		   		    
		     for (int i = 0; i < values.size(); i++ ) {
			   for (int j = 0; j < source.size(); j++) {
				   String value = values.get(i) + source.get(j);
				   retvalues.add(value);
			   }
		     }
		   
	    } else {
		     /*
		      * OR so do just addition
		      */
		       retvalues = values;
		   
		      for (int j = 0; j < source.size(); j++) {
			    String value = source.get(j);
		        retvalues.add(value);
		      }
		   
	    }
		return retvalues;
	}
	
	private static boolean isChildEpath(EPath epath) {
		String objectPath = epath.getLastChildPath();
		String objectType = epath.getLastChildName();
		if (objectPath.equals(objectType)) {
			return false;
		}
		return true;
	}
   
	public static void main(String[] args) {
		try {
			BlockDefinition[] blockdefs = new BlockDefinition[3];
			blockdefs[0] = new BlockDefinition();
			BlockDefinition.Rule ruleC = new BlockDefinition.Rule();
			BlockDefinition.Rule rule = new BlockDefinition.Rule(new String[]{"Person.FnamePhoneticCode","Person.LnamePhoneticCode"});
			ruleC.addChild(rule);
			rule = new BlockDefinition.Rule(new String[] {"Person.Alias.FnamePhoneticCode","Person.Alias.LnamePhoneticCode"});
			ruleC.addChild(rule);
			ruleC.setOperator(Operator.OR);
			blockdefs[0].addRule(ruleC);
			blockdefs[0].setId("1");
			
			blockdefs[1] = new BlockDefinition();
			blockdefs[1].addRule("Person.SSN");
			blockdefs[1].setId("2");
			
			blockdefs[2] = new BlockDefinition();
			blockdefs[2].addRule("Person.FnamePhoneticCode");
			blockdefs[2].addRule("Person.Gender");
			blockdefs[2].addRule("Person.DOB");
			blockdefs[2].setId("3");
	
			String str = null;
			for (int i =0; i < blockdefs.length; i++) {
			   str = blockdefs[i].toString();	
			}			
		} catch (Exception ex) {
		    System.out.println(ex);	
		}	
	}
	
	private List<String> insertIds(List<String> blockids) {
	  List<String> blkids = new ArrayList<String>();
	  blockids = removeDups(blockids);
	  if (blockids == null || blockids.size() == 0) {
		  return null;
	  }
	  for (int i = 0; i < blockids.size(); i++) {  
		  
		  blkids.add(id + ":" + blockids.get(i));
	  }
	  if (blkids.contains("null")) {
		logger.info("blockids");  
	  }
	  return blkids;
	}
	
	private List<String> removeDups(List<String> blockids) {
		if (blockids == null) {
			return blockids;
		}
		if (blockids.size() == 0) {
			return blockids;
		}
		HashMap<String,String> map = new HashMap<String,String>();
		for (int i = 0; i < blockids.size(); i++) {
			String value = blockids.get(i);
			if (value == null || value.contains("null")) {
				continue;
			}
			if (map.get(value)== null) {
			   map.put(value, value);		
			}
		}
		
		Collection<String> col = map.values();
		List<String> ret = new ArrayList<String>(col);
		return ret;
		
	}
		
	private static String convertStarEpath(String e) {
	  String s = e;
	  int index = e.indexOf(".");
	  String firstS = s.substring(0, index);
	  String sub = e.substring(index+1);
	  
	  sub =  sub.replaceAll("\\.", "[*].");
	  return firstS + "." + sub;
	}

	public List<Rule> getRules() {
		return rules_;
	}

}
