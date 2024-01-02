package cn.litteleterry.rpc;

public class RedisRpcException  extends RuntimeException {
    /**
     * Zero-arg constructor.
     */
    public RedisRpcException()
    {
    }

    /**
     * Create the exception.
     */
    public RedisRpcException(String message)
    {
        super(message);
    }

    /**
     * Create the exception.
     */
    public RedisRpcException(String message, Throwable rootCause)
    {
        super(message, rootCause);
    }

    /**
     * Create the exception.
     */
    public RedisRpcException(Throwable rootCause)
    {
        super(rootCause);
    }
}

