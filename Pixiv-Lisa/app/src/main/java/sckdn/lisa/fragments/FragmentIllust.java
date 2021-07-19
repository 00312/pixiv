package sckdn.lisa.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import sckdn.lisa.R;
import sckdn.lisa.activities.BaseActivity;
import sckdn.lisa.activities.SearchActivity;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.activities.TemplateActivity;
import sckdn.lisa.activities.UserActivity;
import sckdn.lisa.adapters.IllustAdapter;
import sckdn.lisa.databinding.FragmentIllustBinding;
import sckdn.lisa.dialogs.MuteDialog;
//import ceui.lisa.file.SAFile;
import sckdn.lisa.download.FileCreator;
import sckdn.lisa.download.IllustDownload;
import sckdn.lisa.model.IllustsBean;
import sckdn.lisa.model.TagsBean;
import sckdn.lisa.notification.BaseReceiver;
import sckdn.lisa.notification.CallBackReceiver;
import sckdn.lisa.utils.Common;
import sckdn.lisa.utils.DensityUtil;
import sckdn.lisa.utils.GlideUtil;
import sckdn.lisa.utils.Params;
import sckdn.lisa.utils.PixivOperate;
import sckdn.lisa.utils.ShareIllust;

import static sckdn.lisa.utils.ShareIllust.URL_Head;


public class FragmentIllust extends SwipeFragment<FragmentIllustBinding> {

    private IllustsBean illust;

    public static FragmentIllust newInstance(IllustsBean illustsBean) {
        Bundle args = new Bundle();
        args.putSerializable(Params.CONTENT, illustsBean);
        FragmentIllust fragment = new FragmentIllust();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initBundle(Bundle bundle) {
        illust = (IllustsBean) bundle.getSerializable(Params.CONTENT);
    }

    @Override
    public void initLayout() {
        mLayoutID = R.layout.fragment_illust;
    }

    @Override
    protected void initView() {
        if (illust.getId() == 0 || !illust.isVisible()) {
            Common.showToast(R.string.string_206);
            finish();
            return;
        }

        if (illust.getSeries() != null && !TextUtils.isEmpty(illust.getSeries().getTitle())) {
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(mContext, TemplateActivity.class);
                    intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "漫画系列详情");
                    intent.putExtra(Params.ID, illust.getSeries().getId());
                    startActivity(intent);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(Common.resolveThemeAttribute(mContext, R.attr.colorPrimary));
                }
            };
            SpannableString spannableString;
            spannableString = new SpannableString(String.format("@%s %s",
                    illust.getTitle(), getString(R.string.string_229)));
            spannableString.setSpan(clickableSpan,
                    illust.getTitle().length() + 2, illust.getTitle().length() + 4,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            baseBind.title.setMovementMethod(LinkMovementMethod.getInstance());
            baseBind.title.setText(spannableString);
        } else {
            baseBind.title.setText(illust.getTitle());
        }
       baseBind.title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Common.copy(mContext, illust.getTitle());
                return true;
            }
        });
        baseBind.toolbar.inflateMenu(R.menu.share);
        baseBind.toolbar.setNavigationOnClickListener(v -> mActivity.finish());
        baseBind.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_share) {
                    new ShareIllust(mContext, illust) {
                        @Override
                        public void onPrepare() {

                        }
                    }.execute();
                    return true;
                } else if (menuItem.getItemId() == R.id.action_dislike) {
                    MuteDialog muteDialog = MuteDialog.newInstance(illust);
                    muteDialog.show(getChildFragmentManager(), "MuteDialog");
                } else if (menuItem.getItemId() == R.id.action_copy_link) {
                    String url = URL_Head + illust.getId();
                    Common.copy(mContext, url);
                } else if (menuItem.getItemId() == R.id.action_show_original) {
                    baseBind.recyclerView.setAdapter(new IllustAdapter(mContext, illust,
                            recyHeight, true));
                } else if (menuItem.getItemId() == R.id.action_mute_illust) {
                    PixivOperate.muteIllust(illust);
                }
                return false;
            }
        });
        if (illust.isIs_bookmarked()) {
            baseBind.postLike.setImageResource(R.drawable.ic_favorite_red_24dp);
        } else {
            baseBind.postLike.setImageResource(R.drawable.ic_favorite_grey_24dp);
        }
        baseBind.postLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (illust.isIs_bookmarked()) {
                    baseBind.postLike.setImageResource(R.drawable.ic_favorite_grey_24dp);
                } else {
                    baseBind.postLike.setImageResource(R.drawable.ic_favorite_red_24dp);
                }
                if (Lisa.sSettings.isPrivateStar()) {
                    PixivOperate.postLike(illust, Params.TYPE_PRIVATE);
                } else {
                    PixivOperate.postLike(illust, Params.TYPE_PUBLUC);
                }
            }
        });
        baseBind.illustTag.setAdapter(new TagAdapter<TagsBean>(illust.getTags()) {
            @Override
            public View getView(FlowLayout parent, int position, TagsBean s) {
                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.recy_single_line_text_new,
                        parent, false);
                String tag = s.getName();
                if (!TextUtils.isEmpty(s.getTranslated_name())) {
                    tag = tag + "/" + s.getTranslated_name();
                }
                tv.setText(tag);
                return tv;
            }
        });
        baseBind.illustTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Intent intent = new Intent(mContext, SearchActivity.class);
                intent.putExtra(Params.KEY_WORD, illust.getTags().get(position).getName());
                intent.putExtra(Params.INDEX, 0);
                startActivity(intent);
                return true;
            }
        });

        baseBind.illustSize.setText(getString(R.string.string_193, illust.getWidth(), illust.getHeight()));
        baseBind.illustId.setText(getString(R.string.string_194, illust.getId()));
        baseBind.userId.setText(getString(R.string.string_195, illust.getUser().getId()));

        final BottomSheetBehavior<?> sheetBehavior = BottomSheetBehavior.from(baseBind.coreLinear);

        baseBind.coreLinear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int realHeight = baseBind.bottomBar.getHeight() +
                        baseBind.viewDivider.getHeight() +
                        baseBind.secondLinear.getHeight();
                final int maxHeight = getResources().getDisplayMetrics().heightPixels * 3 / 4;
                ViewGroup.LayoutParams params = baseBind.coreLinear.getLayoutParams();
                int slideMaxHeight = Math.min(realHeight, maxHeight);
                params.height = slideMaxHeight;
                baseBind.coreLinear.setLayoutParams(params);

                final int bottomCardHeight = baseBind.bottomBar.getHeight();
                final int deltaY = slideMaxHeight - baseBind.bottomBar.getHeight();
                sheetBehavior.setPeekHeight(bottomCardHeight, true);
                baseBind.refreshLayout.setPadding(0, 0, 0, bottomCardHeight - DensityUtil.dp2px(16.0f));
                sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {

                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        baseBind.refreshLayout.setTranslationY(-deltaY * slideOffset * 0.7f);
                    }
                });

                baseBind.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                recyHeight = baseBind.recyclerView.getHeight() - bottomCardHeight + DensityUtil.dp2px(16.0f);
                baseBind.recyclerView.setAdapter(new IllustAdapter(mContext, illust, recyHeight));
                baseBind.coreLinear.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        baseBind.related.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TemplateActivity.class);
                intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "相关作品");
                intent.putExtra(Params.ILLUST_ID, illust.getId());
                intent.putExtra(Params.ILLUST_TITLE, illust.getTitle());
                startActivity(intent);
            }
        });
        baseBind.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TemplateActivity.class);
                intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "相关评论");
                intent.putExtra(Params.ILLUST_ID, illust.getId());
                intent.putExtra(Params.ILLUST_TITLE, illust.getTitle());
                startActivity(intent);
            }
        });
        baseBind.illustLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TemplateActivity.class);
                intent.putExtra(Params.CONTENT, illust);
                intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "喜欢这个作品的用户");
                startActivity(intent);
            }
        });
        if (!TextUtils.isEmpty(illust.getCaption())) {
            baseBind.description.setVisibility(View.VISIBLE);
            baseBind.description.setHtml(illust.getCaption());
        } else {
            baseBind.description.setVisibility(View.GONE);
        }
        baseBind.userHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserActivity.class);
                intent.putExtra(Params.USER_ID, illust.getUser().getId());
                startActivity(intent);
            }
        });
        baseBind.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserActivity.class);
                intent.putExtra(Params.USER_ID, illust.getUser().getId());
                startActivity(intent);
            }
        });
        if (illust.getUser().isIs_followed()) {
            baseBind.follow.setText(R.string.string_177);
        } else {
            baseBind.follow.setText(R.string.string_178);
        }
        baseBind.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (illust.getUser().isIs_followed()) {
                    baseBind.follow.setText(R.string.string_178);
                    PixivOperate.postUnFollowUser(illust.getUser().getId());
                    illust.getUser().setIs_followed(false);
                } else if(Lisa.sSettings.isPrivateFollow()) {
                    baseBind.follow.setText(R.string.string_177);
                    PixivOperate.postFollowUser(illust.getUser().getId(), Params.TYPE_PRIVATE);
                    illust.getUser().setIs_followed(true);
                } else {
                    baseBind.follow.setText(R.string.string_177);
                    PixivOperate.postFollowUser(illust.getUser().getId(), Params.TYPE_PUBLUC);
                    illust.getUser().setIs_followed(true);
                }

            }
        });
        baseBind.userName.setText(illust.getUser().getName());
        baseBind.postTime.setText(String.format("%s投递", illust.getCreate_date().substring(0, 16)));
        baseBind.totalView.setText(String.valueOf(illust.getTotal_view()));
        baseBind.totalLike.setText(String.valueOf(illust.getTotal_bookmarks()));
        baseBind.download.setChangeAlphaWhenPress(true);
        baseBind.related.setChangeAlphaWhenPress(true);
        baseBind.comment.setChangeAlphaWhenPress(true);
        baseBind.download.setOnClickListener(v -> {
            if (illust.getPage_count() == 1) {
                IllustDownload.downloadIllust(illust, (BaseActivity<?>) mContext);
            } else {
               // IllustDownload.downloadAllIllust(illust, (BaseActivity<?>) mContext);
            }
            checkDownload();
        });


        baseBind.download.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (illust.getPage_count() == 1) {
                    String[] IMG_RESOLUTION = new String[]{
                            getString(R.string.string_280),
                            getString(R.string.string_281),
                            getString(R.string.string_282),
                            getString(R.string.string_283)
                    };
                    new QMUIDialog.CheckableDialogBuilder(mContext)
                            .setSkinManager(QMUISkinManager.defaultInstance(mContext))
                            .addItems(IMG_RESOLUTION, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create()
                            .show();
                    return true;
                }
                return false;
            }
        });
        baseBind.illustId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.copy(mContext, String.valueOf(illust.getId()));
            }
        });
        baseBind.userId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.copy(mContext, String.valueOf(illust.getUser().getId()));
            }
        });
        Glide.with(mContext)
                .load(GlideUtil.getUrl(illust.getUser().getProfile_image_urls().getMedium()))
                .into(baseBind.userHead);
    }

    private CallBackReceiver mReceiver;

    @Override
    public void onResume() {
        super.onResume();
        checkDownload();
    }

    private int recyHeight = 0;

    private void checkDownload() {
        //只有1P的作品才检查是否下载过
        if (illust.getPage_count() == 1) {
            if (Lisa.sSettings.getDownloadWay() == 1) {
             //   String displayName = FileCreator.customFileName(illust, 0);
                if (FileCreator.isExist(illust, 0)){
                    baseBind.download.setText(R.string.string_337);
                } {
                    baseBind.download.setText(R.string.string_72);
                }
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        mReceiver = new CallBackReceiver(new BaseReceiver.CallBack() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    int id = bundle.getInt(Params.ID);
                    if (illust.getId() == id) {
                        boolean isLiked = bundle.getBoolean(Params.IS_LIKED);
                        if (isLiked) {
                            illust.setIs_bookmarked(true);
                            baseBind.postLike.setImageResource(R.drawable.ic_favorite_red_24dp);
                            int beforeStarCount = illust.getTotal_bookmarks();
                            int afterStarCount = beforeStarCount + 1;
                            illust.setTotal_bookmarks(afterStarCount);
                            baseBind.totalLike.setText(String.valueOf(afterStarCount));
                        } else {
                            illust.setIs_bookmarked(false);
                            baseBind.postLike.setImageResource(R.drawable.ic_favorite_grey_24dp);
                            int beforeStarCount = illust.getTotal_bookmarks();
                            int afterStarCount = beforeStarCount - 1;
                            illust.setTotal_bookmarks(afterStarCount);
                            baseBind.totalLike.setText(String.valueOf(afterStarCount));
                        }
                    }
                }
            }
        });
        intentFilter.addAction(Params.LIKED_ILLUST);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
        }
        if (baseBind.recyclerView != null) {
            baseBind.recyclerView.setAdapter(null);
        }
        super.onDestroy();
    }

    @Override
    public void vertical() {
        //竖屏
        baseBind.toolbar.setPadding(0, Lisa.statusHeight, 0, 0);
    }

    @Override
    public SmartRefreshLayout getSmartRefreshLayout() {
        return baseBind.refreshLayout;
    }
}
