import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
        expected = expected.divide(new BigDecimal(2));

        assertEquals(expected, result);
    }

    @Test
    public void shouldReturnMapWithMaxAmountInMonth() throws Exception {
        csvImport.add(glueStringsFirst());
        csvImport.add(glueStringsSecond());
        BigDecimal decemberAmount = new BigDecimal(firstAmount);
        csvImport.add(glueStringsThird());
        BigDecimal novemberAmount = new BigDecimal(thirdAmount);
        String[] months1999 = {"199912", "199911"};

        Map<String, BigDecimal> result = csvImport.getMaximumAmountInMonth();
        assertEquals(decemberAmount, result.get(months1999[0]));
        assertEquals(novemberAmount, result.get(months1999[1]));
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
