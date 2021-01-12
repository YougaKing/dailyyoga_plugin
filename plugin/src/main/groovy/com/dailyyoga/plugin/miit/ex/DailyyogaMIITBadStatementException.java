package com.dailyyoga.plugin.miit.ex;

@SuppressWarnings("WeakerAccess")
public class DailyyogaMIITBadStatementException extends DailyyogaMIITException {


    public DailyyogaMIITBadStatementException(String msg) {
        super(msg);
    }

    public DailyyogaMIITBadStatementException(Throwable e) {
        super(e);
    }

    public DailyyogaMIITBadStatementException(String msg, Throwable e) {
        super(msg, e);
    }
}
