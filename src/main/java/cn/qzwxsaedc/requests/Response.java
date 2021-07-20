package cn.qzwxsaedc.requests;

import cn.qzwxsaedc.requests.PreparedRequest;
import kotlin.Suppress;
import okhttp3.Headers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Response {
    private final okhttp3.Response response;
    // 响应内容
    public final byte[] content;
    // 服务器返回的Cookies
    public CookieJar cookies = null;
    // 从发送到接收总共花费的时间
    public final long elapsed;
    // Response.text所用的编码
    public Charset encoding = StandardCharsets.UTF_8;
    // 响应头
    public final Map<String, List<String>> headers;
    // 是否为永久重定向(302)
    public boolean is_permanent_redirect;
    // 是否为重定向(301, 302)
    public final boolean is_redirect;
    // 返回响应的解析头链接
    public final String links = null;
    // 返回重定向链中下一个请求的PreparedRequest
    public final PreparedRequest next = null;
    // status_code < 400 时为true
    public final boolean ok;
    // 响应内容的原始流
    public final InputStream raw;
    // 文字形式的HTTP状态，例如"Not Found"或者"OK"
    public final String reason;
    // 响应的PreparedRequest对象。
    public final PreparedRequest request = null;
    // 数字形式的HTTP状态，例如"404"或者"200"
    public final int status_code;
    // 响应内容，字符编码为encoding
    public String text() {
        try{
            StringBuilder sb = new StringBuilder();
            String tmp = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(raw, encoding));
            while ((tmp = br.readLine()) != null)
                sb.append(tmp);
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    // 最终的URL
    public final String url;
    public Response(okhttp3.Response response) throws IOException {
        this.response = response;
        raw = Objects.requireNonNull(response.body()).byteStream();
        content = raw.readAllBytes();

        elapsed = response.receivedResponseAtMillis() - response.sentRequestAtMillis();

        headers = response.headers().toMultimap();

        status_code = response.code();
        ok = status_code < 400;
        is_permanent_redirect = status_code == 302;
        is_redirect = response.isRedirect();

        url = response.request().url().toString();

        reason = response.message();
    }

    public void close(){
        response.close();
    }

    @Override
    public String toString() {
        return "Response:{code=" + status_code + "}";
    }
}
