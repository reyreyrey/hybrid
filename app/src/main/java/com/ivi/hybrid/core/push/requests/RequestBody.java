package com.ivi.hybrid.core.push.requests;


public  class RequestBody implements IRequest{
    private byte[] body;

    public RequestBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBody(){
        return body;
    }
}
