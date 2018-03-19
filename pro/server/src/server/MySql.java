package server;

/**
 * Klasa zawierająca metody obsługi połączenia z bazą danych oraz zapytania
 * @author ADAM
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import utils.Utils;

public class MySql
{
        private Connection mConn;
        private String mUrl, mUserName, mPassword;
        
        /** 
         * Konstruktor - trzeba wpisac prawidłowe dane
         */
        public MySql()
        {       
            mUrl = "jdbc:mysql://localhost:3306/koaladb";
            mUserName = "root";
            mPassword = "root";
        }
        
        /**
         * Metoda inicjuje połączenie z baza
         */
        public boolean connect()
        {
            try 
            {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } 
            catch (InstantiationException e) 
            {
                e.printStackTrace();
                return false;
            } 
            catch (IllegalAccessException e) 
            {
                    e.printStackTrace();
                    return false;
            } 
            catch (ClassNotFoundException e) 
            {
                    e.printStackTrace();
                    return false;
            }

            try 
            {
                    mConn = DriverManager.getConnection(mUrl, mUserName, mPassword);
            } 
            catch (SQLException e) 
            {
                e.printStackTrace();
                System.out.println("nok");
                return false;
            }
            System.out.println("ok");
            return true;
        }
        /**
         * Rozłącza z bazą danych
         */
        private void disconnect()
        {
            try
            {
                mConn.close();
            } 
            catch (SQLException e) 
            {
                System.err.println(e.toString());
            }
        }
        
        public Connection getConnection()
        {
            return mConn;
        }
        
        /*public PrepareStatement createPrepareStatement(String query)
        {
                return conn.createPrepareStatement(query);
        }*/
        
        /** 
         * Metoda pobiera z bazy danych  
         */
        public ArrayList<Vector<String>> executeQuery(String pmQuery)
        {
            connect();
            Statement lvStatement;
            ResultSet lvRs;
            ArrayList<Vector<String>> lvWynik = new ArrayList();
            try 
            {
                lvStatement = mConn.createStatement();
                lvRs = lvStatement.executeQuery(pmQuery);
                while(lvRs.next())
                {
                    //dane jednego wiersza sa umieszczane w wektorze
                    Vector<String> lvVector= new Vector();
                    lvVector.add(lvRs.getString(1));

                    //na koniec wrzucamy dane do listy
                    lvWynik.add(lvVector);
                }
            } 
            catch (SQLException e) 
            {
                System.err.println(e.toString());
            }
            finally
            {
                disconnect();
            }

            return lvWynik;
        }
        
        /** 
        * Sprawdza czy dana osoba jest dostępna 
        * @param pmLogin Id użytkownika
        */
        public String ifAvalible(String pmLogin)
        {
            connect();
            String lvQuery= "SELECT prof.status FROM app_user_profile prof LEFT JOIN app_contact "
                    + "con on con.owner_id = prof.user_id WHERE prof.user_id = "
                        + pmLogin;// + " AND con.is_ignored != 1";
            Statement lvStatement;
            ResultSet lvRs;
            try 
            {
                lvStatement = mConn.createStatement();
                lvRs = lvStatement.executeQuery(lvQuery);
                //sprawdzamy czy istnieje rekord o takim id 
                if(lvRs.next())
                {
                    return lvRs.getString(1);
                }
            } 
            catch (SQLException e) 
            {
                System.err.println(e.toString());
            }
            finally
            {
                disconnect();
            }

            return Utils.BRAK_REKORDU;
        }
        
        
        /** 
        * Pobiera liste znajomych dla podanego Ip
        * @param pmId Id ownera
        */
        public ArrayList<String> getFriendList(String pmId)
        {
            ArrayList<String> lvWynik = new ArrayList<String>();
            connect();
            String lvQuery= "SELECT friend_id, pseudo FROM app_contact WHERE owner_id = "
                        + "(SELECT id FROM app_user_profile where user_id = " + pmId 
                        + ") AND is_ignored = 0";
            Statement lvStatement;
            ResultSet lvRs;
            try 
            {
                lvStatement = mConn.createStatement();
                lvRs = lvStatement.executeQuery(lvQuery);
                //sprawdzamy czy istnieje rekord o takim id 
                while(lvRs.next())
                {
                    lvWynik.add(lvRs.getString(1));
                    lvWynik.add(lvRs.getString(2));

                }
            } 
            catch (SQLException e) 
            {
                System.err.println(e.toString());
            }
            finally
            {
                disconnect();
            }

            return lvWynik;
        }
        
        /** 
        * Wysyla do bazy liste znajomych
        * UWAGA! usuwa starą liste
        * @param pmId Id ownera
        * @param pmFrendList lista znajomych
        * @return true jesli udało się wysłac liste, w przeciwnym wypadku false
        */
        public List<String> sendFriendList(String pmId, List<String> pmFrendList)
        {         
            Statement lvStatement;
            List<String> lvWynik = new ArrayList<String>();
            //usuwanie starej listy, w razie bledu zwroc false
            if(!deleteFriendListFromDB(pmId))
                return lvWynik;
            connect();
            ResultSet lvRs;
            try 
            { 
                for(int i = 0; i < pmFrendList.size(); i+=2 )
                {
                    String lvQuery = "SELECT id FROM app_user_profile WHERE id = " 
                            + pmFrendList.get(i);
                    lvStatement = mConn.createStatement();
                    lvRs = lvStatement.executeQuery(lvQuery); 
                    if(lvRs.next())
                    {
                        lvQuery = "INSERT INTO app_contact(owner_id, friend_id, pseudo, is_ignored) VALUES ("
                                + "(SELECT id FROM app_user_profile where user_id = " + pmId + ")," + pmFrendList.get(i) 
                                + ",'" + pmFrendList.get(i+1) + "',0)";
                        lvStatement = mConn.createStatement();
                        lvStatement.executeUpdate(lvQuery); 
                    }else
                    {
                        lvWynik.add(pmFrendList.get(i));
                        lvWynik.add(pmFrendList.get(i+1));
                    }
                }        
            } 
            catch (SQLException e) 
            {
                System.err.println(e.toString());
                return lvWynik;
            }
            finally
            {
                disconnect();
            }
            return lvWynik;
        }
            
        /** 
        * Usuwa z bazy liste znajomych
        * @param pmId Id ownera
        * @return true jesli udało się usunąc liste, w przeciwnym wypadku false
        */
        public boolean deleteFriendListFromDB(String pmId)
        {
            connect();
            String lvQuery= "DELETE FROM app_contact WHERE owner_id = " 
                    + "(SELECT id FROM app_user_profile where user_id = " 
                    + pmId + ") AND is_ignored = 0";
            Statement lvStatement;
            try 
            {
                lvStatement = mConn.createStatement();
                lvStatement.executeUpdate(lvQuery);
            } 
            catch (SQLException e) 
            {
                System.err.println(e.toString());
                return false;
            }
            finally
            {
                disconnect();
            }
            return true;
        }
        
        /** 
        * Pobiera Ip 
        * @param pmLogin Id użytkownika
        */
        public String getIp(String pmLogin)
        {
            connect();
            String lvQuery= "SELECT ip FROM app_user_profile WHERE user_id = "
                        + pmLogin;
            Statement lvStatement;
            ResultSet lvRs;
            try 
            {
                lvStatement = mConn.createStatement();
                lvRs = lvStatement.executeQuery(lvQuery);
                //sprawdzamy czy istnieje rekord o takim id 
                if(lvRs.next())
                {
                    return lvRs.getString(1);
                }
            } 
            catch (SQLException e) 
            {
                System.err.println(e.toString());
            }
            finally
            {
                disconnect();
            }

            return Utils.BRAK_REKORDU;
        }
            
        /** 
        * Pobiera sól 
        * @param pmLogin Id użytkownika
        */
        public String getSalt(String pmId)
        {
            connect();
            String lvQuery= "SELECT auth.password FROM auth_user auth "
                    + "WHERE auth.id like '" + pmId + "' OR auth.username like '"
                    + pmId + "'";
            Statement lvStatement;
            ResultSet lvRs;
            try 
            {
                lvStatement = mConn.createStatement();
                lvRs = lvStatement.executeQuery(lvQuery);
                //sprawdzamy czy istnieje rekord o takim id 
                if(lvRs.next())
                {
                    String [] lvTab = lvRs.getString(1).split("\\$");
                    if(lvTab.length > 2)
                        //zwracamy tylko sol
                        return lvTab[1];
                }
            } 
            catch (SQLException e) 
            {
                System.err.println(e.toString());
            }
            finally
            {
                disconnect();
            }

            return Utils.BRAK_REKORDU;
        }
        /** 
         * Metoda pobiera z bazy danych dane i 
         *  uwierzytelnia użytkownka
         * @param pmLogin nazwa lub id osoby logującej sie w komunikatorze
         * @param pmPassword hasło - zaszyfrowane
         */   
        public ArrayList<String> checkAuth(String pmLogin, String pmPassword)
        {
                
            String lvQuery= "SELECT id,username FROM auth_user where password LIKE '%$%$" + pmPassword 
                    + "' AND (username LIKE '" + pmLogin + "' OR id LIKE '" + pmLogin +"')";
            connect();
            Statement lvStatement;
            ResultSet lvRs;
            ArrayList<String> lvArray = new ArrayList();
            try 
            {
                lvStatement = mConn.createStatement();
                lvRs = lvStatement.executeQuery(lvQuery);
                if(lvRs.next())
                {
                    lvArray.add(lvRs.getString(1));
                    lvArray.add(lvRs.getString(2));

                }
                return lvArray;
            } 
            catch (SQLException e) 
            {
                System.err.println(e.toString());
            }
            finally
            {
                disconnect();
            }
            return null;
        }
        
        /** 
         * Pobiera liste danych o znajomych
         */
        public ArrayList<Utils.StatusList> getStatusList(List<String> pmContactsIdList, Utils pmUtils)
        {
            String lvIdList = "";
            for(String elem : pmContactsIdList)
            {
                if(lvIdList.length()>0)
                {
                    lvIdList += "," + elem; 
                }
                else
                {
                    lvIdList = elem;
                }
            }

            if(lvIdList.length() > 0)
            {
                String lvQuery= "SELECT user_id,status FROM app_user_profile WHERE user_id IN "
                        + "(" + lvIdList + ")";
                connect();
                Statement lvStatement;
                ResultSet lvRs;
                ArrayList<String> lvArray = new ArrayList();
                try 
                {
                    lvStatement = mConn.createStatement();
                    lvRs = lvStatement.executeQuery(lvQuery);
                    while(lvRs.next())
                    {
                        lvArray.add(lvRs.getString(1));
                        lvArray.add(lvRs.getString(2));
                    }
                    return pmUtils.makeStatusList(lvArray);
                } 
                catch (SQLException e) 
                {
                    System.err.println(e.toString());
                }
                finally
                {
                    disconnect();
                }
            }
            return new ArrayList<Utils.StatusList>();
        }
        
        /* Metoda <code>executeUpdate</code> zmienia dane w bazie 
         *  INSERT, UPDATE or DELETE
         */
        public boolean executeUpdate(String pmUpdate)
        {
            connect();
            Statement lvStatement;
            String lvUp = pmUpdate;

            try 
            {
                lvStatement = mConn.createStatement();
                lvStatement.executeUpdate(lvUp);

            } 
            catch (SQLException e) 
            {
                System.err.println(e.toString());
                return false;
            }
            finally
            {
                disconnect();
            }
            return true;
        }
        
        /* Metoda robi update Ip oraz ostatniego logowania na bazie  
         *  INSERT, UPDATE or DELETE
         */
        public boolean updateIp(String pmIp, String pmUserId)
        {
            connect();
            Statement lvStatement;
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date lvDate = new Date();
            System.out.println(dateFormat.format(lvDate)+"2");
        
            String lvUp = "UPDATE app_user_profile SET ip = '" + pmIp +"', last_logged = '"
                    + dateFormat.format(lvDate) + "' WHERE user_id = " + pmUserId;
            try 
            {
                lvStatement = mConn.createStatement();
                lvStatement.executeUpdate(lvUp);
                return true;

            } 
            catch (SQLException e) 
            {
                System.err.println(e.toString());
                return false;
            }
            finally
            {
                disconnect();
            }
        }
        
        /* Metoda robi update Statusu   
         *  
         */
        public boolean updateStatus(String pmStatus, String pmUserId)
        {
            connect();
            Statement lvStatement;
        
            String lvUp = "UPDATE app_user_profile SET status = '" + pmStatus 
                    + "' WHERE user_id = " + pmUserId;

            try 
            {
                lvStatement = mConn.createStatement();
                lvStatement.executeUpdate(lvUp);
                return true;

            } 
            catch (SQLException e) 
            {
                System.err.println(e.toString());
                return false;
            }
            finally
            {
                disconnect();
            }
        }
        public boolean createUser(String pmUserName, String pmPassword)
        {
            connect();
            Statement lvStatement;
        
            String lvUp = "INSERT INTO auth_user(username,first_name,last_name,email,password,is_staff,is_active,is_superuser,last_login,date_joined) VALUES ('" + pmUserName + "','Admin','Adminowski','admin@koala.com','sha1$1ejH4HycsFQd$" + pmPassword + "',1,1,1,'2012-11-28 17:24:45','2012-11-25 18:16:37')";

            try 
            {
                lvStatement = mConn.createStatement();
                lvStatement.executeUpdate(lvUp);
                
                lvUp = "INSERT INTO app_user_profile(user_id,is_searchable,last_logged) VALUES (LAST_INSERT_ID(),1,'2012-11-25 18:16:37')";
                
                lvStatement = mConn.createStatement();
                lvStatement.executeUpdate(lvUp);
           
                return true;
            } 
            catch (SQLException e) 
            {
                System.err.println(e.toString());
                return false;
            }
            finally
            {
                disconnect();
            }
        }
}