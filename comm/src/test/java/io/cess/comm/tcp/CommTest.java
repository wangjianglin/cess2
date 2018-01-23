package io.cess.comm.tcp;

import org.junit.Test;

public class CommTest {

    @Test
    public void testCommServer() throws Throwable {
        Communicate server = new ServerCommunicate((session, pack, response) ->
        {
            if (pack instanceof CommandPackage) {
                CommandPackage p = (CommandPackage) pack;
                System.out.println("pack:" + p.getCommand());
                if(p instanceof DetectPackage){
                    //return new DetectPackageResp();
                    response.response(new DetectRespPackage());
                }
            }
            if (pack instanceof JsonRequestPackage) {
                JsonPackage json = (JsonPackage) pack;
                System.out.println("json:" + json.getPath());

                response.response(new JsonResponsePackage());
            }
            //response(pack);
        }, 7890, new SessionListener() {

            @Override
            public void Create(Session session) {

            }

            @Override
            public void Destory(Session session) {

            }
        });

        System.in.read();

        Thread.sleep(Long.MAX_VALUE);
    }

//    @Test
//    public void testComm() throws InterruptedException{
//        Communicate server = new ServerCommunicate((session,pack,response) ->
//        {
//            if (pack instanceof CommandPackage)
//            {
//                CommandPackage p = (CommandPackage) pack;
//                System.out.println("pack:" + p.getCommand());
//            }
//            if (pack instanceof JsonPackage)
//            {
//                JsonPackage json = (JsonPackage) pack;
//                System.out.println("json:" + json.getPath());
//            }
//            //response(pack);
//        }, 7890,new SessionListener(){
//
//            @Override
//            public void Create(Session session) {
//
//            }
//
//            @Override
//            public void Destory(Session session) {
//
//            }});
//
//         Communicate client = new ClientCommunicate((session, pack,response) ->
//         {
//             System.out.println("pack:"+pack);
//         }, "127.0.0.1", 7890);
//
//         DetectPackage detect = new DetectPackage();
//         TcpPackage r = client.send(detect).getResponse();
//         System.out.println("----------------------------------"+r.getSequeue()+r);
//
////         JsonTestPackage jsonPack = new JsonTestPackage();
////         jsonPack.setData("test.");
////         r = client.send(jsonPack).waitForEnd();
////         System.out.println("----------------------------------" + r.getSequeue()+r);
//
//         //Thread.Sleep(1000);
//         client.close();
//
//         server.close();
//         //Thread.Sleep(1000);
//         Thread.sleep(1000);
//         System.out.println("end .");
//         Thread.sleep(1000);
//	}
//
//
//	@Test
//	public void testClientComm() throws InterruptedException{
////		 Communicate server = new Communicate((session,pack,response) ->
////         {
////             if (pack instanceof CommandPackage)
////             {
////                 CommandPackage p = (CommandPackage) pack;
////                 System.out.println("pack:" + p.getCommand());
////             }
////             if (pack instanceof JsonPackage)
////             {
////                 JsonPackage json = (JsonPackage) pack;
////            	 System.out.println("json:" + json.getPath());
////             }
////             //response(pack);
////         }, 7890,new SessionListener(){
////
////			@Override
////			public void Create(Session session) {
////
////			}
////
////			@Override
////			public void Destory(Session session) {
////
////			}});
////
//         Communicate client = new ClientCommunicate((session, pack,response) ->
//         {
//
//         }, "127.0.0.1", 7890);
//
//         DetectPackage detect = new DetectPackage();
//         TcpPackage r = client.send(detect).getResponse();
//         System.out.println("----------------------------------"+r.getSequeue()+r);
//
//         JsonTestPackage jsonPack = new JsonTestPackage();
//         jsonPack.setData("test.");
//         r = client.send(jsonPack).getResponse();
//         System.out.println("----------------------------------" + r.getSequeue()+r);
//
//         //Thread.Sleep(1000);
//         client.close();
//
////         server.close();
//         //Thread.Sleep(1000);
//         Thread.sleep(1000);
//         System.out.println("end .");
//         Thread.sleep(1000);
//	}
}
