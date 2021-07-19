package sckdn.lisa.http;


import android.net.Uri;
import android.text.TextUtils;

import sckdn.lisa.utils.Common;
//import retrofit2.Call;
//import retrofit2.Callback;

public class HostManager {

    public static final String HOST_OLD = "i.pximg.net";
//    public static final String HOST_OLD = "app-api.pixiv.net";
//    public static final String HOST_NEW = "i.pixiv.cat";
    private static final String HTTP_HEAD = "http://";
    private PKCEItem pkceItem;

    private String host;

    private HostManager() {
    }

    public static HostManager get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final HostManager INSTANCE = new HostManager();
    }

    public void init() {

            host = randomHost();


    }

    private String randomHost() {
        String[] already = new String[]{
                "210.140.92.145",
                "210.140.92.141",
                "210.140.92.138",
                "210.140.92.143",
                "210.140.92.146",
                "210.140.92.142",
                "210.140.92.147",
                "210.140.92.139",
                "210.140.92.140",
                "210.140.92.144"
        };
        return already[Common.flatRandom(already.length)];
    }



    public String replaceUrl(String before) {
                return resizeUrl(before);


    }

    private String resizeUrl(String url) {
        if (TextUtils.isEmpty(host)) {
            host = randomHost();
        }
        try {
            Uri uri = Uri.parse(url);
            return HTTP_HEAD + host + uri.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return HTTP_HEAD + host + url.substring(19);
        }
    }

    public PKCEItem getPkce() {
        if (pkceItem == null) {
            try {
                final String verify = PkceUtil.generateCodeVerifier();
                final String challenge = PkceUtil.generateCodeChallange(verify);
                pkceItem = new PKCEItem(verify, challenge);
            } catch (Exception e) {
                e.printStackTrace();
                pkceItem = new PKCEItem(
                        "-29P7XEuFCNdG-1aiYZ9tTeYrABWRHxS9ZVNr6yrdcI",
                        "usItTkssolVsmIbxrf0o-O_FsdvZFANVPCf9jP4jP_0");
            }
        }
        return pkceItem;
    }
}
