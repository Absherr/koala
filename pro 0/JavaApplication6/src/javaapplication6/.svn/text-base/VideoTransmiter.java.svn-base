/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication6;

/**
 *
 * @author kamil
 */
import javax.media.*;
import javax.media.protocol.*;
import javax.media.format.*;

import java.io.IOException;
import java.util.Vector;

public class VideoTransmiter implements Runnable {

    private String addr;
    private Vector devices = null;
    private DataSource source = null;
    private CaptureDeviceInfo cdi;
    private MediaLocator mediaLocator = null;
    private DataSink dataSink = null;
    private Processor mediaProcessor = null;
    private static final Format[] FORMATS = new Format[]{
        //new AudioFormat(AudioFormat.ULAW_RTP)};

        new VideoFormat(VideoFormat.JPEG_RTP)};

    public String getMediaLocator() {
        return mediaLocator.toString();
    }
    private static final ContentDescriptor CONTENT_DESCRIPTOR =
            //new ContentDescriptor(ContentDescriptor.RAW_RTP);
            new FileTypeDescriptor(FileTypeDescriptor.RAW_RTP); //

    public VideoTransmiter(String ip, String port) {
        addr = "rtp://"+ip+":"+port+"/video";
        System.out.println(addr);
       
    }

    public void run() {
        try {
            mediaLocator = new MediaLocator(addr);
            Vector devices = CaptureDeviceManager.getDeviceList(new VideoFormat(null));
            cdi = (CaptureDeviceInfo) devices.elementAt(0);
            source = Manager.createDataSource(cdi.getLocator());
            setDataSource(source);
            startTransmitting();
            System.out.println("Rozpoczeto nadawanie...");
            System.out.println("Wcisnij enter any zakonczyc");

            System.in.read();
            System.out.println("Koncze");
            stopTransmitting();
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public void init() {
    }

    public void startTransmitting() throws IOException {

        mediaProcessor.start();
        dataSink.open();
        dataSink.start();
    }

    public void stopTransmitting() throws IOException {

        dataSink.stop();
        dataSink.close();
        mediaProcessor.stop();
        mediaProcessor.close();
    }

    public void setDataSource(DataSource ds) throws IOException,
            NoProcessorException, CannotRealizeException, NoDataSinkException {


        mediaProcessor = Manager.createRealizedProcessor(
                new ProcessorModel(ds, FORMATS, CONTENT_DESCRIPTOR));


        dataSink = Manager.createDataSink(mediaProcessor.getDataOutput(),
                mediaLocator);
    }
}
