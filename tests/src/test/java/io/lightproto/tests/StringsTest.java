package io.lightproto.tests;

import com.google.protobuf.CodedOutputStream;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class StringsTest {

    private byte[] b1 = new byte[4096];
    private ByteBuf bb1 = Unpooled.wrappedBuffer(b1);

    private byte[] b2 = new byte[4096];

    @BeforeEach
    public void setup() {
        bb1.clear();
        Arrays.fill(b1, (byte) 0);
        Arrays.fill(b2, (byte) 0);
    }

    @Test
    public void testStrings() throws Exception {
        LightProtoStrings.S lps = new LightProtoStrings.S()
                .setId("id");
        lps.addName("a");
        lps.addName("b");
        lps.addName("c");

        assertEquals("id", lps.getId());
        assertTrue(lps.hasNames());
        assertEquals(3, lps.getNamesCount());
        assertEquals("a", lps.getNameAt(0));
        assertEquals("b", lps.getNameAt(1));
        assertEquals("c", lps.getNameAt(2));

        Strings.S pbs = Strings.S.newBuilder()
                .setId("id")
                .addNames("a")
                .addNames("b")
                .addNames("c")
                .build();

        assertEquals(pbs.getSerializedSize(), lps.getSerializedSize());

        lps.writeTo(bb1);
        assertEquals(lps.getSerializedSize(), bb1.readableBytes());

        pbs.writeTo(CodedOutputStream.newInstance(b2));

        assertArrayEquals(b1, b2);

        LightProtoStrings.S parsed = new LightProtoStrings.S();
        parsed.parseFrom(bb1, bb1.readableBytes());

        assertEquals("id", parsed.getId());
        assertTrue(parsed.hasNames());
        assertEquals(3, parsed.getNamesCount());
        assertEquals("a", parsed.getNameAt(0));
        assertEquals("b", parsed.getNameAt(1));
        assertEquals("c", parsed.getNameAt(2));
    }
}
