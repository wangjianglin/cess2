Ext.define('AD.html.view.Viewport', {
    extend: 'Ext.container.Viewport',
    //layout: 'fit',
    id: 'test-viewport',
    //layout:{type:'vbox',align:"stretch"},
    layout:"border",
    defaults:{xtype:'container',style:"background-color:#D3E1F1;"},
    requires: [
        'AD.html.view.Test',
        'AD.html.view.Form'
    ],
    
    initComponent: function() {
    	this.items = [{
        region: 'center',
        //html: '<h1 class="x-panel-header">Page Title</h1>',
        border: true,
        margins: '0 0 5 0',
        xtype: 'test'
        //xtype:'test.south'
        //height:100
    }],
//        this.items = [{
//        region: 'north',
//        //html: '<h1 class="x-panel-header">Page Title</h1>',
//        border: true,
//        margins: '0 0 5 0',
//        xtype: 'test'
//        //height:100
//    }, {
//        region: 'west',
//        collapsible: true,
//        title: 'Navigation',
//        width: 150
//        // could use a TreePanel or AccordionLayout for navigational items
//    }, {
//        region: 'south',
//        title: 'South Panel',
//        collapsible: true,
//        html: 'Information goes here',
//        split: true,
//        height: 100,
//        minHeight: 100
//    }, {
//        region: 'east',
//        title: 'East Panel',
//        collapsible: true,
//        split: true,
//        width: 150
//    }, {
//        region: 'center',
//        xtype: 'tabpanel', // TabPanel itself has no title
//        activeTab: 0,      // First tab active by default
//        items: {
//            title: 'Default Tab',
//            html: 'The first tab\'s content. Others may be added dynamically'
//        }
//    }];
        
        this.callParent(arguments);
    }
});