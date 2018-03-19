package komunikator;

/**
 * @author ADAM
 */
import client.Client;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import utils.Holder;
import utils.Status;

public class Glowne extends javax.swing.JFrame implements ActionListener {
    
    Client mKlient;
    public ListaKontaktowPanel list;
    JMenuBar menuBar, submenu;
    JMenu menu;
    JMenuItem menuItem;
    public String mUser;
    public String mId;
    
    public Glowne(Client pmKlient, String pmUser) {
        this.mKlient = pmKlient;
        this.mUser = pmUser; 
        new Sluchacz(this).start();
        new PobierzStatusyThread(this).start();
        new ServerListener(this).start();
        initComponents();

        list = new ListaKontaktowPanel(this.mKlient,panel, this);

        panel.setSize(500, 550);

        addMenuBar();
        
        
        try {
            this.mKlient.getId();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        gorneMenu = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        panel.setBackground(new java.awt.Color(204, 204, 0));

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout gorneMenuLayout = new javax.swing.GroupLayout(gorneMenu);
        gorneMenu.setLayout(gorneMenuLayout);
        gorneMenuLayout.setHorizontalGroup(
            gorneMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        gorneMenuLayout.setVerticalGroup(
            gorneMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Niewidoczny", "Dostępny" }));
        jComboBox1.setAutoscrolls(true);
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Numer:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gorneMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(gorneMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public void setId(String pmId)
    {
        jLabel1.setText("Numer : " + pmId);
    }
    public String getId()
    {
        return jLabel1.getText();
    }
    private void addMenuBar()
    {
        menuBar = new JMenuBar();
 
        //Build the first menu.
        menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        
        
        menuItem = new JMenuItem("Zakoncz");
        //menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead

        menuItem.addMouseListener(new MouseListener() {
	    
	    @Override
	   public void mouseClicked(MouseEvent e) {           
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.exit(EXIT_ON_CLOSE);
            }

            @Override
            public void mouseReleased(MouseEvent e) {               
            }
            @Override
            public void mouseEntered(MouseEvent e) {  
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        
        menu.add(menuItem);
        menuBar.add(menu);
        /////////////////////////////////////////////////////////////////////
         ////////////////////////////////Drugi popUP////////////////////////
        ////////////////////////////////////////////////////////////////////
        menu = new JMenu("Kontakty");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        
        //a group of JMenuItems
        menuItem = new JMenuItem("Dodaj kontakt",
                                 KeyEvent.VK_T);
        //menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "This doesn't really do anything");
        
        menuItem.addMouseListener(new MouseListener() {
	    
	    @Override
	   public void mouseClicked(MouseEvent e) {          
            }
            @Override
            public void mousePressed(MouseEvent e) {
                new EdytujKontakt(null, false, list).setVisible(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {   
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
             
            }
        });
        menu.add(menuItem);
        
        //a group of JMenuItems
        menuItem = new JMenuItem("Import");

        
        menuItem.addMouseListener(new MouseListener() {
	    
	    @Override
	   public void mouseClicked(MouseEvent e) {           
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
                try {
                    mKlient.importContactsFromServer();
                } catch (Exception exp) {
                    System.out.println(exp.getMessage());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {              
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
             
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Export");
        //menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead

        
        menuItem.addMouseListener(new MouseListener() {
	    
	    @Override
	   public void mouseClicked(MouseEvent e) {        
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
                try {
                    mKlient.exportContactsToServer(new ArrayList<Holder>(list.contacts.vec));
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
               
            }

            @Override
            public void mouseExited(MouseEvent e) {
             
            }
        });
        
        menu.add(menuItem);
        menuBar.add(menu);
        
        gorneMenu.setLayout(new BorderLayout());
	gorneMenu.add(menuBar, BorderLayout.CENTER);
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
       
        try {
             mKlient.logoutFromServer();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_formWindowClosing

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        try {
            if(jComboBox1.getSelectedIndex() == 0)
            {
                mKlient.changeStatus(Status.INVISIBLE);
            }
            else
            if(jComboBox1.getSelectedIndex() == 1)
            {
                mKlient.changeStatus(Status.AVALIBLE);
            }
        } catch (Exception e) {
        }
        
    }//GEN-LAST:event_jComboBox1ActionPerformed

    public void showMessage(String pmMessage)
    {
        JOptionPane.showMessageDialog(null, pmMessage);
    }    
        
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Glowne.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Glowne.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Glowne.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Glowne.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Glowne(null,null).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel gorneMenu;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
