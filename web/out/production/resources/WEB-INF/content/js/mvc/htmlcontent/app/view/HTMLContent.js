Ext.define("AD.Web.Test.view.HTMLContent",{
	extend:"Ext.Container",
	xtype:'form.htmlcontent',
    resizable: false,
    layout:'fit',
    initComponent: function() {
    	var me=this;
    	
    	
//    	me.items=[{
//    	 xtype:'container',
//    	 loader:{
//    	 	//url: 'content.html',
//    	 	url: './../index/index.html',
//        	autoLoad: true
//    	 }
//    	}];
    	me.items=[{
    	 xtype:'container',
    	 html:'<iframe src="./../index/index.html" style="width: 100%; height: 100%; border: 0"/>'
    	}];
    	me.callParent(arguments);
    	
	},

	initEvents:function(){
		//this.on("show",this.focusField);
	},
	
	focusField:function(){
		var f=this.form.getForm();
		//f.findField("ProductName").focus();
	},

	onSave:function(){
		var me=this;
			f=me.form.getForm(),
			id=f.findField("ProductID").getValue();
		if(f.isValid() && f.isDirty()){		
			f.doAction('web.form',{action:'submit',
				success: function(form, action){
					alert('000000000');
			}
			});
//			f.submit({
//				waitMsg:"正在保存...",
//                success: function(form, action){
//                    var me=this,
//                    	result = action.result,
//                    	f=me.form.getForm(),
//                    	companyname=f.findField("SupplierID").getRawValue(),
//                    	categoryname=f.findField("CategoryID").getRawValue(),
//                    	store=Ext.StoreManager.lookup("Product"),
//                    	rec=f.getRecord();
//                    f.updateRecord(rec);
//                	rec.set("CompanyName",companyname);
//                	rec.set("CategoryName",categoryname);
//                    if(result.ProductID){
//                    	rec.set("ProductID",result.ProductID)
//                    	store.insert(0,rec);
//                    	rec.commit();
//						m=store.model;
//						f.loadRecord(new m());
//                    }else{
//                    	rec.commit();
//                    	me.close();
//                    }
//                },
//                failure: function(form, action){
//                    if (action.failureType === "connect") {
//                        Ext.Msg.alert('错误',
//                            '状态:'+action.response.status+': '+
//                            action.response.statusText);
//                        return;
//                    }
//                	if(action.failureType==="server"){
//                       	Ext.Msg.alert('错误', "提交失败，运行错误！");
//                    }
//                },
//                scope:me
//			});
		}else{
			Ext.Msg.alert("修改","请修改数据后再提交。")
		}
	},

    afterShow: function(){
        if (this.animateTarget) {
            //this.center();
        }
        this.callParent(arguments);
    },

	
	onReset:function(){
		this.form.getForm().reset();
	}
	
})