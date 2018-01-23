package io.cess.comm.tcp;

import io.cess.comm.tcp.annotation.ProtocolParserType;
import io.cess.comm.tcp.annotation.ProtocolParserType;
import io.cess.util.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


@ProtocolParserType(6)
public class JsonProtocolParser extends AbstractProtocolParser {

//    private static Map<String, Class<?>> paths = new HashMap<String, Class<?>>();

    static {
        byte[] end_flag = null;
        try {
            end_flag = "\r\n\r\n".getBytes("ascii");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        END_FLAG = end_flag;

        byte[] line_flag = null;
        try {
            line_flag = "\r\n".getBytes("ascii");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        LINE_FLAG = line_flag;

//        paths.put("/json/test", JsonTestPackage.class);
    }

    /// <summary>
    /// 头结束标识
    /// </summary>
    @SuppressWarnings("unused")
    private static final byte[] END_FLAG;// = null;//"\r\n\r\n".getBytes("assic");
    /// <summary>
    /// 换行标识
    /// </summary>
    private static final byte[] LINE_FLAG;// = null;//Encoding.Default.GetBytes("\r\n");


    public TcpPackage getPackage() {
        Map<String, String> headers = new HashMap<String, String>();
        int end = 0;
        int start = 0;
        String tmp = null;
        for (int n = 0; n < count - 2; n++) {
            if (buffer[n] == LINE_FLAG[0]
                    && buffer[n + 1] == LINE_FLAG[1]) {
                end = n;
                if (start == end) {
                    start = end = end + 2;
                    break;
                }

                try {
                    tmp = new String(buffer, start, end - start, "ascii");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
//                    String tmp = Encoding.Default.GetString(buffer, start, end-start);
                String[] tmp2 = tmp.split(":");
//                    headers[tmp2[0]] = tmp2[1];
                if (tmp2.length == 1) {
                    headers.put(tmp2[0], "");
                } else {
                    headers.put(tmp2[0], tmp2[1]);
                }
                start = end = end + 2;
            }
        }
        try {

            String path = headers.get("path");
            String encoding = headers.get("encoding");
            if(encoding == null || "".equals(encoding)){
                encoding = "utf-8";
            }

            headers.remove("path");
            headers.remove("encoding");

            Object json = JsonUtil.deserialize(new String(buffer, start, count - start, encoding));

//            Class<?> pcls = paths.get(headers.get("path"));
            JsonPackage pack = null;
//            if (pcls == null) {
//                pack = new JsonPackage();
//            } else {
//                pack = (JsonPackage) pcls.newInstance();
//            }
//            pack.setValues(JsonUtil.deserialize(json));
//            pack.setHeaders(headers);
            if (state == PackageState.REQUEST){
                JsonRequestPackage requestPackage = JsonPackageManager.newRequestInstance(path);
                if (requestPackage == null){
                    requestPackage = new JsonRequestPackage();
                }

                requestPackage.setValues(json);
                requestPackage.setHeaders(headers);
                pack = requestPackage;
            }else {
                JsonResponsePackage responsePackage = JsonPackageManager.newResponseInstance(path);
                if (responsePackage == null){
                    responsePackage = new JsonResponsePackage();
                }

                responsePackage.setValues(json);
                responsePackage.setHeaders(headers);
                pack = responsePackage;
            }
            return pack;
        } catch (Throwable e) {

        }
        return null;
    }

}
