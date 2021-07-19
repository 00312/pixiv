package sckdn.lisa.repo

import io.reactivex.Observable
import sckdn.lisa.activities.Lisa
import sckdn.lisa.http.Retro
import sckdn.lisa.model.ListIllust

class RelatedIllustRepo(private val illustID: Int) : RemoteRepo<ListIllust>() {

    override fun initApi(): Observable<ListIllust> {
        return Retro.getAppApi().relatedIllust(token(), illustID)
    }

    override fun initNextApi(): Observable<ListIllust> {
        return Retro.getAppApi().getNextIllust(token(), getNextUrl())
    }

    override fun hasNext(): Boolean {
        return Lisa.sSettings.isRelatedIllustNoLimit
    }
}
