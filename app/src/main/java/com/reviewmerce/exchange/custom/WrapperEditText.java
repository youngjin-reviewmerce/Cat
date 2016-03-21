package com.reviewmerce.exchange.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by songmho on 2015-07-12.
 */
public class WrapperEditText extends EditText {
    private KeyImeChange keyImeChangeListener;

    public WrapperEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setKeyImeChangeListener(KeyImeChange listener){
        keyImeChangeListener = listener;
    }

    public interface KeyImeChange {
        public boolean onKeyIme(int keyCode, KeyEvent event);
    }

    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if(keyImeChangeListener != null){
                    keyImeChangeListener.onKeyIme(keyCode, event);
                }
                //하고싶으신 일을 하세요...

        }// switch
        return super.onKeyDown(keyCode, event);
    }// onKeyDown
    */
///*
    @Override
    public boolean onKeyPreIme (int keyCode, KeyEvent event){
        if(keyImeChangeListener != null){
            boolean bRtn = keyImeChangeListener.onKeyIme(keyCode, event);
            if(bRtn == true)
                return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }
   // */
}
