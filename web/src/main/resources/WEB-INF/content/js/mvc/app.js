
Ext.Loader.setConfig({
    enabled: true,
    paths: {
        'AD.Web.Test': 'app'
    }
});

//Ext.direct.Manager.addProvider(Ext.app.REMOTING_API);
//Ext.ns('AD.Web.Test.app');

Ext.onReady(function() {
//	Ext.state.Manager.setProvider(new Ext.state.CookieProvider({
//	    expires: new Date(new Date().getTime()+(1000*60*60))
//	}));	
//	if(Ext.util.Cookies.get("hasLogin")=="true"){
		Ext.create("AD.Web.Test.Application");
//	}else{
//    	Northwind.LoginWin.show();
//    }
});