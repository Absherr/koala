/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Koala;

import javax.swing.JOptionPane;
import utils.Holder;

/**
 *
 * @author Kam
 */
public class Add extends javax.swing.JFrame {

    Contacts_List pmList;
    boolean mCzyEdycja;
    Holder mKontakt;

    /**
     * Creates new form Add
     */
    public Add(Holder tempContact, Contacts_List list, boolean pmCzyEdycja) {
        this.mKontakt = tempContact; 
        this.pmList = list;
        this.mCzyEdycja = pmCzyEdycja;
        initComponents();
        if (mCzyEdycja) {
            this.addButton.setText("Zatwierdź");
        }
    }
    
    boolean Sprawdz_czy_liczba(String s)
    {
        for(int i=0;i<s.length();++i)
        { 
            if(s.charAt(i)<='0'|| s.charAt(i)>='9')
            { 
                return false;   
            } 
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    public void setName(String nazwa) {
        this.nameString.setText(nazwa);
    }

    public void setNumber(String numer) {
        this.numberString.setText(numer);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        nameString = new javax.swing.JTextField();
        numberString = new javax.swing.JTextField();
        nazwa = new javax.swing.JLabel();
        numer = new javax.swing.JLabel();
        closeButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        nameString.setText("Nazwa");
        nameString.setToolTipText("Nazwa kontaktu");
        nameString.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameStringActionPerformed(evt);
            }
        });

        numberString.setText("Numer");
        numberString.setToolTipText("Numer");
        numberString.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberStringActionPerformed(evt);
            }
        });

        nazwa.setText("Nazwa kontaktu");

        numer.setText("Numer");

        closeButton.setText("Anuluj");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        addButton.setText("Dodaj");
        addButton.setToolTipText("");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(addButton)
                        .addGap(18, 18, 18)
                        .addComponent(closeButton)
                        .addGap(0, 105, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nazwa)
                            .addComponent(numer))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(numberString)
                            .addComponent(nameString, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameString, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nazwa))
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numer)
                    .addComponent(numberString, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addButton)
                    .addComponent(closeButton))
                .addGap(36, 36, 36))
        );

        nameString.getAccessibleContext().setAccessibleName("nameString");
        nameString.getAccessibleContext().setAccessibleDescription("nameString");
        numberString.getAccessibleContext().setAccessibleName("numberString");
        numberString.getAccessibleContext().setAccessibleDescription("numberString");
        addButton.getAccessibleContext().setAccessibleName("addButton");
        addButton.getAccessibleContext().setAccessibleDescription("addButton");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void numberStringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numberStringActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numberStringActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.setVisible(false);
        this.pmList.refreshContactList(pmList.contacts.vec);
        this.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        if(nameString.getText().matches("[^a-zA-Z]") || 
                !Sprawdz_czy_liczba(numberString.getText()))
        {
            JOptionPane.showMessageDialog(null, "Wypełnij wszytskie pola poprawnie");
        }else
        {
            if(mCzyEdycja)
            {
                String tempNazwa = mKontakt.getNazwa();
                mKontakt.setNazwa(nameString.getText());
                mKontakt.setId(numberString.getText());
                pmList.contacts.Edit(mKontakt,tempNazwa);
            }else
            {
                pmList.contacts.Add(new Holder(numberString.getText(), nameString.getText(),utils.Status.UNAVALIBLE));
            }
            pmList.refreshContactList(pmList.contacts.vec);
            this.dispose();
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void nameStringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameStringActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameStringActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Add.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Add.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Add.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Add.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Add(null, null, true).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton closeButton;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JTextField nameString;
    private javax.swing.JLabel nazwa;
    private javax.swing.JTextField numberString;
    private javax.swing.JLabel numer;
    // End of variables declaration//GEN-END:variables
}
