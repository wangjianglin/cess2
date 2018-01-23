package io.cess.shorturl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Utils {

    private static final char[] URL_CODE;
    private static final Map<Character,Integer> charToInt = new HashMap<>();

    public static String idToUrl(long id){

        long checkcode = checkCode(id);

        StringBuffer buffer = new StringBuffer();

        if(id <= 0){
            return URL_CODE[0]+"";
        }

        while(id>0){
            buffer.append(URL_CODE[(int)(id%URL_CODE.length)]);
            id /= URL_CODE.length;
        }

        int bit = buffer.length() % 6;

        long index = checkcode * 6 + bit;
        buffer.append(URL_CODE[(int)(index%URL_CODE.length)]);
        return buffer.reverse().toString();
    }

    public static long urlToId(String url){

        long r = 0;

        char[] ch = url.toCharArray();
        for(int n=1;n<url.length();n++){
            int i = charToInt.getOrDefault(ch[n],0);
            r = r * URL_CODE.length + i;
        }

        long checkcode = checkCode(r);
        int bit = (ch.length - 1) % 6;

        long index = checkcode * 6 + bit;

        if(URL_CODE[(int)(index%URL_CODE.length)] == ch[0]){
            return r;
        }

        return -1;
    }

    private static long checkCode(long code) {
        long odd = 0;
        long even = 0;
        long bit = 0;
        for (int n = 0; code > 0; n++) {
            bit = code % 10;
            code /= 10;
            if (n % 2 == 0) {
                if (bit * 2 > 9) {
                    even += bit * 2 - 9;
                } else {
                    even += bit * 2;
                }
            } else {
                odd += bit;
            }
        }
        long r = (odd + even) % 10;
        return (10 - r) % 10;
    }


    static {

        List<Character> chars = new ArrayList<>();
        URL_CODE = new char[62];

        for(int n=0;n<26;n++){
            URL_CODE[n]= (char) (97+n);
            charToInt.put(URL_CODE[n],n);
        }

        for(int n=0;n<26;n++){
            URL_CODE[n+26]= (char) (65+n);
            charToInt.put(URL_CODE[n+26],n+26);
        }


        for(int n=0;n<10;n++){
            URL_CODE[n+52]= (char) (48+n);
            charToInt.put(URL_CODE[n+52],n+52);
        }

    }

    public static void main(String[] args){
//        for(int n=0;n<URL_CODE.length;n++){
//            System.out.println(URL_CODE[n]);
//        }

//        long start = 356657887l;
//        for(long n=start;n<start+2000;n++){
//            String url = idToUrl(n);
//            System.out.println("url:"+url);
//            System.out.println("id:"+urlToId(url));
//
//            System.out.println();
//        }
        Pattern pattern = Pattern.compile("^/((?!/).)*$");

        System.out.println(pattern.matcher("/dlfjklfj").matches());
        System.out.println(pattern.matcher("/dlfjklfj/").matches());
        System.out.println(pattern.matcher("/dlfjklfj/00").matches());
    }
}
