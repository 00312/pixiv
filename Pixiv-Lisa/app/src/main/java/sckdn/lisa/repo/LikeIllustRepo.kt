package sckdn.lisa.repo

import android.text.TextUtils
import io.reactivex.Observable
import sckdn.lisa.http.Retro
import sckdn.lisa.model.ListIllust

class LikeIllustRepo(
    private val userID: Int,
    private val starType: String?,
    var tag: String?
) : RemoteRepo<ListIllust>() {

    override fun initApi(): Observable<ListIllust> {
        return if (TextUtils.isEmpty(tag)) {
            Retro.getAppApi().getUserLikeIllust(token(), userID, starType)
        } else {
            Retro.getAppApi().getUserLikeIllust(token(), userID, starType, tag)
        }
    }

    override fun initNextApi(): Observable<ListIllust> {
        return Retro.getAppApi().getNextIllust(token(), nextUrl)
    }
}
