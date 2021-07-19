package sckdn.lisa.fragments;

import android.os.Bundle;
import android.text.TextUtils;

import sckdn.lisa.R;
import sckdn.lisa.databinding.FragmentImageDetailLocalBinding;
import sckdn.lisa.utils.Params;
import xyz.zpayh.hdimage.state.ScaleType;

public class FragmentLocalImageDetail extends BaseFragment<FragmentImageDetailLocalBinding> {

    private String filePath;

    public static FragmentLocalImageDetail newInstance(String filePath) {
        Bundle args = new Bundle();
        args.putString(Params.FILE_PATH, filePath);
        FragmentLocalImageDetail fragment = new FragmentLocalImageDetail();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initBundle(Bundle bundle) {
        filePath = bundle.getString(Params.FILE_PATH);
    }

    @Override
    public void initLayout() {
        mLayoutID = R.layout.fragment_image_detail_local;
    }

    @Override
    public void initView() {
        if (!TextUtils.isEmpty(filePath) && filePath.contains(".zip")) {
            baseBind.illustImage.setScaleType(ScaleType.CENTER_CROP);
            baseBind.illustImage.setImageURI("res:///" + R.mipmap.zip);
        } else {
            baseBind.illustImage.setImageURI(filePath);
        }
    }
}
