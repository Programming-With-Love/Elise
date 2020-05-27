package site.zido.elise.select;

import site.zido.elise.processor.ResponseContextHolder;
import site.zido.elise.task.api.Source;
import site.zido.elise.task.model.Action;
import site.zido.elise.utils.Safe;
import site.zido.elise.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * a select for judging int values
 *
 * @author zido
 */
public class NumberMatcherSelectHandler implements SelectHandler {

    private static final Pattern CHECK_PATTERN = Pattern.compile("^[0-9,<-]*$");
    private static final char DEFAULT_CHAR_SEP = '<';
    private static final String PATTERN_TEMPLATE = "^[0-9,%s-]*$";

    /**
     * Is support boolean.
     *
     * @param express the express
     * @return the boolean
     */
    public static boolean isSupport(String express) {
        return isSupport(express, DEFAULT_CHAR_SEP);
    }

    /**
     * Is support boolean.
     *
     * @param express the express
     * @param sep     the sep
     * @return the boolean
     */
    public static boolean isSupport(String express, char sep) {
        if (!StringUtils.hasLength(express)) {
            return false;
        }
        if (sep == DEFAULT_CHAR_SEP && !CHECK_PATTERN.matcher(express).find()) {
            return false;
        }
        return express.matches(String.format(PATTERN_TEMPLATE, sep));
    }

    @Override
    public List<Object> select(ResponseContextHolder response, Object partition, Action action) throws SelectorMatchException {
        Object[] extras = action.getExtras();
        String express = Safe.getStrFromArray(extras, 0);
        if ("".equals(express)) {
            throw new SelectorMatchException(String.format("the action: [%s] need a string express like [200<300] means [ 200 <= number <= 300] but get %s", action.getToken(), extras[0]));
        }
        String source = action.getSource();
        Integer target;
        if (Source.matchSource(source, Source.CODE)) {
            target = response.getStatusCode();
        } else {
            throw new SelectorMatchException("match number just support response code");
        }
        char sep = Safe.getCharFromArray(extras, 0, '<');
        if (sep == DEFAULT_CHAR_SEP && !CHECK_PATTERN.matcher(express).find()) {
            throw new SelectorMatchException("express only can contains [0-9," + sep + "-]");
        } else if (!express.matches(String.format(PATTERN_TEMPLATE, sep))) {
            throw new SelectorMatchException("express only can contains [0-9," + sep + "-]");
        }
        List<Region> regions = new ArrayList<>();
        List<Integer> rows = new ArrayList<>();
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
                                throw new RuntimeException("a child express can not contains two or more [" + sep + "] character");
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
                    throw new RuntimeException("every number or segment must be split by [,]");
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
        for (Integer row : rows) {
            if (row.equals(target)) {
                return Collections.singletonList(target);
            }
        }
        for (Region region : regions) {
            if (region.min <= target && region.max >= target) {
                return Collections.singletonList(target);
            }
        }
        return null;
    }

    private static class Region {
        private int max = Integer.MAX_VALUE;
        private int min = Integer.MIN_VALUE;
    }

}
