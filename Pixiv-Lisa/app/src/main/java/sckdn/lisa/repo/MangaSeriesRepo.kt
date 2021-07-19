package sckdn.lisa.repo

import io.reactivex.Observable
import sckdn.lisa.http.Retro
import sckdn.lisa.model.ListMangaSeries

class MangaSeriesRepo(private val userID: Int) : RemoteRepo<ListMangaSeries>() {

    override fun initApi(): Observable<ListMangaSeries> {
        return Retro.getAppApi().getUserMangaSeries(token(), userID)
    }

    override fun initNextApi(): Observable<ListMangaSeries>? {
        return Retro.getAppApi().getNextUserMangaSeries(token(), nextUrl)
    }
}
