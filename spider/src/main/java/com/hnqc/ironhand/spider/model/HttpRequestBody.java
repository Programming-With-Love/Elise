package com.hnqc.ironhand.spider.model;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 请求体描述
 *
 * @author zido
 * @date 2018/35/12
 */
public class HttpRequestBody implements Serializable {
    private static final long serialVersionUID = 2018040215121L;

    /**
     * content类型
     */
    public static enum ContentType {
        /**
         * json
         */
        JSON("application/json"),
        /**
         * xml
         */
        XML("text/xml"),
        /**
         * form
         */
        FORM("application/x-www-form-urlencoded"),
        /**
         * 文件/file
         */
        MULTIPART("multipart/form-data");

        private String value;

        private ContentType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private byte[] body;

    private ContentType contentType;

    private String encoding;

    public HttpRequestBody() {
    }

    public HttpRequestBody(byte[] body, ContentType contentType, String encoding) {
        this.body = body;
        this.contentType = contentType;
        this.encoding = encoding;
    }

    public byte[] getBody() {
        return body;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public static HttpRequestBody json(String json, String encoding) {
        try {
            return new HttpRequestBody(json.getBytes(encoding), ContentType.JSON, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("illegal encoding " + encoding, e);
        }
    }

    public static HttpRequestBody xml(String xml, String encoding) {
        try {
            return new HttpRequestBody(xml.getBytes(encoding), ContentType.XML, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("illegal encoding " + encoding, e);
        }
    }

    public static HttpRequestBody custom(byte[] body, ContentType contentType, String encoding) {
        return new HttpRequestBody(body, contentType, encoding);
    }

    public static HttpRequestBody form(Map<String, Object> params, String encoding) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(params.size());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
        }
        try {
            return new HttpRequestBody(URLEncodedUtils.format(nameValuePairs, encoding).getBytes(encoding), ContentType.FORM, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("illegal encoding " + encoding, e);
        }
    }
}
