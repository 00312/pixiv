package sckdn.lisa.repo

import io.reactivex.Observable
import sckdn.lisa.http.Retro
import sckdn.lisa.model.ListUser

class RecmdUserRepo(private val isHorizontal: Boolean) : RemoteRepo<ListUser>() {

    override fun initApi(): Observable<ListUser> {
        return Retro.getAppApi().getRecmdUser(token())
    }

    override fun initNextApi(): Observable<ListUser>? {
        if (isHorizontal) {
            return null
        }
        return Retro.getAppApi().getNextUser(token(), nextUrl)
    }

    override fun localData(): Boolean {
        return super.localData()
    }
}
