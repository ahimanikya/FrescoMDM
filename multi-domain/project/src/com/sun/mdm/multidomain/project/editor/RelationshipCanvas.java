/*
 * Copyright (c) 2007, Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of Sun Microsystems, Inc. nor the names of its contributors
 *   may be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.sun.mdm.multidomain.project.editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.io.File;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Node;
import org.openide.nodes.Children;
import org.openide.util.Utilities;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DnDConstants;
import java.awt.Graphics2D;
import java.awt.Rectangle;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SwingUtilities;

import org.netbeans.api.project.Project;

/**
 * @author Kevin Kao
 */
public class RelationshipCanvas extends JPanel implements MouseListener, MouseMotionListener, PropertyChangeListener {
    private int diam = 10;
    private Paint paint = Color.BLUE;
    private BufferedImage backingImage;
    private Point last;
    private RelationshipCanvas mRelationshipCanvas;
    private EditorMainApp mEditorMainApp;
    static final Image DOMAINIMAGEICON = Utilities.loadImage("com/sun/mdm/multidomain/project/resources/MultiDomainFolderNode.png");

    
    public RelationshipCanvas(EditorMainApp editorMainApp) {
        mRelationshipCanvas = this;
        mEditorMainApp = editorMainApp;
        addMouseListener(this);
        addMouseMotionListener(this);
        setBackground(Color.WHITE);
        this.setDropTarget(new DropTarget(this, new DropTargetListener() { 
            String dragTargetName = "";
            public void dragEnter(DropTargetDragEvent dtde) {
            }

            public void dragExit(DropTargetEvent dte) {

            }

            public void dragOver(DropTargetDragEvent dtde) {
                if (isMasterIndexProject(dtde)) {
                    dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
                } else {
                    dtde.rejectDrag();
                }
            }

            public void drop(DropTargetDropEvent dtde) {
                // Add DomainNode to the canvas
                Transferable trans = dtde.getTransferable();
                DataFlavor[] dfs = trans.getTransferDataFlavors();
                if (dfs.length > 0) {
                    try {
                        Object obj = trans.getTransferData(dfs[0]);
                        if (obj instanceof Node) {
                            Node node = (Node) obj;
                            Project p = node.getLookup().lookup(Project.class);
                            if (p != null) {
                                FileObject pfobj = p.getProjectDirectory();
                                File selectedDomain = FileUtil.toFile(pfobj);
                                String domainName = selectedDomain.getName();
                                mEditorMainApp.addDomain(selectedDomain);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            public void dropActionChanged(DropTargetDragEvent dtde) {

            }
            
            public boolean isMasterIndexProject(DropTargetDragEvent dtde) {
                dragTargetName = "";
                boolean bRet = false;
                Transferable trans = dtde.getTransferable();

                DataFlavor[] dfs = trans.getTransferDataFlavors();
                if (dfs.length > 0) {
                    try {
                        Object obj = trans.getTransferData(dfs[0]);
                        if (obj instanceof Node) {
                            Node node = (Node) obj;
                            dragTargetName = node.getName();

                            Project p = node.getLookup().lookup(Project.class);
                            if (p != null) {
                                String clsName = p.getClass().getName();
                                if (clsName.equals("com.sun.mdm.index.project.EviewApplication")) {
                                    bRet = true;
                                    Graphics2D g2D = (Graphics2D) mRelationshipCanvas.getGraphics();
                                    Rectangle visRect = mRelationshipCanvas.getVisibleRect();
                                    mRelationshipCanvas.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);
                                    g2D.drawImage(DOMAINIMAGEICON, AffineTransform.getTranslateInstance(dtde.getLocation().getX(), dtde.getLocation().getY()), null);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                return bRet;
            }

        }));
    }
    
    public void propertyChange(final PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                propertyUpdate(evt);
            }
        });
    }

    private void propertyUpdate(PropertyChangeEvent evt) {
        String name   = evt.getPropertyName();
        Object source = evt.getSource();
        // ToDo
        // Get domain's object.xml etc
    }
    
    public void setBrush(int diam) {
        this.diam = diam;
    }
    
    public void setDiam(int val) {
        this.diam = val;
    }
    
    public int getDiam() {
        return diam;
    }
    
    public void setPaint(Paint c) {
        this.paint = c;
    }
    
    public Paint getPaint() {
        return paint;
    }
    
    public Color getColor() {
        if (paint instanceof Color) {
            return (Color) paint;
        } else {
            return Color.BLACK;
        }
    }
    
    public void clear() {
        backingImage = null;
        repaint();
    }
    
    public BufferedImage getImage() {
        int width = Math.min(getWidth(), 1600);
        int height = Math.min(getHeight(),1200);
        if (backingImage == null || backingImage.getWidth() != width || backingImage.getHeight() != height) {
            BufferedImage old = backingImage;
            backingImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics g = backingImage.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            if (old != null) {
                ((Graphics2D) backingImage.getGraphics()).drawRenderedImage(old,
                        AffineTransform.getTranslateInstance(0, 0));
            }
        }
        return backingImage;
    }
    
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawRenderedImage(getImage(), AffineTransform.getTranslateInstance(0,0));
    }
    
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        Graphics2D g = getImage().createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setPaint(getPaint());
        g.setStroke(new BasicStroke(diam, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        if (last == null) {
            last = p;
        }
        g.drawLine(last.x, last.y, p.x, p.y);
        repaint(Math.min(last.x, p.x) - diam / 2 - 1,
                Math.min(last.y, p.y) - diam / 2 - 1,
                Math.abs(last.x - p.x) + diam + 2,
                Math.abs(last.y - p.y) + diam + 2);
        last = p;
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mouseDragged(MouseEvent e) {
        mouseClicked(e);
    }
    
    public void mouseMoved(MouseEvent e) {
        last = null;
    }
    
    JComponent createBrushSizeView() {
        return new BrushSizeView();
    }
    
    
    private class BrushSizeView extends JComponent {
        
        public boolean isOpaque() {
            return true;
        }
        
        public void paint(Graphics g) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(getBackground());
            g.fillRect(0,0,getWidth(),getHeight());
            Point p = new Point(getWidth() / 2, getHeight() / 2);
            int half = getDiam() / 2;
            int diam = getDiam();
            g.setColor(getColor());
            g.fillOval(p.x - half, p.y - half, diam, diam);
        }
        
        public Dimension getPreferredSize() {
            return new Dimension(32, 32);
        }
        
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }
        
    }
    
}
