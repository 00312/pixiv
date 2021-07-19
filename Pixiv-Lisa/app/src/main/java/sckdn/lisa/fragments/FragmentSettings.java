package sckdn.lisa.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LanguageUtils;
//import com.google.firebase.analytics.FirebaseAnalytics;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Locale;

import sckdn.lisa.R;
import sckdn.lisa.activities.BaseActivity;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.activities.TemplateActivity;
import sckdn.lisa.databinding.FragmentSettingsBinding;
import sckdn.lisa.file.LegacyFile;
import sckdn.lisa.helper.ThemeHelper;
import sckdn.lisa.utils.Common;
import sckdn.lisa.utils.Local;
import sckdn.lisa.utils.PixivSearchParamUtil;

import static android.provider.DocumentsContract.EXTRA_INITIAL_URI;
import static sckdn.lisa.utils.Settings.ALL_LANGUAGE;


public class FragmentSettings extends SwipeFragment<FragmentSettingsBinding> {

    @Override
    public void initLayout() {
        mLayoutID = R.layout.fragment_settings;
    }

    @Override
    protected void initData() {
        baseBind.toolbar.setNavigationOnClickListener(view -> mActivity.finish());
        Common.animate(baseBind.parentLinear);

        baseBind.loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new QMUIDialog.CheckBoxMessageDialogBuilder(getActivity())
                        .setTitle(getString(R.string.string_185))
                        .setMessage(getString(R.string.string_186))
                        .setChecked(true)
                        .setSkinManager(QMUISkinManager.defaultInstance(mContext))
                        .addAction(getString(R.string.string_187), new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction(R.string.login_out, new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                Common.logOut(mContext);
                                mActivity.finish();
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });

        baseBind.userManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TemplateActivity.class);
                intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "账号管理");
                startActivity(intent);
            }
        });

        baseBind.editAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TemplateActivity.class);
                intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "绑定邮箱");
                startActivity(intent);
            }
        });

        baseBind.editFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TemplateActivity.class);
                intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "编辑个人资料");
                startActivity(intent);
            }
        });

        baseBind.workSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TemplateActivity.class);
                intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "我的作业环境");
                startActivity(intent);
            }
        });

        baseBind.saveHistory.setChecked(Lisa.sSettings.isSaveViewHistory());          //保存历史记录
        baseBind.saveHistory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Lisa.sSettings.setSaveViewHistory(true);
                } else {
                    Lisa.sSettings.setSaveViewHistory(false);
                }
                Common.showToast("设置成功", 2);
                Local.setSettings(Lisa.sSettings);
            }
        });

        baseBind.saveHistoryRela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseBind.saveHistory.performClick();
            }
        });

        baseBind.showLikeButton.setChecked(Lisa.sSettings.isPrivateStar());               //私人收藏
        baseBind.showLikeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Lisa.sSettings.setPrivateStar(isChecked);
                Common.showToast("设置成功", 2);
                Local.setSettings(Lisa.sSettings);
            }
        });
        baseBind.showLikeButtonRela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseBind.showLikeButton.performClick();
            }
        });
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        baseBind.followPrivateButton.setChecked(Lisa.sSettings.isPrivateFollow());               //私人关注
        baseBind.followPrivateButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Lisa.sSettings.setPrivateFollow(isChecked);
                Common.showToast("设置成功", 2);
                Local.setSettings(Lisa.sSettings);
            }
        });
        baseBind.followPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseBind.followPrivateButton.performClick();
            }
        });
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        baseBind.deleteStarIllust.setChecked(Lisa.sSettings.isDeleteStarIllust());
        baseBind.deleteStarIllust.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Lisa.sSettings.setDeleteStarIllust(true);
                } else {
                    Lisa.sSettings.setDeleteStarIllust(false);
                }
                Common.showToast("设置成功", 2);
                Local.setSettings(Lisa.sSettings);
            }
        });
        baseBind.deleteStarIllustRela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseBind.deleteStarIllust.performClick();
            }
        });

        //setOrderName();
       /* baseBind.orderSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index = Lisa.sSettings.getBottomBarOrder();
                String[] ORDER_NAME = new String[]{
                        getString(R.string.string_343),
                        getString(R.string.string_344),
                        getString(R.string.string_345),
                        getString(R.string.string_346),
                        getString(R.string.string_347),
                        getString(R.string.string_348),
                };
                new QMUIDialog.CheckableDialogBuilder(mActivity)
                        .setCheckedIndex(index)
                        .setSkinManager(QMUISkinManager.defaultInstance(mContext))
                        .addItems(ORDER_NAME, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == index) {
                                    Common.showLog("什么也不做");
                                } else {
                                    Lisa.sSettings.setBottomBarOrder(which);
                                    baseBind.orderSelect.setText(ORDER_NAME[which]);
                                    Local.setSettings(Lisa.sSettings);
                                    Common.showToast(getString(R.string.please_restart_app));
                                }
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });*/
        baseBind.bottomBarOrderRela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseBind.orderSelect.performClick();
            }
        });

        baseBind.autoDns.setChecked(Lisa.sSettings.wallChina());
        baseBind.autoDns.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Lisa.sSettings.setAutoWallChina(true);
                } else {
                    Lisa.sSettings.setAutoWallChina(false);
                }
                Common.showToast("设置成功", 2);
                Local.setSettings(Lisa.sSettings);
            }
        });
        baseBind.fuckChinaRela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseBind.autoDns.performClick();
            }
        });



        //是否显示原图
        baseBind.showOriginalImage.setChecked(Lisa.sSettings.isShowOriginalImage());
        baseBind.showOriginalImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Lisa.sSettings.setShowOriginalImage(isChecked);
                Common.showToast("设置成功");
                Local.setSettings(Lisa.sSettings);
            }
        });
        baseBind.showOriginalImageRela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseBind.showOriginalImage.performClick();
            }
        });


        if (Lisa.sSettings.getDownloadWay() == 1) {
            try {
                baseBind.illustPath.setText(URLDecoder.decode(Lisa.sSettings.getRootPathUri(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            baseBind.illustPath.setText(Lisa.sSettings.getIllustPath());
        }
        baseBind.singleIllustPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Lisa.sSettings.getDownloadWay() == 0) {
                    Common.showToast(getString(R.string.string_329), true);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    if (!TextUtils.isEmpty(Lisa.sSettings.getRootPathUri()) &&
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Uri start = Uri.parse(Lisa.sSettings.getRootPathUri());
                        intent.putExtra(EXTRA_INITIAL_URI, start);
                    }
                    mActivity.startActivityForResult(intent, BaseActivity.ASK_URI);
                }
            }
        });

        final String searchFilter = Lisa.sSettings.getSearchFilter();
        baseBind.searchFilter.setText(PixivSearchParamUtil.getSizeName(searchFilter));
        baseBind.searchFilterRela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new QMUIDialog.CheckableDialogBuilder(mContext)
                        .setCheckedIndex(PixivSearchParamUtil.getSizeIndex(searchFilter))
                        .setSkinManager(QMUISkinManager.defaultInstance(mContext))
                        .addItems(PixivSearchParamUtil.ALL_SIZE_NAME, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Lisa.sSettings.setSearchFilter(PixivSearchParamUtil.ALL_SIZE_VALUE[which]);
                                Common.showToast("设置成功", 2);
                                Local.setSettings(Lisa.sSettings);
                                baseBind.searchFilter.setText(PixivSearchParamUtil.ALL_SIZE_NAME[which]);
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
        baseBind.appLanguage.setText(Lisa.sSettings.getAppLanguage());
        baseBind.appLanguageRela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new QMUIDialog.CheckableDialogBuilder(getActivity())
                        .addItems(ALL_LANGUAGE, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Lisa.sSettings.setAppLanguage(ALL_LANGUAGE[which]);
                                baseBind.appLanguage.setText(ALL_LANGUAGE[which]);
                                Common.showToast("设置成功", 2);
                                Local.setSettings(Lisa.sSettings);
                                if (which == 0) {
                                    LanguageUtils.applyLanguage(Locale.SIMPLIFIED_CHINESE, "");
                                }  else if (which == 1) {
                                    LanguageUtils.applyLanguage(Locale.US, "");
                                }
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });



        baseBind.themeMode.setText(Lisa.sSettings.getThemeType());
        baseBind.themeModeRela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index = ThemeHelper.getThemeType(mContext);
                String[] THEME_NAME = new String[]{
                        getString(R.string.string_298),
                        getString(R.string.string_299),
                        getString(R.string.string_300)
                };
                new QMUIDialog.CheckableDialogBuilder(mActivity)
                        .setCheckedIndex(index)
                        .setSkinManager(QMUISkinManager.defaultInstance(mContext))
                        .addItems(THEME_NAME, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == index) {
                                    Common.showLog("什么也不做");
                                } else {
                                    Lisa.sSettings.setThemeType(((AppCompatActivity) mActivity), THEME_NAME[which]);
                                    baseBind.themeMode.setText(THEME_NAME[which]);
                                    Local.setSettings(Lisa.sSettings);
                                }
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });


        String[] downloadWays = new String[]{
                getString(R.string.string_363),
                getString(R.string.string_364)
        };
        baseBind.downloadWay.setText(downloadWays[Lisa.sSettings.getDownloadWay()]);
        baseBind.downloadWayRela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new QMUIDialog.CheckableDialogBuilder(mActivity)
                        .setCheckedIndex(Lisa.sSettings.getDownloadWay())
                        .setSkinManager(QMUISkinManager.defaultInstance(mContext))
                        .addItems(downloadWays, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == Lisa.sSettings.getDownloadWay()) {
                                    Common.showLog("什么也不做");
                                } else {
                                    Lisa.sSettings.setDownloadWay(which);
                                    baseBind.downloadWay.setText(downloadWays[which]);
                                    Local.setSettings(Lisa.sSettings);
                                }
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

       // baseBind.lineCount.setText(getString(R.string.string_349, Lisa.sSettings.getLineCount()));
       /* baseBind.lineCountRela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = 0;
                if (Lisa.sSettings.getLineCount() == 3) {
                    index = 1;
                } else if (Lisa.sSettings.getLineCount() == 4) {
                    index = 2;
                }
                String[] LINE_COUNT = new String[]{
                        getString(R.string.string_349, 2),
                        getString(R.string.string_349, 3),
                        getString(R.string.string_349, 4)
                };
                final int selectIndex = index;
                new QMUIDialog.CheckableDialogBuilder(mActivity)
                        .setCheckedIndex(index)
                        .setSkinManager(QMUISkinManager.defaultInstance(mContext))
                        .addItems(LINE_COUNT, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == selectIndex) {
                                    Common.showLog("什么也不做");
                                } else {
                                    int lineCount = which + 2;
                                    Lisa.sSettings.setLineCount(lineCount);
                                    baseBind.lineCount.setText(getString(R.string.string_349, lineCount));
                                    Local.setSettings(Lisa.sSettings);
                                    Common.showToast(getString(R.string.please_restart_app), 2);
                                }
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });*/


        setThemeName();
        baseBind.colorSelectRela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TemplateActivity.class);
                intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "主题颜色");
                startActivity(intent);
            }
        });

        baseBind.imageCacheSize.setText(FileUtils.getSize(new LegacyFile().imageCacheFolder(mContext)));
        baseBind.clearImageCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtils.deleteAllInDir(new LegacyFile().imageCacheFolder(mContext));
                Common.showToast("图片缓存清除成功！");
                baseBind.imageCacheSize.setText(FileUtils.getSize(new LegacyFile().imageCacheFolder(mContext)));
            }
        });
        baseBind.refreshLayout.setRefreshHeader(new FalsifyHeader(mContext));
        baseBind.refreshLayout.setRefreshFooter(new FalsifyFooter(mContext));
    }

    @Override
    public SmartRefreshLayout getSmartRefreshLayout() {
        return baseBind.refreshLayout;
    }

   /* private void setOrderName() {
        final int index = Lisa.sSettings.getBottomBarOrder();
        String[] ORDER_NAME = new String[]{
                getString(R.string.string_343),
                getString(R.string.string_344),
                getString(R.string.string_345),
                getString(R.string.string_346),
                getString(R.string.string_347),
                getString(R.string.string_348),
        };
        baseBind.orderSelect.setText(ORDER_NAME[index]);
    }*/

    private void setThemeName() {
        final int index = Lisa.sSettings.getThemeIndex();
        baseBind.colorSelect.setText(FragmentColors.COLOR_NAMES[index]);
    }
}
