package com.hs.wraphttpurlconnection;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：zhanghaitao on 2017/10/12 13:44
 * 邮箱：820159571@qq.com
 *
 * @describe:网络连接类
 */

public class Connection {

    private static final String TAG = "network";

    /**
     * GET请求
     *
     */
    private void requestGet(String baseUrl,HashMap<String, String> paramsMap) {
        try {
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            String requestUrl = baseUrl + tempParams.toString();
            // 新建一个URL对象
            URL url = new URL(requestUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            urlConn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");

            //TODO:在此处查询HTTP的请求行、消息报头。请求正文由客户决定
            urlConn.getRequestProperty("HOST");//请求地址
            urlConn.getRequestProperty("Content-Length");//字节长度
            urlConn.getRequestProperty("Connection");//连接
            urlConn.getRequestProperty("Cache-Control");//缓存状态
            urlConn.getRequestProperty("Content-Type");//正文格式

            //开始连接
            urlConn.connect();
            // 判断请求是否成功

            urlConn.getResponseMessage();
            urlConn.getRequestProperty("Server");
            urlConn.getRequestProperty("Access-Control-Allow-Origin");
            urlConn.getRequestProperty("Cache-Control");
            urlConn.getRequestProperty("Content-Type");
            urlConn.getRequestProperty("Content-Language");
            urlConn.getRequestProperty("Content-Length");
            urlConn.getRequestProperty("Date");
            urlConn.getRequestProperty("");
            urlConn.getRequestProperty("");

            Map<String, List<String>> map = urlConn.getHeaderFields();

            System.out.println("显示响应Header信息...\n");

            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                System.out.println("Key : " + entry.getKey() +
                        " ,Value : " + entry.getValue());
            }

            System.out.println("\n使用key获得响应Header信息 \n");
            List<String> server = map.get("Server");

            if (server == null) {
                System.out.println("Key 'Server' is not found!");
            } else {
                for (String values : server) {
                    System.out.println(values);
                }
            }


            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                Log.e(TAG, "Get方式请求成功，result--->" + result);
            } else {
                Log.e(TAG, "Get方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * POST请求
     *
     * @param paramsMap
     */

    public static void requestPost(String baseUrl,HashMap<String, String> paramsMap) {
        try {
            //合成参数
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            String params = tempParams.toString();
            Log.e(TAG, "Post方式请求，params--->" + params);
            // 请求的参数转换为byte数组
            byte[] postData = params.getBytes();
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("POST");
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            // 配置请求Content-Type
            //urlConn.setRequestProperty("Content-Type", "text/html");
            // 设置通用的请求属性
            //urlConn.setRequestProperty("accept", "**//*");
            //urlConn.setRequestProperty("connection", "Keep-Alive");
            //urlConn.setRequestProperty("Content-Length", String
            // .valueOf(params.length()));

            // 开始连接       urlConn.connect();
            // 发送请求参数
            //这里是字节数组的请求方式
            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
            dos.write(postData);
            dos.flush();
            dos.close();

            /*// 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(urlConn.getOutputStream());
            // 发送请求参数
            printWriter.write(params);//post的参数 xx=xx&yy=yy
            // flush输出流的缓冲
            printWriter.flush();
            printWriter.close();*/

            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                Log.e(TAG, "Post方式请求成功，result--->" + result);
            } else {
                Log.e(TAG, "Post方式请求失败");
            }
            // 关闭连接 urlConn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public static String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    /**
     * 文件下载
     *
     * @param fileUrl
     */

    private void downloadFile(String fileUrl) {
        try {
            // 新建一个URL对象
            URL url = new URL(fileUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod("POST");
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            //urlConn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            //urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接            urlConn.connect();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                String filePath = "";
                File descFile = new File(filePath);
                FileOutputStream fos = new FileOutputStream(descFile);
                ;
                byte[] buffer = new byte[1024];
                int len;
                InputStream inputStream = urlConn.getInputStream();
                while ((len = inputStream.read(buffer)) != -1) {
                    // 写到本地
                    fos.write(buffer, 0, len);
                }
            } else {
                Log.e(TAG, "文件下载失败");
            }
            // 关闭连接            urlConn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 文件上传
     *
     * @param filePath
     * @param paramsMap
     */
    private void upLoadFile(String filePath, HashMap<String, String> paramsMap) {
        try {
            String baseUrl = "https://xxx.com/uploadFile";
            File file = new File(filePath);
            //新建url对象
            URL url = new URL(baseUrl);
            //通过HttpURLConnection对象,向网络地址发送请求
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            //设置该连接允许读取
            urlConn.setDoOutput(true);
            //设置该连接允许写入
            urlConn.setDoInput(true);
            //设置不能适用缓存
            urlConn.setUseCaches(false);
            //设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);   //设置连接超时时间
            //设置读取超时时间
            urlConn.setReadTimeout(5 * 1000);   //读取超时
            //设置连接方法post
            urlConn.setRequestMethod("POST");
            //设置维持长连接
            urlConn.setRequestProperty("connection", "Keep-Alive");
            //设置文件字符集
            urlConn.setRequestProperty("Accept-Charset", "UTF-8");
            //设置文件类型
            //urlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + "*****");
            String name = file.getName();
            DataOutputStream requestStream = new DataOutputStream(urlConn.getOutputStream());
            requestStream.writeBytes("--" + "*****" + "\r\n");
            //发送文件参数信息
            StringBuilder tempParams = new StringBuilder();
            tempParams.append("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + name + "\"; ");
            int pos = 0;
            int size = paramsMap.size();
            for (String key : paramsMap.keySet()) {
                tempParams.append(String.format("%s=\"%s\"", key, paramsMap.get(key), "utf-8"));
                if (pos < size - 1) {
                    tempParams.append("; ");
                }
                pos++;
            }
            //tempParams.append("\r\n");
            // tempParams.append("Content-Type: application/octet-stream\r\n");
            // tempParams.append("\r\n");
            String params = tempParams.toString();
            requestStream.writeBytes(params);
            //发送文件数据
            FileInputStream fileInput = new FileInputStream(file);
            int bytesRead;
            byte[] buffer = new byte[1024];
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            while ((bytesRead = in.read(buffer)) != -1) {
                requestStream.write(buffer, 0, bytesRead);
            }
            requestStream.writeBytes("\r\n");
            requestStream.flush();
            requestStream.writeBytes("--" + "*****" + "--" + "\r\n");
            requestStream.flush();
            fileInput.close();
            int statusCode = urlConn.getResponseCode();
            if (statusCode == 200) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                Log.e(TAG, "上传成功，result--->" + result);
            } else {
                Log.e(TAG, "上传失败");
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

}
