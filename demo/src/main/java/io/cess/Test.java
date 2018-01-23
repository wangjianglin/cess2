package io.cess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Base64;

public class Test {

//    private static final char[] chars = {'a','c','e','i','s','u','v','w','x','o'};
    private static final char[] chars = {'b','d','f','h','k','l','1','2','3','4','5','6','7','8','9','0','-'};

    private static void genDns(char[] ch,int index){
        if(index == ch.length){
                String domain = new String(ch);

                System.out.println("a"+domain+"o");
            return;
        }

        //for(int n=0;n<chars.length - ((index == 0 || index == ch.length-1)?1:0);n++){
        for(int n=0;n<chars.length;n++){
            ch[index] = chars[n];
            genDns(ch,index+1);
        }
    }
    public static void main(String[] args){

//        genDns(new char[2],0);
        genData();
        for(int n=0;n<dns.length-1;n++){
            for(int m=0;m<dns.length-n-1;m++){
                if (dns[m].compareTo(dns[m+1]) > 0){
                    String tmp = dns[m];
                    dns[m] = dns[m+1];
                    dns[m+1] = tmp;
                }
            }
        }

        for(int n=0;n<dns.length;n++){
            System.out.println(dns[n]);
        }
    }

    private static void genData(){
        //            BJdk0UCXDjDytzdnNbJPlHQr0w2JvUdpfBSkAsWufoqqi+nOEjR2A5JHxcma0cfTKf/6J9AOgzvYRRSOOwhKe6FbMUOjrnsnFU2hEfzZqKevn7tCANoF2nypaKSdwJhmGhncbOSrsLKULI9Wq/f7xSIAZZHkh9p/zQdsfXQXA/m7xwbuCTpyyNT1jLruC26exAGCgsOE/00yjcHWV4DK07YT14lNBoH5K7gjyhxfuG97RSLJK285w49huxpURyjPDZ+2vVQ+ZVvGK6v2O/NQXKZDnIki8sKSG5DVwvyKsygoLudK11ScJ0ysjrIGPSDgvqE34OmLzWfqh+9qVLIUYzZRzxbowuZx3UFXJaRNmEvwGap2qAxzvxF2h9Ch/cx2aQi8z+c9G2cSVxLuCJpiTUb2q4sO/C4Je4bU6guJ43ERfKDjjUFWAmeRy1HBmaPC/psVuVXMXQ==
        String str = "BJdk0UCXDjDytzdnNbJPlHQr0w2JvUdpfBSkAsWufoqqi+nOEjR2A5JHxcma0cfTKf/6J9AOgzvYRRSOOwhKe6FbMUOjrnsnFU2hEfzZqKevn7tCANoF2nypaKSdwJhmGhncbOSrsLKULI9Wq/f7xSIAZZHkh9p/zQdsfXQXA/m7xwbuCTpyyNT1jLruC26exAGCgsOE/00yjcHWV4DK07YT14lNBoH5K7gjyhxfuG97RSLJK285w49huxpURyjPDZ+2vVQ+ZVvGK6v2O/NQXKZDnIki8sKSG5DVwvyKsygoLudK11ScJ0ysjrIGPSDgvqE34OmLzWfqh+9qVLIUYzZRzxbowuZx3UFXJaRNmEvwGap2qAxzvxF2h9Ch/cx2aQi8z+c9G2cSVxLuCJpiTUb2q4sO/C4Je4bU6guJ43ERfKDjjUFWAmeRy1HBmaPC/psVuVXMXQ==";

        System.out.println(new String(Base64.getDecoder().decode(str)));
////        File file
//        try {
//            ArrayList<String> list = new ArrayList<>();
//
//            java.io.BufferedReader in = new BufferedReader(new FileReader("/lin/dns/ios3ditig.txt"));
//            String item = null;
//            out:while ((item = in.readLine()) != null){
//                item = item.trim();
//                if("".equals(item) || item.endsWith(":true")){
//                    continue;
//                }
//                int n = 0;
//                for(;n < chars.length;n++) {
//                    char ch = chars[n];
//                    if (item.charAt(0) == ch) {
//                        break ;
//                    }
//                }
//                if(n >= chars.length){
//                    continue ;
//                }
//
//                n = 0;
//                for(;n < chars.length;n++) {
//                    char ch = chars[n];
//                    if (item.charAt(1) == ch) {
//                        break;
//                    }
//                }
//                if(n >= chars.length){
//                    continue ;
//                }
////                for(String s : new String[]{"0","1","2","3","4","5","6","7","8","9","-"}){
////                    if (item.contains(s)) {
////                        continue out;
////                    }
////                }
////                if(!item.substring(0,3).matches(".*\\d$")){
////                    continue ;
////                }
//                list.add(item.substring(0,6));
//            }
//            dns = list.toArray(new String[]{});
//        }catch (Throwable e){
//            e.printStackTrace();
//        }
    }

    private static String[] dns = null;
}
