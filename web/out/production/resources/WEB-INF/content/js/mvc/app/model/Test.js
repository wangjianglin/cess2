Ext.define('AD.Web.Test.model.Test', {
    extend: 'Ext.data.Model',
    fields: ['id', 'name', 'desc'],
     requires: [
       'AD.Web.Test.package.ReadPackage',
		'AD.Web.Test.package.CreatePackage',
		'AD.Web.Test.package.UpdatePackage',
		'AD.Web.Test.package.DestroyPackage'
    ],
    proxy: {
        type: 'ad.web',
        url: 'data/songs.json',
        params:{
        	name:'value'
        },api:{
			        	read:"/Restful/Products",
			        	create:"/Restful/Action?act=add",
			        	update:"/Restful/Action?act=edit",
			        	destroy:"/Restful/Action?act=del"
			        },
			        packages:{
			        	read:{
			        		type:'AD.Web.Test.package.ReadPackage'
			        	},
			        	create:'AD.Web.Test.package.CreatePackage',
			        	update:'AD.Web.Test.package.UpdatePackage',
			        	destroy:'AD.Web.Test.package.DestroyPackage'
			        },
        reader: {
            type: 'ad.json',
            root: 'results'
        }
    }
});