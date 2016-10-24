import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert Szczygielski on 23.10.16.
 */
public class CsvImport {
    private Map<String, String> dates = new HashMap<>();

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

    public BigDecimal getAverageAmount() {
        BigDecimal count = BigDecimal.ZERO;
        BigDecimal iter = BigDecimal.ZERO;
        for (Map.Entry<String, String> entry : dates.entrySet()) {
            BigDecimal amount = new BigDecimal(entry.getValue());
            count = count.add(amount);
            iter = iter.add(BigDecimal.ONE);
        }
        return count.divide(iter);
    }

    public Map<String, BigDecimal> getMaximumAmountInMonth() {
        Map<String, BigDecimal> maximum = new HashMap<>();
        for (Map.Entry<String, String> entry : dates.entrySet()) {
            BigDecimal value = new BigDecimal(entry.getValue());
            String date = entry.getKey();
            String[] splitDate = date.split("-");
            String newKey = splitDate[0] + splitDate[1];

            if(maximum.containsKey(newKey)) {
                if (maximum.get(newKey).compareTo(value) < 0) {
                    maximum.replace(newKey, value);
                }
            } else {
                maximum.put(newKey, value);
            }
        }

        return maximum;
    }
}
