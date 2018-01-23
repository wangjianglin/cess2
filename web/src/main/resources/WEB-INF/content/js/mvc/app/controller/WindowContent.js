Ext.define('AD.Web.Test.controller.WindowContent', {
    extend: 'Ext.app.Controller',

    requires: [
       'AD.Web.Test.view.Test',
       'AD.Web.Test.view.WindowContent'
    ],
    stores: [
        'Tests'
    ],

    models: [
        'Test'
    ],
    views:["Test","WindowContent"],
    refs:[
    	 {ref:'tmpview',selector:'#mv-test-window-content'}
    ],
    
    init:function(){
    	var me = this;
    	//view=me.getOrderViewView(),
    		//c=me.getContentPage();
        me.control({
            '#mv-test-window-content button[action=test]': {
                click: this.test
            },'#mv-test-window-content': {
                click: function(){
                	var v2 = this.getTmpview();
    				confirm("1111111111111111111111111111111");
                },
                destroy: function(){
                	var v2 = this.getTmpview();
    				confirm("4444444444444444444444444444444");
                },render: function(){
                	var v2 = this.getTmpview();
    				confirm("55555555555555555555555555");
                },added: function(){
                	var v2 = this.getTmpview();
    				confirm("66666666666666666666666666666--"+v2.params);
                }
            }
        });
    },
    
    test:function(){
    	var v = this.getWindowContentView();
    	var v2 = this.getTmpview();
    	v2.result = "88888888888888888888888888";
    	confirm("----------"+v2.params+"****************");
    }
});