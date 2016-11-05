import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Robert Szczygielski on 23.10.16.
 */
public class CsvImport {
    private Map<String, BigDecimal> dates = new HashMap<>();

    public String[] getSplitLine(String toSplit) {
        String[] afterSplit = toSplit.split("\\; ");
        return afterSplit;
    }

    public void add(String toAdd) {
        String[] toMap = getSplitLine(toAdd);
        if(!dates.containsKey(toMap[0])) {
            dates.put(toMap[0], new BigDecimal(toMap[1]));
        }
    }

    public BigDecimal getAmountForDate(String date) {
        return dates.get(date);
    }

    public BigDecimal getMinimumAmount() {
        return lookForMaximumValueInCollection(false);
    }

    public BigDecimal getMaximumAmount() {
        return lookForMaximumValueInCollection(true);
    }

    private BigDecimal lookForMaximumValueInCollection(boolean lookMax) {
        BigDecimal toReturn = null;
        for (Map.Entry<String, BigDecimal> entry : dates.entrySet()) {
            BigDecimal current = entry.getValue();
            boolean shouldBeMaximum = lookMax
                    ? compareAndCheckNull(toReturn, current) < 0
                    : compareAndCheckNull(current, toReturn) < 0;
            if(toReturn == null || shouldBeMaximum) {
                toReturn = current;
            }
        }
        return toReturn;
    }

    private <T extends Comparable> int compareAndCheckNull(T a, T b) {
        if (a == null || b == null) {
            return 0;
        }
        return a.compareTo(b);
    }

    public BigDecimal getAverageAmount() {
        BigDecimal count = dates.entrySet()
                                .stream()
                                .map(e -> e.getValue())
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal allElements = new BigDecimal(dates.size());

        return count.divide(allElements);
    }

    public Map<String, BigDecimal> getMaximumAmountInMonth() {
        return getCollectionWithValuesWho(true);
    }

    public Map<String,BigDecimal> getMinimumAmountInMonth() {
        return getCollectionWithValuesWho(false);
    }

    private Map<String, BigDecimal> getCollectionWithValuesWho(boolean areMaximum) {
        Map<String, BigDecimal> toReturn = new HashMap<>();
        for (Map.Entry<String, BigDecimal> entry : dates.entrySet()) {
            StringBuilder newKey = getPrepareKeyAndValue(entry);
            BigDecimal firstToCompare = areMaximum
                    ? entry.getValue()
                    : toReturn.get(newKey.toString());
            BigDecimal secondToCompare = areMaximum
                    ? toReturn.get(newKey.toString())
                    : entry.getValue();

            if(toReturn.containsKey(newKey.toString())) {
                boolean isMaximum = isFirstGreater(firstToCompare, secondToCompare);
                if(isMaximum) {
                    ifMonthWasRepeated(toReturn, firstToCompare, newKey);
                }
            } else {
                ifMonthWasNotRepeated(toReturn, areMaximum ? firstToCompare : secondToCompare, newKey);
            }
        }
        return toReturn;
    }

    public Map<String, BigDecimal> getAverageAmountInMonth() {
        Map<String, BigDecimal> average = new HashMap<>();
        Map<String, BigDecimal> howManyAmount = new HashMap<>();

        for (Map.Entry<String, BigDecimal> entry : dates.entrySet()) {
            BigDecimal value = entry.getValue();
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

    private StringBuilder getPrepareKeyAndValue(Map.Entry<String, BigDecimal> entry) {
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
