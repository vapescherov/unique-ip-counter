package com.example.ipcounter.builder;

public class DefaultIPAddressBuilder implements IPAddressBuilder {

    private int value;
    private int segmentValue;
    private boolean hasUncommitted;
    private int segmentCount;

    public DefaultIPAddressBuilder() {
        this.value = 0;
        this.segmentValue = 0;
        this.hasUncommitted = false;
        this.segmentCount = 0;
    }

    @Override
    public void append(byte c) {
        this.hasUncommitted = true;
        if (c == '.') {
            appendCurrentSegmentToIP();
        } else if (isAsciiDigit(c)) {
            appendAsciiDigitToCurrentSegment(c - '0');
        } else {
            throw new IllegalStateException("Invalid character encountered: " + c);
        }
    }

    @Override
    public int build() {
        appendCurrentSegmentToIP();
        if (this.segmentCount != 4) {
            throw new IllegalStateException("Incomplete IP address: expected 4 segments, got " + segmentCount);
        }

        int ip = this.value;
        clearState();
        return ip;
    }

    @Override
    public boolean hasUncommittedData() {
        return this.hasUncommitted;
    }

    private void clearState() {
        this.value = 0;
        this.segmentValue = 0;
        this.hasUncommitted = false;
        this.segmentCount = 0;
    }

    private void appendAsciiDigitToCurrentSegment(int digit) {
        int newValue = appendDigit(this.segmentValue, digit);
        if (newValue > 255) {
            throw new IllegalStateException("IP segment value exceeds 255");
        }
        this.segmentValue = newValue;
    }

    private void appendCurrentSegmentToIP() {
        this.value = appendSegmentToIP(this.value, this.segmentValue);
        this.segmentValue = 0;
        this.segmentCount++;
        if (this.segmentCount > 4) {
            throw new IllegalStateException("Invalid IP: more than four segments");
        }
    }

    private static int appendDigit(int number, int digit) {
        if (digit < 0 || digit > 9) {
            throw new IllegalStateException("Provided character is not a valid digit: " + digit);
        }
        return number * 10 + digit;
    }

    private static boolean isAsciiDigit(byte c) {
        return c >= '0' && c <= '9';
    }

    private static int appendSegmentToIP(int ipPart, int segment) {
        return (ipPart << 8) | segment;
    }

}
