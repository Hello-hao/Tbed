package cn.hellohao.exception;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/6 8:51
 */
//创建自定义异常
//
public class StorageSourceInitException extends RuntimeException {
    public StorageSourceInitException() {
    }

    public StorageSourceInitException(String message) {
        super(message);
    }

    public StorageSourceInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageSourceInitException(Throwable cause) {
        super(cause);
    }

    public StorageSourceInitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
