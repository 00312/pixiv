package sckdn.lisa.repo

import io.reactivex.Observable
import sckdn.lisa.http.Retro
import sckdn.lisa.model.ListComment

class CommentRepo(private val illustID: Int) : RemoteRepo<ListComment>() {

    override fun initApi(): Observable<ListComment> {
        return Retro.getAppApi().getComment(token(), illustID)
    }

    override fun initNextApi(): Observable<ListComment> {
        return Retro.getAppApi().getNextComment(token(), nextUrl)
    }
}
