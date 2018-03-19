/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;

class JavaApplication6 extends JFrame {

    public static void main(String[] args) {

        try {
            /*   AudioReciver v1 =  new AudioReciver();
            AudioTransmiter v2 = new AudioTransmiter();
            
            Thread t1 = new Thread(v1);
            Thread t2 = new Thread(v2);
            
            t1.start();
            
            t2.start();*/
            //AudioTransmiter v2 = new AudioTransmiter("192.168.2.108", "22222");
            AudioReciver v1 = new AudioReciver("188.47.226.57", "22222");

            Thread t1 = new Thread(v1);
            //Thread t2 = new Thread(v2);

            t1.start();
            //t2.start();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    // TODO code application logic here
}
