package com.thilinas.twallpapers.customs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.thilinas.twallpapers.R;


/**
 * Created by Thilina on 13-Feb-17.
 */

public class CustomEditText extends EditText {

    public CustomEditText(Context c) {
        super(c);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init( context, attrs );
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init( context, attrs );
    }

    private void init(Context context, AttributeSet attrs) {
        if(!isInEditMode()){
            try {
                TypedArray ta = context.obtainStyledAttributes( attrs, R.styleable.Fonts );
                if ( ta != null ) {
                    String fontAsset = ta.getString( R.styleable.Fonts_font );
                    if ( !fontAsset.isEmpty() ) {
                        int type = Integer.parseInt( fontAsset );
                        Typeface typeFace = FontManager.getInstance( context ).getByType( type );
                        ta.recycle();
                        super.setTypeface( typeFace );
                    }
                }
            }catch (Exception e){    }
        }
    }
}

