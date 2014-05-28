/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.collaide.fileuploader.views;

import com.collaide.fileuploader.App;
import com.collaide.fileuploader.controllers.UserController;
import com.collaide.fileuploader.views.listeners.SignInListener;
import java.util.EventListener;
import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;

/**
 *
 * @author leo
 */
public class SignInPanel extends javax.swing.JPanel {

    private final EventListenerList listeners = new EventListenerList();
    /**
     * Creates new form SignInPanel
     */
    public SignInPanel() {
        initComponents();
    }

    public void addSignInListener(SignInListener listener) {
        listeners.add(SignInListener.class, listener);
    }
    public SignInListener[] getSignInListener() {
        return listeners.getListeners(SignInListener.class);
    }
    
    public void fireLoginSuccessful() {
        for(SignInListener listener : getSignInListener()) {
            listener.loginSuccess();
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

        jLabel1 = new javax.swing.JLabel();
        jtfEmail = new javax.swing.JTextField();
        jpfUserPassword = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jbSignIn = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Sign in to collaide.com");

        jtfEmail.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jtfEmail.setText("admin@example.com");

        jpfUserPassword.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jpfUserPassword.setText("password");

        jLabel2.setText("Email");

        jLabel3.setText("Password");

        jbSignIn.setText("Sign in");
        jbSignIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSignInActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbSignIn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jpfUserPassword, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jtfEmail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpfUserPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jbSignIn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbSignInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSignInActionPerformed
        UserController userC = new UserController();
        if(userC.signIn(jtfEmail.getText(), String.valueOf(jpfUserPassword.getPassword()))) {
            fireLoginSuccessful();
        } else {
            JOptionPane.showMessageDialog(this, "Wrong combination email/password", "Sign in error",  JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jbSignInActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton jbSignIn;
    private javax.swing.JPasswordField jpfUserPassword;
    private javax.swing.JTextField jtfEmail;
    // End of variables declaration//GEN-END:variables
}