
package com.cds_jo.GalaxySalesApp;
import android.graphics.Bitmap;

/**
 *
 * @author manish.s
 *
 */

public class Items {
    Bitmap image;
    String title;

    public String getSCR_CODE() {
        return SCR_CODE;
    }

    public void setSCR_CODE(String SCR_CODE) {
        this.SCR_CODE = SCR_CODE;
    }

    String SCR_CODE;
    public Items(Bitmap image, String title,String SCR_CODE) {
        super();
        this.image = image;
        this.title = title;
        this.SCR_CODE = SCR_CODE;
    }
    public Items( ) {
        super();

    }
    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


}
