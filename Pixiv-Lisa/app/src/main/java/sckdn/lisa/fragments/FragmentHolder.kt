@file:Suppress("DEPRECATION")

package sckdn.lisa.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import sckdn.lisa.R
import sckdn.lisa.activities.Lisa
import sckdn.lisa.databinding.FragmentHolderBinding
import sckdn.lisa.utils.Params
import sckdn.lisa.viewmodel.UserViewModel

class FragmentHolder : BaseFragment<FragmentHolderBinding>() {

    private lateinit var mUserViewModel: UserViewModel

    companion object {
        @JvmStatic
        fun newInstance(): FragmentHolder {
            return FragmentHolder()
        }
    }

    override fun initModel() {
        mUserViewModel = ViewModelProvider(mActivity).get(UserViewModel::class.java)
    }

    override fun initLayout() {
        mLayoutID = R.layout.fragment_holder
    }

    override fun initView() {
        val data = mUserViewModel.user.value ?: return

        var TITLES: Array<String>

        if (data.userId == Lisa.sUserModel.user.id) {
            TITLES = arrayOf("收藏", "其他")
        } else {
            TITLES = arrayOf("插画", "其他")
        }

        val items = arrayOf<Fragment>(
            if (data.userId == Lisa.sUserModel.user.id) {
                FragmentLikeIllust.newInstance(data.userId, Params.TYPE_PUBLUC)
            } else {
                FragmentUserIllust.newInstance(data.userId, false)
            },
            FragmentUserRight()
        )
        @Suppress("DEPRECATION")
        baseBind.viewPager.adapter = object : FragmentPagerAdapter(childFragmentManager) {

            override fun getItem(position: Int): Fragment {
                return items[position]
            }

            override fun getCount(): Int {
                return TITLES.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return TITLES[position]
            }
        }
        baseBind.tabLayout.setupWithViewPager(baseBind.viewPager)
    }
}
