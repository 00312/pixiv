package sckdn.lisa.repo

import android.content.Context
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import io.reactivex.Observable
import io.reactivex.functions.Function
import sckdn.lisa.R
import sckdn.lisa.core.FilterMapper
import sckdn.lisa.http.Retro
import sckdn.lisa.model.ListIllust
import sckdn.lisa.view.MyDeliveryHeader

class RightRepo(var restrict: String?) : RemoteRepo<ListIllust>() {

    override fun initApi(): Observable<ListIllust> {
        return Retro.getAppApi().getFollowUserIllust(token(), restrict)
    }

    override fun initNextApi(): Observable<ListIllust> {
        return Retro.getAppApi().getNextIllust(token(), nextUrl)
    }

    override fun getFooter(context: Context): RefreshFooter {
        @Suppress("DEPRECATION")
        return ClassicsFooter(context).setPrimaryColor(context.resources.getColor(R.color.fragment_center))
    }

    override fun getHeader(context: Context): RefreshHeader {
        return MyDeliveryHeader(context)
    }

    override fun mapper(): Function<ListIllust, ListIllust> {
        return FilterMapper()
    }

    /*override fun localData(): Boolean {
        return Dev.isDev
    }*/
}
