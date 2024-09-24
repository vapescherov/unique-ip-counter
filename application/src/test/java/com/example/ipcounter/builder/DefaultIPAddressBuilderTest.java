package com.example.ipcounter.builder;

import org.junit.jupiter.api.Test;

import static com.example.ipcounter.util.TestUtils.ip;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultIPAddressBuilderTest {

    private final IPAddressBuilder builder = new DefaultIPAddressBuilder();

    @Test
    public void buildValidIPAddress_ShouldReturnCorrectParsedIP() {
        String ip = "192.168.1.1";
        for (byte c : ip.getBytes()) {
            builder.append(c);
        }
        assertEquals(ip(192, 168, 1, 1), builder.build());
    }

    @Test
    public void append_ShouldHaveUncommittedData() {
        builder.append((byte) '1');
        assertTrue(builder.hasUncommittedData());
    }

    @Test
    public void buildWithoutCompletingSegments_ShouldThrowIllegalStateException() {
        String ipPart = "192.168";
        for (byte c : ipPart.getBytes()) {
            builder.append(c);
        }
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    public void appendInvalidCharacter_ShouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> builder.append((byte) 'a'));
    }

    @Test
    public void appendMoreThanFourSegments_ShouldThrowIllegalStateException() {
        String ip = "192.168.1.1.1";
        for (byte c : ip.getBytes()) {
            builder.append(c);
        }
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    public void segmentValueExceeds255_ShouldThrowIllegalArgumentException() {
        String ip = "300.168.1.1";
        assertThrows(IllegalStateException.class, () -> {
            for (byte c : ip.getBytes()) {
                builder.append(c);
            }
        });
    }

    @Test
    public void build_ShouldClearStateAfterBuild() {
        String ip = "192.168.1.1";
        for (byte c : ip.getBytes()) {
            builder.append(c);
        }
        builder.build();

        assertFalse(builder.hasUncommittedData());
        assertThrows(IllegalStateException.class, builder::build);
    }

}