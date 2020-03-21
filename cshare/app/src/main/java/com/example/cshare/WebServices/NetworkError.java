package com.example.cshare.WebServices;

public class NetworkError extends Throwable {
    private final Throwable error;

    public NetworkError(Throwable e) {
        super(e);
        this.error = e;
    }

}
