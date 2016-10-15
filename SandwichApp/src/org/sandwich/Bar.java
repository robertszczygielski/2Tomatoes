package org.sandwich;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Robert Szczygielski on 15.10.16.
 */
public class Bar {
    private static final java.lang.String BREAD = "BREAD";

    public String getSandwich(String bread) {
        String[] afterSplit = bread.toUpperCase().split(BREAD);

        if(afterSplit.length > 1) {
            return afterSplit[1].toLowerCase();
        } else if(afterSplit.length == 1) {
            return afterSplit[0].toLowerCase();
        }
        return "";
    }
}
