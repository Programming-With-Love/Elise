package site.zido.elise.matcher;

import site.zido.elise.utils.Asserts;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * a matcher for judging values
 *
 * @author zido
 */
public class NumberExpressMatcher implements EliseMatcher {

    private static final Pattern CHECK_PATTERN = Pattern.compile("^[1-9,<-]*$");
    private List<Region> regions;
    private List<Integer> rows;

    public NumberExpressMatcher(String express) {
        this(express, '<');
    }

    public NumberExpressMatcher(String express, char sep) {
        Asserts.hasLength(express, "express can't be null or empty");
        if ((sep == '<' && !CHECK_PATTERN.matcher(express).find()) || !express.matches("^[1-9," + sep + "-]*$")) {
            throw new IllegalArgumentException("express only can contains [1-9," + sep + "-]");
        }
        regions = new ArrayList<>();
        rows = new ArrayList<>();
        String[] segments = express.split(",");
        for (String segment : segments) {
            if (segment.indexOf(sep) >= 0) {
                Region region = new Region();
                char[] chars = segment.toCharArray();
                if (chars[0] == sep && chars.length > 1) {
                    region.max = Integer.parseInt(new String(chars, 1, chars.length));
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
                                throw new IllegalArgumentException("a child express can not contains two or more [" + sep + "] character");
                            }
                            tempNumberBuilder.append(ch);
                        }
                        String s = tempNumberBuilder.toString();
                        region.max = Integer.parseInt(s);
                    }
                }
                regions.add(region);
            } else {
                Asserts.hasLength(segment, "every number or segment must be split by [,]");
                rows.add(Integer.valueOf(segment));
            }
        }
    }

    private static class Region {
        private int max = Integer.MAX_VALUE;
        private int min = Integer.MIN_VALUE;
    }

    @Override
    public boolean matches(Object target) {
        if (!(target instanceof Number)) {
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

}
