package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
public class Server extends Thread
{
	private static ServerSocket mServerSocket;
	private static Socket mClientSocket;
        public boolean mCzyNaluchiwac;
        private Okienko mOkienko;
        private ArrayList<ThreadServer> mList;
        public Server(Okienko pmOkienko)
        {
            mOkienko = pmOkienko;
            mCzyNaluchiwac = true;
            mList = new ArrayList<>();
        }    
        public void run()   
        {
            try
		{
                        mServerSocket = new ServerSocket(63400);
                        while(mCzyNaluchiwac)
                        {
                            mOkienko.addTxt("Czekam na polaczenie...");
                            mClientSocket = mServerSocket.accept();
                            mOkienko.addTxt("Polaczylem sie z kilentem otwieram watek");
                            ThreadServer lvTemp = new ThreadServer(mClientSocket,mOkienko);
                            lvTemp.start();
                            mList.add(lvTemp);
                        }
                        
                         mOkienko.addTxt("Serwer przestał nasłuchiwać");
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
        }
        
        public void wylaczSerwer()
        {
            try {
                for(ThreadServer elem : mList)
                {
                    elem.lvCzyCzytaj = false;
                   // elem.mClient.close();
                   // elem.notLogged("");
                    elem.interrupt();   
                }
            
                mServerSocket.close();
            } catch (Exception e) {
                mOkienko.addTxt(e.getMessage());
            }
        }
        
        
	public static void main(String[] args)
	{
		// Wait for client to connect on 63400
		try
		{
                        mServerSocket = new ServerSocket(63400);
                        while(true)
                        {
                            System.out.println("Czekam na polaczenie...");
                            mClientSocket = mServerSocket.accept();
                            System.out.println("Polaczylem sie z kilentem otwieram watek");
                            //new ThreadServer(mClientSocket,).start();
                        }
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
}

