Ext.define('AD.Web.Test.controller.Form', {
   extend: 'Ext.app.Controller',

//    views:["Form"]
//    
    init:function(){
    	
    	alert('---------------------------------');
		debugger
    	
    	var me = this;
    	//view=me.getOrderViewView(),
    		//c=me.getContentPage();
        me.control({
            '#mv-test button[action=list]': {
                click: this.testList
            }
        });
    }
    
    ,testList:function(){
    	
    }
});