package com.youxia.fragment;

import com.youxia.BaseFragment;
import com.youxia.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.tsz.afinal.FinalActivity;

public class FragmentTask extends BaseFragment {
	
	public static final String TAG = FragmentTask.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_task, container, false);
		FinalActivity.initInjectedView(this,root);
		return root;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	
	public void btnClick(View v){
		
	}
}
