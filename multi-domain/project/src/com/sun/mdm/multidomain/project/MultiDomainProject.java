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
package com.sun.mdm.multidomain.project;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ant.AntArtifact;
//import org.netbeans.modules.compapp.projects.base.ui.IcanproCustomizerProvider;
//import org.netbeans.modules.compapp.projects.base.ui.IcanproLogicalViewProvider;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.SubprojectProvider;
import org.netbeans.spi.project.support.ant.AntProjectEvent;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.support.ant.AntProjectListener;
import org.netbeans.spi.project.support.ant.GeneratedFilesHelper;
import org.netbeans.spi.project.support.ant.ProjectXmlSavedHook;
import org.netbeans.spi.project.support.ant.PropertyUtils;
import org.netbeans.spi.project.ui.PrivilegedTemplates;
import org.netbeans.spi.project.ui.RecommendedTemplates;
import org.netbeans.spi.project.support.ant.ReferenceHelper;
import org.netbeans.spi.project.support.ant.PropertyEvaluator;
import org.netbeans.spi.project.support.ant.SourcesHelper;
import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.netbeans.spi.queries.FileBuiltQueryImplementation;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;
import org.openide.util.Mutex;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import java.util.HashMap;
import java.util.Map;
import org.netbeans.modules.j2ee.deployment.devmodules.api.J2eeModule;
import org.netbeans.modules.j2ee.deployment.devmodules.spi.J2eeModuleProvider;
import org.netbeans.spi.java.project.support.ui.BrokenReferencesSupport;
import org.netbeans.spi.project.AuxiliaryConfiguration;
import org.netbeans.spi.project.ant.AntArtifactProvider;
import org.netbeans.spi.project.support.ant.EditableProperties;
import org.openide.modules.InstalledFileLocator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.apache.tools.ant.module.api.support.ActionUtils;
//import com.sun.mdm.index.util.Logger;
import com.sun.mdm.multidomain.util.Logger;

/**
 * Represents one ejb module project
 */
public class MultiDomainProject implements Project, AntProjectListener {
    private static final Logger mLogger = Logger.getLogger(
            MultiDomainProject.class.getName()
        
        );

    private static final Icon PROJECT_ICON = new ImageIcon(Utilities.loadImage("com/sun/mdm/multidomain/project/resources/MultiDomainMDMProjectIcon.png")); // NOI18N
    public static final String SOURCES_TYPE_ICANPRO = "BIZPRO";

    public static final String MODULE_INSTALL_NAME = "modules/com-sun-mdm-multidomain-project.jar";
    public static final String MODULE_INSTALL_CBN = "com.sun.mdm.multidomain.project";
    public static final String MODULE_INSTALL_DIR = "module.install.dir";

    public static final String COMMAND_GENEVIEW = "gen-mdm-index-files";
    public static final String COMMAND_GENLOADER = "gen-loader-zip";
    //public static final String COMMAND_GENBULKLOADER = "gen-bulkloader-zip";
    
    public static final String COMMAND_CLEANSER = "gen-cleanser-zip";
    public static final String COMMAND_PROFILER = "gen-profiler-zip";
    
    public static final String COMMAND_REDEPLOY = "redeploy";
    public static final String COMMAND_DEPLOY = "deploy";
    
    private final AntProjectHelper helper;
    private final PropertyEvaluator eval;
    private final ReferenceHelper refHelper;
    private final GeneratedFilesHelper genFilesHelper;
    private final Lookup lookup;
    private final ProjectEar appModule;
    private MultiDomainProjectLogicalViewProvider projectLogicalViewProvider;

    public MultiDomainProject(final AntProjectHelper helper) throws IOException {
        this.helper = helper;
        eval = createEvaluator();
        AuxiliaryConfiguration aux = helper.createAuxiliaryConfiguration();
        refHelper = new ReferenceHelper(helper, aux, helper.getStandardPropertyEvaluator());
        genFilesHelper = new GeneratedFilesHelper(helper);
        appModule = new ProjectEar(this);
        lookup = createLookup(aux);
        helper.addAntProjectListener(this);
        
    }

    public FileObject getProjectDirectory() {
        return helper.getProjectDirectory();
    }

    @Override
    public String toString() {
        return "MultiDomainProject[" + getProjectDirectory() + "]"; // NOI18N
    }

    private PropertyEvaluator createEvaluator() {
        // XXX might need to use a custom evaluator to handle active platform substitutions... TBD
        return helper.getStandardPropertyEvaluator();
    }

    PropertyEvaluator evaluator() {
        return eval;
    }

    public Lookup getLookup() {
        return lookup;
    }
    
    public AntProjectHelper getAntProjectHelper() {
        return helper;
    }
    
    FileObject getFileObject(String propname) {
        String prop = helper.getStandardPropertyEvaluator().getProperty(propname);
        if (prop != null) {
            return helper.resolveFileObject(prop);
        } else {
            return null;
        }
    }
    
    File getFile(String propname) {
        String prop = helper.getStandardPropertyEvaluator().getProperty(propname);
        if (prop != null) {
            return helper.resolveFile(prop);
        } else {
            return null;
        }
    }
    
    //for deploy ear file. it is used in ProjectEar.java
    public FileObject getOrCreateMetaInfDir() {
        String metaInfProp = helper.getStandardPropertyEvaluator().
                getProperty(MultiDomainProjectProperties.META_INF);
        if (metaInfProp == null) {
            File projectProperties = helper.resolveFile(AntProjectHelper.PROJECT_PROPERTIES_PATH);
            if (projectProperties.exists()) {
                mLogger.warn("Cannot resolve " + MultiDomainProjectProperties.META_INF + // NOI18N
                        " property for " + this); // NOI18N
            }
            return null;
        }
        FileObject metaInfFO = null;
        try {
            File prjDirF = FileUtil.toFile(getProjectDirectory());
            File rootF = prjDirF;
            while (rootF.getParentFile() != null) {
                rootF = rootF.getParentFile();
            }
            File metaInfF = PropertyUtils.resolveFile(prjDirF, metaInfProp);
            String metaInfPropRel = PropertyUtils.relativizeFile(rootF, metaInfF);
            assert metaInfPropRel != null;
            metaInfFO = FileUtil.createFolder(FileUtil.toFileObject(rootF), metaInfPropRel);
        } catch (IOException ex) {
            assert false : ex;
        }
        return metaInfFO;
    }

    public String getServerID() {
        return helper.getStandardPropertyEvaluator().getProperty(MultiDomainProjectProperties.J2EE_SERVER_TYPE);
    }
    
    public String getServerInstanceID() {
        return helper.getStandardPropertyEvaluator().getProperty(MultiDomainProjectProperties.J2EE_SERVER_INSTANCE);
    }
    
    public String getJ2eePlatformVersion() {
        return  helper.getStandardPropertyEvaluator().getProperty(MultiDomainProjectProperties.J2EE_PLATFORM);
    }
    
    public ProjectEar getAppModule() {
        return appModule;
    }


    private Lookup createLookup(AuxiliaryConfiguration aux) {
        SubprojectProvider spp = refHelper.createSubprojectProvider();
        FileBuiltQueryImplementation fileBuilt = helper.createGlobFileBuiltQuery(helper.getStandardPropertyEvaluator(), 
            new String[] {"${src.dir}/*.java"}, // NOI18N
            new String[] {"${build.classes.dir}/*.class"} // NOI18N
        );
        final SourcesHelper sourcesHelper = new SourcesHelper(helper, evaluator());
        //String webModuleLabel = org.openide.util.NbBundle.getMessage(IcanproCustomizerProvider.class, "LBL_Node_EJBModule"); //NOI18N
        //String srcJavaLabel = org.openide.util.NbBundle.getMessage(IcanproCustomizerProvider.class, "LBL_Node_Sources"); //NOI18N

        //sourcesHelper.addPrincipalSourceRoot("${"+MultiDomainProjectProperties.SOURCE_ROOT+"}", webModuleLabel, /*XXX*/null, null);
        //sourcesHelper.addPrincipalSourceRoot("${"+MultiDomainProjectProperties.SRC_DIR+"}", srcJavaLabel, /*XXX*/null, null);

        //sourcesHelper.addTypedSourceRoot("${"+MultiDomainProjectProperties.SRC_DIR+"}", SOURCES_TYPE_ICANPRO, srcJavaLabel, /*XXX*/null, null);
        //sourcesHelper.addTypedSourceRoot("${"+MultiDomainProjectProperties.SRC_DIR+"}", JavaProjectConstants.SOURCES_TYPE_JAVA, srcJavaLabel, /*XXX*/null, null);
        ProjectManager.mutex().postWriteRequest(new Runnable() {
            public void run() {
                sourcesHelper.registerExternalRoots(FileOwnerQuery.EXTERNAL_ALGORITHM_TRANSIENT);
            }
        });
        projectLogicalViewProvider = new MultiDomainProjectLogicalViewProvider(this, helper, evaluator(), spp, refHelper);
        return Lookups.fixed(new Object[] {
            new Info(),
            aux,
            helper.createCacheDirectoryProvider(),
            appModule,
            helper,
            spp,
            new MultiDomainProjectActionProvider( this, helper, refHelper ),
            projectLogicalViewProvider,
            //new IcanproCustomizerProvider( this, helper, refHelper ),
            new AntArtifactProviderImpl(),
            new ProjectXmlSavedHookImpl(),
            new ProjectOpenedHookImpl(),
            new MultiDomainProjectOperations(this),
            fileBuilt,
            new RecommendedTemplatesImpl(),
            refHelper,
            sourcesHelper.createSources(),
            helper.createSharabilityQuery(evaluator(),
                new String[] {"${"+MultiDomainProjectProperties.SOURCE_ROOT+"}"},
                new String[] {
                    "${"+MultiDomainProjectProperties.BUILD_DIR+"}",
                    "${"+MultiDomainProjectProperties.DIST_DIR+"}"}
                )
        });
    }

    public void configurationXmlChanged(AntProjectEvent ev) {
        if (ev.getPath().equals(AntProjectHelper.PROJECT_XML_PATH)) {
            // Could be various kinds of changes, but name & displayName might have changed.
            Info info = (Info)getLookup().lookup(ProjectInformation.class);
            info.firePropertyChange(ProjectInformation.PROP_NAME);
            info.firePropertyChange(ProjectInformation.PROP_DISPLAY_NAME);
        }
    }

    public void propertiesChanged(AntProjectEvent ev) {
        // currently ignored
        //TODO: should not be ignored!
    }

    String getBuildXmlName() {
        String storedName = helper.getStandardPropertyEvaluator().getProperty(MultiDomainProjectProperties.BUILD_FILE);
        if (storedName == null) {
            storedName = GeneratedFilesHelper.BUILD_XML_PATH;
        }
        return storedName;
    }

    // Package private methods -------------------------------------------------

    FileObject getSourceDirectory() {
        String srcDir = helper.getStandardPropertyEvaluator().getProperty("src.dir"); // NOI18N
        return helper.resolveFileObject(srcDir);
    }

    /** Return configured project name. */
    public String getName() {
        return (String) ProjectManager.mutex().readAccess(new Mutex.Action() {
            public Object run() {
                Element data = helper.getPrimaryConfigurationData(true);
                // XXX replace by XMLUtil when that has findElement, findText, etc.
                NodeList nl = data.getElementsByTagNameNS(MultiDomainProjectType.PROJECT_CONFIGURATION_NAMESPACE, "name");
                if (nl.getLength() == 1) {
                    nl = nl.item(0).getChildNodes();
                    if (nl.getLength() == 1 && nl.item(0).getNodeType() == Node.TEXT_NODE) {
                        Object name = nl.item(0).getNodeValue();
                        return ((Text) nl.item(0)).getNodeValue();
                    }
                }
                return "???"; // NOI18N
            }
        });
    }
    public void setName(final String name) {
        ProjectManager.mutex().writeAccess(new Mutex.Action() {
            public Object run() {
                Element data = helper.getPrimaryConfigurationData(true);
                // XXX replace by XMLUtil when that has findElement, findText, etc.
                NodeList nl = data.getElementsByTagNameNS(MultiDomainProjectType.PROJECT_CONFIGURATION_NAMESPACE, "name"); // NOI18N
                Element nameEl;
                if (nl.getLength() == 1) {
                    nameEl = (Element) nl.item(0);
                    NodeList deadKids = nameEl.getChildNodes();
                    while (deadKids.getLength() > 0) {
                        nameEl.removeChild(deadKids.item(0));
                    }
                } else {
                    nameEl = data.getOwnerDocument().createElementNS(MultiDomainProjectType.PROJECT_CONFIGURATION_NAMESPACE, "name"); // NOI18N
                    data.insertBefore(nameEl, /* OK if null */data.getChildNodes().item(0));
                }
                nameEl.appendChild(data.getOwnerDocument().createTextNode(name));
                helper.putPrimaryConfigurationData(data, true);
                return null;
            }
        });
    }
    
    public org.openide.nodes.Node getRootNode() {
        return projectLogicalViewProvider.getRootNode();
    }

    // Private innerclasses ----------------------------------------------------

    private final class Info implements ProjectInformation {

        private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        Info() {}

        void firePropertyChange(String prop) {
            pcs.firePropertyChange(prop, null, null);
        }

        public String getName() {
            return MultiDomainProject.this.getName();
        }

        public String getDisplayName() {
            return MultiDomainProject.this.getName();
        }

        public Icon getIcon() {
            return PROJECT_ICON;
        }

        public Project getProject() {
            return MultiDomainProject.this;
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }

    }

    private final class ProjectXmlSavedHookImpl extends ProjectXmlSavedHook {

        ProjectXmlSavedHookImpl() {}

        protected void projectXmlSaved() throws IOException {
            genFilesHelper.refreshBuildScript(
                GeneratedFilesHelper.BUILD_IMPL_XML_PATH,
                MultiDomainProject.class.getResource("resources/build-impl.xsl"),
                false);
            genFilesHelper.refreshBuildScript(
                getBuildXmlName(),
                MultiDomainProject.class.getResource("resources/build.xsl"),
                false);
        }

    }

    private final class ProjectOpenedHookImpl extends ProjectOpenedHook {

        ProjectOpenedHookImpl() {}

        protected void projectOpened() {
            try {
                Map<String, J2eeModuleProvider> mods = new HashMap<String, J2eeModuleProvider>();
                FileObject multiDomaindir= helper.getProjectDirectory();
                EditableProperties ep = helper.getProperties(AntProjectHelper.PROJECT_PROPERTIES_PATH);
                //add EJB project to Multi-Domain MDM project for deployment
                String path = ep.getProperty(MultiDomainProjectProperties.EJB_DIR);
                FileObject subAppDirFO = multiDomaindir.getFileObject(path);
                Project p = ProjectManager.getDefault().findProject(subAppDirFO);
                J2eeModuleProvider jmp = p.getLookup().lookup(J2eeModuleProvider.class);
                if (null != jmp) {
                    J2eeModule jm = jmp.getJ2eeModule();
                    if (null != jm) {
                        mods.put(path+".jar", jmp);
                    }
                }
                //add Web Project to Multi-Domain MDM project for deployment
                path = ep.getProperty(MultiDomainProjectProperties.WAR_DIR);
                subAppDirFO = multiDomaindir.getFileObject(path);
                p = ProjectManager.getDefault().findProject(subAppDirFO);
                jmp = p.getLookup().lookup(J2eeModuleProvider.class);
                if (null != jmp) {
                    J2eeModule jm = jmp.getJ2eeModule();
                    if (null != jm) {
                        mods.put(path+".war", jmp);
                    }
                }
                
                getAppModule().setModules(mods);
                // Check up on build scripts.
                genFilesHelper.refreshBuildScript(
                    GeneratedFilesHelper.BUILD_IMPL_XML_PATH,
                    MultiDomainProject.class.getResource("resources/build-impl.xsl"),
                    true);
                genFilesHelper.refreshBuildScript(
                    getBuildXmlName(),
                    MultiDomainProject.class.getResource("resources/build.xsl"),
                    true);
                // Set MODULE_INSTALL_DIR.
                ProjectManager.mutex().writeAccess(new Mutex.Action() {
                    public Object run() {
                        EditableProperties pep = helper.getProperties(AntProjectHelper.PRIVATE_PROPERTIES_PATH);
                        pep.setProperty("netbeans.user", System.getProperty("netbeans.user"));


                        File f = InstalledFileLocator.getDefault().locate(MODULE_INSTALL_NAME, MODULE_INSTALL_CBN, false);
                        if (f != null) {
                            pep.setProperty(MODULE_INSTALL_DIR, f.getParentFile().getPath());
                        }

                        helper.putProperties(AntProjectHelper.PRIVATE_PROPERTIES_PATH, pep);
                        try {
                            ProjectManager.getDefault().saveProject(MultiDomainProject.this);
                        } catch (IOException e) {
                            ErrorManager.getDefault().notify(e);
                        }
                        return null;
                    }
                });
                
                String autoGenerate = ep.getProperty(MultiDomainProjectProperties.AUTO_GENERATE);
                if (autoGenerate != null && autoGenerate.equals("Yes")) {
                    ep.remove(MultiDomainProjectProperties.AUTO_GENERATE);
                    helper.putProperties(AntProjectHelper.PROJECT_PROPERTIES_PATH, ep);
                    final FileObject buildXml = multiDomaindir.getFileObject(getBuildXmlName());
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                ActionUtils.runTarget(buildXml, new String[] {MultiDomainProject.COMMAND_GENEVIEW}, null);
                            } catch (IOException e) {
                                ErrorManager.getDefault().notify(e);
                            }
                        }
                    });
                }
            } catch (IOException e) {
                ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, e);
            }

            //if (IcanproLogicalViewProvider.hasBrokenLinks(helper, refHelper)) {
            //    BrokenReferencesSupport.showAlert();
            //}
            
        }

        protected void projectClosed() {
            // Probably unnecessary, but just in case:
            try {
                ProjectManager.getDefault().saveProject(MultiDomainProject.this);
            } catch (IOException e) {
                ErrorManager.getDefault().notify(e);
            }
        }
    }

    /**
     * Exports the main JAR as an official build product for use from other scripts.
     * The type of the artifact will be {@link AntArtifact}.
     */
    private final class AntArtifactProviderImpl implements AntArtifactProvider {

        public AntArtifact[] getBuildArtifacts() {
            return new AntArtifact[] {
                helper.createSimpleAntArtifact(
                        MultiDomainProjectProperties.ARTIFACT_TYPE_JBI_ASA + ":" +
                        helper.getStandardPropertyEvaluator().getProperty(
                        MultiDomainProjectProperties.JBI_SE_TYPE),
                        MultiDomainProjectProperties.SE_DEPLOYMENT_JAR,
                        helper.getStandardPropertyEvaluator(), "dist_se", "clean"), // NOI18N
            };
        }
    }

    private static final class RecommendedTemplatesImpl implements RecommendedTemplates, PrivilegedTemplates {

        // List of primarily supported templates

        private static final String[] TYPES = new String[] {
            "XML",                  // NOI18N
            "simple-files"          // NOI18N
        };

        private static final String[] PRIVILEGED_NAMES = new String[] {
            "Templates/CAPS/eView.eview",
        };

        public String[] getRecommendedTypes() {
            return TYPES;
        }

        public String[] getPrivilegedTemplates() {
            return PRIVILEGED_NAMES;
        }


    }
}
