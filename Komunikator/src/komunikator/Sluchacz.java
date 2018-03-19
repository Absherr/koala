
package komunikator;

import java.util.ArrayList;
import java.util.List;
import client.Client;
import client.ClientMessenger;
import javax.swing.JOptionPane;
import utils.Holder;
/**
 *
 * @author ADAM
 */

    public class Sluchacz extends Thread{
    Glowne window;
    Client mClient;
    public Sluchacz(Glowne okno) {
        this.window = okno;
        mClient = okno.mKlient;
    }
    
    
    @Override
    public void run() {
        while(mClient.getCzyZalgowany()) {
            try {
                ArrayList<String> commands = (ArrayList<String>) mClient.readFromServer();
                if (utils.CommandMessage.IMPORT_CONTACTS.equalsIgnoreCase(commands.get(0))) {         
                    if(commands.size() == 1)
                    {
                        window.showMessage("Pobrana lista jest pusta");
                    }else
                    {
                        refreshContactList(commands.subList(1, commands.size()));
                        window.showMessage("Pobrano liste kontaktów");
                    }
                }else
                if (utils.CommandMessage.FAIL_EXPORT_CONTACTS.equalsIgnoreCase(commands.get(0))) {         
                    String str = "";
                    for(int i = 1;i<commands.size();i+=2)
                    {
                        Integer temp = (i-1)/2;
                   
                        str =  str + temp.toString()+ ") " + commands.get(i+1) + " - ";
                        str = str + commands.get(i) + "\n";
                    }    
                    window.showMessage("Nie istnieją na serwerze następujące osoby(nazwa kontaktu - numer) : \n" + str);
                }else
                if (utils.CommandMessage.GET_STATUS_LIST.equalsIgnoreCase(commands.get(0))) {         
                    for(int i = 1;i<commands.size();i+=2)
                    {
                        String lvPseudo = window.list.contacts.Get_Pseudo(commands.get(i));
                        Holder lvHolder =  window.list.contacts.Get_Contact(lvPseudo);
                        lvHolder.setStatus(commands.get(i+1)!= null ? commands.get(i+1) : utils.Status.UNAVALIBLE);
                        window.list.contacts.Edit(lvHolder, lvPseudo);
                    } 
                    window.list.refreshContactList(window.list.contacts.vec);
                    
                }else
                if (utils.CommandMessage.MESS_RESPONSE.equalsIgnoreCase(commands.get(0))) {         
                    System.out.println(commands.get(1));
                    ClientMessenger lvMessenger = new ClientMessenger(commands.get(1),63402);
                    new Receiver(lvMessenger,window.mUser,window.mId).setVisible(true);
                }else
                if (utils.CommandMessage.GET_ID.equalsIgnoreCase(commands.get(0))) {         
                    window.setId(commands.get(1));
                    window.mId = commands.get(1);
                }else
                if (utils.CommandMessage.NOT_LOGGED.equalsIgnoreCase(commands.get(0))) {         
                    window.dispose();
                    JOptionPane.showMessageDialog(null, "Serwer offline");
                    new Logowanie().setVisible(true);
                }if (utils.CommandMessage.EXPORT_CONTACTS.equalsIgnoreCase(commands.get(0))) {         
                    JOptionPane.showMessageDialog(null, "Export listy kontaktów zakonczony sukcesem ");
                }
                
            } catch (Exception e) {
                System.out.println(e.getMessage());
                
                    //new Logowanie().setVisible(true);
                    //window.dispose();
                    //JOptionPane.showMessageDialog(null, "Serwer offline");
                   // this.interrupt();
            }
        }
    }

    private void refreshContactList(List<String> subList) {
       ListaKontaktow lvListaKontaktow = new ListaKontaktow();
       lvListaKontaktow.DeleteAll();
        for(int i = 0; i < subList.size(); i+=2) {
           utils.Holder kontakt = new utils.Holder(subList.get(i), subList.get(i + 1), utils.Status.UNAVALIBLE);
           lvListaKontaktow.Add(kontakt);
        }
     
        window.list.contacts = lvListaKontaktow;
        window.list.refreshContactList(lvListaKontaktow.vec);
      
    }


}
