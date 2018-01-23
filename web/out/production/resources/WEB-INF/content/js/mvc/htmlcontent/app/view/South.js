Ext.define('AD.Web.Test.view.South', {
    extend: 'Ext.Container',
    //alias: 'widget.test',
    xtype: 'test.south',
    id: 'mv-test-south',
    //layout:"border",
    defaultType: 'button',
    //height:300,
    
    initComponent: function() {
    	var me = this;
    	me.items = [
    	
//    	,
    	{
                text: '刷新',
                action: 'list2',
               region:'center'
            },{
    		region:'center',
    		xtype:'htmlContainer',
    		width:300,
    		heigth:100,
    		url: './../index/index.html'
    	}
    	];
    	
    	
    	this.callParent(arguments);
    }
});