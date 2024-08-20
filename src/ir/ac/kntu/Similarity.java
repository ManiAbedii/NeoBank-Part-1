package ir.ac.kntu;

public class Similarity {

    public static double similarity(String str1, String str2) {
        String longer = str1, shorter = str2;
        if (str1.length() < str2.length()) { // longer should always have greater length
            longer = str2;
            shorter = str1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* both strings are zero length */
        }

        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    public static int editDistance(String str1, String str2) {
        str1 = str1.toLowerCase();
        str2 = str2.toLowerCase();

        int[] costs = new int[str2.length() + 1];
        for (int i = 0; i <= str1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= str2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (str1.charAt(i - 1) != str2.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) {
                costs[str2.length()] = lastValue;
            }
        }
        return costs[str2.length()];
    }
}
