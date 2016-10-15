package org.logger;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by Robert Szczygielski on 15.10.16.
 */
public class LogTest {

    private Logger logger;
    private String testString = "test";

    @Before
    public void setup() {
        logger = new Logger();
    }

    @Test
    public void shouldReturnERRORAndTestString() throws Exception {
        logger.setLog(Logger.messState.ER, testString);
        String result = logger.getMessage();

        assertThat(result, CoreMatchers.containsString("ERROR " + testString));
    }

    @Test
    public void shouldReturnWARNAndTestString() throws Exception {
        logger.setLog(Logger.messState.WR, testString);
        String result = logger.getMessage();

        assertThat(result, CoreMatchers.containsString("WARN " + testString));
    }

    @Test
    public void shouldReturnINFOAndTestString() throws Exception {
        logger.setLog(Logger.messState.IN, testString);
        String result = logger.getMessage();

        assertThat(result, CoreMatchers.containsString("INFO " + testString));
    }

    @Test
    public void shouldReturnDateInMessage() throws Exception {
        logger.setLog(Logger.messState.IN, testString);
        String result = logger.getMessage();

        assertTrue(result.matches("\\d{4}/\\d{2}/\\d{2}\\s\\d{2}\\:\\d{2}\\:\\d{2}.*"));
    }
}
