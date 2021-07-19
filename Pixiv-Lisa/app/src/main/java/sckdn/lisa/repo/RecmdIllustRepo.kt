package sckdn.lisa.repo

import io.reactivex.Observable
import sckdn.lisa.http.Retro
import sckdn.lisa.model.ListIllust
import sckdn.lisa.model.RecmdIllust

open class RecmdIllustRepo(
    private val dataType: String?
) : RemoteRepo<ListIllust>() {

    override fun initApi(): Observable<RecmdIllust> {
        return if ("漫画" == dataType) {
            Retro.getAppApi().getRecmdManga(token())
        } else {
            Retro.getAppApi().getRecmdIllust(token())
        }
    }

    override fun initNextApi(): Observable<ListIllust> {
        return Retro.getAppApi().getNextIllust(token(), nextUrl)
    }
}
