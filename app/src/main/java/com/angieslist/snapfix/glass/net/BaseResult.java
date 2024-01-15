package com.angieslist.snapfix.glass.net;

import java.util.List;

public class BaseResult<T> {

    @Override
    public String toString() {
        return "BaseResult [status=" + status + ", message=" + message
                + ", data=" + data + "]";
    }

    public static final String STATUS_OK = "ok";
    public static final String STATUS_KO = "ko";
    private String status = STATUS_KO;
    private String message;
    private List<T> data;

    public static String getStatusOk() {
        return STATUS_OK;
    }

    public static String getStatusKo() {
        return STATUS_KO;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<T> getData() {
        return data;
    }

    public boolean isResultGood() {
        return STATUS_OK.equals(status) && data != null && data.size() > 0 ;
    }
}