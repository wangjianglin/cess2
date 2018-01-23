AD.define('AD.Web.Test.package.UpdatePackage',{
		extend: 'AD.Web.Packages.Package',
		version : new AD.Web.Packages.Version(0,0),
		location:'/web/action/ext!update.action',
		data:null,
		getParams:function(){
			//var tmp = this.callParent();
			return {data:this.data};
		},
		constructor: function (config) {
         this.base(arguments); // calls My.app.Panel's constructor
         //...
     }
	});