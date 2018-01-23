Ext.define('AD.Web.Test.view.WindowContent', {
    extend: 'Ext.Container',
    //alias: 'widget.test',
    xtype: 'test-window-content',
    id: 'mv-test-window-content',
    layout:"border",
    defaultType: 'button',
    //alias: 'widget.songcontrols',
    //height: 70,
    initComponent: function() {
    	this.items = [Ext.create('Ext.panel.Panel', {
                //title: 'Layout Window',
                //closable: true,
                closeAction: 'hide',
                width: 600,
                minWidth: 350,
                modal:true,
                height: 350,
                layout: {
                    type: 'border',
                    padding: 5
                },
                items: [{
                	region:'north',
                	xtype:'button',
                	action:'test',
                	text:'测试'
                },{
                    region: 'west',
                    title: 'Navigation',
                    width: 200,
                    split: true,
                    collapsible: true,
                    floatable: false
                }, {
                    region: 'center',
                    xtype: 'tabpanel',
                    items: [{
                        title: 'Bogus Tab',
                        html: 'Hello world 1'
                    }, {
                        title: 'Another Tab',
                        html: 'Hello world 2'
                    }, {
                        title: 'Closable Tab',
                        html: 'Hello world 3',
                        closable: true
                    }]
                }]
            })];
    	this.callParent();
    },
    
    test:function(){
    	alert("8888888888888888888888");
    }
});