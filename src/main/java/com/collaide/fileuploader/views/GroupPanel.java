/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collaide.fileuploader.views;

import com.collaide.fileuploader.models.CurrentUser;
import com.collaide.fileuploader.models.Group;
import com.collaide.fileuploader.requests.GroupsRequest;
import java.awt.BorderLayout;
import javax.swing.JLabel;

/**
 *
 * @author leo
 */
public class GroupPanel extends javax.swing.JPanel {

    /**
     * Creates new form GroupPanel
     */
    public GroupPanel() {
        initComponents();
        jlWelcome.setText(jlWelcome.getText() + " " + CurrentUser.getUser().getName());
        for (Group group : GroupsRequest.index()) {
            JLabel label = new JLabel(group.getName());
            label.setText("ahsdf");
            jpRepository.add(label);
            invalidate();
            validate();
            repaint();
            System.out.println("label . " + label.getText() + " " + group.getName());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgRepositories = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jlInfo = new javax.swing.JLabel();
        jlWelcome = new javax.swing.JLabel();
        jpRepository = new javax.swing.JPanel();

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel1.setText("Select one of your repository to synchronize");

        jlInfo.setText("Pleas wait during your repositories are loading");

        jlWelcome.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jlWelcome.setText("Welcome");

        javax.swing.GroupLayout jpRepositoryLayout = new javax.swing.GroupLayout(jpRepository);
        jpRepository.setLayout(jpRepositoryLayout);
        jpRepositoryLayout.setHorizontalGroup(
            jpRepositoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 391, Short.MAX_VALUE)
        );
        jpRepositoryLayout.setVerticalGroup(
            jpRepositoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpRepository, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jlInfo))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(170, 170, 170)
                        .addComponent(jlWelcome)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jlWelcome)
                .addGap(18, 18, 18)
                .addComponent(jlInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpRepository, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgRepositories;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jlInfo;
    private javax.swing.JLabel jlWelcome;
    private javax.swing.JPanel jpRepository;
    // End of variables declaration//GEN-END:variables
}
