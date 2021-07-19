package sckdn.lisa.file;

import android.content.Context;
import android.net.Uri;
import android.provider.DocumentsContract;

import androidx.documentfile.provider.DocumentFile;

import sckdn.lisa.activities.Lisa;
import sckdn.lisa.download.FileCreator;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.utils.Common;


public class SAFile {

    public static DocumentFile getDocument(Context context, IllustsBean illust, int index) {
        DocumentFile root = rootFolder(context);
        String displayName = FileCreator.customFileName(illust, index);
        String id = DocumentsContract.getTreeDocumentId(root.getUri());
        id = id + "/" + displayName;
        Uri childrenUri = DocumentsContract.buildDocumentUriUsingTree(root.getUri(), id);
        DocumentFile realFile = DocumentFile.fromSingleUri(context, childrenUri);
        if (realFile != null && realFile.exists()) {
            try {
                DocumentsContract.deleteDocument(context.getContentResolver(), realFile.getUri());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return root.createFile(getMimeTypeFromIllust(illust, index), displayName);
    }

    public static DocumentFile rootFolder(Context context) {
        String rootUriString = Lisa.sSettings.getRootPathUri();
        Uri uri = Uri.parse(rootUriString);
        try {
            return DocumentFile.fromTreeUri(context, uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isFileExists(Context context, String displayName) {
        DocumentFile root = rootFolder(context);
        if (root != null) {
            String id = DocumentsContract.getTreeDocumentId(root.getUri());
            id = id + "/" + displayName;
            Uri childrenUri = DocumentsContract.buildDocumentUriUsingTree(root.getUri(), id);
            DocumentFile realFile = DocumentFile.fromSingleUri(context, childrenUri);
            return realFile != null && realFile.exists();
        } else {
            return false;
        }
    }

    public static String getMimeTypeFromIllust(IllustsBean illust, int index) {
        String url;
        if (illust.getPage_count() == 1) {
            url = illust.getMeta_single_page().getOriginal_image_url();
        } else {
            url = illust.getMeta_pages().get(index).getImage_urls().getOriginal();
        }

        String result = "png";
        if (url.contains(".")) {
            result = url.substring(url.lastIndexOf(".") + 1);
        }
        Common.showLog("getMimeTypeFromIllust fileUrl: " + url + ", fileType: " + result);
        return "image/" + result;
    }
}
