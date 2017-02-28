package com.thu.web.school;
import com.thu.domain.Pic;
import com.thu.domain.Question;
import com.thu.domain.TUser;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import java.util.Date;
//import java.text.DateFormat;
//import org.apache.axis2.client.S
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

import javax.xml.namespace.QName;
import java.lang.Integer;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

/**
 *
 *Created by hatim on 2016/12/11.
 * 该类可以将一个soap消息发送至服务端，并获取响应的soap消息
 *
 */

public class SoapUtil {

    public static String invokeSrv(String endpoint, Object soapXml) throws IOException {
        StringBuilder sb = new StringBuilder();

        String method = "POST";
        String contentType = "application/x-java-serialized-object;charset=UTF-8";

        URL url = new URL(endpoint);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

        // POST请求
        urlConn.setRequestMethod(method);
        // 设置要发送消息
        urlConn.setDoOutput(true);
        // 设置要读取响应消息
        urlConn.setDoInput(true);
        // POST不能使用cache
        urlConn.setUseCaches(false);

        urlConn.setInstanceFollowRedirects(true);

//        urlConn.setRequestProperty("SOAPAction", action);
        urlConn.setRequestProperty("Content-Type", contentType);

//        urlConn.setRequestProperty("@test_flag", "Y");
        urlConn.connect();

        // 向输出流写出数据，这些数据将存到内存缓冲区中
//        PrintWriter pw = new PrintWriter(urlConn.getOutputStream());
//        pw.write(soapXml);

        // 现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。
        ObjectOutputStream objOutputStrm = new ObjectOutputStream(urlConn.getOutputStream());
        // 向对象输出流写出数据，这些数据将存到内存缓冲区中
        objOutputStrm.writeObject(soapXml);
        objOutputStrm.flush();
        objOutputStrm.close();
        // 刷新对象输出流，将任何字节都写入潜在的流中
//        pw.flush();


        // 关闭流对象。此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中,
        // 在调用下边的getInputStream()函数时才把准备好的http请求正式发送到服务器
//        pw.close();
        // 接收返回消息
        // 解析返回值编码格式
        String charset = "UTF-8";
        String ct = urlConn.getContentType();
        Pattern p = Pattern.compile("charset=.*;?");
        Matcher m = p.matcher(ct);
        if (m.find()) {
            charset = m.group();
            // 去除charset=和;,如果有的话
            if (charset.endsWith(";")) {
                charset = charset.substring(charset.indexOf("=") + 1, charset.indexOf(";"));
            } else {
                charset = charset.substring(charset.indexOf("=") + 1);
            }
            // charset = "\"UTF-8\"";
            // 去除引号 ,如果有的话
            if (charset.contains("\"")) {
                charset = charset.substring(1, charset.length() - 1);
            }
            charset = charset.trim();
        }

        // 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
        // <===注意，实际发送请求的代码段就在这里
        InputStream inStream = urlConn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, charset));

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        urlConn.disconnect();

        return sb.toString();
    }


    public static void main(String[] args) throws ServiceException {
        String endpoint = "http://wx.93001.cn/services/wsActionPort.jws?wsdl";
        String KEY = "sdf!2l12@#2dsfDFSDF";
        String sign = SchoolPartController.MD5(KEY + 0 + KEY);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("sign",sign);
        jsonObject.put("id",0);
//        Question question=questionService.findById(qid);
//        if(question==null)
//            return Erro_Zongban;
//        TUser user_stu=question.getTUser();
        jsonObject.put("title","test from BeautifulTsinghua");
        jsonObject.put("content","test from BeautifulTsinghua, you should not responde.");
//        List<Pic> pictures=question.getPics();
        List<String> pic_url=new ArrayList<>();
        pic_url.add("http://www.baidu.com/11.jpg");
//        if(pictures==null)
//            return Erro_Zongban;
//        for(Pic pic:pictures){
//            pic_url.add(pic.getPath());
//        }

        jsonObject.put("img_url",pic_url);
        jsonObject.put("person","zongban");
        jsonObject.put("contact","13290981234");
        jsonObject.put("deadLine",Convert(LocalDateTime.now()));

        StringBuffer sb = new StringBuffer();
//        InputStream in = null;
//        BufferedReader bf = null;
        try {
//            in = SoapUtil.class.getClassLoader().getResourceAsStream("soapRequest.xml");
//            bf = new BufferedReader(new InputStreamReader(in));
//            String line = "";
//            while ((line = bf.readLine()) != null) {
//                sb.append(line).append("\n");
//            }
            //输入请求
            System.out.println(jsonObject.toString());
            System.out.println("------------------------------------------");
            //返回请求
//            System.out.println(invokeSrv(endpoint, jsonObject.toString()));
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(endpoint);
            call.setOperationName("receive");//WSDL里面描述的接口名称
            call.addParameter("json", XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);//接口的参数
            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);//设置返回类型
            String temp = jsonObject.toString();
            String result = (String)call.invoke(new Object[]{temp});
            //给方法传递参数，并且调用方法
            System.out.println("result is "+result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static  String Convert(Object o){
        if(o!=null)
            return o.toString();
        return null;
    }

}