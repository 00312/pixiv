package sckdn.lisa.repo

import io.reactivex.Observable
import sckdn.lisa.http.Retro
import sckdn.lisa.model.ListTrendingtag

class HotTagRepo(
    private val contentType: String?
) : RemoteRepo<ListTrendingtag>() {

    override fun initApi(): Observable<ListTrendingtag> {
        return Retro.getAppApi().getHotTags(token(), contentType)
    }

    override fun initNextApi(): Observable<ListTrendingtag>? {
        return null
    }
}
