package com.dailyyoga.plugin.miit.ex;

public class DailyyogaMIITException extends RuntimeException {

    private Throwable myCause;

    public Throwable getCause() {
        return (myCause == this ? null : myCause);
    }

    public synchronized Throwable initCause(Throwable cause) {
        myCause = cause;
        return this;
    }

    private String message;

    public String getReason() {
        if (message != null) {
            return message;
        } else {
            return this.toString();
        }
    }

    public DailyyogaMIITException(String msg) {
        super(msg);
        message = msg;
        initCause(null);
    }


    public DailyyogaMIITException(Throwable e) {
        super(e);
        message = null;
        initCause(e);
    }


    public DailyyogaMIITException(String msg, Throwable e) {
        this(msg);
        initCause(e);
    }
}
