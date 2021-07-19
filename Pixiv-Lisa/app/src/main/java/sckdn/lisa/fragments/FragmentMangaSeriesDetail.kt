package sckdn.lisa.fragments

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import sckdn.lisa.R
import sckdn.lisa.adapters.BaseAdapter
import sckdn.lisa.adapters.IAdapter
import sckdn.lisa.databinding.FragmentBaseListBinding
import sckdn.lisa.model.IllustsBean
import sckdn.lisa.model.ListMangaOfSeries
import sckdn.lisa.repo.BaseRepo
import sckdn.lisa.repo.MangaSeriesDetailRepo
import sckdn.lisa.utils.Params

class FragmentMangaSeriesDetail :
    NetListFragment<FragmentBaseListBinding, ListMangaOfSeries, IllustsBean>() {

    private var seriesId: Int = 0

    override fun initBundle(bundle: Bundle) {
        seriesId = bundle.getInt(Params.ID)
    }

    companion object {
        @JvmStatic
        fun newInstance(seriesId: Int): FragmentMangaSeriesDetail {
            return FragmentMangaSeriesDetail().apply {
                arguments = Bundle().apply {
                    putInt(Params.ID, seriesId)
                }
            }
        }
    }

    override fun adapter(): BaseAdapter<*, out ViewDataBinding> {
        return IAdapter(allItems, mContext)
    }

    override fun repository(): BaseRepo {
        return MangaSeriesDetailRepo(seriesId)
    }

    override fun getToolbarTitle(): String {
        return getString(R.string.string_230)
    }

    override fun initRecyclerView() {
        staggerRecyclerView()
    }
}
