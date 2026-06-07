package com.ubs.ledger.exception;

public class LedgerException
    extends RuntimeException {

    private final int errorCode;

    public LedgerException(String message) {
        super(message);
        this.errorCode = -1;
    }

    public LedgerException(
        String message, Throwable cause
    ) {
        super(message, cause);
        this.errorCode = -1;
    }

    public LedgerException(
        String message, int errorCode
    ) {
        super(message);
        this.errorCode = errorCode;
    }

    public LedgerException(
        String message,
        Throwable cause,
        int errorCode
    ) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
