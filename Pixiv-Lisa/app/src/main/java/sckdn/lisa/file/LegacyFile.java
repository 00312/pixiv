package sckdn.lisa.file;

import android.content.Context;

import java.io.File;

import sckdn.lisa.interfaces.FileProxy;
import sckdn.lisa.utils.Common;

public class LegacyFile implements FileProxy {

    private static final String IMAGE_CACHE = "/image_manager_disk_cache";

    @Override
    public File imageCacheFolder(Context context) {
        File cacheDir = new File(context.getCacheDir().getPath() + IMAGE_CACHE);
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        Common.showLog("LegacyFile imageCacheFolder " + cacheDir.getPath());
        return cacheDir;
    }
}
