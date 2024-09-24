package com.example.ipcounter.intset;

import java.util.BitSet;

public class BitSetBasedIntSet implements IntSet {

    private final BitSet positive;
    private final BitSet negative;

    public BitSetBasedIntSet() {
        this.positive = new BitSet();
        this.negative = new BitSet();
    }

    @Override
    public void add(int value) {
        if (value < 0) {
            negative.set(-(value + 1));
        } else {
            positive.set(value);
        }
    }

    @Override
    public long size() {
        long positiveSize = cardinality(positive);
        long negativeSize = cardinality(negative);
        return positiveSize + negativeSize;
    }

    private long cardinality(BitSet bitSet) {
        int cardinality = bitSet.cardinality();
        if (cardinality == Integer.MIN_VALUE) {
            return -((long) cardinality);
        }

        if (cardinality < 0) {
            throw new IllegalStateException("Cardinality can't be negative");
        }
        return cardinality;
    }

}
