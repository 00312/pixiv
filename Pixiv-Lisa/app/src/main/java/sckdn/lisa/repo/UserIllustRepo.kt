package sckdn.lisa.repo

import io.reactivex.Observable
import io.reactivex.functions.Function
import sckdn.lisa.core.FilterMapper
import sckdn.lisa.http.Retro
import sckdn.lisa.model.ListIllust
import sckdn.lisa.utils.Params

class UserIllustRepo(private val userID: Int) : RemoteRepo<ListIllust>() {

    override fun initApi(): Observable<ListIllust> {
        return Retro.getAppApi().getUserSubmitIllust(token(), userID, Params.TYPE_ILLUST)
    }

    override fun initNextApi(): Observable<ListIllust> {
        return Retro.getAppApi().getNextIllust(token(), nextUrl)
    }

    override fun mapper(): Function<in ListIllust, ListIllust> {
        return FilterMapper()
    }
}
