package bo;

import com.henja.liqnot.ws.ApiFunction;

/**
 * Shared Data to use with SharedDataCentral
 * Created by henry on 09/01/2017.
 */

interface SharedData {

    boolean isValid();
    ApiFunction getUpdateFunction();
}
