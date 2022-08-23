package com.example.demo.common.exception;

/**
 * 공통 익셉션
 *
 * @author EDA
 * @since 2022-02-22
 * @see {@link RuntimeException}
 */
public class CommonException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String errorMsg = "";

    public CommonException() {
        super();
    }

    public CommonException(Throwable cause) {
        super(cause);
    }

    public CommonException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);

        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
