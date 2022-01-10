package jte2.engine.sdk.leveleditor.net;

import org.junit.jupiter.api.Test;
import jte2.engine.sdk.leveleditor.Editor;
import jte2.engine.twilight.networking.ConnectionRefusedException;

class HTTPUpdateTest {
    @Test
    void test2(){
        test();
    }

    void test(){
        Editor editor = new Editor();
        HTTPUpdate update = new HTTPUpdate(editor);
        try {
            UpdateData data = update.checkLatestVersion();
            System.out.println(data.getVersion());
            update.updateVersion("1.2");

        } catch (ConnectionRefusedException | UpdateException e) {
            e.printStackTrace();
        }
    }

}