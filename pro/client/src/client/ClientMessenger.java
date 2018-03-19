/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import utils.CommandMessage;
import utils.Utils;

/**
 *
 * @author ADAM
 */
public class ClientMessenger {
        public Socket mSocket;
	private PrintWriter mPrintWriter;
        private BufferedReader mInput;
        private final String mEmptySendMessage = "";
        private Utils mUtils;
        public ClientMessenger(String pmIdServer,int pmPort) throws Exception
        {
            try
            {
                mSocket = new Socket(pmIdServer,pmPort);
                mPrintWriter = new PrintWriter(mSocket.getOutputStream(),true);
                mInput = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                mUtils = new Utils();
                
            }catch(Exception e)
            {
                    throw(e);
            }
        }
        public ClientMessenger(Socket pmSocket) throws Exception
        {
            try
            {
                mSocket = pmSocket;
                mPrintWriter = new PrintWriter(mSocket.getOutputStream(),true);
                mInput = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                mUtils = new Utils();
                
            }catch(Exception e)
            {
                    throw(e);
            }
            
        }
        
        /**
        *  Zczytuje wiadomosci ,
        */
        public List<String> read() throws Exception
        {
            List<String> lvMessageList = new ArrayList<String>();
            try
            {
                String lvMessage;
                while((lvMessage = mInput.readLine()) != null && !"".equalsIgnoreCase(lvMessage))
                {
                    lvMessageList.add(lvMessage);
                    System.out.println("znal: "+lvMessage);
                }

                if(lvMessageList.isEmpty())
                    {
                        if(CommandMessage.MESS.equalsIgnoreCase(lvMessageList.get(0)))
                        {
                            System.out.println("Przychodzaca wiadomosc");
                        }
                        else
                        if(CommandMessage.CALL.equalsIgnoreCase(lvMessageList.get(0)))
                        {
                            System.out.println("Przychodzaca rozmowa");
                        }
                        else
                        if(CommandMessage.END_CALL.equalsIgnoreCase(lvMessageList.get(0)))
                        {
                            System.out.println("Koniec rozmowy");
                        }
                        else
                        if(CommandMessage.END_MESSAGE.equalsIgnoreCase(lvMessageList.get(0)))
                        {
                            System.out.println("Koniec rozmowy txt");
                        }
                        else
                        if(CommandMessage.GET_ID.equalsIgnoreCase(lvMessageList.get(0)))
                        {
                            System.out.println("Pobranie ID");
                        }
                }
                return lvMessageList;
            }
            catch(Exception e)
            {
                throw new Exception("Blad przy czytaniu ze strumienia");
            }
        }
        
        /**
         * Metoda wysyła wiadomosci tekstowe 
         * @param pmMessage 
         */
        public void sendTxt(String pmMessage,String pmSenderId)
        {
            mPrintWriter.println(CommandMessage.MESS);
            mPrintWriter.println(pmSenderId);
            mPrintWriter.println(pmMessage);
            mPrintWriter.println(mEmptySendMessage);
        }
        
        public void endTxt()
        {
            mPrintWriter.println(CommandMessage.END_MESSAGE);
            mPrintWriter.println(mEmptySendMessage);
        }
            
}
