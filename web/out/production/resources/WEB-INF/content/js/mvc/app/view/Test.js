Ext.define('AD.Web.Test.view.Test', {
    extend: 'Ext.Container',
    //alias: 'widget.test',
    xtype: 'test',
    id: 'mv-test',
    layout:"border",
    defaultType: 'button',
    //alias: 'widget.songcontrols',
    //height: 70,
    
    initComponent: function() {
    	var me = this;
    	me.grid=Ext.widget("grid",{
			title:"订单",region:"center",minHeight:200,
			tbar:{xtype:"pagingtoolbar",store:"Tests",displayInfo:true},
			selMode:{mode:"SINGLE"},store:"Tests",
			//collapsible: true,
			columns:[
				{xtype:"rownumberer",sortable:false,width:60},
				{text:'订单号',dataIndex:'artist'},
				{text:'客户编号',dataIndex:'album'},
				{text:'客户名称',dataIndex:'name',sortable:false,flex:1},
				{xtype:"datecolumn",text:'订购日期',dataIndex:'OrderDate',format:"Y-m-d",width:100}
			]
//			,
//			viewConfig:{
//				listeners:{
//					scope:me,
//					refresh:me.onOrderRefresh
//				}
//			},
//			listeners:{
//				scope:me,
//				selectionchange:me.onOrderSelect
//			}
    	});
    	//me.layout="border";
    	me.items = [
    		{
    			width:600,
    			xtype:'test.south',
				region:'west'
    		}
//    		{
//    			xtype:'container',
//    			//html:'<iframe src=content.html style="width: 100%; height: 100%; border: 0"/>'
//			}
    		,{
    		xtype:'container',
    		defaultType:'button',
    		layout:{type:'hbox',
    		align:'left'
    		},
    		region:'north'
    		,
    			items:[{
                text: '刷新',
                action: 'list'
            },{
            	text:'获取',
            	action:'get'
    		},{
                text: '新增',
                action: 'add'
            },{
                text: '编辑',
                action: 'edit'
            },{
                text: '删除',
                action: 'del'
            },{
                text: 'add',
                action: 'add-test'
            },{
                text: 'form',
                action: 'form-test'
            },{
                text: 'HTML内容',
                action: 'form-html-content'
            }
            ,{
                text: 'URL',
                action: 'from-url-content'
            },{
                text: 'to control',
                action: 'content-to-control'
            }]
            }
            ,{
            	xtype:'container',
            	region:'center',
            	items:[me.grid],
            	id:'center-content'
            }
            ]; 

        
        this.callParent(arguments);
    }
});