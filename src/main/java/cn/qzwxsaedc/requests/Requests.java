package cn.qzwxsaedc.requests.entity;

import cn.qzwxsaedc.requests.entity.libs.IgnoreCheckSSLSocketClient;
import cn.qzwxsaedc.requests.exception.NotImplementedException;
import kotlin.Suppress;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

@Suppress(names = "unused")
public class Requests {
    private OkHttpClient client = null;
    OkHttpClient.Builder client_builder = new OkHttpClient.Builder();
    Request.Builder request_builder = new Request.Builder();
    public Requests(){
        client = new OkHttpClient();
    }
    public Requests(OkHttpClient client){
        this.client = client;
    }

    public Response requests(String method, String url) {
        method = method.toUpperCase();
        url = String.format("%s%s", url, query_string);

        try{
            try {
                printArguments();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (bin_data.length > 0)
                request_builder.method(method, RequestBody.create(bin_data));
            else if (!method.equals("GET"))
                throw new IllegalArgumentException(String.format("%s 方法必须设置data", method));

            return new Response(client.newCall(request_builder.url(url).build()).execute());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Requests setParams (final TreeMap<String, String> params) {
        this.params = params;
        StringBuilder sb_params = new StringBuilder("?");
        for(Map.Entry<String, String> entry : params.entrySet())
            sb_params.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .append("&");
        sb_params.deleteCharAt(sb_params.length() - 1);
        query_string = sb_params.toString();
        return this;
    }
    public Requests setData (final String data) {
        this.data = data;
        if(form != null || files != null)
            throw new IllegalArgumentException("参数冲突: data/form/files不可同时使用。");
        setBinData(data);
        return this;
    }
    public Requests setForm (final String form) throws NotImplementedException {
        this.form = form;
        if(data != null || files != null)
            throw new IllegalArgumentException("参数冲突: data/form/files不可同时使用。");
        throw new NotImplementedException();
//        return this;
    }
    public Requests setHeaders (final TreeMap<String, String> headers) {
        this.headers = headers;
        for(Map.Entry<String, String> entry : headers.entrySet())
            request_builder.addHeader(entry.getKey(), entry.getValue());
        return this;
    }
    public Requests setFiles (final String[] files) throws IOException {
        if(data != null || form != null)
            throw new IllegalArgumentException("参数冲突: data/json/files不可同时使用。");
        if(files.length > 2)
            throw new IllegalArgumentException("参数files最多只能接受两个值。");

        this.files = files;

        switch (files.length){
            case 1:
                File target = new File(files[0]);
                if(!target.exists())
                    throw new FileNotFoundException(String.format("文件 %s 不存在。", files[0]));
                setBinData(target);
                break;
            case 2:
                setBinData(files[1]);
                break;
            default:
                throw new IllegalArgumentException("长度不能为0。");
        }
        return this;
    }
    public Requests setAuth (final String[] auth) {
        if(auth.length > 2)
            throw new IllegalArgumentException("参数auth最多只能接受两个值。");
        this.auth = auth;
        return this;
    }
    public Requests setTimeout (final Long[] timeout) {
        if(timeout.length > 3)
            throw new IllegalArgumentException("参数timeout最多只能接受三个值。");
        this.timeout = timeout;

        switch (timeout.length){
            case 3:
                client_builder.writeTimeout(Duration.ofMillis(timeout[2]));
            case 2:
                client_builder.readTimeout(Duration.ofMillis(timeout[1]));
            case 1:
                client_builder.connectTimeout(Duration.ofMillis(timeout[0]));
                break;
            default:
                throw new IllegalArgumentException("长度不能为0。");
        }
        return this;
    }
    public Requests setAllowRedirects (final boolean allow_redirects) {
        this.allow_redirects = allow_redirects;
        client_builder.followRedirects(allow_redirects);
        client_builder.followSslRedirects(allow_redirects);
        return this;
    }
    public Requests setVerify (final String verify) throws NotImplementedException {
        this.verify = verify;
        if(verify.toLowerCase().equals("false"))
            client_builder.sslSocketFactory(IgnoreCheckSSLSocketClient.getSSLSocketFactory(),
                    IgnoreCheckSSLSocketClient.getX509TrustManager())
                    .hostnameVerifier(IgnoreCheckSSLSocketClient.getHostnameVerifier());
        else if(verify.toLowerCase().equals("true")){}
        else
            throw new NotImplementedException();
        return this;
    }
    public Requests setStream (final boolean stream) {
        this.stream = stream;
        return this;
    }
    public Requests setCert (final String cert) throws NotImplementedException {
        this.cert = cert;
        throw new NotImplementedException();
//        return this;
    }

    public void printArguments() throws IllegalAccessException {
        List<Field> fields = new ArrayList<>();
        {
            Class<?> tmp = this.getClass();
            while (tmp != null) {
                fields.addAll(Arrays.asList(tmp.getDeclaredFields()));
                tmp = tmp.getSuperclass();
            }
        }
        for(Field field : fields) {
            field.setAccessible(true);
            if(field.get(this) == null)
                continue;
            if(field.getType().isArray()){
                System.out.printf("%s: {", field.getName());
                for(Object item : (Object[])field.get(this))
                    System.out.printf("'%s' ", item);
                System.out.println("\010}");
            }else
                System.out.printf("%s: %s%n", field.getName(), field.get(this).toString());
        }
        
    }

    private void setBinData(final String str){
        bin_data = str.getBytes(StandardCharsets.UTF_8);
    }
    private void setBinData(final File file) throws IOException {
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
        bin_data = reader.readAllBytes();
    }

    private TreeMap<String, String> params = null;
    private String data = null;
    private String form = null;
    private TreeMap<String, String> headers = null;
    private String[] files = null;
    private String[] auth = null;
    private Long[] timeout = null;
    private boolean allow_redirects = true;
    private String verify = "true";
    private boolean stream = true;
    private String cert = null;

    private String query_string = "";
    private byte[] bin_data = new byte[0];
}
