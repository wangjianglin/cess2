//package io.cess.shorturl;
//
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.UUID;
//
///**
// * Created by on 01.02.16.
// *
// * @author David Steiman
// */
//@RestController
//@RequestMapping("/foo")
//public class WebController {
//
//    @RequestMapping(method = RequestMethod.GET)
//    @PreAuthorize("hasAuthority('FOO_READ')")
//    public String readFoo() {
//        return "read foo " + UUID.randomUUID().toString();
//    }
//
//    @PreAuthorize("#oauth2.clientHasRole('FOO_WRITE') and #oauth2.clientHasRole('FOO_READ')")
//    @RequestMapping(method = RequestMethod.POST)
//    public String writeFoo() {
//        return "write foo " + UUID.randomUUID().toString();
//    }
//
//    @PreAuthorize("hasAuthority('goods:select')")
//    @RequestMapping("a")
//    public String ok(){
//        return "ok a";
//    }
//
//    @PreAuthorize("#oauth2.isOAuth()")
//    @RequestMapping("v")
//    public String okv(){
//        return "ok v";
//    }
//
//    @PreAuthorize("#oauth2.isClient()")
//    @RequestMapping("c")
//    public String okc(){
//        return "ok c";
//    }
//
//    @PreAuthorize("#oauth2.hasScope('foo')")
//    @RequestMapping("s")
//    public String oks(){
//        return "ok s";
//    }
//}
