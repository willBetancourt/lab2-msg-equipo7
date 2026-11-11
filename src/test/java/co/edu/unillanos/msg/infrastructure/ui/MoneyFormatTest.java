package co.edu.unillanos.msg.infrastructure.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MoneyFormatTest {

    @Test
    void formatsWholeAmountWithTwoDecimals() {
        assertEquals("$ 1160.00", MoneyFormat.format(1160));
    }

    @Test
    void formatsCentsWithLeadingZero() {
        assertEquals("$ 0.50", MoneyFormat.format(0.5));
    }

    @Test
    void formatsNegativeAmounts() {
        assertEquals("$ -750.00", MoneyFormat.format(-750));
    }
}
