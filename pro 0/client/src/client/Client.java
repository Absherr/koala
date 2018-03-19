package client;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ADAM
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import utils.CommandMessage;
import utils.Utils;
/**
 * Klasa odpowiedzialna za obsługę połaczenia serwer - klient
 * @author ADAM
 */
public class Client
{
	private Socket mSocket;
	private PrintWriter mPrintWriter;
        private BufferedReader mInput;
        private boolean mCzyZalogowany;
        private final String mEmptySendMessage = "";
        private Utils mUtils;
        public Client(String pmIdServer,int pmPort) throws Exception
        {
            try
            {
                mSocket = new Socket(pmIdServer,pmPort);
                mPrintWriter = new PrintWriter(mSocket.getOutputStream(),true);
                mInput = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                mCzyZalogowany = false;
                mUtils = new Utils();
                
            }catch(Exception e)
            {
                    System.out.println(e);
            }
        }
        public ClientMessenger listener() throws Exception
        {
            ServerSocket mServerSocket;
            Socket mClientSocket;
            // Wait for client to connect on 63400
            try
            {
                    mServerSocket = new ServerSocket(63000);
                    System.out.println("Czekam na polaczenie...");
                    mClientSocket = mServerSocket.accept();
                    System.out.println("Polaczylem sie z rozmówcą");
                    return new ClientMessenger(mClientSocket);
            }
            catch(IOException e)
            {
                    throw(e);
            }
        }
        /**
        *  Zczytuje pojedyncze odpowiedzi serwera,
        *  na żądania klienta 
        */
        private List<String> read() throws Exception
        {
            List<String> lvMessageList = new ArrayList<String>();
            try
            {
                String lvMessage;
                while((lvMessage = mInput.readLine()) != null && !"".equalsIgnoreCase(lvMessage))
                {
                    lvMessageList.add(lvMessage);
                    System.out.println("znazalem: " + lvMessage);
                }
            }catch(Exception e)
            {
                    throw new Exception("Blad czytania ze strumienia");
            }
            return lvMessageList;
        }
        /**
        *  Zczytuje wiadomosci z serwera,
        */
        public List<String> readFromServer() throws Exception
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

                if(mCzyZalogowany && !lvMessageList.isEmpty())
                    {
                        if(CommandMessage.NOT_STATUS_CHANGE.equalsIgnoreCase(lvMessageList.get(0)))
                        {
                            System.out.println("Nieudana zmiana statusu");
                        }
                        else
                        if(CommandMessage.STATUS_CHANGE.equalsIgnoreCase(lvMessageList.get(0)))
                        {
                            System.out.println("Zmiana statusu");
                        }
                        else
                        if(CommandMessage.MESS_REQUEST.equalsIgnoreCase(lvMessageList.get(0)))
                        {
                            System.out.println("Rozmówca zapytał o rozmowe");
                            //do usuniecia
                            ClientMessenger cm = new ClientMessenger(lvMessageList.get(1),63400);
                            cm.sendTxt("elo", "10");
                            cm.read();
                        }
                        else
                        if(CommandMessage.MESS_RESPONSE.equalsIgnoreCase(lvMessageList.get(0)))
                        {
                            System.out.println("Dostałem ip rozmówcy");
                        }
                        else
                        if(CommandMessage.IMPORT_CONTACTS.equalsIgnoreCase(lvMessageList.get(0)))
                        {
                            System.out.println("Import danych zakonczony sukcesem");
                        }
                        else
                        if(CommandMessage.EXPORT_CONTACTS.equalsIgnoreCase(lvMessageList.get(0)))
                        {
                            System.out.println("Export danych zakonczony sukcesem");
                        }
                        else
                        if(CommandMessage.FAIL_EXPORT_CONTACTS.equalsIgnoreCase(lvMessageList.get(0)))
                        {
                            System.out.println("Nieudane wyslanie danych o kontaktach na serwer");
                        }
                        else   
                        if(CommandMessage.LOGOUT.equalsIgnoreCase(lvMessageList.get(0)))
                        {
                            System.out.println("Wylogowano");
                            mCzyZalogowany = false;
                        }
                }
                return lvMessageList;
            }
            catch(Exception e)
            {
                mCzyZalogowany = false;
                throw new Exception("Blad przy czytaniu ze strumienia");
            }
        }
        /**
        *  Logowanie klienta na serwer, pobiera dane statusów na start
        * @param pmIdUser identyfikator użytkownika
        * @param pmPassword hasło użytkownika
        * @param pmContactsList lista indentyfikatorów znajomych
        *  
        */
        public ArrayList<Utils.StatusList> loginToServer(String pmIdUser, String pmPassword, 
                List<String> pmContactsList) throws Exception
        {
            if(mUtils.isEmpty(pmIdUser) && mUtils.isEmpty(pmPassword))
            {
                throw new Exception("Puste haslo lub login!!!");
            }else
            if(!mCzyZalogowany)
            {
                //pobranie soli
                mPrintWriter.println(CommandMessage.GET_SALT);
                mPrintWriter.println(pmIdUser);
                mPrintWriter.println(mEmptySendMessage);
                //czekanie na odpowiedz serwera
                List<String> lvSalt = read();
                if(!lvSalt.isEmpty() && CommandMessage.GET_SALT.equalsIgnoreCase(lvSalt.get(0)) && 
                        !mUtils.isEmpty(lvSalt.get(1)))
                {
                    //logowanie na serwer
                    mPrintWriter.println(CommandMessage.LOGIN);
                    mPrintWriter.println(pmIdUser);
                    //przesłanie zaszyfrowanego hasła
                    mPrintWriter.println(utils.Coder.code(lvSalt.get(1) + pmPassword));
                    mPrintWriter.println(mEmptySendMessage);
                    //czekanie na odpowiedz serwera
                    List<String> lvMessageList = read();
                    //jesli zalogowano
                    if(!lvMessageList.isEmpty() && CommandMessage.LOGIN.equalsIgnoreCase(lvMessageList.get(0)))
                    {
                        mCzyZalogowany = true;
                        System.out.println("zalogowany");
                        return getStatusLists(pmContactsList);
                    }else if(!lvMessageList.isEmpty() && CommandMessage.NOT_LOGGED.equalsIgnoreCase(lvMessageList.get(0)))
                    {
                        mCzyZalogowany = false;
                        throw new Exception("Nieudane logowanie");
                    }
                }
                
                return null;
            }else
            {
                throw new Exception("Jednostka jest juz zalogowana!!!");
            }
                
        }
        
        /**
        *  Pobieranie listy statusów znajomych z listy
        *  @param pmContactsIdList lista indentyfikatorów znajomych
        *  @return lista statusow zawierajaca klase StatusList
        */
        public ArrayList<Utils.StatusList> getStatusLists(List<String> pmContactsIdList) throws Exception
        {
            //pobieranie statusów znajmomych
            mPrintWriter.println(CommandMessage.GET_STATUS_LIST);
            if(pmContactsIdList != null)
            {
                for(String lvId : pmContactsIdList)
                {
                    mPrintWriter.println(lvId);
                }
            }
            mPrintWriter.println(mEmptySendMessage);
            //czekanie na odpowiedz serwera i czytanie ze strumienia
            List<String> lvMessageList = read();
            if(!lvMessageList.isEmpty() && CommandMessage.GET_STATUS_LIST.equalsIgnoreCase(lvMessageList.get(0)))
            {
                
                System.out.println("Lista statusów pobrana");
                return mUtils.makeStatusList(lvMessageList.subList(1, lvMessageList.size()));
            }else if(lvMessageList.isEmpty())
            {
                throw new Exception("Bład przy pobieraniu listy statusów");
            }
            return null;
        }
        
        /**
        *  Pobieranie listy statusów znajomych z listy
        *  @param pmContactsIdList lista indentyfikatorów znajomych
        *  @return lista statusow zawierajaca klase StatusList
        */
        public void getStatusListsDuring(List<String> pmContactsIdList) throws Exception
        {
            //pobieranie statusów znajmomych
            mPrintWriter.println(CommandMessage.GET_STATUS_LIST);
            if(pmContactsIdList != null)
            {
                for(String lvId : pmContactsIdList)
                {
                    mPrintWriter.println(lvId);
                }
            }
            mPrintWriter.println(mEmptySendMessage);
        }
        
        /**
         * Klasa służaca do wylogowania użytkownika
         */
        public void logoutFromServer() throws Exception
        {
            if(mCzyZalogowany)
            {
                mPrintWriter.println(CommandMessage.LOGOUT);
                mPrintWriter.println(mEmptySendMessage);
                mCzyZalogowany = false;
            }else
            {
                throw new Exception("Jednostka nie jest zalogowana!!!");
            }
        }
        
        /**
         * Klasa służaca do wylogowania użytkownika
         */
        public void getId() throws Exception
        {
            if(mCzyZalogowany)
            {
                mPrintWriter.println(CommandMessage.GET_ID);
                mPrintWriter.println(mEmptySendMessage);
            }else
            {
                throw new Exception("Jednostka nie jest zalogowana!!!");
            }
        }
        
        
        /**
         * Klasa służaca do rejestracji uzytkownika
         */
        public boolean createUser(String pmName,String pmPassword) throws Exception
        {
            
            {
                mPrintWriter.println(CommandMessage.CREATE_USER);
                mPrintWriter.println(pmName);
                mPrintWriter.println(utils.Coder.code("1ejH4HycsFQd" + pmPassword));
                mPrintWriter.println(mEmptySendMessage);
                List<String> lvMessageList = read();
                if(!lvMessageList.isEmpty() && CommandMessage.CREATE_USER.equalsIgnoreCase(lvMessageList.get(0)))
                {
                    return true;
                }else
                {
                    return false;
                }
            }
        }
        
        /**
         * Funkcja wysyła do serwera dane
         * o kontaktach
         * @param pmContactsList Lista kontaktów, którą chcemy zapisać w bazue
         */
        public void exportContactsToServer(ArrayList<utils.Holder> pmContactsList) throws Exception
        {
            if(mCzyZalogowany)
            {
                mPrintWriter.println(CommandMessage.EXPORT_CONTACTS);
                for(utils.Holder pmElem : pmContactsList)
                {
                    mPrintWriter.println(pmElem.getId());
                    mPrintWriter.println(pmElem.getNazwa());
                }
                mPrintWriter.println(mEmptySendMessage);
            }else
            {
                throw new Exception("Jednostka nie jest zalogowana!!!");
            }
        }
        
        /**
         * Funkcja wysyła żądanie do serwera o pobranie
         * danych o znajomych
         */
        public void importContactsFromServer() throws Exception
        {
            if(mCzyZalogowany)
            {
                mPrintWriter.println(CommandMessage.IMPORT_CONTACTS);
                mPrintWriter.println(mEmptySendMessage);
            }else
            {
                throw new Exception("Jednostka nie jest zalogowana!!!");
            }
        }
        
        /**
         * Wysyła zapytanie dotyczące rozpoczecia rozmowy txt z podanym loginem
         * do serwera
         * @param pmLoginReceiver ID osoby, z którą chcemy nawiązać rozmowe
         * 
         */
        public synchronized void requestTxtToServer(String pmLoginReceiver) throws Exception
        {
            if(mCzyZalogowany)
            {
                mPrintWriter.println(CommandMessage.MESS_REQUEST);
                mPrintWriter.println(pmLoginReceiver);
                mPrintWriter.println(mEmptySendMessage);
            }else
            {
                throw new Exception("Jednostka nie jest zalogowana!!!");
            }
        }
        
        /**
         * Zmiana statusu
         * @param  pmStatus Nazwa statusu
         */
        public void changeStatus(String pmStatus) throws Exception
        {
            if(mCzyZalogowany)
            {
                mPrintWriter.println(CommandMessage.STATUS_CHANGE);
                mPrintWriter.println(pmStatus);
                mPrintWriter.println(mEmptySendMessage);
            }else
            {
                throw new Exception("Jednostka nie jest zalogowana!!!");
            }
        }
        public boolean getCzyZalgowany()
        {
            return mCzyZalogowany;
        }
        
        public String getIp()
        {
            return mSocket.getInetAddress().toString().substring(1);
        }
        
        public int getPort()
        {
            return mSocket.getPort();
        }
        
	public static void main(String[] args)
	{
            try
            {
                Client klient = new Client("127.0.0.1",63400);
//                ArrayList<String> lvArray = new ArrayList();
//                lvArray.add("10");
//                lvArray.add("7");
//                
//                ArrayList<utils.Holder> lvHolder = new ArrayList<utils.Holder>();
//                lvHolder.add(new utils.Holder("17","a",1));
//                lvHolder.add(new utils.Holder("99","a",1));
//                lvHolder.add(new utils.Holder("19","b",1));
//                
//                klient.loginToServer("admin","root",lvArray);
//                klient.changeStatus(utils.Status.AVALIBLE);
//                klient.requestTxtToServer("15");
//                klient.exportContactsToServer(lvHolder);
//                klient.importContactsFromServer();
                new ThreadServer(klient).start();
                //klient.logoutFromServer();
            }
            catch(Exception e)
            {
                    System.out.println(e);
            }
	}
}

class ThreadServer extends Thread
{
        Client mClient;
        public ThreadServer(Client klient)
        {
            mClient = klient;
        }
    
    @Override
        public void run()  
    {
        try
        {
            Socket mSocket;
            PrintWriter mPrintWriter;
            BufferedReader mInput;
            //while(mClient.getCzyZalgowany())
            //mClient.readFromServer();
           // mSocket = mClient.listener();
           // ClientMessenger messenger = new ClientMessenger(mSocket);
            
            
            

        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
