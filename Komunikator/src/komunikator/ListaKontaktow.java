
package komunikator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import utils.Holder;
import utils.Status;
/**
 *
 * @author ADAM
 */
//wszystkie kontakty zapisane w pliku
public class ListaKontaktow
{
    Vector<Holder> vec = new Vector<Holder>();
    ListaKontaktow()
    {
        File f = new File("kontakty.txt");
        if(f.exists())
        {
            try
            {
            FileInputStream fstream = new FileInputStream("kontakty.txt");
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null)   
            {
            // Print the content on the console
                String lvId = strLine;
                String lvNazwa = br.readLine();
                vec.add(new Holder(lvId,lvNazwa,Status.UNAVALIBLE));
            }
            //Close the input stream
            in.close();
            }catch (Exception e)
            {//Catch exception if any
                System.err.println("Error: " + e.getMessage());
            }
        }else
        {
            try
            {
                f.createNewFile();
            }catch(Exception e)
            {//Catch exception if any
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
    //zapisuje kontakty z vektora do pliku
    public void Save_List()
    {
        try
        {
            // Create file 
            FileWriter fstream = new FileWriter("kontakty.txt");
            BufferedWriter out = new BufferedWriter(fstream);
            for(int i = 0;i<vec.size();i++)
            {
               out.write(vec.get(i).getId() + "\n");
               out.write(vec.get(i).getNazwa() + "\n");
            }
            //Close the output stream
            out.close();
        }catch (Exception e)
        {//Catch exception if any
        System.err.println("Error: " + e.getMessage());
        }
        
    }
    // dodaje do vectora kontakt i zapisuje do pliku zmiany
    public boolean Add(Holder c)
    {
        for(int i = 0;i<vec.size();i++)
        {
            if(vec.get(i).getId().equalsIgnoreCase(c.getId())) return false;
            if(vec.get(i).getNazwa().equalsIgnoreCase(c.getNazwa())) return false;
        }
        vec.add(c);
        Save_List();
        return true;
    }
    
    //edytuje kontakt
    public boolean Edit(Holder c, String pmNazwa)
    {
        for(int i = 0;i<vec.size();i++)
        {
            if(vec.get(i).getNazwa().equalsIgnoreCase(pmNazwa))
            {   
                vec.setElementAt(c, i);
                Save_List();
                return true;
            }
        }
        return false;
    }
    //usuwa kontakt
    public boolean Delete(String pmNazwa)
    {
        for(int i = 0;i<vec.size();i++)
        {
            if(vec.get(i).getNazwa().equalsIgnoreCase(pmNazwa))
            {   
                vec.remove(i);
                Save_List();
                return true;
            }
        }
        return false;
    }
    
    
    //Zwraca numer dla podanego pseudonimu
    public String Get_Number(String pmNazwa)
    {
        for(int i = 0;i<vec.size();i++)
        {
            if(vec.get(i).getNazwa().equalsIgnoreCase(pmNazwa))
            {   
                return vec.get(i).getId();
            }
        }
        return "-1";
    }
    
    
    //zwraca pseudonim dla podanego numeru
    public String Get_Pseudo(String numer)
    {
        for(int i = 0;i<vec.size();i++)
        {
            if(vec.get(i).getId().equalsIgnoreCase(numer))
            {   
                return vec.get(i).getNazwa();
            }
        }
        return "-1";
    }
    
    //zwraca obiekt contact
    public Holder Get_Contact(String pmNazwa)
    {
        for(int i = 0;i<vec.size();i++)
        {
            if(vec.get(i).getNazwa().equalsIgnoreCase(pmNazwa))
            {   
                return vec.get(i);
            }
        }
        return null;
    }
    
    public List getListId()
    {
        List lvLista = new ArrayList();
        for(Holder elem : vec)
        {
            lvLista.add(elem.getId());
        }
        return lvLista;
    }
    
    public void DeleteAll()
    {
        ArrayList<Holder> lvLista = new ArrayList(vec);
        for(Holder elem : lvLista)
        {
            Delete(elem.getNazwa());
        }
    }
}