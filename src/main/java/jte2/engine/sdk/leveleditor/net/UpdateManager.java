package jte2.engine.sdk.leveleditor.net;

import jte2.engine.twilight.networking.ConnectionRefusedException;

import java.util.Map;

public interface UpdateManager {
    UpdateData checkLatestVersion() throws ConnectionRefusedException, UpdateException;

    void updateVersion(String version) throws ConnectionRefusedException, UpdateException;

    Map<Integer, UpdateData> checkAvailableVersion();
}
