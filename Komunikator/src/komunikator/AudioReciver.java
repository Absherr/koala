/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package komunikator;

/**
 *
 * @author kamil
 */
import java.awt.BorderLayout;
import javax.media.*;
import javax.swing.JFrame;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AudioReciver implements Runnable {

    private String addr;
    private Player audioPlayer = null;
    private MediaLocator mediaLocator = null;
    public Receiver mWindow;

    public AudioReciver(String ip, String port) {
        addr = "rtp://" + ip + ":" + port + "/audio";
        System.out.println(addr);
        this.mWindow = null;
    }

    public AudioReciver(String ip, String port, Receiver okno) {
        addr = "rtp://" + ip + ":" + port + "/audio";
        System.out.println(addr);
        this.mWindow = okno;
    }

    public void run() {
        while ((mWindow != null) && (this.mWindow.czyRozmowaGlosowa)) {
            try {
                mediaLocator = new MediaLocator(addr);
                audioPlayer = Manager.createRealizedPlayer(mediaLocator);

                System.out.println("Wcisnij enter any zakonczyc");
                play();
                System.in.read();
                System.out.println("Koncze");
                stop();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public void play() {
        audioPlayer.start();
    }

    public void stop() {
        audioPlayer.stop();
        audioPlayer.close();
    }
}