package com.dailyyoga.plugin.miit.ex;

public class DailyyogaMIITBadTypeException extends DailyyogaMIITException {

    public DailyyogaMIITBadTypeException(String msg) {
        super(msg);
    }

    public DailyyogaMIITBadTypeException(Throwable e) {
        super(e);
    }

    public DailyyogaMIITBadTypeException(String msg, Throwable e) {
        super(msg, e);
    }
}
