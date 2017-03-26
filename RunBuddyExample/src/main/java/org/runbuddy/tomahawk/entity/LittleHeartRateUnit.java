package org.runbuddy.tomahawk.entity;

/**
 * Created by Johnny Chou on 2017/2/19.
 */

public class LittleHeartRateUnit {
    private String id;
    private String value;
    private String status;

    public LittleHeartRateUnit(){super();}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
