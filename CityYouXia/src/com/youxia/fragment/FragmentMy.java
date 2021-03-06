package com.youxia.fragment;

import net.tsz.afinal.FinalActivity;

import com.youxia.BaseFragment;
import com.youxia.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("InflateParams")
public class FragmentMy extends BaseFragment {
	
	public static final String TAG = FragmentMy.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_my, container, false);
		FinalActivity.initInjectedView(this,root);
		return root;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView();
	}

	private void initView() {
	}
}
