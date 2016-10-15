package org.sandwich;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Robert Szczygielski on 15.10.16.
 */
public class BarTest {

    private Bar bar;

    @Before
    public void setup() {
        bar = new Bar();
    }

    @Test
    public void shouldReturnEmptyStringFor1xBread() {
        String result = bar.getSandwich("Bread");

        assertEquals("", result);
    }

    @Test
    public void shouldReturnEmptyStringFor1xNotClearBread() {
        String result = bar.getSandwich("XXXBread");

        assertEquals("", result);
    }

    @Test
    public void shouldReturnEmptyStringForEmptySandwich() {
        String result = bar.getSandwich("");

        assertEquals("", result);
    }

    @Test
    public void shouldReturnFishForComponentAnd1Bread() {
        String result = bar.getSandwich("BreadFish");

        assertEquals("fish", result);
    }

    @Test
    public void shouldReturnFishChipsForComponentAnd1Bread() {
        String result = bar.getSandwich("BreadFishChips");

        assertEquals("fishchips", result);
    }

    @Test
    public void shouldReturnFishAnd2Bread() {
        String result = bar.getSandwich("BreadFishBread");

        assertEquals("fish", result);
    }

    @Test
    public void shouldReturnFishChipsAndNotCleanBread() {
        String result = bar.getSandwich("xxxxBreadFishChipsbread");

        assertEquals("fishchips", result);
    }

    @Test
    public void shouldReturnFishChipsForSmallSandwich() {
        String result = bar.getSandwich("breadfishchipsbread");

        assertEquals("fishchips", result);
    }

    @Test
    public void shouldReturnFishChipsForBigSandwich() {
        String result  = bar.getSandwich("BREADFISHCHIPSBREAD");

        assertEquals("fishchips", result);
    }
}
