/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication6;

/**
 *
 * @author kamil
 */
import java.awt.BorderLayout;
import javax.media.*;
import javax.swing.JFrame;
import java.io.IOException;

public class VideoReciver implements Runnable {

    private String addr;
    private Player audioPlayer = null;
    private MediaLocator mediaLocator = null;

    public VideoReciver(String ip, String port) throws IOException, NoPlayerException, CannotRealizeException {
        addr = "rtp://"+ip+":"+port+"/video";
      
    }

    public void run() {
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

    public void play() {
        audioPlayer.start();
        JFrame f = new JFrame();
        f.setTitle("Test Webcam");
        f.setLayout(new BorderLayout());
        f.add(audioPlayer.getVisualComponent(), BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
    }

    public void stop() {
        audioPlayer.stop();
        audioPlayer.close();
    }
}