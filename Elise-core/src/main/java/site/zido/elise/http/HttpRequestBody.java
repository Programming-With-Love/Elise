package site.zido.elise.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * http request body,
 *
 * @author zido
 */
public class HttpRequestBody implements Serializable {
    private static final long serialVersionUID = 2018040215121L;
    private byte[] body;
    private Http.ContentType contentType;
    private String encoding;

    /**
     * Instantiates a new Http request body.
     */
    public HttpRequestBody() {
    }

    /**
     * Instantiates a new Http request body.
     *
     * @param body        the body
     * @param contentType the content type
     * @param encoding    the encoding
     */
    public HttpRequestBody(byte[] body, Http.ContentType contentType, String encoding) {
        this.body = body;
        this.contentType = contentType;
        this.encoding = encoding;
    }

    /**
     * Json http request body.
     *
     * @param json     the json
     * @param encoding the encoding
     * @return the http request body
     */
    public static HttpRequestBody json(String json, String encoding) {
        try {
            return new HttpRequestBody(json.getBytes(encoding), Http.ContentType.APPLICATION_JSON, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("illegal encoding " + encoding, e);
        }
    }

    /**
     * Xml http request body.
     *
     * @param xml      the xml
     * @param encoding the encoding
     * @return the http request body
     */
    public static HttpRequestBody xml(String xml, String encoding) {
        try {
            return new HttpRequestBody(xml.getBytes(encoding), Http.ContentType.TEXT_XML, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("illegal encoding " + encoding, e);
        }
    }

    /**
     * Custom http request body.
     *
     * @param body        the body
     * @param contentType the content type
     * @param encoding    the encoding
     * @return the http request body
     */
    public static HttpRequestBody custom(byte[] body, Http.ContentType contentType, String encoding) {
        return new HttpRequestBody(body, contentType, encoding);
    }

    /**
     * Form http request body.
     *
     * @param params   the params
     * @param encoding the encoding
     * @return the http request body
     */
    public static HttpRequestBody form(Map<String, Object> params, String encoding) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(params.size());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
        }
        try {
            return new HttpRequestBody(URLEncodedUtils.format(nameValuePairs, encoding).getBytes(encoding), Http.ContentType.APPLICATION_X_WWW_FORM_URLENCODED, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("illegal encoding " + encoding, e);
        }
    }

    /**
     * Get body byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * Sets body.
     *
     * @param body the body
     */
    public void setBody(byte[] body) {
        this.body = body;
    }

    /**
     * Gets content type.
     *
     * @return the content type
     */
    public Http.ContentType getContentType() {
        return contentType;
    }

    /**
     * Sets content type.
     *
     * @param contentType the content type
     */
    public void setContentType(Http.ContentType contentType) {
        this.contentType = contentType;
    }

    /**
     * Gets encoding.
     *
     * @return the encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Sets encoding.
     *
     * @param encoding the encoding
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

}
