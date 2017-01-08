package bo;

import android.media.Image;

/**
 * Created by javier on 06/01/2017.
 */

public class NotifierCurrencyData {
    public NotifierCurrency currency;
    public Image image;

    public NotifierCurrencyData(NotifierCurrency currency, Image image){
        this.currency = currency;
        this.image = image;
    }

    public NotifierCurrency getCurrency(){
        return this.currency;
    }

    public Image getImage(){
        return this.image;
    }
}
