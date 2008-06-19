/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.index.project.ui.applicationeditor;

/**
 *
 * @author wee
 */
public class ConfigValidation {
    
    /*
     * validateObject method will return "Success" if Object created
     * by wizard is valid.  Otherwise, it will return appropriate error
     * description.
     * @return msg - error string if not valid.  Otherwise return "Success"
     */
    public static String validateObject(EntityNode primaryNode) {
        String msg = "Success";
        int cnt = primaryNode.getChildCount();
        if (cnt > 0) {
            EntityNode fieldNode = (EntityNode) primaryNode.getChildAt(0);
            if (!fieldNode.isField()) {
                msg = "Primary object node \"" + primaryNode.getName() + "\" contains no fields!";
            } else {
                boolean foundOneMatchType = false;
                for (int i = 0; i < cnt; i++) {
                    EntityNode objNode = (EntityNode) primaryNode.getChildAt(i);
                    if (objNode.isSub() && objNode.getChildCount() <= 0) {
                        msg = "Object node \"" + objNode.getName() + "\" contains no fields!";
                        break;
                    }

                    //if the node is sub node,check to make sure that sub node has at last one match type
                    if (validateSubNode(objNode)) {
                        foundOneMatchType = true;
                    }
                }

                if (!foundOneMatchType) {
                    msg = "The \"" + primaryNode.getRoot() + "\" Application must define at least one match type!";
                }
            }
        } else {
            msg = "Primary object node \"" + primaryNode.getName() + "\" contains no fields!";
        }
        return msg;

    }
    
    private static boolean validateSubNode(EntityNode objNode) {
        if (objNode.isSub()) {
            for (int j = 0; j < objNode.getChildCount(); j++) {
                EntityNode objChildNode = (EntityNode) objNode.getChildAt(j);
                if (objChildNode.isSub()) {
                    if (validateSubNode(objChildNode)) {
                        return true;
                    }
                } else {
                    if (objChildNode.getMatchType() != null) {
                        return true;
                    }
                }

            }
        } else {
            if (objNode.getMatchType() != null) {
               return true;
            }
        }
        return false;
    }

}
