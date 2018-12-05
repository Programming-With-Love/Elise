package site.zido.elise.select.matcher;

import site.zido.elise.select.Matcher;
import site.zido.elise.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * a select for judging int values
 *
 * @author zido
 */
public class NumberExpressMatcher implements Matcher {

    private static final Pattern CHECK_PATTERN = Pattern.compile("^[0-9,<-]*$");
    private static final char DEFAULT_CHAR_SEP = '<';
    private static final String PATTERN_TEMPLATE = "^[0-9,%s-]*$";
    private List<Region> regions;
    private List<Integer> rows;

    /**
     * Instantiates a new Number express matcher.
     *
     * @param express the express
     * @throws CompilerException the compiler exception
     */
    public NumberExpressMatcher(String express) throws CompilerException {
        this(express, DEFAULT_CHAR_SEP);
    }

    public static boolean isSupport(String express) {
        return isSupport(express, DEFAULT_CHAR_SEP);
    }

    public static boolean isSupport(String express, char sep) {
        if (!StringUtils.hasLength(express)) {
            return false;
        }
        if (sep == DEFAULT_CHAR_SEP && !CHECK_PATTERN.matcher(express).find()) {
            return false;
        } else if (!express.matches(String.format(PATTERN_TEMPLATE, sep))) {
            return false;
        }
        return true;
    }

    /**
     * Instantiates a new Number express matcher.
     *
     * @param express the express
     * @param sep     the sep
     * @throws CompilerException the compiler exception
     */
    public NumberExpressMatcher(String express, char sep) throws CompilerException {
        if (!StringUtils.hasLength(express)) {
            throw new CompilerException("express can't be null or empty");
        }
        if (sep == DEFAULT_CHAR_SEP && !CHECK_PATTERN.matcher(express).find()) {
            throw new CompilerException("express only can contains [0-9," + sep + "-]");
        } else if (!express.matches(String.format(PATTERN_TEMPLATE, sep))) {
            throw new CompilerException("express only can contains [0-9," + sep + "-]");
        }
        regions = new ArrayList<>();
        rows = new ArrayList<>();
        String[] segments = express.split(",");
        for (String segment : segments) {
            if (segment.indexOf(sep) >= 0) {
                Region region = new Region();
                char[] chars = segment.toCharArray();
                if (chars[0] == sep && chars.length > 1) {
                    region.max = Integer.parseInt(new String(chars, 1, chars.length - 1));
                } else {
                    StringBuilder tempNumberBuilder = new StringBuilder(chars.length - 1);
                    int i = 0;
                    for (; i < chars.length; i++) {
                        char ch = chars[i];
                        if (ch == sep) {
                            String s = tempNumberBuilder.toString();
                            region.min = Integer.parseInt(s);
                            tempNumberBuilder.delete(0, tempNumberBuilder.length());
                            break;
                        }
                        tempNumberBuilder.append(ch);
                    }
                    if (i < chars.length - 1) {
                        for (++i; i < chars.length; i++) {
                            char ch = chars[i];
                            if (ch == sep) {
                                throw new CompilerException("a child express can not contains two or more [" + sep + "] character");
                            }
                            tempNumberBuilder.append(ch);
                        }
                        String s = tempNumberBuilder.toString();
                        region.max = Integer.parseInt(s);
                    }
                }
                regions.add(region);
            } else {
                if (!StringUtils.hasLength(segment)) {
                    throw new CompilerException("every number or segment must be split by [,]");
                }
                rows.add(Integer.valueOf(segment));
            }
        }
        for (Region region : regions) {
            if (region.max < region.min) {
                region.max = region.max ^ region.min;
                region.min = region.max ^ region.min;
                region.max = region.max ^ region.min;
            }
        }
    }

    @Override
    public boolean matches(Object target) {
        if (!(target instanceof java.lang.Number)) {
            return false;
        }
        for (Integer row : rows) {
            if (row.equals(target)) {
                return true;
            }
        }
        for (Region region : regions) {
            if (region.min <= (Integer) target && region.max >= (Integer) target) {
                return true;
            }
        }
        return false;
    }

    private static class Region {
        private int max = Integer.MAX_VALUE;
        private int min = Integer.MIN_VALUE;
    }

}
