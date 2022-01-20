package jte2.engine.sdk.leveleditor;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class ChangelogManagerTest {
    @Test
    void test() throws IOException {
        ChangelogManager changelogManager = new ChangelogManager();
        ChangelogManager.ChangeLog changeLog = changelogManager.getChangeLog();
        changelogManager.updateChangelog(changeLog);
    }

}