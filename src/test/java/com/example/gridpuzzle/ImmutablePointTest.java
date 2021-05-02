package com.example.gridpuzzle;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class ImmutablePointTest {

    @Test
    public void testEqualsHashCode() {
        EqualsVerifier.simple()
                .forClass(ImmutablePoint.class)
                .verify();
    }
}
