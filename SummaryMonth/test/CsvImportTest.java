import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by Robert Szczygielski on 23.10.16.
 */
public class CsvImportTest {
    private final String dateFormat = "YYYY-MM-DD";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    private final String firstDate = "1999-12-01";
    private final String secendDate = "1999-12-02";
    private final String firstAmount = "30";
    private final String secendAmount = "10";

    @Test
    public void shouldSplitDateAndAmount() throws Exception {
        CsvImport csvImport = new CsvImport();
        String[] result = csvImport.getSplitLine(firstDate + ", " + firstAmount);

        assertEquals(firstDate, result[0]);
        assertEquals(firstAmount, result[1]);
    }

    @Test
    public void shouldReturnDateInCorrectFormat() throws Exception {
        CsvImport csvImport = new CsvImport();
        String[] result = csvImport.getSplitLine(firstDate + ", " + firstAmount);
        Date date = simpleDateFormat.parse(result[0]);
        assertEquals(simpleDateFormat.parse(firstDate), date);
    }

    @Test
    public void shouldReturnAddDateAndAmount() throws Exception {
        CsvImport csvImport = new CsvImport();
        csvImport.add(firstDate + ", " + firstAmount);
        String result = csvImport.getAmountForDate(firstDate);

        assertEquals(firstAmount, result);
    }

    @Test
    public void shouldReturnMinimumAmount() throws Exception {
        CsvImport csvImport = new CsvImport();
        csvImport.add(firstDate + ", " + firstAmount);
        csvImport.add(secendDate + ", " + secendAmount);
        String result = csvImport.getMinimumAmount();

        assertEquals(secendAmount, result);
    }

    @Test
    public void shouldReturnMaximumAmount() throws Exception {
        CsvImport csvImport = new CsvImport();
        csvImport.add(firstDate + ", " + firstAmount);
        csvImport.add(secendDate + ", " + secendAmount);
        String result = csvImport.getMaximumAmount();

        assertEquals(firstAmount, result);

    }
}
