package com.jihye.dividend.exception;

public abstract class AbstractException extends RuntimeException{

    abstract public int getStatusCode();
    abstract public String getMessage();
}
