package sckdn.lisa.utils;

import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.PathUtils;

import sckdn.lisa.helper.ThemeHelper;

public class Settings {

    public static final String[] ALL_LANGUAGE = new String[]{"简体中文"};

    //只包含1P图片的下载路径
    public static final String FILE_PATH_SINGLE = PathUtils.getExternalPicturesPath() + "/LisaImages";

    //log日志，
    public static final String FILE_LOG_PATH = PathUtils.getExternalDownloadsPath() + "/LisaFiles";

    //WEB下载
    public static final String WEB_DOWNLOAD_PATH = PathUtils.getExternalPicturesPath() + "/LisaWeb";

    private int themeIndex;

    private int lineCount = 2;

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    public int getThemeIndex() {
        return themeIndex;
    }

    public void setThemeIndex(int themeIndex) {
        this.themeIndex = themeIndex;
    }

    private long currentProgress = 0L;

    public void setCurrentProgress(long currentProgress) {
        this.currentProgress = currentProgress;
    }

    private boolean trendsForPrivate = false;

    //浏览历史List点击动画
    private boolean viewHistoryAnimate = true;

    //设置页面进场动画
    private boolean settingsAnimate = true;

    //屏蔽，不显示已收藏的作品，默认不屏蔽
    private boolean deleteStarIllust = false;

    //是否自动添加DNS，true开启直连  false自行代理
    private boolean autoWallChina = true;

    private boolean relatedIllustNoLimit = true;

    //使用pixiv cat 代理 展示图片
    private boolean usePixivCat = false;

    //二级详情FragmentImageDetail 图片显示原图
    private boolean showOriginalImage = false;

    //是否显示开屏 dialog
    private boolean showPixivDialog = true;

    //默认私人收藏
    private boolean privateStar = false;

    //默认私人关注
    private boolean privateFollow = false;

    //列表页面是否显示收藏按钮
    private boolean showLikeButton = true;

    //直接下载单个作品所有P
    private boolean directDownloadAllImage = true;

    private boolean saveViewHistory = true;

    private boolean r18DivideSave = false;

    //单P作品的文件名是否带P0
    private boolean hasP0 = false;

    //作品详情使用新页面
    private boolean useFragmentIllust = true;

    //个人中心使用新页面
    private boolean useNewUserPage = true;

    private String illustPath = "";

    private String novelPath = "";

    private String gifResultPath = "";

    private String gifZipPath = "";

    private String gifUnzipPath = "";

    private String webDownloadPath = "";

    private int novelHolderColor = 0;

    private int bottomBarOrder = 0;

    private boolean reverseDialogNeverShowAgain = false;

    private String appLanguage = "";

    private String fileNameJson = "";

    private String rootPathUri = "";

    private int downloadWay = 0; //0传统模式，保存到Pictures目录下。    1 SAF模式保存到自选目录下

    public String getAppLanguage() {
        if(!TextUtils.isEmpty(appLanguage)){
            return appLanguage;
        } else {
            return ALL_LANGUAGE[0];
        }
    }

    public int getDownloadWay() {
        return downloadWay;
    }

    public void setDownloadWay(int downloadWay) {
        this.downloadWay = downloadWay;
    }

   public boolean isR18DivideSave() {
        return r18DivideSave;
    }

   public void setR18DivideSave(boolean r18DivideSave) {
        this.r18DivideSave = r18DivideSave;
    }

    public String getRootPathUri() {
        return rootPathUri;
    }

    public void setRootPathUri(String rootPathUri) {
        this.rootPathUri = rootPathUri;
    }

    public boolean isPrivateStar() {
        return privateStar;
    }

    public void setPrivateStar(boolean privateStar) {
        this.privateStar = privateStar;
    }

    public boolean isPrivateFollow(){
        return privateFollow;
    }

    public void setPrivateFollow(boolean privateFollow){
        this.privateFollow = privateFollow;
    }

    public void setAppLanguage(String appLanguage) {
        this.appLanguage = appLanguage;
    }

    public String getThemeType() {
        if (TextUtils.isEmpty(themeType)) {
            return ThemeHelper.DEFAULT_MODE;
        }
        return themeType;
    }



    public void setThemeType(AppCompatActivity activity, String themeType) {
        this.themeType = themeType;
        ThemeHelper.applyTheme(activity, themeType);
    }

    public boolean isDeleteStarIllust() {
        return deleteStarIllust;
    }

    public void setDeleteStarIllust(boolean pDeleteStarIllust) {
        deleteStarIllust = pDeleteStarIllust;
    }


    private String themeType = "";

    //收藏量筛选搜索结果
    private String searchFilter = "";

    public Settings() {
    }

    public boolean isSaveViewHistory() {
        return saveViewHistory;
    }

    public void setSaveViewHistory(boolean saveViewHistory) {
        this.saveViewHistory = saveViewHistory;
    }

    public String getSearchFilter() {
        return TextUtils.isEmpty(searchFilter) ? "" : searchFilter;
    }



    public void setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
    }

    public boolean isRelatedIllustNoLimit() {
        return relatedIllustNoLimit;
    }



    public boolean wallChina() {
        return autoWallChina;
    }

    public void setAutoWallChina(boolean autoWallChina) {
        this.autoWallChina = autoWallChina;
    }


    public boolean isUseFragmentIllust() {
        return useFragmentIllust;
    }

    public void setUseFragmentIllust(boolean useFragmentIllust) {
        this.useFragmentIllust = useFragmentIllust;
    }

    public boolean isViewHistoryAnimate() {
        return viewHistoryAnimate;
    }

    public void setViewHistoryAnimate(boolean viewHistoryAnimate) {
        this.viewHistoryAnimate = viewHistoryAnimate;
    }

    public boolean isSettingsAnimate() {
        return settingsAnimate;
    }

    public void setSettingsAnimate(boolean settingsAnimate) {
        this.settingsAnimate = settingsAnimate;
    }

    public boolean isShowOriginalImage() {
        return showOriginalImage;
    }

    public void setShowOriginalImage(boolean showOriginalImage) {
        this.showOriginalImage = showOriginalImage;
    }

    public boolean isDirectDownloadAllImage() {
        return directDownloadAllImage;
    }

    public void setDirectDownloadAllImage(boolean directDownloadAllImage) {
        this.directDownloadAllImage = directDownloadAllImage;
    }

    public String getIllustPath() {
        return TextUtils.isEmpty(illustPath) ? FILE_PATH_SINGLE : illustPath;
    }

    public void setIllustPath(String illustPath) {
        this.illustPath = illustPath;
    }



    public void setGifResultPath(String gifResultPath) {
        this.gifResultPath = gifResultPath;
    }



    public void setGifZipPath(String gifZipPath) {
        this.gifZipPath = gifZipPath;
    }



    public void setGifUnzipPath(String gifUnzipPath) {
        this.gifUnzipPath = gifUnzipPath;
    }

    public String getWebDownloadPath() {
        return TextUtils.isEmpty(webDownloadPath) ? WEB_DOWNLOAD_PATH : "webDownloadPath";
    }

    public void setWebDownloadPath(String webDownloadPath) {
        this.webDownloadPath = webDownloadPath;
    }

    public boolean isTrendsForPrivate() {
        return trendsForPrivate;
    }

    public void setTrendsForPrivate(boolean trendsForPrivate) {
        this.trendsForPrivate = trendsForPrivate;
    }

    public boolean isShowPixivDialog() {
        return showPixivDialog;
    }

    public void setShowPixivDialog(boolean showPixivDialog) {
        this.showPixivDialog = showPixivDialog;
    }

    public boolean isReverseDialogNeverShowAgain() {
        return reverseDialogNeverShowAgain;
    }

    public void setReverseDialogNeverShowAgain(boolean reverseDialogNeverShowAgain) {
        this.reverseDialogNeverShowAgain = reverseDialogNeverShowAgain;
    }

    public boolean isShowLikeButton() {
        return showLikeButton;
    }

    public void setShowLikeButton(boolean pShowLikeButton) {
        showLikeButton = pShowLikeButton;
    }

    public String getFileNameJson() {
        return fileNameJson;
    }

    public void setFileNameJson(String fileNameJson) {
        this.fileNameJson = fileNameJson;
    }

    public boolean isHasP0() {
        return hasP0;
    }

    public void setHasP0(boolean hasP0) {
        this.hasP0 = hasP0;
    }


    public int getBottomBarOrder() {
        return bottomBarOrder;
    }

    public void setBottomBarOrder(int bottomBarOrder) {
        this.bottomBarOrder = bottomBarOrder;
    }

    public boolean isUseNewUserPage() {
        return useNewUserPage;
    }

    public void setUseNewUserPage(boolean useNewUserPage) {
        this.useNewUserPage = useNewUserPage;
    }
}
