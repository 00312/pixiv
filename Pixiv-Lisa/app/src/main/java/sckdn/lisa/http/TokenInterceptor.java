package sckdn.lisa.http;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import sckdn.lisa.R;
import sckdn.lisa.activities.Lisa;
import sckdn.lisa.fragments.FragmentLogin;
import sckdn.lisa.model.UserModel;
import sckdn.lisa.utils.Common;
import sckdn.lisa.utils.Local;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

/**
 * 全局自动刷新Token的拦截器
 */
public class TokenInterceptor implements Interceptor {

    private static final String TOKEN_ERROR_1 = "Error occurred at the OAuth process";
    private static final String TOKEN_ERROR_2 = "Invalid refresh token";
    private static final int TOKEN_LENGTH = 50;

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        if (isTokenExpired(response)) {
            Common.showLog("getNewToken 检测到是过期Token ");
            response.close();
            String newToken = getNewToken(request.header("Authorization"));
            Request newRequest = chain.request()
                    .newBuilder()
                    .header("Authorization", newToken)
                    .build();
            return chain.proceed(newRequest);
        }
        return response;
    }


    /**
     * 根据Response，判断Token是否失效
     *
     * @param response
     * @return
     */
    private boolean isTokenExpired(Response response) {
        final String body = Common.getResponseBody(response);
        Common.showLog("isTokenExpired body " + body);
        if (response.code() == 400) {
            if (body.contains(TOKEN_ERROR_1)) {
                Common.showLog("isTokenExpired 000");
                return true;
            } else if(body.contains(TOKEN_ERROR_2)){
                Lisa.sUserModel.getUser().setIs_login(false);
                Local.saveUser(Lisa.sUserModel);
                Common.showToast(R.string.string_340);
                Common.restart();
                Common.showLog("isTokenExpired 111");
                return false;
            } else {
                Common.showLog("isTokenExpired 222");
                return false;
            }
        } else {
            Common.showLog("isTokenExpired 333");
            return false;
        }
    }

    /**
     * 同步请求方式，获取最新的Token，解决多并发请求多次刷新token的问题
     *
     * @return
     */
    private synchronized String getNewToken(String tokenForThisRequest) throws IOException {
        if (Lisa.sUserModel.getAccess_token().equals(tokenForThisRequest) ||
                tokenForThisRequest.length() != TOKEN_LENGTH ||
                Lisa.sUserModel.getAccess_token().length() != TOKEN_LENGTH) {
            Common.showLog("getNewToken 主动获取最新的token old:" + tokenForThisRequest + " new:" + Lisa.sUserModel.getAccess_token());
            UserModel userModel = Local.getUser();
            Call<UserModel> call = Retro.getAccountApi().newRefreshToken(
                    FragmentLogin.CLIENT_ID,
                    FragmentLogin.CLIENT_SECRET,
                    FragmentLogin.REFRESH_TOKEN,
                    userModel.getRefresh_token(),
                    Boolean.TRUE);
            UserModel newUser = call.execute().body();
            if (newUser != null) {
                newUser.getUser().setPassword(
                        Lisa.sUserModel.getUser().getPassword()
                );
                newUser.getUser().setIs_login(true);
            }
            Local.saveUser(newUser);
            Common.showLog("getNewToken 获取到了最新的 token:" + newUser.getAccess_token());
            return newUser.getAccess_token();
        } else {
            Common.showLog("getNewToken 使用最新的token old:" + tokenForThisRequest + " new:" + Lisa.sUserModel.getAccess_token());
            return Lisa.sUserModel.getAccess_token();
        }
    }
}
