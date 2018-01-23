Ext.define('AD.html.view.Test', {
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
    	
    	me.items = [
    		
    		{
    		xtype:'container',
    		defaultType:'button',
    		layout:{type:'hbox',
    		align:'left'
    		},
    		region:'north'
    		,
    			items:[{
                text: 'AD.Context',
                action: 'AD.Context'
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