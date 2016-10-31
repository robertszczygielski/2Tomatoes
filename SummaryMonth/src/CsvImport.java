import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert Szczygielski on 23.10.16.
 */
public class CsvImport {
    private Map<String, String> dates = new HashMap<>();

    public String[] getSplitLine(String toSplit) {
        String[] afterSplit = toSplit.split("\\; ");
        return afterSplit;
    }

    public void add(String toAdd) {
        String[] toMap = getSplitLine(toAdd);
        if(!dates.containsKey(toMap[0])) {
            dates.put(toMap[0], toMap[1]);
        }
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
            StringBuilder newKey = getPrepareKeyAndValue(entry);

            if(maximum.containsKey(newKey.toString())) {
                boolean isMaximum = maximum.get(newKey.toString()).compareTo(value) < 0;
                ifMonthWasRepeated(maximum, value, newKey, isMaximum);
            } else {
                ifMonthWasNotRepeated(maximum, value, newKey);
            }
        }

        return maximum;
    }


    public Map<String,BigDecimal> getMinimumAmountInMonth() {
        Map<String, BigDecimal> minimum = new HashMap<>();
        for (Map.Entry<String, String> entry : dates.entrySet()) {
            BigDecimal value = new BigDecimal(entry.getValue());
            StringBuilder newKey = getPrepareKeyAndValue(entry);

            if(minimum.containsKey(newKey.toString())) {
                boolean isMinimum = minimum.get(newKey.toString()).compareTo(value) > 0;
                ifMonthWasRepeated(minimum, value, newKey, isMinimum);
            } else {
                ifMonthWasNotRepeated(minimum, value, newKey);
            }
        }

        return minimum;
    }

    public Map<String,BigDecimal> getAverageAmountInMonth() {
        Map<String, BigDecimal> average = new HashMap<>();
        Map<String, BigDecimal> howManyAmount = new HashMap<>();

        for (Map.Entry<String, String> entry : dates.entrySet()) {
            BigDecimal value = new BigDecimal(entry.getValue());
            StringBuilder newKey = getPrepareKeyAndValue(entry);

            if(average.containsKey(newKey.toString())) {
                ifMonthWasRepeatedForAvg(average, howManyAmount, value, newKey);
            } else {
                ifMonthWasNotRepeated(average, value, newKey);
                ifMonthWasNotRepeated(howManyAmount, BigDecimal.ONE, newKey);
            }
        }

        prepareAverage(average, howManyAmount);

        return average;
    }

    private void ifMonthWasRepeated(Map<String, BigDecimal> maximum, BigDecimal value, StringBuilder newKey, boolean isMaximum) {
        if (isMaximum) {
            maximum.replace(
                    newKey.toString(),
                    value
            );
        }
    }

    private void ifMonthWasNotRepeated(Map<String, BigDecimal> minimum, BigDecimal value, StringBuilder newKey) {
        minimum.put(
                newKey.toString(),
                value
        );
    }

    private StringBuilder getPrepareKeyAndValue(Map.Entry<String, String> entry) {
        String date = entry.getKey();
        String[] splitDate = date.split("-");

        StringBuilder newKey = new StringBuilder(splitDate[0]);
        newKey.append("-")
                .append(splitDate[1]);
        return newKey;
    }

    private void ifMonthWasRepeatedForAvg(Map<String, BigDecimal> average, Map<String, BigDecimal> howManyAmount, BigDecimal value, StringBuilder newKey) {
        BigDecimal oldAmount = average.get(newKey.toString());
        value = value.add(oldAmount);

        average.replace(
                newKey.toString(),
                value
        );

        BigDecimal newCount = howManyAmount.get(newKey.toString());
        newCount = newCount.add(BigDecimal.ONE);

        howManyAmount.replace(
                newKey.toString(),
                newCount
        );
    }

    private void prepareAverage(Map<String, BigDecimal> average, Map<String, BigDecimal> howManyAmount) {
        for (Map.Entry<String, BigDecimal> entry : average.entrySet()) {
            String key = entry.getKey();
            BigDecimal newAverage = average.get(key);
            newAverage = newAverage.divide(
                    howManyAmount.get(key)
            );

            average.replace(
                    key,
                    newAverage
            );
        }
    }
}
