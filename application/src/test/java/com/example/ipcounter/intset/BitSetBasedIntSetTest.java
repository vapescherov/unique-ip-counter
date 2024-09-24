package com.example.ipcounter.intset;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BitSetBasedIntSetTest {

    @Test
    void newlyCreatedSet_ShouldHaveSizeZero() {
        IntSet set = new BitSetBasedIntSet();
        assertEquals(0, set.size());
    }

    @Test
    void addingElementZero_ShouldIncreaseSizeToOne() {
        IntSet set = new BitSetBasedIntSet();
        set.add(0);
        assertEquals(1, set.size());
    }

    @Test
    void addingElementMaxInt_ShouldIncreaseSizeToOne() {
        IntSet set = new BitSetBasedIntSet();
        set.add(Integer.MAX_VALUE);
        assertEquals(1, set.size());
    }

    @Test
    void addingElementMinInt_ShouldIncreaseSizeToOne() {
        IntSet set = new BitSetBasedIntSet();
        set.add(Integer.MIN_VALUE);
        assertEquals(1, set.size());
    }

    @Test
    void addingSingleElement_ShouldIncreaseSizeToOne() {
        IntSet set = new BitSetBasedIntSet();
        set.add(10);
        assertEquals(1, set.size());
    }

    @Test
    void addingTwoDifferentElements_ShouldIncreaseSizeToTwo() {
        IntSet set = new BitSetBasedIntSet();
        set.add(10);
        set.add(100);
        assertEquals(2, set.size());
    }

    @Test
    void addingSameElementMultipleTimes_ShouldNotChangeSize() {
        IntSet set = new BitSetBasedIntSet();
        set.add(10);
        set.add(10);
        assertEquals(1, set.size());
    }

    @Test
    void addingNegativeAndPositiveElements_ShouldGiveCorrectSize() {
        IntSet set = new BitSetBasedIntSet();
        set.add(-10);
        set.add(10);
        assertEquals(2, set.size());
    }

    @Test
    void addingNonNegativeNumbers_ShouldHaveCorrectSize() {
        IntSet set = new BitSetBasedIntSet();

        IntStream.rangeClosed(0, Integer.MAX_VALUE)
                .forEach(set::add);

        long expected = 1L << 31;
        assertEquals(expected, set.size());
    }

    @Test
    void addingNegativeNumbers_ShouldHaveCorrectSize() {
        IntSet set = new BitSetBasedIntSet();

        IntStream.range(Integer.MIN_VALUE, 0)
                .forEach(set::add);

        long expected = 1L << 31;
        assertEquals(expected, set.size());
    }

    @Test
    void addingAllIntNumbers_ShouldHaveCorrectSize() {
        IntSet set = new BitSetBasedIntSet();

        IntStream.rangeClosed(Integer.MIN_VALUE, Integer.MAX_VALUE)
                .forEach(set::add);

        long expected = 1L << 32;
        assertEquals(expected, set.size());
    }

}
