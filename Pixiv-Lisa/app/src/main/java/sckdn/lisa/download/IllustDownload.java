package sckdn.lisa.download;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.documentfile.provider.DocumentFile;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.ArrayList;
import java.util.List;


import sckdn.lisa.R;
import sckdn.lisa.activities.BaseActivity;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.core.DownloadItem;
import sckdn.lisa.core.Manager;
import sckdn.lisa.file.SAFile;
import sckdn.lisa.http.HostManager;
import sckdn.lisa.interfaces.FeedBack;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.utils.Common;

import static android.provider.DocumentsContract.EXTRA_INITIAL_URI;

public class IllustDownload {

    public static void downloadIllust(IllustsBean illust, BaseActivity<?> activity) {
        check(activity, () -> {
            if (illust.getPage_count() == 1) {
                DownloadItem item = new DownloadItem(illust, 0);
                item.setUrl(getUrl(illust, 0));
                item.setShowUrl(getShowUrl(illust, 0));
                Common.showToast(1 + "个任务已经加入下载队列");
                Manager.get().addTask(item, activity);
            }
        });
    }

    public static void downloadIllust(IllustsBean illust, Context context) {
        if (illust.getPage_count() == 1) {
            DownloadItem item = new DownloadItem(illust, 0);
            item.setUrl(getUrl(illust, 0));
            item.setShowUrl(getShowUrl(illust, 0));
            Common.showToast(1 + "个任务已经加入下载队列");
            Manager.get().addTask(item, context);
        }
    }

    public static void downloadIllust(IllustsBean illust, int index, BaseActivity<?> activity) {
        check(activity, () -> {
            if (illust.getPage_count() == 1) {
                downloadIllust(illust, activity);
            } else {
                DownloadItem item = new DownloadItem(illust, index);
                item.setUrl(getUrl(illust, index));
                item.setShowUrl(getShowUrl(illust, index));
                Common.showToast(1 + "个任务已经加入下载队列");
                Manager.get().addTask(item, activity);
            }
        });
    }


  /*  public static void downloadAllIllust(IllustsBean illust, BaseActivity<?> activity) {
        check(activity, () -> {
            if (illust.getPage_count() == 1) {
                downloadIllust(illust, activity);
            } else {
                List<DownloadItem> tempList = new ArrayList<>();
                for (int i = 0; i < illust.getPage_count(); i++) {
                    DownloadItem item = new DownloadItem(illust, i);
                    item.setUrl(getUrl(illust, i));
                    item.setShowUrl(getShowUrl(illust, i));
                    tempList.add(item);
                }
                Common.showToast(tempList.size() + "个任务已经加入下载队列");
                Manager.get().addTasks(tempList, activity);
            }
        });
    }

    public static void downloadAllIllust(IllustsBean illust, Context context) {
        if (illust.getPage_count() == 1) {
            downloadIllust(illust, context);
        } else {
            List<DownloadItem> tempList = new ArrayList<>();
            for (int i = 0; i < illust.getPage_count(); i++) {
                DownloadItem item = new DownloadItem(illust, i);
                item.setUrl(getUrl(illust, i));
                item.setShowUrl(getShowUrl(illust, i));
                tempList.add(item);
            }
            Common.showToast(tempList.size() + "个任务已经加入下载队列");
            Manager.get().addTasks(tempList, context);
        }
    }


    public static void downloadAllIllust(List<IllustsBean> beans, BaseActivity<?> activity) {
        check(activity, () -> {
            List<DownloadItem> tempList = new ArrayList<>();
            for (int i = 0; i < beans.size(); i++) {
                if (beans.get(i).isChecked()) {
                    final IllustsBean illust = beans.get(i);

                    if (illust.getPage_count() == 1) {
                        DownloadItem item = new DownloadItem(illust, 0);
                        item.setUrl(getUrl(illust, 0));
                        item.setShowUrl(getShowUrl(illust, 0));
                        tempList.add(item);
                    } else {
                        for (int j = 0; j < illust.getPage_count(); j++) {
                            DownloadItem item = new DownloadItem(illust, j);
                            item.setUrl(getUrl(illust, j));
                            item.setShowUrl(getShowUrl(illust, j));
                            tempList.add(item);
                        }
                    }
                }
            }
            Common.showToast(tempList.size() + "个任务已经加入下载队列");
            Manager.get().addTasks(tempList, activity);
        });
    }*/

    public static String getUrl(IllustsBean illust, int index) {
        if (illust.getPage_count() == 1) {
            return HostManager.get().replaceUrl(illust.getMeta_single_page().getOriginal_image_url());
        } else {
            return HostManager.get().replaceUrl(illust.getMeta_pages().get(index).getImage_urls().getOriginal());
        }
    }

    public static String getShowUrl(IllustsBean illust, int index) {
        if (illust.getPage_count() == 1) {
            return illust.getImage_urls().getMedium();
        } else {
            return illust.getMeta_pages().get(index).getImage_urls().getMedium();
        }
    }

    public static void check(BaseActivity<?> activity, FeedBack feedBack) {
        if (Lisa.sSettings.getDownloadWay() == 1) {
            if (TextUtils.isEmpty(Lisa.sSettings.getRootPathUri())) {
                activity.setFeedBack(feedBack);
                new QMUIDialog.MessageDialogBuilder(activity)
                        .setTitle(activity.getResources().getString(R.string.string_143))
                        .setMessage(activity.getResources().getString(R.string.string_313))
                        .setSkinManager(QMUISkinManager.defaultInstance(activity))
                        .addAction(0, activity.getResources().getString(R.string.string_142),
                                QMUIDialogAction.ACTION_PROP_NEGATIVE,
                                (dialog, index) -> dialog.dismiss())
                        .addAction(0, activity.getResources().getString(R.string.string_312),
                                (dialog, index) -> {
                                    try {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                                                if (!TextUtils.isEmpty(Lisa.sSettings.getRootPathUri()) &&
                                                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    Uri start = Uri.parse(Lisa.sSettings.getRootPathUri());
                                                    intent.putExtra(EXTRA_INITIAL_URI, start);
                                                }
                                                activity.startActivityForResult(intent, BaseActivity.ASK_URI);
                                            }
                                        }).start();
                                    } catch (Exception e) {
                                        Common.showToast(e.toString());
                                        e.printStackTrace();
                                    }
                                    dialog.dismiss();
                                })
                        .show();
            } else {
                DocumentFile root = SAFile.rootFolder(activity);
                if (root == null || !root.exists() || !root.isDirectory()) {
                    activity.setFeedBack(feedBack);
                    new QMUIDialog.MessageDialogBuilder(activity)
                            .setTitle(activity.getResources().getString(R.string.string_143))
                            .setMessage(activity.getResources().getString(R.string.string_365))
                            .setSkinManager(QMUISkinManager.defaultInstance(activity))
                            .addAction(0, activity.getResources().getString(R.string.string_142),
                                    QMUIDialogAction.ACTION_PROP_NEGATIVE,
                                    (dialog, index) -> dialog.dismiss())
                            .addAction(0, activity.getResources().getString(R.string.string_366),
                                    (dialog, index) -> {
                                        try {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                                                    if (!TextUtils.isEmpty(Lisa.sSettings.getRootPathUri()) &&
                                                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                        Uri start = Uri.parse(Lisa.sSettings.getRootPathUri());
                                                        intent.putExtra(EXTRA_INITIAL_URI, start);
                                                    }
                                                    activity.startActivityForResult(intent, BaseActivity.ASK_URI);
                                                }
                                            }).start();
                                        } catch (Exception e) {
                                            Common.showToast(e.toString());
                                            e.printStackTrace();
                                        }
                                        dialog.dismiss();
                                    })
                            .show();
                } else {
                    if (feedBack != null) {
                        try {
                            feedBack.doSomething();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            if (feedBack != null) {
                try {
                    feedBack.doSomething();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
