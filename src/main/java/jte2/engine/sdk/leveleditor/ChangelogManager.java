package jte2.engine.sdk.leveleditor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.eclipse.swt.internal.C;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class ChangelogManager {

    public enum Priority {
        GENERAL_CHANGES,
        MINOR_CHANGES,
        SECURITY_CHANGES
    }

    public static class ChangeLog {
        public @JsonIgnore String filename;
        public boolean hasContent;
        public String readTime;
        public boolean hasRead;
        public String content;
        public String title;
        public String version;
        public Priority priority;
    }

    public void updateChangelog(@NotNull ChangeLog changeLog) throws IOException{
        File file = new File(changeLog.filename);
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.writeValue(file, changeLog);
    }

    public ChangeLog getChangeLog() throws IOException {
        File file = new File("changes.log");
        if(!file.exists()){
            file = new File("changes.log.xml");
        }
        if(file.exists()){
            String content = Files.readString(Path.of(file.getPath()));
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            ChangeLog changeLog = xmlMapper.readValue(content, ChangeLog.class);
            changeLog.filename = file.getName();
            return changeLog;
        }else {
            throw new NullPointerException("no changelogs found");
        }
    }

    public void displayChangelog(ChangeLog changelog){

    }
}
