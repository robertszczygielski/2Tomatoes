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
        String[] result = csvImport.getSplitLine(glueStrings(firstDate, firstAmount));

        assertEquals(firstDate, result[0]);
        assertEquals(firstAmount, result[1]);
    }


    @Test
    public void shouldReturnDateInCorrectFormat() throws Exception {
        String[] result = csvImport.getSplitLine(glueStrings(firstDate, firstAmount));
        Date date = simpleDateFormat.parse(result[0]);
        assertEquals(simpleDateFormat.parse(firstDate), date);
    }

    @Test
    public void shouldReturnAddDateAndAmount() throws Exception {
        csvImport.add(glueStrings(firstDate, firstAmount));
        String result = csvImport.getAmountForDate(firstDate);

        assertEquals(firstAmount, result);
    }

    @Test
    public void shouldReturnMinimumAmount() throws Exception {
        csvImport.add(glueStrings(firstDate, firstAmount));
        csvImport.add(glueStrings(secondDate, secondAmount));
        String result = csvImport.getMinimumAmount();

        assertEquals(secondAmount, result);
    }


    @Test
    public void shouldReturnMaximumAmount() throws Exception {
        csvImport.add(glueStrings(firstDate, firstAmount));
        csvImport.add(glueStrings(secondDate, secondAmount));
        String result = csvImport.getMaximumAmount();

        assertEquals(firstAmount, result);
    }

    @Test
    public void shouldReturnAverageAmount() throws Exception {
        csvImport.add(glueStrings(firstDate, firstAmount));
        csvImport.add(glueStrings(secondDate, secondAmount));
        BigDecimal result = csvImport.getAverageAmount();
        BigDecimal expected = new BigDecimal(firstAmount);
        expected = expected.add(new BigDecimal(secondAmount));
        expected = expected.divide(new BigDecimal("2"));

        assertEquals(expected, result);
    }

    @Test
    public void shouldReturnMapWithMaxAmountInMonth() throws Exception {
        csvImport.add(glueStrings(firstDate, firstAmount));
        csvImport.add(glueStrings(secondDate, secondAmount));
        csvImport.add(glueStrings(thirdDate, thirdAmount));
        String[] months1999 = {"1999-12", "1999-11"};

        Map<String, BigDecimal> result = csvImport.getMaximumAmountInMonth();

        BigDecimal decemberAmount = new BigDecimal(firstAmount);
        assertEquals(decemberAmount, result.get(months1999[0]));

        BigDecimal novemberAmount = new BigDecimal(thirdAmount);
        assertEquals(novemberAmount, result.get(months1999[1]));
    }

    @Test
    public void shouldReturnMapWithMinAmountInMonth() throws Exception {
        csvImport.add(glueStrings(firstDate, firstAmount));
        csvImport.add(glueStrings(secondDate, secondAmount));
        csvImport.add(glueStrings(thirdDate, thirdAmount));
        String[] months1999 = {"1999-12", "1999-11"};

        Map<String, BigDecimal> result = csvImport.getMinimumAmountInMonth();

        BigDecimal decemberAmount = new BigDecimal(secondAmount);
        assertEquals(decemberAmount, result.get(months1999[0]));

        BigDecimal novemberAmount = new BigDecimal(thirdAmount);
        assertEquals(novemberAmount, result.get(months1999[1]));
    }

    @Test
    public void shouldReturnAvgAmountInMonth() throws Exception {
        csvImport.add(glueStrings(firstDate, firstAmount));
        csvImport.add(glueStrings(secondDate, secondAmount));
        csvImport.add(glueStrings(thirdDate, thirdAmount));
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

    @Test
    public void shouldReturnMaximumFirstValueForDuplicatePrices() throws Exception {
        csvImport.add(glueStrings(firstDate, firstAmount));
        csvImport.add(glueStrings(firstDate, thirdAmount));
        String[] months1999 = {"1999-12"};

        Map<String, BigDecimal> result = csvImport.getMaximumAmountInMonth();

        BigDecimal decemberAmount = new BigDecimal(firstAmount);
        assertEquals(decemberAmount, result.get(months1999[0]));
    }

    @Test
    public void shouldReturnMinimumFirstValueForDuplicatePrices() throws Exception {
        csvImport.add(glueStrings(firstDate, firstAmount));
        csvImport.add(glueStrings(firstDate, thirdAmount));
        String[] months1999 = {"1999-12"};

        Map<String, BigDecimal> result = csvImport.getMinimumAmountInMonth();

        BigDecimal decemberAmount = new BigDecimal(firstAmount);
        assertEquals(decemberAmount, result.get(months1999[0]));
    }

    private String glueStrings(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            sb.append(string);
            sb.append("; ");
        }

        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }
}
