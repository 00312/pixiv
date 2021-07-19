package sckdn.lisa.http;

import java.util.HashMap;
import java.util.List;

import sckdn.lisa.model.ListBookmarkTag;
import sckdn.lisa.model.ListComment;
import sckdn.lisa.model.ListIllust;
import sckdn.lisa.model.ListMangaOfSeries;
import sckdn.lisa.model.ListMangaSeries;
import sckdn.lisa.model.ListSimpleUser;
import sckdn.lisa.model.ListTag;
import sckdn.lisa.model.ListTrendingtag;
import sckdn.lisa.model.ListUser;
import sckdn.lisa.model.RecmdIllust;
import sckdn.lisa.model.CommentHolder;
import sckdn.lisa.model.IllustSearchResponse;
import sckdn.lisa.model.MutedHistory;
import sckdn.lisa.model.NullResponse;
import sckdn.lisa.model.Preset;
import sckdn.lisa.model.UserDetailResponse;
import sckdn.lisa.model.UserState;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface AppApi {

    //用作各个页面请求数据
    String API_BASE_URL = "https://app-api.pixiv.net/";

    /**
     * 获取排行榜
     *
     * @param mode
     * @return
     */
    @GET("v1/illust/ranking?filter=for_android")
    Observable<ListIllust> getRank(@Header("Authorization") String token,
                                   @Query("mode") String mode,
                                   @Query("date") String date);


    /**
     * 推荐榜单
     *
     * @param token
     * @return
     */
    @GET("v1/illust/recommended?include_privacy_policy=true&filter=for_android&include_ranking_illusts=true")
    Observable<RecmdIllust> getRecmdIllust(@Header("Authorization") String token);


    @GET("v1/manga/recommended?include_privacy_policy=true&filter=for_android&include_ranking_illusts=true")
    Observable<RecmdIllust> getRecmdManga(@Header("Authorization") String token);


    @GET("v1/trending-tags/{type}?filter=for_android&include_translated_tag_results=true")
    Observable<ListTrendingtag> getHotTags(@Header("Authorization") String token,
                                           @Path("type") String type);


    /**
     * 原版app登录时候的背景墙
     *
     * @param token
     * @return
     */
    @GET("v1/walkthrough/illusts?filter=for_android")
    Observable<ListIllust> getLoginBg(@Header("Authorization") String token);


    /**
     * /v1/search/illust?
     * filter=for_android&
     * include_translated_tag_results=true&
     * word=%E8%89%A6%E9%9A%8A%E3%81%93%E3%82%8C%E3%81%8F%E3%81%97%E3%82%87%E3%82%93&
     * sort=date_desc& 最新
     * sort=date_asc& 旧的在前面
     * search_target=exact_match_for_tags 标签完全匹配
     * search_target=partial_match_for_tags 标签部分匹配
     * search_target=title_and_caption 标题或简介
     */
    @GET("v1/search/illust?filter=for_android&include_translated_tag_results=true")
    Observable<ListIllust> searchIllust(@Header("Authorization") String token,
                                        @Query("word") String word,
                                        @Query("sort") String sort,
                                        @Query("search_target") String search_target);




    @GET("v2/illust/related?filter=for_android")
    Observable<ListIllust> relatedIllust(@Header("Authorization") String token,
                                         @Query("illust_id") int illust_id);


    /**
     * 推荐用户
     *
     * @param token
     * @return
     */
    @GET("v1/user/recommended?filter=for_android")
    Observable<ListUser> getRecmdUser(@Header("Authorization") String token);


    @GET("v1/user/bookmarks/illust")
    Observable<ListIllust> getUserLikeIllust(@Header("Authorization") String token,
                                             @Query("user_id") int user_id,
                                             @Query("restrict") String restrict,
                                             @Query("tag") String tag);

    @GET("v1/user/bookmarks/illust")
    Observable<ListIllust> getUserLikeIllust(@Header("Authorization") String token,
                                             @Query("user_id") int user_id,
                                             @Query("restrict") String restrict);



    @GET("v1/user/illusts?filter=for_android")
    Observable<ListIllust> getUserSubmitIllust(@Header("Authorization") String token,
                                               @Query("user_id") int user_id,
                                               @Query("type") String type);




    @GET("v2/illust/follow")
    Observable<ListIllust> getFollowUserIllust(@Header("Authorization") String token,
                                               @Query("restrict") String restrict);


   /* @GET("v1/spotlight/articles?filter=for_android")    //特辑文章列表
    Observable<ListArticle> getArticles(@Header("Authorization") String token,
                                        @Query("category") String category);*/


    ///v1/user/detail?filter=for_android&user_id=24218478
    @GET("v1/user/detail?filter=for_android")
    Observable<UserDetailResponse> getUserDetail(@Header("Authorization") String token,
                                                 @Query("user_id") int user_id);



    @FormUrlEncoded
    @POST("v1/user/follow/add")
    Observable<NullResponse> postFollow(@Header("Authorization") String token,
                                        @Field("user_id") int user_id,
                                        @Field("restrict") String followType);

    @FormUrlEncoded
    @POST("v1/user/follow/delete")
    Observable<NullResponse> postUnFollow(@Header("Authorization") String token,
                                          @Field("user_id") int user_id);


    /**
     * 获取userid 所关注的人
     *
     * @param token
     * @param user_id
     * @param restrict
     * @return
     */
    @GET("v1/user/following?filter=for_android")
    Observable<ListUser> getFollowUser(@Header("Authorization") String token,
                                       @Query("user_id") int user_id,
                                       @Query("restrict") String restrict);


    //获取关注 这个userid 的人
   /* @GET("v1/user/follower?filter=for_android")
    Observable<ListUser> getWhoFollowThisUser(@Header("Authorization") String token,
                                              @Query("user_id") int user_id);*/


    @GET("v1/illust/comments")
    Observable<ListComment> getComment(@Header("Authorization") String token,
                                       @Query("illust_id") int illust_id);


    @GET
    Observable<ListComment> getNextComment(@Header("Authorization") String token,
                                           @Url String nextUrl);


    @FormUrlEncoded
    @POST("v1/illust/comment/add")
    Observable<CommentHolder> postComment(@Header("Authorization") String token,
                                          @Field("illust_id") int illust_id,
                                          @Field("comment") String comment);

    @FormUrlEncoded
    @POST("v1/illust/comment/add")
    Observable<CommentHolder> postComment(@Header("Authorization") String token,
                                          @Field("illust_id") int illust_id,
                                          @Field("comment") String comment,
                                          @Field("parent_comment_id") int parent_comment_id);

    @FormUrlEncoded
    @POST("v2/illust/bookmark/add")
    Observable<NullResponse> postLike(@Header("Authorization") String token,
                                      @Field("illust_id") int illust_id,
                                      @Field("restrict") String restrict);


    @FormUrlEncoded
    @POST("v2/illust/bookmark/add")
    Observable<NullResponse> postLike(@Header("Authorization") String token,
                                      @Field("illust_id") int illust_id,
                                      @Field("restrict") String restrict,
                                      @Field("tags[]") String... tags);

    @FormUrlEncoded
    @POST("v1/illust/bookmark/delete")
    Observable<NullResponse> postDislike(@Header("Authorization") String token,
                                         @Field("illust_id") int illust_id);




    @GET("v1/illust/detail?filter=for_android")
    Observable<IllustSearchResponse> getIllustByID(@Header("Authorization") String token,
                                                   @Query("illust_id") int illust_id);


    @GET("v1/search/user?filter=for_android")
    Observable<ListUser> searchUser(@Header("Authorization") String token,
                                    @Query("word") String word);


    @GET("v1/search/popular-preview/illust?filter=for_android&include_translated_tag_results=true&merge_plain_keyword_results=true&search_target=exact_match_for_tags")
    Observable<ListIllust> popularPreview(@Header("Authorization") String token,
                                          @Query("word") String word);



    // v2/search/autocomplete?merge_plain_keyword_results=true&word=%E5%A5%B3%E4%BD%93 HTTP/1.1
    @GET("v2/search/autocomplete?merge_plain_keyword_results=true")
    Observable<ListTrendingtag> searchCompleteWord(@Header("Authorization") String token,
                                                   @Query("word") String word);


    /**
     * 获取收藏的标签
     */
    //GET v1/user/bookmark-tags/illust?user_id=41531382&restrict=public HTTP/1.1
    @GET("v1/user/bookmark-tags/illust")
    Observable<ListTag> getBookmarkTags(@Header("Authorization") String token,
                                        @Query("user_id") int user_id,
                                        @Query("restrict") String restrict);


    @GET
    Observable<ListTag> getNextTags(@Header("Authorization") String token,
                                    @Url String nextUrl);


    // GET v2/illust/bookmark/detail?illust_id=72878894 HTTP/1.1


    @GET("v2/illust/bookmark/detail")
    Observable<ListBookmarkTag> getIllustBookmarkTags(@Header("Authorization") String token,
                                                      @Query("illust_id") int illust_id);


    /**
     * 获取已屏蔽的标签/用户
     * <p>
     * 这功能感觉做了没啥卵用，因为未开会员的用户只能屏蔽一个标签/用户，
     * <p>
     * 你屏蔽了一个用户，就不能再屏蔽标签，屏蔽了标签，就不能屏蔽用户，而且都只能屏蔽一个，擦
     *
     * @param token
     * @return
     */
    @GET("v1/mute/list")
    Observable<MutedHistory> getMutedHistory(@Header("Authorization") String token);


    //获取好P友
    @GET("v1/user/mypixiv?filter=for_android")
    Observable<ListUser> getNiceFriend(@Header("Authorization") String token,
                                       @Query("user_id") int user_id);

    //获取最新作品
    @GET("v1/illust/new?filter=for_android")
    Observable<ListIllust> getNewWorks(@Header("Authorization") String token,
                                       @Query("content_type") String content_type);


    //获取好P友
    @GET("v1/user/me/state")
    Observable<UserState> getAccountState(@Header("Authorization") String token);

    @Multipart
    @POST("v1/user/profile/edit")
    Observable<NullResponse> updateUserProfile(@Header("Authorization") String token,
                                               @Part List<MultipartBody.Part> parts);



    @GET("v1/illust/bookmark/users?filter=for_android")
    Observable<ListSimpleUser> getUsersWhoLikeThisIllust(@Header("Authorization") String token,
                                                         @Query("illust_id") int illust_id);



    @GET("v1/illust/series?filter=for_android")
    Observable<ListMangaOfSeries> getMangaSeriesById(@Header("Authorization") String token,
                                                     @Query("illust_series_id") int illust_series_id);


    @GET("v1/user/illust-series")
    Observable<ListMangaSeries> getUserMangaSeries(@Header("Authorization") String token,
                                                   @Query("user_id") int user_id);


    @FormUrlEncoded
    @POST("v1/user/workspace/edit")
    Observable<NullResponse> editWorkSpace(@Header("Authorization") String token,
                                           @FieldMap HashMap<String, String> fields);


    @GET("v1/user/profile/presets")
    Observable<Preset> getPresets(@Header("Authorization") String token);



    @GET
    Observable<ListMangaSeries> getNextUserMangaSeries(@Header("Authorization") String token,
                                                       @Url String next_url);

    @GET
    Observable<ListUser> getNextUser(@Header("Authorization") String token,
                                     @Url String next_url);

    @GET
    Observable<ListSimpleUser> getNextSimpleUser(@Header("Authorization") String token,
                                                 @Url String next_url);


    @GET
    Observable<ListIllust> getNextIllust(@Header("Authorization") String token,
                                         @Url String next_url);




    //https://app-api.pixiv.net/web/v1/login?code_challenge=
    // BpI4XJUk4nHHBwbhTNdunQDhB4Ca0M3yBcC_v7E0lUw&

    @GET("web/v1/login?code_challenge_method=S256&client=pixiv-android")
    Observable<String> tryLogin(@Query("code_challenge") String code_challenge);
}
