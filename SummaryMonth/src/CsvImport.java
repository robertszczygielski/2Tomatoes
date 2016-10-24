import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert Szczygielski on 23.10.16.
 */
public class CsvImport {
    private Map<String, String> dates = new HashMap<>();
    private String minimumPrce;

    public String[] getSplitLine(String toSplit) {
        String[] afterSplit = toSplit.split("\\, ");
        return afterSplit;
    }

    public void add(String toAdd) {
        String[] toMap = getSplitLine(toAdd);
        dates.put(toMap[0], toMap[1]);
    }

    public String getAmountForDate(String date) {
        return (String) dates.get(date);
    }

    public String getMinimumAmount() {
        BigDecimal minimum = new BigDecimal("100000");
        for (Map.Entry<String, String> entry : dates.entrySet()) {
            BigDecimal current = new BigDecimal(entry.getValue());
            if(minimum.compareTo(current) > 0) {
                minimum = current;
            }
        }
        return minimum.toString();
    }

    public String getMaximumAmount() {
        BigDecimal maximum = BigDecimal.ZERO;
        for (Map.Entry<String, String> entry : dates.entrySet()) {
            BigDecimal current = new BigDecimal(entry.getValue());
            if(maximum.compareTo(current) < 0) {
                maximum = current;
            }
        }
        return maximum.toString();
    }
}
