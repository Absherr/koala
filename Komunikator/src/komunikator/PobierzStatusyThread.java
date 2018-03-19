
package komunikator;

import client.Client;
import java.util.ArrayList;
import java.util.List;
import utils.Holder;

/**
 *
 * @author ADAM
 */
public class PobierzStatusyThread extends Thread{
    Glowne window;
    Client mClient;
    public PobierzStatusyThread(Glowne okno) {
        this.window = okno;
        mClient = okno.mKlient;
    }
    
    
    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        
        while(mClient.getCzyZalgowany()) {
            try {
                ArrayList<String> lvListId = new ArrayList<String>();
                for(Holder elem : window.list.contacts.vec)
                {
                    lvListId.add(elem.getId());
                }    
                mClient.getStatusListsDuring(lvListId);
                Thread.sleep(5000);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            
        }
    }
}

