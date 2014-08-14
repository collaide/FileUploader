/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.views;

import com.collaide.fileuploader.models.Group;
import com.collaide.fileuploader.models.GroupSync;
import com.collaide.fileuploader.models.GroupSyncList;
import com.collaide.fileuploader.models.user.CurrentUser;
import com.collaide.fileuploader.views.listeners.SingleGroupInfoListener;
import javax.swing.event.EventListenerList;

/**
 *
 * @author leo
 */
public class SingleGroupInfoPanel extends javax.swing.JPanel {

    private Group group;
    private final EventListenerList eventsList = new EventListenerList();

    /**
     * Creates new form SingleGroupInfoPanel
     */
    public SingleGroupInfoPanel() {
        initComponents();
    }

    public SingleGroupInfoPanel(Group group) {
        this();
        this.group = group;
        setSynchronizedMessage();
        if(group != null && CurrentUser.isGroupSynchronized(group.getId())) {
            GroupSync groupSync = CurrentUser.getUser()
                    .getGroupSyncList()
                    .getGroupSync(group.getId());
            groupSync.startObserving();
        }
    }

    public void addSingleGroupInfoListener(SingleGroupInfoListener listener) {
        eventsList.add(SingleGroupInfoListener.class, listener);
    }

    public SingleGroupInfoListener[] getSinigleGroupInfoListeners() {
        return eventsList.getListeners(SingleGroupInfoListener.class);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
        setSynchronizedMessage();
    }

    public void setSynchronizedMessage() {
        String synchronizedMsg = "";
        if (CurrentUser.isGroupSynchronized(getGroup().getId())) {
            synchronizedMsg = " is synchronized";
        }
        jlGroupInfo.setText("\"" + getGroup().getName() + "\"" + synchronizedMsg);
    }       

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jlGroupInfo = new javax.swing.JLabel();
        jbModify = new javax.swing.JButton();

        jlGroupInfo.setText("jLabel1");

        jbModify.setText("modify");
        jbModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModifyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlGroupInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbModify)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlGroupInfo)
                    .addComponent(jbModify)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModifyActionPerformed
        for (SingleGroupInfoListener listener : getSinigleGroupInfoListeners()) {
            listener.modifyClicked(this);
        }
    }//GEN-LAST:event_jbModifyActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbModify;
    private javax.swing.JLabel jlGroupInfo;
    // End of variables declaration//GEN-END:variables
}
