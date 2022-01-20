package jte2.engine.sdk.leveleditor.net;

import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.http.message.BasicHttpRequest;
import org.apache.hc.core5.http.nio.support.BasicRequestProducer;
import org.apache.hc.core5.http.support.BasicRequestBuilder;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import jte2.engine.sdk.leveleditor.Editor;
import jte2.engine.twilight.networking.ConnectionRefusedException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class HTTPUpdate implements UpdateManager {
    private final static int SERVER_NOT_FOUND_ERROR =       0xF001;
    private final static int SERVER_INVALID_RESPONSE =      0xF002;
    private final static int SERVER_DATA_INCOMPATIBILITY =  0xF003;
    private final static int SERVER_UPDATE_ARCHIVE_FAULT =  0xF004;
    private final static int UPDATE_FOLDER_PREPARE_FAULT =  0xF005;
    private final static int UPDATE_UNZIP_FAULT =           0xF006;
    private final static int UPDATE_FEEDBACK_FAULT =        0xF007;
    private final static int GENERAL_IO_FAULT =             0xF008;
    private final static String VERSION_ENDPOINT_HOST = "lagpixel.pl";
    private final static String VERSION_ENDPOINT = String.format("https://%s/jte/editor", VERSION_ENDPOINT_HOST);
    private final static Logger logger = LogManager.getLogger(HTTPUpdate.class);
    private final Editor editor;

    public HTTPUpdate(Editor editor){
        this.editor = editor;
    }

    private static class UpdateDataImpl implements UpdateData{
        private final int build, number;
        private final String name, version;

        public UpdateDataImpl(int number, String name, int build, String version){
            this.number = number;
            this.name = name;
            this.build = build;
            this.version = version;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getVersionNumber() {
            return number;
        }

        @Override
        public String getVersion() {
            return version;
        }

        @Override
        public int getBuild() {
            return build;
        }
    }

    Optional<String> resolveFileName(HttpResponse response) {
        return Arrays.stream(response.getFirstHeader("Content-Disposition").getElements())
                .map(element -> element.getParameterByName("filename"))
                .filter(Objects::nonNull)
                .map(NameValuePair::getValue)
                .findFirst();
    }

    public String downloadVersion(String version) throws ConnectionRefusedException, UpdateException {
        if(!Files.isDirectory(Path.of("update"))){
            try {
                Files.createDirectory(Path.of("update"));
            } catch (IOException e) {
                logger.error("Cannot create disk space for update", e);
                throw new UpdateException(String.format("Error: %x", UPDATE_FOLDER_PREPARE_FAULT));
            }
        }

        final IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setSoTimeout(Timeout.ofSeconds(5))
                .build();

        final CloseableHttpAsyncClient client = HttpAsyncClients.custom()
                .setIOReactorConfig(ioReactorConfig)
                .build();

        client.start();

        final org.apache.hc.core5.http.HttpHost target = new org.apache.hc.core5.http.HttpHost(VERSION_ENDPOINT_HOST);
        final BasicHttpRequest request = BasicRequestBuilder.get()
                .setHttpHost(target)
                .setPath("/jte/editor/download/" + version)
                .build();

        final Future<UpdateResponseConsumer.HttpResult> future;

        try {
            future = client.execute(new BasicRequestProducer(request, null), new UpdateResponseConsumer("update\\" + version), null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        UpdateResponseConsumer.HttpResult updateResult;

        try {
            updateResult = future.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Cannot complete update download task", e);
            throw new UpdateException(e);
        }

        return updateResult.getUpdatePath();
    }

    @Override
    public UpdateData checkLatestVersion() throws ConnectionRefusedException, UpdateException {
        logger.info("Checking update for version [" + Editor.getEditorVersion() + "]");
        HttpEntity entity;
        int serverVerNumber, build;
        if(!Files.isDirectory(Path.of("update"))){
            try {
                Files.createDirectory(Path.of("update"));
            } catch (IOException e) {
                logger.error("Cannot create disk space for update", e);
                throw new UpdateException(String.format("Error: %x", UPDATE_FOLDER_PREPARE_FAULT));
            }
        }
        try {
            HttpClient httpClient = HttpClients.createDefault();
            logger.debug("Sending GET request to endpoint [" + VERSION_ENDPOINT + "]");
            HttpGet request = new HttpGet(VERSION_ENDPOINT + "/api/latest-version");
            HttpResponse response = httpClient.execute(request);
            logger.debug("Server response with: {}, {}", response.getProtocolVersion(), response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() != 200) {
                switch (response.getStatusLine().getStatusCode()) {
                    case 404: {
                        throw new UpdateException(String.format("Error: 0x%x", SERVER_NOT_FOUND_ERROR));
                    }

                    default: {
                        throw new UpdateException(String.format("Error: 0x%x", SERVER_INVALID_RESPONSE));
                    }
                }
            }


            entity = response.getEntity();
        } catch (Exception e) {
            logger.error("Failed to send update check request", e);
            throw new UpdateException(e.getMessage());
        }

        JSONObject jsonObject;
        String name, version;
        try {
            jsonObject = new JSONObject(EntityUtils.toString(entity));
            serverVerNumber = jsonObject.getInt("version_number");
            build = jsonObject.getInt("build");
            name = jsonObject.getString("name");
            version = jsonObject.getString("version");
        } catch (Exception e) {
            logger.warn("Data parsing error from [" + VERSION_ENDPOINT + "], " + e.getMessage());
            throw new UpdateException(String.format("Error: 0x%x", SERVER_INVALID_RESPONSE));
        }

        return new UpdateDataImpl(serverVerNumber, name, build, version);

    }

    @Override
    public void updateVersion(String version) throws ConnectionRefusedException, UpdateException {
        logger.info("Starting installing version [" + version + "] of JTEEditor");
        try {
            if (Files.isDirectory(Paths.get("update\\" + version))) {
                FileUtils.deleteDirectory(new File("update\\" + version));
            }
            Files.createDirectory(Paths.get("update\\" + version));
        }catch (Exception e){
            logger.error("Cannot prepare folder for update", e);
            throw new UpdateException(String.format("Error: 0x%x", UPDATE_FOLDER_PREPARE_FAULT));
        }

        String filename = downloadVersion(version);
        logger.info("Installing updates...");
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(filename);
        }catch (Exception e){
            logger.error("Cannot read update archive", e);
            throw new UpdateException(String.format("Error: 0x%x", SERVER_UPDATE_ARCHIVE_FAULT));
        }

        try {
            if (Files.isDirectory(Paths.get("update\\" + version + "\\" + version + "_update"))) {
                FileUtils.deleteDirectory(new File("update\\" + version + "\\" + version + "_update"));
            }
            Files.createDirectory(Paths.get("update\\" + version + "\\" + version + "_update"));
            logger.info("Installing update [" + version + "]");
            zipFile.extractAll("update\\" + version + "\\" + version + "_update");
        }catch (Exception e){
            logger.error("Cannot extract update archive", e);
            throw new UpdateException(String.format("Error: 0x%x", UPDATE_UNZIP_FAULT));
        }
        try {
            JSONObject update_json = new JSONObject();
            update_json.put("update", true);
            update_json.put("path", "update\\" + version + "\\" + version + "_update");
            FileWriter fileWriter = new FileWriter("update\\update.json");
            fileWriter.write(update_json.toString());
            fileWriter.flush();
            fileWriter.close();
        }catch (Exception e){
            logger.error("Cannot left feedback for updater", e);
            throw new UpdateException(String.format("Error: 0x%x", UPDATE_FEEDBACK_FAULT));
        }

        try {
            FileUtils.forceDelete(new File(String.valueOf(Path.of(filename))));
        } catch (IOException e) {
            logger.error("Cannot clean-up update garbage. Remove manually file [" + Path.of(filename).toAbsolutePath() + "]");
        }

        logger.info("Installing done. Restart request.");
    }

    @Override
    public Map<Integer, UpdateData> checkAvailableVersion() {
        return null;
    }
}
