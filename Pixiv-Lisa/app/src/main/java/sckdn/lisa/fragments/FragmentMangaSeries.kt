package sckdn.lisa.fragments

import android.content.Intent
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import sckdn.lisa.R
import sckdn.lisa.activities.TemplateActivity
import sckdn.lisa.adapters.BaseAdapter
import sckdn.lisa.adapters.MangaSeriesAdapter
import sckdn.lisa.databinding.FragmentBaseListBinding
import sckdn.lisa.model.ListMangaSeries
import sckdn.lisa.model.MangaSeriesItem
import sckdn.lisa.repo.BaseRepo
import sckdn.lisa.repo.MangaSeriesRepo
import sckdn.lisa.utils.DensityUtil
import sckdn.lisa.utils.Params
import sckdn.lisa.view.LinearItemDecorationNoLRTB

class FragmentMangaSeries :
    NetListFragment<FragmentBaseListBinding, ListMangaSeries, MangaSeriesItem>() {

    private var userID: Int = 0

    override fun initBundle(bundle: Bundle) {
        userID = bundle.getInt(Params.USER_ID)
    }

    companion object {
        @JvmStatic
        fun newInstance(userID: Int): FragmentMangaSeries {
            return FragmentMangaSeries().apply {
                arguments = Bundle().apply {
                    putInt(Params.USER_ID, userID)
                }
            }
        }
    }

    override fun adapter(): BaseAdapter<*, out ViewDataBinding> {
        return MangaSeriesAdapter(
            allItems,
            mContext
        ).setOnItemClickListener { _, position, _ ->
            val intent = Intent(mContext, TemplateActivity::class.java)
            intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "漫画系列详情")
            intent.putExtra(Params.ID, allItems[position].id)
            startActivity(intent)
        }
    }

    override fun repository(): BaseRepo {
        return MangaSeriesRepo(userID)
    }

    override fun getToolbarTitle(): String {
        return getString(R.string.string_230)
    }

    override fun initRecyclerView() {
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.addItemDecoration(LinearItemDecorationNoLRTB(DensityUtil.dp2px(1.0f)))
    }
}
