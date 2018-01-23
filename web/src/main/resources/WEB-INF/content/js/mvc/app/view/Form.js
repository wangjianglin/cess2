Ext.define("AD.Web.Test.view.Form",{
	extend:"Ext.Container",
	xtype:'form.view',
    resizable: false,
    
    requires: [
       'AD.Web.Test.controller.Form'
    ],
    controllers:['Form'],
    initComponent: function() {
    	var me=this;
    	
    	me.form=Ext.create(AD.form.Panel,{
			border:false,bodyPadding:5,
			bodyStyle:"background:#DFE9F6",
			//trackResetOnLoad:true,
			waitTitle:"请等待...",
			//api:{submit:Ext.app.Product.Add},
			packages:{
				submit:'AD.Web.Packages.TestPackage'
			},
			fieldDefaults: {
             	labelWidth:80,labelSeparator:"：",anchor:"0"
			},
			items:[
				{xtype:"textfield",fieldLabel:"产品编号",
					name:"ProductID",readOnly:true,
					readOnlyCls:"x-item-disabled"
				},
				{xtype:"textfield",fieldLabel:"产品名称",
					name:"ProductName",allowBlank:false,
					maxLength:40
				},
				{xtype:"combobox",name:"SupplierID",
					fieldLabel:"供应商",valueField:"SupplierID",
					displayField:"CompanyName"//,
					//store:"SupplierCombo",
					//forceSelection:false,queryMode:"local",
					//allowBlank:false,minChars:1
            	},
				{xtype:"combobox",name:"CategoryID",
					fieldLabel:"产品类别",valueField:"CategoryID",
					displayField:"CategoryName"//,
					//store:"CategoryCombo",
					//forceSelection:true,queryMode:"local",
					//allowBlank:false,minChars:1
            	},
				{xtype:"textfield",fieldLabel:"单位",
					name:"QuantityPerUnit",maxLength:20
				},
				{xtype:"numberfield",fieldLabel:"单价",
					name:"UnitPrice",hideTrigger:true,
					minValue:0,autoStripChars:true
				},
				{xtype:"displayfield",fieldLabel:"库存",name:"UnitsInStock"},
				{xtype:"displayfield",fieldLabel:"订购数量",name:"UnitsOnOrder"},
				{xtype:"displayfield",fieldLabel:"再订购量",name:"ReorderLevel"},
				{xtype:"checkbox",fieldLabel:"",boxLabel:'停产',
					name:'Discontinued',inputValue:true,hideEmptyLabel:false
				}
			],
			dockedItems: [{
				xtype: 'toolbar',dock:'bottom',ui:'footer',layout:{pack:"center"},
			    items: [
					{text:"保存",disabled:false,formBind:true,handler:me.onSave,scope:me}
					,
					{text:"重置",handler:me.onReset,scope:me}
			    ]
			}]
    	});
    	
    	me.items=[me.form]
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