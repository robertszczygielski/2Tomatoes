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
        return getCollectionWithValuesWho(true);
    }

    public Map<String,BigDecimal> getMinimumAmountInMonth() {
        return getCollectionWithValuesWho(false);
    }

    private Map<String, BigDecimal> getCollectionWithValuesWho(boolean areMaximum) {
        Map<String, BigDecimal> toReturn = new HashMap<>();
        for (Map.Entry<String, String> entry : dates.entrySet()) {
            StringBuilder newKey = getPrepareKeyAndValue(entry);
            BigDecimal firstToCompare = areMaximum
                    ? new BigDecimal(entry.getValue())
                    : toReturn.get(newKey.toString());
            BigDecimal secondToCompare = areMaximum
                    ? toReturn.get(newKey.toString())
                    : new BigDecimal(entry.getValue());

            if(toReturn.containsKey(newKey.toString())) {
                boolean isMaximum = isFirstGreater(firstToCompare, secondToCompare);
                if(isMaximum) {
                    ifMonthWasRepeated(toReturn, firstToCompare, newKey);
                }
            } else {
                ifMonthWasNotRepeated(toReturn, firstToCompare, newKey);
            }
        }
        return toReturn;
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

    private boolean isFirstGreater(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) > 0;
    }

    private void ifMonthWasRepeated(Map<String, BigDecimal> collection, BigDecimal value, StringBuilder newKey) {
        collection.replace(
                newKey.toString(),
                value
        );
    }

    private void ifMonthWasNotRepeated(Map<String, BigDecimal> collection, BigDecimal value, StringBuilder newKey) {
        collection.put(
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

        ifMonthWasRepeated(average, value, newKey);

        BigDecimal newCount = howManyAmount.get(newKey.toString());
        newCount = newCount.add(BigDecimal.ONE);

        ifMonthWasRepeated(howManyAmount, newCount, newKey);
    }

    private void prepareAverage(Map<String, BigDecimal> average, Map<String, BigDecimal> howManyAmount) {
        for (Map.Entry<String, BigDecimal> entry : average.entrySet()) {
            String key = entry.getKey();
            BigDecimal newAverage = average.get(key);
            newAverage = newAverage.divide(
                    howManyAmount.get(key)
            );

            ifMonthWasRepeated(average, newAverage, new StringBuilder(key));
        }
    }
}
