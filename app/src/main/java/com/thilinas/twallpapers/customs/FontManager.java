package com.thilinas.twallpapers.customs;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Thilina on 13-Feb-17.
 */

public class FontManager {
    private static FontManager Instance;
    private Context context;
    private Typeface karla;
    private Typeface karlaBold;
    private Typeface karlaItalic;
    private Typeface karlaRegular;
    private Typeface montserrat;
    private Typeface montserratBold;
    private Typeface montserratItalic;
    private Typeface montserratRegular;

    private FontManager(Context context) {
        this.context = context;
    }

    public synchronized static FontManager getInstance(Context context) {
        if ( Instance == null )
            Instance = new FontManager( context );
        return Instance;
    }

    public Typeface getByType(int type) {
        switch ( type ) {
            case 0:
                return FontManager.getInstance( context ).getKarla();
            case 1:
                return FontManager.getInstance( context ).getKarlaBold();
            case 2:
                return FontManager.getInstance( context ).getKarlaItalic();
            case 3:
                return FontManager.getInstance( context ).getKarlaRegular();
            case 4:
                return FontManager.getInstance( context ).getMontserrat();
            case 5:
                return FontManager.getInstance( context ).getMontserratBold();
            case 6:
                return FontManager.getInstance( context ).getMontserratItalic();
            case 7:
                return FontManager.getInstance( context ).getMontserratRegular();
            default:
                return Typeface.DEFAULT;
        }
    }

    public Typeface getKarla() {
        if ( karla == null) {
            karla =  FontCache.get("fonts/KarlaBold.ttf", context);
        }
        return karla;
    }

    public Typeface getKarlaBold() {
        if ( karlaBold == null) {
            karlaBold =  FontCache.get("fonts/KarlaBold.ttf", context);
        }
        return karlaBold;
    }

    public Typeface getKarlaItalic() {
        if (karlaItalic == null) {
            karlaItalic =  FontCache.get("fonts/KarlaItalic.ttf", context);
        }
        return karlaItalic;
    }

    public Typeface getKarlaRegular() {
        if (karlaRegular == null) {
            karlaRegular =  FontCache.get("fonts/KarlaRegular.ttf", context);
        }
        return karlaRegular;
    }

    public Typeface getMontserrat() {
        if (montserrat == null) {
            montserrat =  FontCache.get("fonts/MontserratBold.otf", context);
        }
        return montserrat; }

    public Typeface getMontserratBold() {
        if (montserratBold == null) {
            montserratBold =  FontCache.get("fonts/MontserratBold.otf", context);
        }
        return montserratBold; }

    public Typeface getMontserratItalic() {
        if (montserratItalic == null) {
            montserratItalic =  FontCache.get("fonts/MontserratLight.otf", context);
        }
        return montserratItalic; }

    public Typeface getMontserratRegular() {
        if (montserratRegular == null) {
            montserratRegular =  FontCache.get("fonts/MontserratRegular.ttf", context);
        }
        return montserratRegular;
    }
}
