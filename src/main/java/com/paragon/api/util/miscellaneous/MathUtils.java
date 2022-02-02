package com.paragon.api.util.miscellaneous;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {

    /**
     * Rounds a value to the nearest place
     * @param number The number to round
     * @param scale The scale
     * @return The rounded number
     */
    public static double roundDouble(double number, int scale) {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(scale, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
