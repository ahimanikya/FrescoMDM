/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.parser;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author wee
 */
public class MIQueryBuilder {
  
    private final String mFmt = "                [QueryType]";
    private final String mTagConfiguration = "Configuration";
    private final String mTagQueryBuilderConfig = "QueryBuilderConfig";
    private final String mTagModuleName = "module-name";
    private final String mTagParserClass = "parser-class";
    private final String mTagQueryBuilder = "query-builder";
    private final String mTagConfig = "config";
    
    private ArrayList mQueryBuilderName = new ArrayList();

    /**
     * @return ArrayList ret QueryBuilders
     */
    public ArrayList getQueryBuilders() {
        return mQueryBuilderName;
    }


    
    private void parseQueryBuilder(Node node) {
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node tmp = nnm.getNamedItem("name");
            if (tmp != null) {
                mQueryBuilderName.add(tmp.getNodeValue());
            }
        }
    }
    
    void parseQueryBuilderConfig(Node node) {
        NamedNodeMap nnm = node.getAttributes();

        NodeList nl2 = node.getChildNodes();
        for (int i2 = 0; i2 < nl2.getLength(); i2++) {
            if (nl2.item(i2).getNodeType() == Node.ELEMENT_NODE) {
                String name2 = ((Element) nl2.item(i2)).getTagName();
                if (mTagQueryBuilder.equals(name2)) {
                    parseQueryBuilder(nl2.item(i2));
                }
            }
        }
    }
    
    /**
     * parse
     * @param node Node
     */
    public void parse(Node node) {
        if (node.getNodeType() == Node.DOCUMENT_NODE) {
            NodeList nl1 = node.getChildNodes();
            Node element = null;
            for (int i = 0; i < nl1.getLength(); i++) {
                if (nl1.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    element = nl1.item(i);
                    break;
                }
            }

            if (null != element
                     && ((Element) element).getTagName().equals(mTagConfiguration)
                     && element.hasChildNodes()) {
                nl1 = element.getChildNodes();
                for (int i1 = 0; i1 < nl1.getLength(); i1++) {
                    if (nl1.item(i1).getNodeType() == Node.ELEMENT_NODE) {
                        String name = ((Element) nl1.item(i1)).getTagName();
                        if (mTagQueryBuilderConfig.equals(name)) {
                            parseQueryBuilderConfig(nl1.item(i1));
                        }
                    }
                }
            }
        }
        
    }

}
