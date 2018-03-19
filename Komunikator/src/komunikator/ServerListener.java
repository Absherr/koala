
package komunikator;

import client.Client;
import client.ClientMessenger;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import utils.Holder;

/**
 *
 * @author ADAM
 */
public class ServerListener  extends Thread{
    
    Glowne widnow;
    public ServerListener(Glowne okno) {
        this.widnow = okno;
    }
    
    
    @Override
    public void run() {
        try {
        ClientMessenger lvMessanger;
                ServerSocket mServerSocket;
                Socket mClientSocket;
            // Wait for client to connect on 63400
            
                mServerSocket = new ServerSocket(63402);
        while(true) {
            
            
                
                
                System.out.println("Czekam na polaczenie...");
                mClientSocket = mServerSocket.accept();
                System.out.println("Polaczylem sie z rozmówcą");
                lvMessanger = new ClientMessenger(mClientSocket);
            
                new Receiver(lvMessanger,widnow.mUser,widnow.mId).setVisible(true);
            
            
        }
        } catch (Exception e) {
                System.out.println(e.getMessage());
            }
    }
}


