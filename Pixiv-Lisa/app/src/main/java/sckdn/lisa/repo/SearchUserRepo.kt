package sckdn.lisa.repo

import io.reactivex.Observable
import sckdn.lisa.http.Retro
import sckdn.lisa.model.ListUser

class SearchUserRepo(private val word: String?) : RemoteRepo<ListUser>() {

    override fun initApi(): Observable<ListUser> {
        return Retro.getAppApi().searchUser(token(), word)
    }

    override fun initNextApi(): Observable<ListUser> {
        return Retro.getAppApi().getNextUser(token(), nextUrl)
    }
}
