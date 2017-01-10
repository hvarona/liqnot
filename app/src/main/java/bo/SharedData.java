package bo;

import com.henja.liqnot.ws.ApiFunction;

/**
 * Created by henry on 09/01/2017.
 */

public interface SharedData {

    public boolean isValid();
    public ApiFunction getUpdateFunction();
}
