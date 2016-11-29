package com.ccnode.codegenerator.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created on 3/24/16.
 */
public abstract class BaseRequest implements Serializable {

    private static final long serialVersionUID = 790960324431455971L;

    private String requestNo;

    public String getRequestNo() {
        return requestNo;
    }

}
