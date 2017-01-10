package bo;

import com.henja.liqnot.ws.ApiFunction;
import com.henja.liqnot.ws.GetEquivalentRate;

import java.util.Date;

/**
 * Created by henry on 09/01/2017.
 */

public class AssetEquivalentRate implements SharedData {
    private static final int EXPIRATION_TIME = 300000;

    private Asset monitor;
    private Asset compareTo;
    private double value = 0;
    private Date expirationDate;

    public AssetEquivalentRate(Asset monitor, Asset compareTo) {
        this.monitor = monitor;
        this.compareTo = compareTo;
        expirationDate = new Date(0);
    }

    public Asset getMonitor() {
        return monitor;
    }

    public Asset getCompareTo() {
        return compareTo;
    }

    @Override
    public boolean isValid() {
        return expirationDate.after(new Date());
    }

    @Override
    public ApiFunction getUpdateFunction() {
        return new GetEquivalentRate(monitor,compareTo);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
        expirationDate = new Date(System.currentTimeMillis()+EXPIRATION_TIME);
    }
}
