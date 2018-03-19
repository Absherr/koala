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

public class AudioTransmiter implements Runnable {

    private String addr;
    private Vector devices = null;
    private DataSource source = null;
    private CaptureDeviceInfo cdi;
    private MediaLocator mediaLocator = null;
    private DataSink dataSink = null;
    private Processor mediaProcessor = null;
    private static final Format[] FORMATS = new Format[]{
        new AudioFormat(AudioFormat.ULAW_RTP)};

    //new VideoFormat(VideoFormat.JPEG_RTP)};
    public String getMediaLocator() {
        return mediaLocator.toString();
    }
    private static final ContentDescriptor CONTENT_DESCRIPTOR =
            //new ContentDescriptor(ContentDescriptor.RAW_RTP);
            new FileTypeDescriptor(FileTypeDescriptor.RAW_RTP); //

    public AudioTransmiter(String ip, String port) {
        addr = "rtp://" + ip + ":" + port + "/audio";
        System.out.println(addr);
    }

    public AudioTransmiter(String ip, String port, boolean pad) {
        addr = "rtp://" + ip + ":" + port + "/audio";
        System.out.println(addr);
    }

    public void run() {
        
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
