/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 * klasa informacji o znajomym
 * przechowywana u klienta
 *
 */
public class Holder 
{
    private String id;
    private String nazwa;
    private String status;

    public Holder () 
    {
        id = null;
        nazwa = null;
    }

    public Holder (String a, String b, String c) 
    {
        id = a;
        nazwa = b;
        status = c;
    }

    public String getId() 
    {
        return id;
    }

    public void setId(String a) 
    {
        id = a;
    }

    public String getNazwa() {
    return nazwa;
    }

    public void setNazwa (String a) {
    nazwa = a;
    }

    public String getStatus() {
    return status;
    }

    public void setStatus(String a) {
    status = a;
    }
}
