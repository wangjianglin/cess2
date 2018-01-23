Ext.define('AD.html.controller.Test', {
    extend: 'Ext.app.Controller',

//    requires: [
//       'AD.Web.Test.view.Test',
//       'AD.Web.Test.view.WindowContent',
//       'AD.Web.Test.view.Form',
//       'AD.Web.Test.view.HTMLContent'
//    ],
//    stores: [
//        'Tests'
//    ],
	refs:[
	{ref:'centerContent',selector:'#center-content'}
	],
//    models: [
//        'Test'
//    ],
    views:["Test"],
    
    init:function(){
    	var me = this;
    	//view=me.getOrderViewView(),
    		//c=me.getContentPage();
        me.control({
            '#mv-test button[action=AD.Context]': {
                click: this.ADContext
            },'#mv-test button[action=add]': {
                click: this.testAdd
            },'#mv-test button[action=edit]': {
                click: this.testEdit
            },'#mv-test button[action=del]': {
                click: this.testDelete
            },'#mv-test button[action=get]':{
            	click:this.getTest
            },'#mv-test button[action=add-test]':{
            	click:this.addTest
            },'#mv-test button[action=form-test]':{
            	click:this.formTest
            },'#mv-test button[action=form-html-content]':{
            	click:this.formHtmlContent
            },'#mv-test button[action=from-url-content]':{
            	click:this.fromURLContent
            },'#mv-test button[action=content-to-control]':{
            	click:this.contentToControl
            }
        });
    	//c.add(view);
    },
    contentToControl:function(){
    	var rt = this.getCenterContent();
    	AD.window('./../index.html',{
    		renderTarget:'control',
    		renderTo:rt
    	})
    },
    fromURLContent:function(){
    	AD.window('content.html',{
    		renderTarget:'popup'
    	});
    },
    formHtmlContent:function(){
    	
    	var content = Ext.widget('form.htmlcontent');
                
                AD.window(content,{
                title:'form test',
                model:true,
                listeners:{
                destroy:function(){
                	//console.log(option);
                	alert('000000000000000'+content.result+'1111111111111111111');
                }
                }});
    },
    formTest:function(){
    	var content = Ext.widget('form.view2');
                
                AD.window(content,{
                title:'form test',
                model:true,
                listeners:{
                destroy:function(){
                	//console.log(option);
                	alert('000000000000000'+content.result+'1111111111111111111');
                }
                }});
    },
    addTest:function(){
    	//var content = Ext.create('AD.Web.Test.view.WindowContent',{
         //           region:'center',
         //           params:'999999999999999'
         //       });
           //     content.params = "999999999999999999";
                
                content = Ext.widget('test-window-content');
                content.params = "999999999999999999";
                content.region = 'center';
                
                AD.window(content,{
                title:'--',
                model:true,
                listeners:{
                destroy:function(){
                	//console.log(option);
                	alert('000000000000000'+content.result+'1111111111111111111');
                }
                }});
//    	 win = Ext.create('widget.window', {
//                title: 'Layout Window',
//                closable: true,
//                closeAction: 'destroy',
//                width: 600,
//                minWidth: 350,
//                modal:true,
//                height: 350,
//                layout: {
//                    type: 'border',
//                    padding: 5
//                },
//                items: [content],
//                listeners:{
//                destroy:function(){
//                	//console.log(option);
//                	alert('000000000000000'+content.result+'1111111111111111111');
//                }
//                }
//            });
//    
//        
//        win.show(null,function(option){
//        	console.log(option);
//        },null);
        
    },
    getTest:function(){
    	//model.proxy.packages.get.userId = lll
    	AD.Web.Test.model.Test.load(1,{
    		scope:this,
    		action:'get',
			success: function(d,o) {
				this.test=d;
				alert('success!');
			},
			failure:function(d,o){
				alert('exception!');
			}
		});
    },
    testDelete:function(){
    	this.test.destroy({
    		scope:this,
			success: function(d,o) {
				alert('success!');
			},
			failure:function(d,o){
				alert('exception!');
			}
		});
    },
    testEdit:function(){
    	this.test.save({
    		scope:this,
			success: function(d,o) {
				alert('success!');
			},
			failure:function(d,o){
				alert('exception!');
			}
		});
    },
    testAdd:function(){
    	//var store = this.getTestsStore();
    	//store.add();
		var model = this.getTestModel();
		//model.creat
		//var t=Ext.ModelManager.create({id:"",TestName:"新增"},"AD.Web.Test.model.Test");
		var t=Ext.create("AD.Web.Test.model.Test");
		model.proxy.packages.create.data = {id:34,name:'name',desc:'desc'};
						t.save({
    						scope:this,	
							success: function(d,o) {
        						//panel.update(o.response.responseText);
								this.test = d;
								alert('success!');
    						},
    						failure:function(d,o){
    							alert('exception!');
    						}
						});
    },
    ADContext:function(){
    	
		AD.Context.SetAttribute('test','value');
    	//alert('---------');
    }
});