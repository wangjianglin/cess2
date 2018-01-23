package io.cess.comm;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Whois {
	private static final int DEFAULT_PORT = 43;
	private static final char[] chars = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','1','2','3','4','5','6','7','8','9','0','-'};
	//private static final char[] chars = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','-'};
//    private static final char[] chars = {'a','c','e','i','m','n','o','r','s','t','u','v','w','x','z'};
//    private static final char[] chars = {'a','c','e','i','s','u','v','w','x','o'};
	private static final int THREAD_SIZE = 20;
	private ExecutorService pool = Executors.newFixedThreadPool(THREAD_SIZE);
	private volatile int threadSize = 0;
	private volatile ReentrantLock threadLock = new ReentrantLock();
	private volatile Condition threadCondition = threadLock.newCondition();

	private String tld = "com";
	private int retryCount = 1000000;
	private long retryInterval = 1000;//以毫秒为单位
	private PrintStream print = System.out;
	private PrintStream errorPrint;
	private PrintStream notFoundPrint;
	private OnWhois onWhois;
	private IsRegister isRegister;

	private int count = 0;

	public void whois(int l){
		whois(l,0);
	}
	public void whois(int l,String startStr){
		if(startStr == null || startStr.isEmpty()){
			whois(l,0);
			return;
		}
		char[] ch = startStr.toCharArray();
		int base = 1;
		int bit = 0;
		int start = 0;
		for(int n=ch.length - 1;n>=0;n--){
			bit = 0;
			for(int m=0;m<chars.length;m++){
				if(chars[m] == ch[n]){
					bit = m;
					break;
				}
			}
			start += base * bit;
			base *= chars.length;
		}
		whois(l,start);
	}

	public void whois(int l,int start){
		char[] ch = new char[l];
		for(int n=0;n<l;n++){
			ch[n] = chars[0];
		}
		whoisImpl(ch,0,start);
		threadLock.lock();
		while(threadSize != 0){
			try {
				threadCondition.await();
			} catch (InterruptedException e) {
			}
		}
		threadLock.unlock();
	}

	private  void whoisImpl(char[] ch,int index,int start){
		if(index == ch.length){
			if(start <= ++count){
				String domain = new String(ch) + "." + tld;
				if(onWhois != null) {
					Boolean r = onWhois.isWhois(domain);
					if(r != null){
						if(print != null){
							print.println(domain+":"+r);
						}
						return;
					}
				}
				addQuery(domain);
			}
			return;
		}

		for(int n=0;n<chars.length;n++){
			if((index == 0 || index == ch.length-1) && '-' == chars[n]){
				continue;
			}
			ch[index] = chars[n];
			whoisImpl(ch,index+1,start);
		}
	}

	public Boolean isRegister(String domain){
		try {
			String whois = query(domain);
			if(isRegister != null) {
				return isRegister.isRegister(domain,whois);
			}
			return !whois.contains("NOT FOUND");
		} catch (Throwable e) {
		}
		return null;
	}
	private Boolean repay(String domain){
		int count = 0;
		while(count < retryCount){
			count++;
			Boolean r = isRegister(domain);
			if(r != null){
				return r;
			}
			try {
				Thread.sleep(retryInterval);
			} catch (InterruptedException e) {
			}
		}
		return null;
	}

	private void addQuery(final String str){

		threadLock.lock();

		while(threadSize >= THREAD_SIZE * 2){
			try {
				threadCondition.await();
			} catch (InterruptedException e) {
			}
		}
		threadSize++;

		threadLock.unlock();;

		pool.execute(new Runnable(){

			@Override
			public void run() {

				Boolean r = repay(str);

				if(print != null) {
					print.println(str + ":" + (r!=null?r:"error"));
				}

				if(Boolean.FALSE.equals(r) && notFoundPrint != null){
					notFoundPrint.println(str + ":" + r);
				}

				if(r == null && errorPrint != null){
					errorPrint.println(str + ":error");
				}

				threadLock.lock();
				threadSize--;
				threadCondition.signalAll();
				threadLock.unlock();
			}
		});
	}


	public static String query(String domain) throws Exception {
		String server = "abcnews.com";
		String tld = getTLD(domain);
		if ("com".equals(tld)) {
			server = "whois.verisign-grs.com";
		} else if ("net".equals(tld)) {
			server = "whois.verisign-grs.com";
		} else if ("org".equals(tld)) {
			server = "whois.pir.org";
		} else if ("cn".equals(tld)) {
			server = "whois.cnnic.cn";
		} else if ("jp".equals(tld)) {
			server = "whois.jprs.jp";
		} else if ("kr".equals(tld)) {
			server = "whois.kr";
		} else if ("io".equals(tld)) {
			server = "whois.nic.io";
		}else {
			server = "whois.nic."+tld;
		}
		return query(domain, server);
	}

	public static String query(String domain, String server) throws Exception {
		Socket socket = new Socket(server, DEFAULT_PORT);
		socket.setSoTimeout(8000);
		socket.setKeepAlive(false);

		PrintWriter out = new PrintWriter(socket.getOutputStream());
		out.println(domain);
		out.flush();


		ByteArrayOutputStream ret = new ByteArrayOutputStream();
		InputStream _in = socket.getInputStream();
		byte[] bs = new byte[1024];
		int count = 0;
		while((count = _in.read(bs)) != -1){
			ret.write(bs, 0, count);
		}

		socket.close();
		return ret.toString();
	}

	private static String getTLD(String domain) {
		final int index;
		return (domain == null || (index = domain.lastIndexOf('.') + 1) < 1) ? domain
				: (index < (domain.length())) ? domain.substring(index) : "";
	}

	public void shutdown(){
		pool.shutdown();
	}


	public void setOnWhois(OnWhois onWhois){
		this.onWhois = onWhois;
	}

	public void setIsRegister(IsRegister isRegister){
		this.isRegister = isRegister;
	}

	public interface OnWhois {
		Boolean isWhois(String domain);
	}

	public interface IsRegister{
		public Boolean isRegister(String domain,String whois);
	}
	//    private static  long start = 0;
//    private static  long count = 0;
	public static void main(String[] args) throws Exception {


//        String whois = w.query("as-v.io");
//        System.out.println("w:"+whois);
//        System.out.println("ok:" + whois.contains("No match for "));
//        for()
//    	print = new PrintStream(new FileOutputStream(new File("log.txt"), true),true);
//    	printError = new PrintStream(new FileOutputStream(new File("log-error.txt"), true),true);
//        print = System.out;
//        printError = System.out;
//        whois(Integer.parseInt(args[0]));

//        System.out.println(query("fcbb.io"));

		Whois w = new Whois();
		w.print = System.out;
//        w.notFoundPrint = null;
////        w.errorPrint = null;

//        w.print = null;
//        w.notFoundPrint = System.out;
//        w.errorPrint = System.out;
		w.retryInterval = 1500;
		w.retryCount = 100;
		w.tld = "com";

		if(args != null && args.length > 0 && args[0] != null && !"".equals(args[0].trim())) {
			w.tld = args[0].trim().toLowerCase();
		}

//        w.setOnWhois(new DigitendOnWhois());
		if(args != null && args.length > 1){
			w.setOnWhois(new FileOnWhois(args[1]));
		}

		switch (w.tld) {
			case "com":
				w.setIsRegister(ComIsRegister);
				break;
			case "net":
				w.setIsRegister(NetIsRegister);
				break;
			case "io":
				w.setIsRegister(IoIsRegister);
				break;
			case "cn":
				w.setIsRegister(CnIsRegister);
				break;
		}

//        w.threadSize = 20;
		w.whois(3);
		System.out.println("-------------------------");
		w.shutdown();
	}


	private static final IsRegister ComIsRegister = new IsRegister() {
		@Override
		public Boolean isRegister(String domain,String whois) {

			try {
				return !whois.contains("No match for \""+domain.toUpperCase()+"\"");
			} catch (Throwable e) {
			}
			return null;
		}
	};

	private static final IsRegister CnIsRegister = new IsRegister() {
		@Override
		public Boolean isRegister(String domain,String whois) {

			try {
				return !whois.contains("No matching record");
			} catch (Throwable e) {
			}
			return null;
		}
	};

	private static final IsRegister NetIsRegister = new IsRegister() {
		@Override
		public Boolean isRegister(String domain,String whois) {

			try {
				return !whois.contains("No match for \""+domain.toUpperCase()+"\"");
			} catch (Throwable e) {
			}
			return null;
		}
	};

	private static final IsRegister IoIsRegister = new IsRegister() {
		@Override
		public Boolean isRegister(String domain,String whois) {

			try {
				return !whois.contains("NOT FOUND");
			} catch (Throwable e) {
			}
			return null;
		}
	};

	private static final class FileOnWhois implements OnWhois {

		private BufferedReader in = null;
		public FileOnWhois(String fileName) throws FileNotFoundException {
			in = new BufferedReader(new FileReader(fileName));
		}

		private Map<String,String> whoises = new HashMap<>();
		@Override
		public Boolean isWhois(String domain) {
			String whois = getDomainWhois(domain);
			if(whois == null){
				return null;
			}
			String r = whois.substring(domain.length()+1);
			if("true".equals(r)){
				return true;
			}else if("false".equals(r)){
				return false;
			}
			return null;
		}

		private String getDomainWhois(String domain){
			if(whoises.containsKey(domain)){
				return whoises.remove(domain);
			}
			String item = null;
			while (true) {
				try {
					item = in.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (item == null || "".equals(item)) {
					return null;
				}
				if(!item.startsWith(domain)) {
					int index = item.indexOf(':');
					if(index > 0){
						whoises.put(item.substring(0,index), item);
					}
				}else{
					return item;
				}
			}
		}
	};

	private static final class DigitendOnWhois implements OnWhois {

		@Override
		public Boolean isWhois(String domain) {
//            if(domain.matches("(\\d+\\.io)$")){
//                return null;
//            }
			if(domain.charAt(domain.length()-4) >= '0' && domain.charAt(domain.length()-4) <= '9'){
				return null;
			}
			return true;
		}
	};
}
