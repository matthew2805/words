

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class StringUtil {
    public static int countEqualIgnoreCaseAndSpaces(String[] words, String sample) {
        if (words == null || sample == null || sample.isEmpty()) return 0;
        var k = 0;
        for (String str : words) if (str.strip().equalsIgnoreCase(sample.strip())) k++;
        return k;
    }

    public static String[] splitWords(String text) {
        Pattern pattern = Pattern.compile("[,.;: ?!]");
        return (text == null
                || text.isEmpty()
                || Pattern.matches("[,.;: ?!]+", text))
                ? null : Arrays.stream(pattern.split(text)).
                filter(Predicate.not(String::isEmpty)).toArray(String[]::new);
    }

    public static String convertPath(String path, boolean toWin) {
        if (path == null || path.isEmpty()) return null;
        Pattern unix = Pattern.
                compile("^([\\w/]|~/?|\\.\\.?/|\\.\\.?$)(((\\w+ ?(\\.\\w{3})?|(/\\.{2}))/?)+)?$");
        Pattern win = Pattern.
                compile("^([\\w\\\\]|C:\\\\|\\.\\.?\\\\|\\.\\.?$)(((\\w+ ?(\\.\\w{3})?|(\\\\\\.{2}))\\\\?)+)?$");
        if (Pattern.matches(unix.pattern(), path)
                || Pattern.matches(win.pattern(), path))
            return toWin ? convertToWin(path) : convertToUnix(path);
        else return null;
    }

    private static String convertToWin(String path) {
        return path.replaceAll("^~", "C:\\\\User").
                replaceAll("^/", "C:\\\\").
                replaceAll("/", "\\\\");
    }

    private static String convertToUnix(String path) {
        return path.replaceAll("^C:\\\\User", "~").
                replaceAll("^C:\\\\", "/").
                replaceAll("\\\\", "/");

    }

    public static String joinWords(String[] words) {
        if (words == null || words.length == 0
                || Arrays.stream(words).allMatch(String::isEmpty)) return null;
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (String str : words) if (!str.isEmpty()) joiner.add(str);
        return joiner.toString();
    }

    public static void main(String[] args) {
        System.out.println("Test 1: countEqualIgnoreCaseAndSpaces");
        String[] words = new String[]{" WordS    \t", "words", "w0rds", "WOR  DS",};
        String sample = "words   ";
        int countResult = countEqualIgnoreCaseAndSpaces(words, sample);
        System.out.println("Result: " + countResult);
        int expectedCount = 2;
        System.out.println("Must be: " + expectedCount);

        System.out.println("Test 2: splitWords");
        String text = "   ,, first, second!!!! third";
        String[] splitResult = splitWords(text);
        System.out.println("Result : " + Arrays.toString(splitResult));
        String[] expectedSplit = new String[]{"first", "second", "third"};
        System.out.println("Must be: " + Arrays.toString(expectedSplit));

        System.out.println("Test 3: convertPath");
        String unixPath = "/some/unix/path";
        String convertResult = convertPath(unixPath, true);
        System.out.println("Result: " + convertResult);
        String expectedWinPath = "C:\\some\\unix\\path";
        System.out.println("Must be: " + expectedWinPath);

        System.out.println("Test 4: joinWords");
        String[] toJoin = new String[]{"go", "with", "the", "", "FLOW"};
        String joinResult = joinWords(toJoin);
        System.out.println("Result: " + joinResult);
        String expectedJoin = "[go, with, the, FLOW]";
        System.out.println("Must be: " + expectedJoin);
    }
}