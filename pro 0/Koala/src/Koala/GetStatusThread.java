
package Koala;

import client.Client;
import java.util.ArrayList;
import java.util.List;
import utils.Holder;

/**
 *
 * @author KAM
 */
public class GetStatusThread extends Thread{
    MainFrame window;
    Client mClient;
    public GetStatusThread(MainFrame okno) {
        this.window = okno;
        mClient = okno.getProgram();
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

