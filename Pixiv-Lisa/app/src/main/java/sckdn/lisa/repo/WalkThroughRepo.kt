package sckdn.lisa.repo

import io.reactivex.Observable
import sckdn.lisa.http.Retro
import sckdn.lisa.model.ListIllust

class WalkThroughRepo : RemoteRepo<ListIllust>() {

    override fun initApi(): Observable<ListIllust> {
        return Retro.getAppApi().getLoginBg(token())
    }

    override fun initNextApi(): Observable<ListIllust>? {
        return null
    }
}
