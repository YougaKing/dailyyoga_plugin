package com.dailyyoga.plugin.droidassist.ex;

@SuppressWarnings("WeakerAccess")
public class DroidAssistBadStatementException extends DroidAssistException {


    public DroidAssistBadStatementException(String msg) {
        super(msg);
    }

    public DroidAssistBadStatementException(Throwable e) {
        super(e);
    }

    public DroidAssistBadStatementException(String msg, Throwable e) {
        super(msg, e);
    }
}
