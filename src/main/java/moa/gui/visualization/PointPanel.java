/*
 *    PointPanel.java
 *    Copyright (C) 2010 RWTH Aachen University, Germany
 *    @author Jansen (moa@cs.rwth-aachen.de)
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package moa.gui.visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class PointPanel extends JPanel{
    static final int POINTSIZE = 4;
    DataPoint point;

    protected int x_dim = 0;
    protected int y_dim = 1;
    protected Color col;
    protected Color default_color = Color.BLACK;

    protected int panel_size;
    protected int window_size;
    protected boolean highligted = false;

    protected double decayRate;
    protected double decayThreshold;


    /** Creates new form ObjectPanel */
    public PointPanel(DataPoint point, double decayRate, double decayThreshold) {
        this.point = point;
        this.col = Color.BLACK;
        this.panel_size = POINTSIZE;
        this.decayRate = decayRate;
        this.decayThreshold = decayThreshold;
        this.col = default_color;

        setVisible(true);
        setOpaque(false);
        setSize(new Dimension(1,1));
        setLocation(0,0);
        initComponents();

    }



    public void updateLocation(){
        window_size = Math.min(this.getParent().getWidth(),this.getParent().getHeight());

        StreamPanel sp = (StreamPanel)getParent().getParent();
        x_dim = sp.getActiveXDim();
        y_dim = sp.getActiveYDim();

        setSize(new Dimension(panel_size+1,panel_size+1));
        setLocation((int)(point.value(x_dim)*window_size-(panel_size/2)),(int)(point.value(y_dim)*window_size-(panel_size/2)));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 296, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 266, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics g) {
        point.updateWeight(RunVisualizer.getCurrentTimestamp(), decayRate);
        if(point.weight() < decayThreshold){
            getParent().remove(this);
            return;
        }

        Color color = getColor();
        Color errcolor = getErrorColor();
        if(errcolor == null){
            errcolor = color;
            panel_size = POINTSIZE;
        }
        else{
            panel_size = POINTSIZE+2;
        }
        
        updateLocation();
        g.setColor(errcolor);
        g.drawOval(0, 0, panel_size, panel_size);
        g.setColor(color);
        g.fillOval(0, 0, panel_size, panel_size);

        setToolTipText(point.getInfo(x_dim, y_dim));

    }

            

    private Color getErrorColor(){
        String cmdvalue = point.getMeasureValue("CMM");
        Color color = null;
        if(!cmdvalue.equals("")){
            double err = Double.parseDouble(cmdvalue);
            if(err > 0.00001){
                if(err > 0.7) err = 1;
                int alpha = (int)(100+155*err);
                color = new Color(255, 0, 0, alpha);
            }
            if(err == 0.00001){
                color = new Color(255, 0, 0,100);
            }
        }
        return color;
    }

    private Color getColor(){
        Color color = null;

        if(getParent() instanceof StreamPanel){
            StreamPanel sp = (StreamPanel) getParent();
            ClusterPanel cp = sp.getHighlightedClusterPanel();
            if(cp !=null){
                if(cp.getClusterLabel()==point.classValue()){
                    color = Color.BLUE;
                }
            }
        }

        if(color == null){
            int alpha = (int)(point.weight()*200+55);
            float numCl = 10;
            Color c = getPointColorbyClass((int)point.classValue(), numCl);

            color = new Color(c.getRed(),c.getGreen(),c.getBlue(),alpha);
        }
        return color;
    }

    public static Color getPointColorbyClass(int classValue, float numClasses){
        Color c;
        if(classValue != -1)
            c = new Color(Color.HSBtoRGB((float)((classValue+1)/numClasses), 1f, 240f/240));
        else
            c = Color.GRAY;
        return c;
    }


    public void highlight(boolean enabled){
        highligted = enabled;
        repaint();
    }


    public String getObjectInfo(){
        return point.getInfo(x_dim, y_dim);
    }

    @Override
    public String getToolTipText() {
        return super.getToolTipText();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables


    public String getSVGString(int width){
        StringBuffer out = new StringBuffer();

        int x = (int)(point.value(x_dim)*window_size);
        int y = (int)(point.value(y_dim)*window_size);
        int radius = panel_size/2;
        //radius = 1;

        Color c = getColor();

        String color = "rgb("+c.getRed()+","+c.getGreen()+","+c.getBlue()+")";
        double trans = c.getAlpha()/255.0;

        out.append("<circle ");
        out.append("cx='"+x+"' cy='"+y+"' r='"+radius+"'");
        out.append(" stroke='"+color+"' stroke-width='0' fill='"+color+"' fill-opacity='"+trans+"' stroke-opacity='"+trans+"'/>");
        out.append("\n");
        return out.toString();
    }

}
