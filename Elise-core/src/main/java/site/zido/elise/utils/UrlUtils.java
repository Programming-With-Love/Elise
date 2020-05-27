package site.zido.elise.utils;

import site.zido.elise.E;
import site.zido.elise.http.Pair;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Url utils.
 *
 * @author zido
 */
public class UrlUtils {
    private final static String QUERY_START_CHARACTER = "?";
    private static final int RADIX = 16;
    private static final String NAME_VALUE_SEPARATOR = "=";
    private static final char AND_SEPARATOR = '&';
    /**
     * Unreserved characters, i.e. alphanumeric, plus: {@code _ - ! . ~ ' ( ) *}
     * <p>
     * This list is the same as the {@code unreserved} list in
     * <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC 2396</a>
     */
    private static final BitSet UNRESERVED = new BitSet(256);
    /**
     * Punctuation characters: , ; : $ & + =
     * <p>
     * These are the additional characters allowed by userinfo.
     */
    private static final BitSet PUNCT = new BitSet(256);
    /**
     * Characters which are safe to use in userinfo,
     * i.e. {@link #UNRESERVED} plus {@link #PUNCT}uation
     */
    private static final BitSet USERINFO = new BitSet(256);
    /**
     * Characters which are safe to use in a path,
     * i.e. {@link #UNRESERVED} plus {@link #PUNCT}uation plus / @
     */
    private static final BitSet PATHSAFE = new BitSet(256);
    /**
     * Characters which are safe to use in a query or a fragment,
     * i.e. {@link #RESERVED} plus {@link #UNRESERVED}
     */
    private static final BitSet URIC = new BitSet(256);
    /**
     * Reserved characters, i.e. {@code ;/?:@&=+$,[]}
     * <p>
     * This list is the same as the {@code reserved} list in
     * <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC 2396</a>
     * as augmented by
     * <a href="http://www.ietf.org/rfc/rfc2732.txt">RFC 2732</a>
     */
    private static final BitSet RESERVED = new BitSet(256);
    /**
     * Safe characters for x-www-form-urlencoded data, as per java.net.URLEncoder and browser behaviour,
     * i.e. alphanumeric plus {@code "-", "_", ".", "*"}
     */
    private static final BitSet URLENCODER = new BitSet(256);
    private static Pattern patternForProtocol = Pattern.compile("[\\w]+://");

    static {
        // unreserved chars
        // alpha characters
        for (int i = 'a'; i <= 'z'; i++) {
            UNRESERVED.set(i);
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            UNRESERVED.set(i);
        }
        // numeric characters
        for (int i = '0'; i <= '9'; i++) {
            UNRESERVED.set(i);
        }
        UNRESERVED.set('_'); // these are the charactes of the "mark" list
        UNRESERVED.set('-');
        UNRESERVED.set('.');
        UNRESERVED.set('*');
        URLENCODER.or(UNRESERVED); // skip remaining unreserved characters
        UNRESERVED.set('!');
        UNRESERVED.set('~');
        UNRESERVED.set('\'');
        UNRESERVED.set('(');
        UNRESERVED.set(')');
        // punct chars
        PUNCT.set(',');
        PUNCT.set(';');
        PUNCT.set(':');
        PUNCT.set('$');
        PUNCT.set('&');
        PUNCT.set('+');
        PUNCT.set('=');
        // Safe for userinfo
        USERINFO.or(UNRESERVED);
        USERINFO.or(PUNCT);

        // URL path safe
        PATHSAFE.or(UNRESERVED);
        PATHSAFE.set('/'); // segment separator
        PATHSAFE.set(';'); // param separator
        PATHSAFE.set(':'); // rest as per list in 2396, i.e. : @ & = + $ ,
        PATHSAFE.set('@');
        PATHSAFE.set('&');
        PATHSAFE.set('=');
        PATHSAFE.set('+');
        PATHSAFE.set('$');
        PATHSAFE.set(',');

        RESERVED.set(';');
        RESERVED.set('/');
        RESERVED.set('?');
        RESERVED.set(':');
        RESERVED.set('@');
        RESERVED.set('&');
        RESERVED.set('=');
        RESERVED.set('+');
        RESERVED.set('$');
        RESERVED.set(',');
        RESERVED.set('['); // added by RFC 2732
        RESERVED.set(']'); // added by RFC 2732

        URIC.or(RESERVED);
        URIC.or(UNRESERVED);
    }

    /**
     * Encode format string.
     *
     * @param parameters the parameters
     * @param charset    the charset
     * @return the string
     */
    public static String encodeFormat(final List<? extends Pair> parameters, final Charset charset) {
        return encodeFormat(parameters, AND_SEPARATOR, charset);
    }

    /**
     * Encode format string.
     *
     * @param parameters         the parameters
     * @param parameterSeparator the parameter separator
     * @param charset            the charset
     * @return the string
     */
    public static String encodeFormat(final List<? extends Pair> parameters,
                                      final char parameterSeparator,
                                      final Charset charset) {
        final StringBuilder result = new StringBuilder();
        for (final Pair parameter : parameters) {
            final String encodedName = encodeFormFields(parameter.getName(), charset);
            final String encodedValue = encodeFormFields(parameter.getValue(), charset);
            if (result.length() > 0) {
                result.append(parameterSeparator);
            }
            result.append(encodedName);
            if (encodedValue != null) {
                result.append(NAME_VALUE_SEPARATOR);
                result.append(encodedValue);
            }
        }
        return result.toString();
    }

    private static String encodeFormFields(final String content, final Charset charset) {
        if (content == null) {
            return null;
        }
        return urlEncode(content, charset != null ? charset : E.UTF_8, URLENCODER, true);
    }

    /**
     * Url encode string.
     *
     * @param content the content
     * @param charset the charset
     * @return the string
     */
    public static String urlEncode(final String content, final Charset charset) {
        return urlEncode(content, charset != null ? charset : E.UTF_8, URLENCODER, true);
    }

    private static String urlEncode(
        final String content,
        final Charset charset,
        final BitSet safechars,
        final boolean blankAsPlus) {
        if (content == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder();
        final ByteBuffer bb = charset.encode(content);
        while (bb.hasRemaining()) {
            final int b = bb.get() & 0xff;
            if (safechars.get(b)) {
                buf.append((char) b);
            } else if (blankAsPlus && b == ' ') {
                buf.append('+');
            } else {
                buf.append("%");
                final char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, RADIX));
                final char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, RADIX));
                buf.append(hex1);
                buf.append(hex2);
            }
        }
        return buf.toString();
    }

    /**
     * 规范url
     *
     * @param url   url
     * @param refer refer
     * @return 规范化后的url string
     */
    public static String canonicalizeUrl(String url, String refer) {
        URL base;
        try {
            try {
                base = new URL(refer);
            } catch (MalformedURLException e) {
                // the base is unsuitable, but the attribute may be abs on its own, so try that
                URL abs = new URL(refer);
                return abs.toExternalForm();
            }
            // workaround: java resolves '//path/file + ?foo' to '//path/?foo', not '//path/file?foo' as desired
            if (url.startsWith(QUERY_START_CHARACTER)) {
                url = base.getPath() + url;
            }
            URL abs = new URL(base, url);
            return abs.toExternalForm();
        } catch (MalformedURLException e) {
            return "";
        }
    }

    /**
     * Fix illegal character in url string.
     *
     * @param url the url
     * @return the string
     */
    public static String fixIllegalCharacterInUrl(String url) {
        return url.replace(" ", "%20").replaceAll("#+", "#");
    }

    /**
     * Remove protocol string.
     *
     * @param url the url
     * @return the string
     */
    public static String removeProtocol(String url) {
        return patternForProtocol.matcher(url).replaceAll("");
    }

    /**
     * Gets domain.
     *
     * @param url the url
     * @return the domain
     */
    public static String getDomain(String url) {
        String domain = removeProtocol(url);
        int i = domain.indexOf("/", 1);
        if (i > 0) {
            domain = domain.substring(0, i);
        }
        return removePort(domain);
    }

    /**
     * Remove port string.
     *
     * @param domain the domain
     * @return the string
     */
    public static String removePort(String domain) {
        int portIndex = domain.indexOf(":");
        if (portIndex != -1) {
            return domain.substring(0, portIndex);
        } else {
            return domain;
        }
    }


}
