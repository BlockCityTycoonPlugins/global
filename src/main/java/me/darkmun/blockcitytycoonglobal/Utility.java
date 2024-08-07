package me.darkmun.blockcitytycoonglobal;

import java.text.DecimalFormat;

public class Utility {
    private static final String[] units = new String[] {"тыс.", "млн.", "млрд.", "трлн.", "квдр.", "квнт.", "скст."};

    public static String formatNumber(double num) {
        String result;
        DecimalFormat df;
        df = new DecimalFormat("#.###");

        result = df.format(num);
        double curNum = num;

        for (int i = 0; curNum/1000d >= 1; i++) {
            curNum = curNum/1000d;
            result = df.format(curNum) + " " + units[i];
        }
        return result;
    }
}
