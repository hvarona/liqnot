package bo;

import com.henja.liqnot.R;

/**
 * Created by javier on 04/01/2017.
 */
public enum NotifierCurrency {
    BTS("BTS", R.mipmap.bitshares_icon),
    BLOCKPAY("BLOCKPAY", R.mipmap.blockpay_icon);

    private final String name;
    private final int icon;
    NotifierCurrency(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName(){
        return this.name;
    }

    public int getIcon(){
        return this.icon;
    }

    public String toString(){
        return this.name;
    }
}
