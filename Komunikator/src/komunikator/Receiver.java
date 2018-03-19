
package komunikator;
import utils.CommandMessage;
import client.ClientMessenger;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author ADAM
 */
public class Receiver extends javax.swing.JFrame {

    ClientMessenger mClient;
    public String mUser;
    public String mId;
    public boolean czyRozmowaGlosowa;
    
    
    public Receiver(ClientMessenger pmClient, String pmUser, String pmId) {
        this.mClient = pmClient;
        this.mId = pmId;
        this.mUser = pmUser;
        new ThreadServer(this.mClient,this).start();
        this.czyRozmowaGlosowa = false;
        initComponents();
        this.voiceButton.setEnabled(true);
        this.stopButton.setEnabled(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        voiceButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setText("Okno rozmowy :");

        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jButton2.setText("Wyślij");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        voiceButton.setText("Rozmowa");
        voiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voiceButtonActionPerformed(evt);
            }
        });

        stopButton.setText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(stopButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(voiceButton)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(52, 52, 52))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(voiceButton)
                    .addComponent(stopButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        addTxt(mUser,jTextArea2.getText());
        mClient.sendTxt(mId, jTextArea2.getText());
        jTextArea2.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        
        try {
           mClient.endTxt();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_formWindowClosing

    private void voiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voiceButtonActionPerformed
        this.czyRozmowaGlosowa = true;
        this.stopButton.setEnabled(true);
        this.voiceButton.setEnabled(false);
        AudioTransmiter v2 = new AudioTransmiter(this.mClient.mSocket.getInetAddress().toString(), "22222", this);
        AudioReciver v1 = new AudioReciver(this.mClient.mSocket.getInetAddress().toString(), "22222", this);
        Thread t1 = new Thread(v1);
        Thread t2 = new Thread(v2);
        t1.start();
        t2.start();
    }//GEN-LAST:event_voiceButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        this.czyRozmowaGlosowa = false;
        this.stopButton.setEnabled(false);
        this.voiceButton.setEnabled(true);
        //implementacja konczenia watkow
    }//GEN-LAST:event_stopButtonActionPerformed

    public void addTxt(String pmFrom,String pmMessage)
    {
        ListaKontaktow lvListaKontaktow = new ListaKontaktow();
        String lvTemp = lvListaKontaktow.Get_Pseudo(pmFrom);
        if(!lvTemp.equalsIgnoreCase("-1")) pmFrom = lvTemp;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date lvDate = new Date();
        jTextArea1.setText(jTextArea1.getText() + "\n" + dateFormat.format(lvDate)+ "("+ pmFrom +")\n" + pmMessage);
        jTextArea1.setCaretPosition(jTextArea1.getText().length());
    }
    public static void main(String args[]) {
        
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
            java.util.logging.Logger.getLogger(Receiver.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Receiver.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Receiver.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Receiver.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Receiver(null,null,null).setVisible(true);
            }
        });
    }
    private void addTabListener()
    {
        KeyListener event;
        event = new KeyListener()
        {
            
            @Override
            public void keyPressed( KeyEvent e )
            {
  
            }

            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
  
            }

            @Override
            public void keyReleased(KeyEvent e) {   
                
                if( e.getKeyCode() == KeyEvent.VK_ENTER )
                {
                    jButton2ActionPerformed(null);
                }               
            }
 
        };
        jTextArea2.addKeyListener(event);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JButton stopButton;
    private javax.swing.JButton voiceButton;
    // End of variables declaration//GEN-END:variables
}

class ThreadServer extends Thread
{
        ClientMessenger mClient;
        Receiver mReceiver;
        
        public ThreadServer(ClientMessenger klient,Receiver pmReceiver)
        {
            this.mClient = klient;
            this.mReceiver = pmReceiver;
        }
    
    @Override
        public void run()  
    {
        try
        {
           while(true)
           {
               ArrayList<String> lvList =(ArrayList) mClient.read();
               if(lvList.get(0).equalsIgnoreCase(CommandMessage.MESS))
               {
                   //mReceiver.mFriend = lvList.get(1);
                   mReceiver.addTxt(lvList.get(2),lvList.get(1));
               }else
                if(lvList.get(0).equalsIgnoreCase(CommandMessage.END_MESSAGE))
               {
                   //mReceiver.mFriend = lvList.get(1);
                   this.mReceiver.dispose();
               } 
           }    
        }catch(Exception e)
        {
            this.mReceiver.dispose();
            System.out.println(e.getMessage());
        }
    }
}
