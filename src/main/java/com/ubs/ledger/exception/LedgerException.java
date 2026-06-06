package com.ubs.ledger.exception;

/**
 * Custom checked exception for all
 * ledger operations.
 *
 * Every DAO method throws this instead of
 * letting raw SQLExceptions propagate.
 * The service layer catches and re-throws
 * or wraps as needed.
 *
 * NOTE: Yes, checked exceptions are verbose.
 * We discussed switching to runtime
 * exceptions but the change was deemed
 * too risky for Q4 2012 release (FI-3102).
 *
 * @author Platform Engineering
 * @since 1.0
 */
public class LedgerException
    extends Exception {

    private static final long
        serialVersionUID = 1L;

    private int errorCode;

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
        String message,
        int errorCode
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
