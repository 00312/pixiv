package sckdn.lisa.repo

import android.text.TextUtils
import io.reactivex.Observable
import io.reactivex.functions.Function
import sckdn.lisa.core.FilterMapper
import sckdn.lisa.http.Retro
import sckdn.lisa.model.ListIllust
import sckdn.lisa.utils.PixivOperate
import sckdn.lisa.viewmodel.SearchModel

class SearchIllustRepo(
    var keyword: String?,
    var sortType: String?,
    var searchType: String?,
    var starSize: String?,
    var isPopular: Boolean
) : RemoteRepo<ListIllust>() {

    private var filterMapper: FilterMapper? = null

    override fun initApi(): Observable<ListIllust> {
        return if (isPopular) {
            Retro.getAppApi().popularPreview(token(), keyword)
        } else {
            PixivOperate.insertSearchHistory(keyword, 0)
            Retro.getAppApi().searchIllust(
                token(),
                keyword + if (TextUtils.isEmpty(starSize)) "" else " $starSize",
                sortType,
                searchType
            )
        }
    }

    override fun initNextApi(): Observable<ListIllust> {
        return Retro.getAppApi().getNextIllust(token(), nextUrl)
    }

    override fun mapper(): Function<in ListIllust, ListIllust> {
        if (this.filterMapper == null) {
            this.filterMapper = FilterMapper().enableFilterStarSize()
        }
        return this.filterMapper!!
    }

    fun update(searchModel: SearchModel, pop: Boolean) {
        keyword = searchModel.keyword.value
        sortType = searchModel.sortType.value
        searchType = searchModel.searchType.value
        starSize = searchModel.starSize.value
        isPopular = pop

        this.filterMapper?.updateStarSizeLimit(this.getStarSizeLimit())
    }

    fun getStarSizeLimit(): Int {
        if (TextUtils.isEmpty(this.starSize)) {
            return 0
        }
        val match = Regex("""\d+""").find(starSize!!)
        if (match != null) {
            return match.value.toInt()
        }
        return 0
    }
}
