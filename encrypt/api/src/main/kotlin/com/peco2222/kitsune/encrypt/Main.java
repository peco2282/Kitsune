package com.peco2222.kitsune.encrypt;

public class Main {
    static {
        System.loadLibrary("mycrypto");
    }

    public native void encrypt(String input, String output);
}
