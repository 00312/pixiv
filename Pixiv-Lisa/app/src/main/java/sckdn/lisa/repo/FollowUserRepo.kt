package sckdn.lisa.repo

import io.reactivex.Observable
import sckdn.lisa.http.Retro
import sckdn.lisa.model.ListUser

class FollowUserRepo(
    private val userID: Int,
    private val starType: String?
) : RemoteRepo<ListUser>() {

    override fun initApi(): Observable<ListUser> {
        return Retro.getAppApi().getFollowUser(token(), userID, starType)
    }

    override fun initNextApi(): Observable<ListUser> {
        return Retro.getAppApi().getNextUser(token(), nextUrl)
    }
}
