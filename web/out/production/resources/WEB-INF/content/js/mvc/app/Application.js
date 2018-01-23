//Ext.application({
//    name: 'AD.Web.Test',
//    
//    autoCreateViewport: true,
//    
//    models: ['Test'],    
//    stores: ['Tests'],
//    controllers: ['Test']
//});

Ext.define('AD.Web.Test.Application', {
    extend: 'Ext.app.Application',
    name: 'AD.Web.Test',

    controllers: ["Test","WindowContent"],
	//views:['Form'],
    autoCreateViewport: true,

    launch: function() {
        AD.Web.Test.app = this;
    }
    
});