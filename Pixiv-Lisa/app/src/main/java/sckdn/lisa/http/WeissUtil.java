package sckdn.lisa.http;

import androidx.webkit.ProxyConfig;
import androidx.webkit.ProxyController;
import androidx.webkit.WebViewFeature;

import java.util.concurrent.Executor;



public class WeissUtil {

    public static final int PORT = 9801;
    public static boolean use_weiss = false;

    public static void start() {
        if (!use_weiss) {
            return;
        }
        try {
//            Weiss.start(String.valueOf(PORT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void end() {
        if (!use_weiss) {
            return;
        }
        try {
//            Weiss.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void proxy() {
        if (!use_weiss) {
            return;
        }
        try {
            if (WebViewFeature.isFeatureSupported(WebViewFeature.PROXY_OVERRIDE)) {
                String proxyUrl = "127.0.0.1:" + PORT;
                ProxyConfig proxyConfig = new ProxyConfig.Builder()
                        .addProxyRule(proxyUrl)
                        .addDirect()
                        .build();
                ProxyController.getInstance().setProxyOverride(proxyConfig, new Executor() {
                    @Override
                    public void execute(Runnable command) {
                        command.run();
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
