package com.example.ipcounter.builder;

public interface IPAddressBuilder {
    void append(byte b);

    int build();

    boolean hasUncommittedData();
}
