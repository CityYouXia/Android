package com.youxia.widget;

import com.youxia.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class CustomLoadingView extends FrameLayout implements View.OnClickListener {

    private View loadingView;
    private View failedView;
    private View networkView;
    
    private State state;
    private OnRetryListener listener;

    public interface OnRetryListener {
        void onRetry();
    }

    public enum State {
        loading, failed, network, done
    }

    public CustomLoadingView(Context context) {
        super(context);
        initView(context);
    }

    public CustomLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CustomLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }
  

    private void initView(Context context) {
    	
        LayoutInflater.from(context).inflate(R.layout.customer_loading_layout, this);
        
        loadingView = findViewById(R.id.customer_loading);
        failedView = findViewById(R.id.customer_loading_failed);
        networkView = findViewById(R.id.customer_network_none);
        
        setOnClickListener(this);
        
        notifyViewChanged(State.loading);
    }

    public void notifyViewChanged(State state) {
        this.state = state;
        switch (state) {
            case loading:
                setVisibility(View.VISIBLE);                
                loadingView.setVisibility(View.VISIBLE);
                failedView.setVisibility(View.GONE);
                networkView.setVisibility(View.GONE);
                break;
            case failed:
                setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.GONE);
                failedView.setVisibility(View.VISIBLE);
                networkView.setVisibility(View.GONE);
                break;
            case network:
                setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.GONE);
                failedView.setVisibility(View.GONE);
                networkView.setVisibility(View.VISIBLE);
                break;
            case done:
                setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    public void setOnRetryListener(OnRetryListener listener) {
        this.listener = listener;
    }
    
    public void onClick(View v) {
        if (listener != null && state == State.network) {
            listener.onRetry();
        }
    }
}
