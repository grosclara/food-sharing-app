package com.example.cshare.data.apiresponses;

/**
 * Possible status types of a response provided to the UI
 */
public enum Status {
    SUCCESS,
    ERROR,
    /**
     * Special status to reset a live data once the request has taken place and the result has
     * been processed.
     */
    COMPLETE

}
