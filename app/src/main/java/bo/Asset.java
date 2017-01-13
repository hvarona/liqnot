package bo;

import com.henja.liqnot.ws.ApiFunction;
import com.henja.liqnot.ws.GetAsset;

/**
 * Asset Bussisness Object
 *
 * Created by henry on 09/01/2017.
 */

public class Asset extends BO implements SharedData{

    private String id;
    private String symbol;
    private int precision = -1;
    private String type;
    private boolean valid = false;

    public Asset(String symbol) {
        this.symbol = symbol;
    }

    public Asset(String id, String symbol, int precision, String type) {
        this.id = id;
        this.symbol = symbol;
        this.precision = precision;
        this.type = type;
        this.valid=true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        valid = true;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getPrecision() {
        return precision;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public ApiFunction getUpdateFunction() {
        return new GetAsset(this.symbol);
    }
}
