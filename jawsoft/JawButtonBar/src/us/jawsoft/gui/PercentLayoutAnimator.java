/*
 * PercentLayoutAnimator.java
 *
 * Created on October 27, 2005, 3:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package us.jawsoft.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Timer;

/**
 * Animates a PercentLayout
 */
public class PercentLayoutAnimator implements ActionListener {
    
    private Timer animatorTimer;
    private List tasks = new ArrayList();
    private PercentLayout layout;
    private Container container;
    
    public PercentLayoutAnimator(Container container, PercentLayout layout) {
        this.container = container;
        this.layout = layout;
    }
    
    public void setTargetPercent(Component component, float percent) {
        PercentLayout.Constraint oldConstraint = layout.getConstraint(component);
        if (oldConstraint instanceof PercentLayout.PercentConstraint) {
            setTargetPercent(component,
                    ((PercentLayout.PercentConstraint)oldConstraint).floatValue(), percent);
        }
    }
    
    public void setTargetPercent(Component component, float startPercent, float endPercent) {
        tasks.add(new PercentTask(component, startPercent, endPercent));
    }
    
    public void start() {
        animatorTimer = new Timer(15, this);
        animatorTimer.start();
    }
    
    public void stop() {
        animatorTimer.stop();
    }
    
    public void actionPerformed(ActionEvent e) {
        boolean allCompleted = true;
        
        for (Iterator iter = tasks.iterator(); iter.hasNext();) {
            PercentTask element = (PercentTask)iter.next();
            if (!element.isCompleted()) {
                allCompleted = false;
                element.execute();
            }
        }
        
        container.invalidate();
        container.doLayout();
        container.repaint();
        
        if (allCompleted) {
            stop();
        }
    }
    
    class PercentTask {
        
        Component component;
        
        float targetPercent;
        float currentPercent;
        
        boolean completed;
        boolean incrementing;
        float delta;
        
        public PercentTask(Component component, float currentPercent,
                float targetPercent) {
            this.component = component;
            this.currentPercent = currentPercent;
            this.targetPercent = targetPercent;
            
            float diff = targetPercent - currentPercent;
            incrementing = diff > 0;
            delta = diff / 10;
        }
        
        public void execute() {
            currentPercent += delta;
            if (incrementing) {
                if (currentPercent > targetPercent) {
                    currentPercent = targetPercent;
                    completed = true;
                }
            } else {
                if (currentPercent < targetPercent) {
                    currentPercent = targetPercent;
                    completed = true;
                }
            }
            
            layout.setConstraint(component, new PercentLayout.PercentConstraint(
                    currentPercent));
        }
        
        public boolean isCompleted() {
            return completed;
        }
    }
    
}
