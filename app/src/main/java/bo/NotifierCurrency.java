package bo;

import com.henja.liqnot.R;

/**
 * Created by javier on 04/01/2017.
 */
public enum NotifierCurrency {
    UNKNOWN("UNKNOWN", R.mipmap.bitshares_icon, false),
    BTS("BTS", R.mipmap.bitshares_icon, false),
    BLOCKPAY("BLOCKPAY", R.mipmap.blockpay_icon, false);

    private final String name;
    private final int icon;
    private final boolean isSmartcoin;
    NotifierCurrency(String name, int icon, boolean isSmartcoin) {
        this.name = name;
        this.icon = icon;
        this.isSmartcoin = isSmartcoin;
    }

    public String getName(){
        return this.name;
    }

    public int getIcon(){
        return this.icon;
    }

    public boolean isSmartcoin() {
        return isSmartcoin;
    }

    public String toString(){
        return this.name;
    }
}
