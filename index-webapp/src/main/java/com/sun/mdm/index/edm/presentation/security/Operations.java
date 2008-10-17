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

     
/*
 * Operations.java 
 * Created on November 20, 2007
 *  
 */

package com.sun.mdm.index.edm.presentation.security;

import com.sun.mdm.index.edm.control.UserProfile;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;


public class Operations {

    private boolean AuditLog_SearchView;
    private boolean AuditLog_Print;
    private boolean EO_Deactivate;
    private boolean EO_SearchViewSBR;
    private boolean EO_Compare;
    private boolean EO_PrintSBR;
    private boolean EO_Activate;
    private boolean EO_Edit;
    private boolean EO_Unmerge;
    private boolean EO_PrintComparison;
    private boolean EO_Merge;
    private boolean EO_Create;
    private boolean EO_ViewMergeTree;
    private boolean EO_OverwriteSBR;
    private boolean EO_LinkSBRFields;
    private boolean EO_UnlinkSBRFields;
    private boolean EO_LockSBRFields;
    private boolean EO_UnlockSBRFields;
    private boolean AssumedMatch_SearchView;
    private boolean AssumedMatch_Undo;
    private boolean AssumedMatch_Print;
    private boolean TransLog_SearchView;
    private boolean TransLog_Print;
    private boolean SO_Remove;
    private boolean SO_SearchView;
    private boolean SO_Edit;
    private boolean SO_Compare;
    private boolean SO_Add;
    private boolean SO_Unmerge;
    private boolean SO_Merge;
    private boolean SO_Print;
    private boolean SO_Deactivate;
    private boolean SO_Activate;
    private boolean PotDup_SearchView;
    private boolean PotDup_Print;
    private boolean PotDup_Unresolve;
    private boolean PotDup_ResolveUntilRecalc;
    private boolean PotDup_ResolvePermanently;
    private boolean Reports_View;
    private boolean Reports_MergedRecords;
    private boolean Reports_DeactivatedEUIDs;
    private boolean Reports_UnmergedRecords;
    private boolean Reports_Updates;
    private boolean Reports_Activity;
    private boolean Reports_Duplicates;
    private boolean Reports_AssumedMatches;
    private boolean Field_VIP;
    private HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    

    public Operations(){
        UserProfile userProfile = (UserProfile) session.getAttribute("userProfile");
        if (userProfile != null) {
            this.setAuditLog_SearchView(userProfile.isAllowed("AuditLog_SearchView"));
            this.setAuditLog_Print(userProfile.isAllowed("AuditLog_Print"));
            this.setEO_Activate(userProfile.isAllowed("EO_Activate"));
            this.setEO_Compare(userProfile.isAllowed("EO_Compare"));
            this.setEO_Create(userProfile.isAllowed("EO_Create"));
            this.setEO_Deactivate(userProfile.isAllowed("EO_Deactivate"));
            this.setEO_Activate(userProfile.isAllowed("EO_Activate"));
            this.setEO_Edit(userProfile.isAllowed("EO_Edit"));
            this.setEO_LinkSBRFields(userProfile.isAllowed("EO_LinkSBRFields"));
            this.setEO_LockSBRFields(userProfile.isAllowed("EO_LockSBRFields"));
            this.setEO_Merge(userProfile.isAllowed("EO_Merge"));
            this.setEO_OverwriteSBR(userProfile.isAllowed("EO_OverwriteSBR"));
            this.setEO_PrintComparison(userProfile.isAllowed("EO_PrintComparison"));
            this.setEO_PrintSBR(userProfile.isAllowed("EO_PrintSBR"));
            this.setEO_SearchViewSBR(userProfile.isAllowed("EO_SearchViewSBR"));
            this.setEO_UnlinkSBRFields(userProfile.isAllowed("EO_UnlinkSBRFields"));
            this.setEO_UnlockSBRFields(userProfile.isAllowed("EO_UnlockSBRFields"));
            this.setEO_Unmerge(userProfile.isAllowed("EO_Unmerge"));
            this.setEO_ViewMergeTree(userProfile.isAllowed("EO_ViewMergeTree"));
            this.setAssumedMatch_Print(userProfile.isAllowed("AssumedMatch_Print"));
            this.setAssumedMatch_SearchView(userProfile.isAllowed("AssumedMatch_SearchView"));
            this.setAssumedMatch_Undo(userProfile.isAllowed("AssumedMatch_Undo"));
            this.setTransLog_Print(userProfile.isAllowed("TransLog_Print"));
            this.setTransLog_SearchView(userProfile.isAllowed("TransLog_SearchView"));
            this.setSO_Activate(userProfile.isAllowed("SO_Activate"));
            this.setSO_Add(userProfile.isAllowed("SO_Add"));
            this.setSO_Compare(userProfile.isAllowed("SO_Compare"));
            this.setSO_Deactivate(userProfile.isAllowed("SO_Deactivate"));
            this.setSO_Edit(userProfile.isAllowed("SO_Edit"));
            this.setSO_Merge(userProfile.isAllowed("SO_Merge"));
            this.setSO_Print(userProfile.isAllowed("SO_Print"));
            this.setSO_Remove(userProfile.isAllowed("SO_Remove"));
            this.setSO_SearchView(userProfile.isAllowed("SO_SearchView"));
            this.setSO_Unmerge(userProfile.isAllowed("SO_Unmerge"));
            this.setPotDup_SearchView(userProfile.isAllowed("PotDup_SearchView"));
            this.setPotDup_Print(userProfile.isAllowed("PotDup_Print"));
            this.setPotDup_ResolvePermanently(userProfile.isAllowed("PotDup_ResolvePermanently"));
            this.setPotDup_ResolveUntilRecalc(userProfile.isAllowed("PotDup_ResolveUntilRecalc"));
            this.setPotDup_Unresolve(userProfile.isAllowed("PotDup_Unresolve"));
            this.setReports_Activity(userProfile.isAllowed("Reports_Activity"));
            this.setReports_DeactivatedEUIDs(userProfile.isAllowed("Reports_DeactivatedEUIDs"));
            this.setReports_Duplicates(userProfile.isAllowed("Reports_Duplicates"));
            this.setReports_MergedRecords(userProfile.isAllowed("Reports_MergedRecords"));
            this.setReports_UnmergedRecords(userProfile.isAllowed("Reports_UnmergedRecords"));
            this.setReports_Updates(userProfile.isAllowed("Reports_Updates"));
            this.setReports_AssumedMatches(userProfile.isAllowed("Reports_AssumedMatches"));
            this.setReports_View(userProfile.isAllowed("Reports_View"));
            this.setField_VIP(userProfile.isAllowed("Field_VIP"));
        }
    }
    public boolean isAuditLog_SearchView() {
        return AuditLog_SearchView;
    }

    public void setAuditLog_SearchView(boolean AuditLog_SearchView) {
        this.AuditLog_SearchView = AuditLog_SearchView;
    }

    public boolean isAuditLog_Print() {
        return AuditLog_Print;
    }

    public void setAuditLog_Print(boolean AuditLog_Print) {
        this.AuditLog_Print = AuditLog_Print;
    }

    public boolean isEO_Deactivate() {
        return EO_Deactivate;
    }

    public void setEO_Deactivate(boolean EO_Deactivate) {
        this.EO_Deactivate = EO_Deactivate;
    }

    public boolean isEO_SearchViewSBR() {
        return EO_SearchViewSBR;
    }

    public void setEO_SearchViewSBR(boolean EO_SearchViewSBR) {
        this.EO_SearchViewSBR = EO_SearchViewSBR;
    }

    public boolean isEO_Compare() {
        return EO_Compare;
    }

    public void setEO_Compare(boolean EO_Compare) {
        this.EO_Compare = EO_Compare;
    }

    public boolean isEO_PrintSBR() {
        return EO_PrintSBR;
    }

    public void setEO_PrintSBR(boolean EO_PrintSBR) {
        this.EO_PrintSBR = EO_PrintSBR;
    }

    public boolean isEO_Activate() {
        return EO_Activate;
    }

    public void setEO_Activate(boolean EO_Activate) {
        this.EO_Activate = EO_Activate;
    }

    public boolean isEO_Edit() {
        return EO_Edit;
    }

    public void setEO_Edit(boolean EO_Edit) {
        this.EO_Edit = EO_Edit;
    }

    public boolean isEO_Unmerge() {
        return EO_Unmerge;
    }

    public void setEO_Unmerge(boolean EO_Unmerge) {
        this.EO_Unmerge = EO_Unmerge;
    }

    public boolean isEO_PrintComparison() {
        return EO_PrintComparison;
    }

    public void setEO_PrintComparison(boolean EO_PrintComparison) {
        this.EO_PrintComparison = EO_PrintComparison;
    }

    public boolean isEO_Merge() {
        return EO_Merge;
    }

    public void setEO_Merge(boolean EO_Merge) {
        this.EO_Merge = EO_Merge;
    }

    public boolean isEO_Create() {
        return EO_Create;
    }

    public void setEO_Create(boolean EO_Create) {
        this.EO_Create = EO_Create;
    }

    public boolean isEO_ViewMergeTree() {
        return EO_ViewMergeTree;
    }

    public void setEO_ViewMergeTree(boolean EO_ViewMergeTree) {
        this.EO_ViewMergeTree = EO_ViewMergeTree;
    }

    public boolean isEO_OverwriteSBR() {
        return EO_OverwriteSBR;
    }

    public void setEO_OverwriteSBR(boolean EO_OverwriteSBR) {
        this.EO_OverwriteSBR = EO_OverwriteSBR;
    }

    public boolean isEO_LinkSBRFields() {
        return EO_LinkSBRFields;
    }

    public void setEO_LinkSBRFields(boolean EO_LinkSBRFields) {
        this.EO_LinkSBRFields = EO_LinkSBRFields;
    }

    public boolean isEO_UnlinkSBRFields() {
        return EO_UnlinkSBRFields;
    }

    public void setEO_UnlinkSBRFields(boolean EO_UnlinkSBRFields) {
        this.EO_UnlinkSBRFields = EO_UnlinkSBRFields;
    }

    public boolean isEO_LockSBRFields() {
        return EO_LockSBRFields;
    }

    public void setEO_LockSBRFields(boolean EO_LockSBRFields) {
        this.EO_LockSBRFields = EO_LockSBRFields;
    }

    public boolean isEO_UnlockSBRFields() {
        return EO_UnlockSBRFields;
    }

    public void setEO_UnlockSBRFields(boolean EO_UnlockSBRFields) {
        this.EO_UnlockSBRFields = EO_UnlockSBRFields;
    }

    public boolean isAssumedMatch_SearchView() {
        return AssumedMatch_SearchView;
    }

    public void setAssumedMatch_SearchView(boolean AssumedMatch_SearchView) {
        this.AssumedMatch_SearchView = AssumedMatch_SearchView;
    }

    public boolean isAssumedMatch_Undo() {
        return AssumedMatch_Undo;
    }

    public void setAssumedMatch_Undo(boolean AssumedMatch_Undo) {
        this.AssumedMatch_Undo = AssumedMatch_Undo;
    }

    public boolean isAssumedMatch_Print() {
        return AssumedMatch_Print;
    }

    public void setAssumedMatch_Print(boolean AssumedMatch_Print) {
        this.AssumedMatch_Print = AssumedMatch_Print;
    }

    public boolean isTransLog_SearchView() {
        return TransLog_SearchView;
    }

    public void setTransLog_SearchView(boolean TransLog_SearchView) {
        this.TransLog_SearchView = TransLog_SearchView;
    }

    public boolean isTransLog_Print() {
        return TransLog_Print;
    }

    public void setTransLog_Print(boolean TransLog_Print) {
         this.TransLog_Print = TransLog_Print;
    }

    public boolean isSO_Remove() {
        return SO_Remove;
    }

    public void setSO_Remove(boolean SO_Remove) {
        this.SO_Remove = SO_Remove;
    }

    public boolean isSO_SearchView() {
        return SO_SearchView;
    }

    public void setSO_SearchView(boolean SO_SearchView) {
        this.SO_SearchView = SO_SearchView;
    }

    public boolean isSO_Edit() {
        return SO_Edit;
    }

    public void setSO_Edit(boolean SO_Edit) {
        this.SO_Edit = SO_Edit;
    }

    public boolean isSO_Compare() {
        return SO_Compare;
    }

    public void setSO_Compare(boolean SO_Compare) {
        this.SO_Compare = SO_Compare;
    }

    public boolean isSO_Add() {
        return SO_Add;
    }

    public void setSO_Add(boolean SO_Add) {
        this.SO_Add = SO_Add;
    }

    public boolean isSO_Unmerge() {
        return SO_Unmerge;
    }

    public void setSO_Unmerge(boolean SO_Unmerge) {
        this.SO_Unmerge = SO_Unmerge;
    }

    public boolean isSO_Merge() {
        return SO_Merge;
    }

    public void setSO_Merge(boolean SO_Merge) {
        this.SO_Merge = SO_Merge;
    }

    public boolean isSO_Print() {
        return SO_Print;
    }

    public void setSO_Print(boolean SO_Print) {
        this.SO_Print = SO_Print;
    }

    public boolean isSO_Deactivate() {
        return SO_Deactivate;
    }

    public void setSO_Deactivate(boolean SO_Deactivate) {
        this.SO_Deactivate = SO_Deactivate;
    }

    public boolean isSO_Activate() {
        return SO_Activate;
    }

    public void setSO_Activate(boolean SO_Activate) {
        this.SO_Activate = SO_Activate;
    }

    public boolean isPotDup_SearchView() {
        return PotDup_SearchView;
    }

    public void setPotDup_SearchView(boolean PotDup_SearchView) {
        this.PotDup_SearchView = PotDup_SearchView;
    }


    public boolean isPotDup_Unresolve() {
        return PotDup_Unresolve;
    }

    public void setPotDup_Unresolve(boolean PotDup_Unresolve) {
        this.PotDup_Unresolve = PotDup_Unresolve;
    }

    public boolean isPotDup_ResolveUntilRecalc() {
        return PotDup_ResolveUntilRecalc;
    }

    public void setPotDup_ResolveUntilRecalc(boolean PotDup_ResolveUntilRecalc) {
        this.PotDup_ResolveUntilRecalc = PotDup_ResolveUntilRecalc;
    }

    public boolean isPotDup_ResolvePermanently() {
        return PotDup_ResolvePermanently;
    }

    public void setPotDup_ResolvePermanently(boolean PotDup_ResolvePermanently) {
        this.PotDup_ResolvePermanently = PotDup_ResolvePermanently;
    }

    public boolean isReports_View() {
        return Reports_View;
    }

    public void setReports_View(boolean Reports_View) {
        this.Reports_View = Reports_View;
    }

    public boolean isReports_MergedRecords() {
        return Reports_MergedRecords;
    }

    public void setReports_MergedRecords(boolean Reports_MergedRecords) {
        this.Reports_MergedRecords = Reports_MergedRecords;
    }

    public boolean isReports_DeactivatedEUIDs() {
        return Reports_DeactivatedEUIDs;
    }

    public void setReports_DeactivatedEUIDs(boolean Reports_DeactivatedEUIDs) {
        this.Reports_DeactivatedEUIDs = Reports_DeactivatedEUIDs;
    }

    public boolean isReports_UnmergedRecords() {
        return Reports_UnmergedRecords;
    }

    public void setReports_UnmergedRecords(boolean Reports_UnmergedRecords) {
        this.Reports_UnmergedRecords = Reports_UnmergedRecords;
    }

    public boolean isReports_Updates() {
        return Reports_Updates;
    }

    public void setReports_Updates(boolean Reports_Updates) {
        this.Reports_Updates = Reports_Updates;
    }

    public boolean isReports_Activity() {
        return Reports_Activity;
    }

    public void setReports_Activity(boolean Reports_Activity) {
        this.Reports_Activity = Reports_Activity;
    }

    public boolean isReports_Duplicates() {
        return Reports_Duplicates;
    }

    public void setReports_Duplicates(boolean Reports_Duplicates) {
        this.Reports_Duplicates = Reports_Duplicates;
    }

    
    public boolean isField_VIP() {
        return Field_VIP;
    }

    public void setField_VIP(boolean Field_VIP) {
        this.Field_VIP = Field_VIP;
    }

    public boolean isReports_AssumedMatches() {
        return Reports_AssumedMatches;
    }

    public void setReports_AssumedMatches(boolean Reports_AssumedMatches) {
        this.Reports_AssumedMatches = Reports_AssumedMatches;
    }

    public boolean isPotDup_Print() {
        return PotDup_Print;
    }

    public void setPotDup_Print(boolean PotDup_Print) {
        this.PotDup_Print = PotDup_Print;
    }
}
