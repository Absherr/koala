/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import utils.*;

/**
 *
 * @author ADAM
 */
class ThreadServer extends Thread
{
    public Socket mClient;
    private MySql mConn;
    private boolean mCzyZalogowany;
    private String mLogin;
    private String mId;
    private Utils mUtils;
    private Okienko mOkienko;
    public boolean lvCzyCzytaj;
    PrintWriter lvOutput;
    public ThreadServer(Socket pmClient, Okienko pmOkienko)
    {
            this.mClient = pmClient;
            mConn = new MySql();
            mCzyZalogowany = false;
            mLogin = "";
            mOkienko = pmOkienko;
            lvCzyCzytaj = true;
            try {
            lvOutput = new PrintWriter(mClient.getOutputStream(), true);
        } catch (Exception e) {
        }
            
    }

    @Override
    public void run()   
    {
        try
        {
            
            BufferedReader lvInput = new BufferedReader(new InputStreamReader(mClient.getInputStream()));
            String lvIdUser = mClient.getInetAddress().toString();
            mOkienko.addTxt("ip: " +  lvIdUser);
            final String lvSendEmptyMessage = "";
            mUtils = new Utils();

            while(lvCzyCzytaj)
            {
                try
                {
                    String lvMessage;
                    List<String> lvMessageList = new ArrayList<String>();

                    while((lvMessage = lvInput.readLine()) != null && !"".equalsIgnoreCase(lvMessage))
                    {
                        lvMessageList.add(lvMessage);
                        //mOkienko.addTxt("znal: "+lvMessage);
                    }

                    int lvSizeMessage = lvMessageList.size();
                    mOkienko.addTxt("znalazlem: "+lvMessageList.get(0));
                    if(lvSizeMessage > 0)
                    {
                        if(mCzyZalogowany)
                        {
                            if(CommandMessage.MESS_REQUEST.equalsIgnoreCase(lvMessageList.get(0)))
                            {
                                mOkienko.addTxt("mess request");
                                if(lvMessageList.size()>=1)
                                {
                                    String lvId = lvMessageList.get(1);
                                    //executeQuery sprawdzenie czy odbiorca jest online
                                    String lvStatus = mConn.ifAvalible(lvId);
                                    if(!Utils.BRAK_REKORDU.equalsIgnoreCase(lvStatus))
                                    {
                                        if(Status.AVALIBLE.equalsIgnoreCase(lvStatus) ||
                                                Status.INVISIBLE.equalsIgnoreCase(lvStatus))
                                        {
                                            lvOutput.println(CommandMessage.MESS_RESPONSE);
                                            //pobiera adres z bazy
                                            lvOutput.println(mConn.getIp(lvId).substring(1));
                                            lvOutput.println(lvId);
                                            lvOutput.println(lvSendEmptyMessage);
                                        }else
                                        {
                                            
                                        }
                                    }else
                                    {
                                        lvOutput.println(CommandMessage.NO_EXIST_CONTACT);
                                        lvOutput.println(lvId);
                                        lvOutput.println(lvSendEmptyMessage);
                                    }
                                }
                            }else
                            //prosba od klienta o pobranie listy znajomych
                            if(CommandMessage.IMPORT_CONTACTS.equalsIgnoreCase(lvMessageList.get(0)))
                            {
                                mOkienko.addTxt("import");
                                    //executeQuery do pobrania calej listy znajomych
                                ArrayList<String> lvWynik = mConn.getFriendList(mId);
                                lvOutput.println(CommandMessage.IMPORT_CONTACTS);
                                while(!lvWynik.isEmpty())
                                {
                                    lvOutput.println(lvWynik.get(0));
                                    lvWynik.remove(0);
                                }
                                lvOutput.println(lvSendEmptyMessage);
                            }else
                            if(CommandMessage.EXPORT_CONTACTS.equalsIgnoreCase(lvMessageList.get(0)))
                            {
                                mOkienko.addTxt("export");
                                    //wyslanie statusow na serwer
                                List<String> lvFailAddFriend = mConn.sendFriendList(mId,lvMessageList.subList(1, lvMessageList.size()));
                                if(lvFailAddFriend.isEmpty())
                                {
                                    lvOutput.println(CommandMessage.EXPORT_CONTACTS);
                                    lvOutput.println(lvSendEmptyMessage);
                                }else
                                {
                                    lvOutput.println(CommandMessage.FAIL_EXPORT_CONTACTS);
                                    while(!lvFailAddFriend.isEmpty())
                                    {
                                        lvOutput.println(lvFailAddFriend.get(0));
                                        lvFailAddFriend.remove(0);
                                    }
                                    lvOutput.println(lvSendEmptyMessage);
                                }

                            }else
                            if(CommandMessage.GET_STATUS_LIST.equalsIgnoreCase(lvMessageList.get(0)))
                            {
                                mOkienko.addTxt("get status");
                                //pobranie statusow znajomych
                                ArrayList<Utils.StatusList> lvWynik = mConn.getStatusList(lvMessageList
                                        .subList(1, lvMessageList.size()),mUtils);
                                lvOutput.println(CommandMessage.GET_STATUS_LIST);
                                while(!lvWynik.isEmpty())
                                {
                                    lvOutput.println(lvWynik.get(0).getId());
                                    lvOutput.println(lvWynik.get(0).getStatus());
                                    lvWynik.remove(0);
                                }
                                lvOutput.println(lvSendEmptyMessage);
                            }else
                            if(CommandMessage.GET_ID.equalsIgnoreCase(lvMessageList.get(0)))
                            {
                                lvOutput.println(CommandMessage.GET_ID);
                                lvOutput.println(mId);
                                lvOutput.println(lvSendEmptyMessage);
                            }else
                            if(CommandMessage.LOGOUT.equalsIgnoreCase(lvMessageList.get(0)))
                            {
                                //update rekordow że uzytkownik jest wylogowany
                                mConn.updateStatus(Status.UNAVALIBLE, mId);
                                mOkienko.addTxt("logout");
                                lvOutput.println(CommandMessage.LOGOUT);
                                lvOutput.println(lvSendEmptyMessage);
                                mClient.close();
                                mCzyZalogowany = false;
                                lvCzyCzytaj = false;
                            }
                            if(CommandMessage.STATUS_CHANGE.equalsIgnoreCase(lvMessageList.get(0)))
                            {
                                //update rekordu statusu
                                
                                if(lvMessageList.size() > 1 && (utils.Status.AVALIBLE.equalsIgnoreCase(
                                    lvMessageList.get(1)) || utils.Status.INVISIBLE.equalsIgnoreCase(lvMessageList.get(1))) 
                                    && mConn.updateStatus(lvMessageList.get(1), mId))
                                {
                                    mOkienko.addTxt("status change");
                                    lvOutput.println(CommandMessage.STATUS_CHANGE);
                                    lvOutput.println(lvSendEmptyMessage);
                                }else
                                {
                                    mOkienko.addTxt("fail status change");
                                    lvOutput.println(CommandMessage.NOT_STATUS_CHANGE);
                                    lvOutput.println(lvSendEmptyMessage);
                                }
                            }
                        }else
                        if(CommandMessage.LOGIN.equalsIgnoreCase(lvMessageList.get(0)))
                        {
                            mOkienko.addTxt("login");
                            if(lvMessageList.size() >= 3)
                            {
                                ArrayList<String> lvArralyList = mConn.checkAuth(lvMessageList.get(1),lvMessageList.get(2));
                                if(lvArralyList != null && lvArralyList.size() >= 2)
                                {
                                    //sprawdzanie czy nie ma oczekujacych wiadomosci na bazie
                                    //
                                    //mConn.executeUpdate("");
                                    lvOutput.println(CommandMessage.LOGIN);
                                    lvOutput.println(lvSendEmptyMessage);
                                    mCzyZalogowany = true;
                                    mLogin = lvArralyList.get(1);
                                    mId = lvArralyList.get(0);
                                    mConn.updateIp(mClient.getInetAddress().toString(),mId);
                                    mConn.updateStatus(utils.Status.INVISIBLE, mId);

                                }else
                                {
                                    notLogged(lvOutput,lvSendEmptyMessage);
                                }
                            }else
                            {
                                notLogged(lvOutput,lvSendEmptyMessage);
                            }
                        }else
                        if(CommandMessage.GET_SALT.equalsIgnoreCase(lvMessageList.get(0)))
                        {
                            if(!mUtils.isEmpty(lvMessageList.get(1)))
                            {
                                String lvSalt = mConn.getSalt(lvMessageList.get(1));
                                if(!mUtils.BRAK_REKORDU.equalsIgnoreCase(lvSalt))
                                {
                                    mOkienko.addTxt("get salt - success");
                                    lvOutput.println(CommandMessage.GET_SALT);
                                    lvOutput.println(lvSalt);
                                    lvOutput.println(lvSendEmptyMessage);
                                }else
                                {
                                    notLogged(lvOutput,lvSendEmptyMessage);
                                }
                                
                            }
                        }else
                        if(CommandMessage.CREATE_USER.equalsIgnoreCase(lvMessageList.get(0)))
                        {
                            if(lvMessageList.size() == 3 && 
                                mConn.createUser(lvMessageList.get(1), lvMessageList.get(2)))
                            {    
                                lvOutput.println(CommandMessage.CREATE_USER);
                                lvOutput.println(lvSendEmptyMessage);
                            }else
                            {
                                lvOutput.println(CommandMessage.NOT_CREATE_USER); 
                                lvOutput.println(lvSendEmptyMessage);
                            }
                        }
                        else
                        {
                            notLogged(lvOutput,lvSendEmptyMessage);
                        }
                    }
                }
                catch(Exception e)
                {
                    //update rekordow że uzytkownik jest wylogowany
                    if(mId != null)
                        mConn.updateStatus(utils.Status.UNAVALIBLE, mId);
                    throw new Exception("Blad przy czytaniu ze strumienia uzytkownika " + lvIdUser);
                }
            }
            mOkienko.addTxt("Watek z uzytkownikiem " + lvIdUser + " konczy prace");
            if(mId != null)
                mConn.updateStatus(utils.Status.UNAVALIBLE, mId);
            notLogged(lvOutput,lvSendEmptyMessage);
            Thread.sleep(1500);
            mClient.close();
            

        }
        catch (Exception e) 
        {
            try 
            {
                //update rekordow że uzytkownik jest wylogowany
                if(mId != null)
                    mConn.updateStatus(utils.Status.UNAVALIBLE, mId);
                throw new Exception("Jeden watek serwera rzucił wyjątkiem, prawdopodobnie uzytkownik sie nie wylogowal");
            } 
            catch (Exception ex) 
            {
                Logger.getLogger(ThreadServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void notLogged(PrintWriter pmOutput,String pmSendEmptyMessage)
    {
        pmOutput.println(CommandMessage.NOT_LOGGED);
        pmOutput.println(pmSendEmptyMessage);
        mCzyZalogowany = false;
        if(mId != null)
            mConn.updateStatus(utils.Status.UNAVALIBLE, mId);
    }
     public void notLogged(String pmSendEmptyMessage)
    {
        lvOutput.println(CommandMessage.NOT_LOGGED);
        lvOutput.println(pmSendEmptyMessage);
        mCzyZalogowany = false;
        if(mId != null)
            mConn.updateStatus(utils.Status.UNAVALIBLE, mId);
    }
}
