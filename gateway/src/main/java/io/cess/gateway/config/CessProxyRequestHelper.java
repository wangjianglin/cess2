//package io.cess.gateway.config;
//
//import com.netflix.zuul.context.RequestContext;
//import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
//
//import javax.servlet.http.HttpServletRequest;
//
//public class CessProxyRequestHelper extends ProxyRequestHelper {
//
//    private static final String PROXY_KEY = "proxy";
//
//    private ZuulExtProperties properties;
//
//    public CessProxyRequestHelper(ZuulExtProperties properties){
//        this.properties = properties;
//    }
//
//    @Override
//    public String buildZuulRequestURI(HttpServletRequest request) {
//
//        RequestContext ctx = RequestContext.getCurrentContext();
//
//        ZuulExtProperties.ZuulRoute route = properties.getRoutes().get(ctx.get(PROXY_KEY));
//
//        String prefix = "";
//        if(route != null){
//            prefix = route.getPrefix();
//        }
//
//        return prefix + "/" +super.buildZuulRequestURI(request).replace("//","/");
//    }
//}
