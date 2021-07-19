package sckdn.lisa.repo

import io.reactivex.Observable
import sckdn.lisa.http.Retro
import sckdn.lisa.model.ListSimpleUser

class SimpleUserRepo(private val illustID: Int) : RemoteRepo<ListSimpleUser>() {

    override fun initApi(): Observable<ListSimpleUser> {
        return Retro.getAppApi().getUsersWhoLikeThisIllust(token(), illustID)
    }

    override fun initNextApi(): Observable<ListSimpleUser> {
        return Retro.getAppApi().getNextSimpleUser(token(), nextUrl)
    }
}
