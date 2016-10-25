import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by Robert Szczygielski on 23.10.16.
 */
public class CsvImportTest {
    private final String dateFormat = "YYYY-MM-DD";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    private final String firstDate = "1999-12-01";
    private final String secondDate = "1999-12-02";
    private final String firstAmount = "30";
    private final String secondAmount = "10";
    private final String thirdDate = "1999-11-01";
    private final String thirdAmount = "3";

    private CsvImport csvImport;

    @Before
    public void setup() {
        csvImport = new CsvImport();
    }

    @Test
    public void shouldSplitDateAndAmount() throws Exception {
        String[] result = csvImport.getSplitLine(glueStringsFirst());

        assertEquals(firstDate, result[0]);
        assertEquals(firstAmount, result[1]);
    }


    @Test
    public void shouldReturnDateInCorrectFormat() throws Exception {
        String[] result = csvImport.getSplitLine(glueStringsFirst());
        Date date = simpleDateFormat.parse(result[0]);
        assertEquals(simpleDateFormat.parse(firstDate), date);
    }

    @Test
    public void shouldReturnAddDateAndAmount() throws Exception {
        csvImport.add(glueStringsFirst());
        String result = csvImport.getAmountForDate(firstDate);

        assertEquals(firstAmount, result);
    }

    @Test
    public void shouldReturnMinimumAmount() throws Exception {
        csvImport.add(glueStringsFirst());
        csvImport.add(glueStringsSecond());
        String result = csvImport.getMinimumAmount();

        assertEquals(secondAmount, result);
    }


    @Test
    public void shouldReturnMaximumAmount() throws Exception {
        csvImport.add(glueStringsFirst());
        csvImport.add(glueStringsSecond());
        String result = csvImport.getMaximumAmount();

        assertEquals(firstAmount, result);
    }

    @Test
    public void shouldReturnAverageAmount() throws Exception {
        csvImport.add(glueStringsFirst());
        csvImport.add(glueStringsSecond());
        BigDecimal result = csvImport.getAverageAmount();
        BigDecimal expected = new BigDecimal(firstAmount);
        expected = expected.add(new BigDecimal(secondAmount));
        expected = expected.divide(new BigDecimal("2"));

        assertEquals(expected, result);
    }

    @Test
    public void shouldReturnMapWithMaxAmountInMonth() throws Exception {
        csvImport.add(glueStringsFirst());
        csvImport.add(glueStringsSecond());
        csvImport.add(glueStringsThird());
        String[] months1999 = {"1999-12", "1999-11"};

        Map<String, BigDecimal> result = csvImport.getMaximumAmountInMonth();

        BigDecimal decemberAmount = new BigDecimal(firstAmount);
        assertEquals(decemberAmount, result.get(months1999[0]));

        BigDecimal novemberAmount = new BigDecimal(thirdAmount);
        assertEquals(novemberAmount, result.get(months1999[1]));
    }

    @Test
    public void shouldReturnMapWithMinAmountInMonth() throws Exception {
        csvImport.add(glueStringsFirst());
        csvImport.add(glueStringsSecond());
        csvImport.add(glueStringsThird());
        String[] months1999 = {"1999-12", "1999-11"};

        Map<String, BigDecimal> result = csvImport.getMinimumAmountInMonth();

        BigDecimal decemberAmount = new BigDecimal(secondAmount);
        assertEquals(decemberAmount, result.get(months1999[0]));

        BigDecimal novemberAmount = new BigDecimal(thirdAmount);
        assertEquals(novemberAmount, result.get(months1999[1]));
    }

    @Test
    public void shouldReturnAvgAmountInMonth() throws Exception {
        csvImport.add(glueStringsFirst());
        csvImport.add(glueStringsSecond());
        csvImport.add(glueStringsThird());
        String[] months1999 = {"1999-12", "1999-11"};

        Map<String, BigDecimal> result = csvImport.getAverageAmountInMonth();

        BigDecimal decemberAmount = new BigDecimal(firstAmount);
        decemberAmount = decemberAmount.add(new BigDecimal(secondAmount));
        decemberAmount = decemberAmount.divide(new BigDecimal("2"));
        assertEquals(decemberAmount, result.get(months1999[0]));

        BigDecimal novemberAmount = new BigDecimal(thirdAmount);
        assertEquals(novemberAmount, result.get(months1999[1]));
    }

    @Test
    public void shouldReturnEmptyMapForMaximum() throws Exception {
        Map<String, BigDecimal> result = csvImport.getMaximumAmountInMonth();

        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void shouldReturnEmptyMapForMinimum() throws Exception {
        Map<String, BigDecimal> result = csvImport.getMinimumAmountInMonth();

        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void shouldReturnEmptyMapForAverage() throws Exception {
        Map<String, BigDecimal> result = csvImport.getAverageAmountInMonth();

        assertEquals(Collections.emptyMap(), result);
    }

    private String glueStringsFirst() {
        return firstDate + ", " + firstAmount;
    }

    private String glueStringsSecond() {
        return secondDate + ", " + secondAmount;
    }

    private String glueStringsThird() {
        return thirdDate + ", " + thirdAmount;
    }
}
