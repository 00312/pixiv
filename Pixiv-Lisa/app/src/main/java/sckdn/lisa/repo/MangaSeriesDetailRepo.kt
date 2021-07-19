package sckdn.lisa.repo

import io.reactivex.Observable
import sckdn.lisa.http.Retro
import sckdn.lisa.model.ListMangaOfSeries

class MangaSeriesDetailRepo(private val seriesID: Int) : RemoteRepo<ListMangaOfSeries>() {

    override fun initApi(): Observable<ListMangaOfSeries> {
        return Retro.getAppApi().getMangaSeriesById(token(), seriesID)
    }

    override fun initNextApi(): Observable<ListMangaOfSeries>? {
        return null
    }
}
