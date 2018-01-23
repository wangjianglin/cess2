//Ext.application({
//    name: 'AD.Web.Test',
//    
//    autoCreateViewport: true,
//    
//    models: ['Test'],    
//    stores: ['Tests'],
//    controllers: ['Test']
//});

Ext.define('AD.html.Application', {
    extend: 'Ext.app.Application',
    name: 'AD.html',

    controllers: ["Test"],
	views:['Test'],
    autoCreateViewport: true,

    launch: function() {
        AD.html.app = this;
    }
    
});