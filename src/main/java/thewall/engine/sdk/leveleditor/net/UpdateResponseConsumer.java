package thewall.engine.sdk.leveleditor.net;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.attachment.ContentDisposition;
import org.apache.hc.client5.http.async.methods.AbstractBinResponseConsumer;
import org.apache.hc.client5.http.async.methods.AbstractCharResponseConsumer;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.http.NameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class UpdateResponseConsumer extends AbstractBinResponseConsumer<UpdateResponseConsumer.HttpResult> {
    private final static Logger logger = LogManager.getLogger();
    private final JFrame frame = new JFrame("JTEEditor Update");
    private final JProgressBar pb = new JProgressBar();
    JLabel jLabel = new JLabel("Download speed:          ");

    private BufferedOutputStream fileStream;
    private final String path;
    private HttpResult response;

    public UpdateResponseConsumer(String path) throws IOException {
        this.path = path;

        frame.add(jLabel);
    }

    public static final class HttpResult {
        private final HttpResponse response;
        private Throwable throwable;
        private boolean isSuccess;
        private final String path;

        private HttpResult(boolean isSuccess, Throwable throwable, HttpResponse response, String path){
            this.throwable = throwable;
            this.isSuccess = isSuccess;
            this.response = response;
            this.path = path;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public HttpResponse getResponse() {
            return response;
        }

        public String getUpdatePath() {
            return path;
        }
    }

    private @Nullable String resolveFileName(HttpResponse response) {
        try {
            ContentDisposition contentDisposition = new ContentDisposition(response.getHeader("Content-Disposition").getValue());
            return contentDisposition.getParameter("filename");
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void start(HttpResponse response, ContentType contentType) throws HttpException, IOException {
        String filename = resolveFileName(response);
        String path;
        path = this.path + "\\" + Objects.requireNonNullElse(filename, "update.zip");
        try {
            this.fileStream = new BufferedOutputStream(FileUtils.openOutputStream(new File(path), false));
        }catch (Exception e){
            logger.error("Cannot open I/O for [" + path + "]", e);
            throw e;
        }

        pb.setMinimum(0);
        pb.setMaximum(Integer.parseInt(response.getHeader("Content-Length").getValue()));
        pb.setStringPainted(true);

        // add progress bar
        frame.setLayout(new FlowLayout());
        frame.getContentPane().add(pb);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(300, 100);
        frame.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);

        this.response = new HttpResult(true, null, response, path);
    }

    @Override
    protected HttpResult buildResult() {
        return response;
    }

    @Override
    protected int capacityIncrement() {
        return Integer.MAX_VALUE;
    }


    long bytesRead = 0;
    int bytesPerSecond = 0;
    long lastTime = 0;
    long downloadTimeStart = 0;
    @Override
    protected void data(ByteBuffer data, boolean endOfStream) throws IOException {
        if(downloadTimeStart == 0){
            downloadTimeStart = System.currentTimeMillis();
            lastTime = System.currentTimeMillis();
        }
        while (data.hasRemaining()) {
            if(++bytesRead % 5000 == 0){
                pb.setValue((int) bytesRead);
            }
            long currentTime = System.currentTimeMillis();
            if(currentTime - lastTime > 1000){
                jLabel.setText("Download speed: " + FileUtils.byteCountToDisplaySize(bytesRead) + "/s");
                bytesPerSecond = 0;
                lastTime = currentTime;
            }
            ++bytesPerSecond;
            fileStream.write(data.get());
        }

        if (endOfStream) {
            double endTime = (System.currentTimeMillis() - downloadTimeStart) / 1000.0;
            logger.info("Download done in [" + endTime + ".s]");
            JOptionPane.showMessageDialog(frame, "Update complete! Restarting editor.");
            frame.dispose();
            fileStream.flush();
            fileStream.close();
        }
    }

    @Override
    public void releaseResources() {
    }

    @Override
    public void failed(Exception cause) {
        this.response = new HttpResult(false, cause, null, null);
    }
}
