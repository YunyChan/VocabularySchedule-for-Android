package com.example.vocabularyschedule;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import com.example.vocabularyschedule.SyncHorizontalScrollViewListener;

public class SyncHorizontalScrollView extends HorizontalScrollView {
    private SyncHorizontalScrollViewListener syncScrollViewListener = null;
    
    public SyncHorizontalScrollView(Context context) {
        super(context);
        
    }
    
    public SyncHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
    }
    
    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY){
        super.onScrollChanged(x, y, oldX, oldY);
        if (syncScrollViewListener != null){
            syncScrollViewListener.onScrollChanged(this, x, y, oldX, oldY);
        }
    }
    
    public void setScrollViewListener (SyncHorizontalScrollViewListener syncScrollViewListener){
            this.syncScrollViewListener = syncScrollViewListener;
    }
}
