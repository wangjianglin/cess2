


test("http test:",function(){

//	var b = 4;
//	var a = {
//	
//	};
//	debugger
//	a.b = 'ddd';
//	var d = a["b"];//var d = a.b;
//	
//	var A = function(){
//		this.x = 5;
//		this.y = 6;
//		this.c = function(){
//			alert('cvv');
//		}
//	};
//	
//	A.prototype.f = function(){
//		alert('0000000000000');
//	}
//	
//	var avar = new A();
//	
//	avar.f();
//
//
//	for(var av in a){
//		
//	}
	var d = 7;
	var a = 8;
	var A = (function(c){
		var a = c;
		return function(){
			a++;
			//alert('a='+a);
		}
	})(9);
	A();
	a =1;
	ok(1 == 1 ,'OK!');
}
);


test("AD.ns:",function(){

	//ok(AD.test.fun() == "test",'OK!');
	AD.ns("AD.test");
	
	AD.test.fun = function(){
		return "test";
	};
	ok(AD.test.fun() == "test",'OK!');
}
);
//
//test('ext extend',function(){
//	Ext.define('ext.tmp.BaseClass',{
//		data:'base data',
//		getData:function(){
//			return this.data;
//		},
//		
//		getBaseData:function(){
//			return this.getData();
//		},
//		constructor:function(){
//			
//		}
//	});
//	
//	Ext.define('ext.tmp.SubClass',{
//		data:'sub data',
//		getData:function(){
//			return this.data;
//		}
//	});
//	
//	var base = AD.create('ext.tmp.BaseClass');
//	var sub = AD.create('ext.tmp.SubClass');
//	ok(true,'base getData():' + base.getData());
//	ok(true,'sub getData():' + sub.getData());
//	ok(true,'base getBaseData():'+sub.getBaseData());
//});
test('ad extend',function(){
	AD.define('tmp.BaseClass',{
		data:'base data',
		getData:function(){
			return this.data;
		},
		
		getBaseData:function(){
			return this.getData()+':' + this.data + ':' + this.getSubData();
		},
		constructor:function(){
			
		}
	});
	
	AD.define('tmp.SubClass',{
		data:'sub data',
		extend:'tmp.BaseClass',
		getData:function(){
			return this.data;
		},getSubData:function(){
			return this.data;
		}
	});
	
	var base = AD.create('tmp.BaseClass');
	var sub = AD.create('tmp.SubClass');
	ok(true,'base getData():' + base.getData());
	ok(true,'sub getData():' + sub.getData());
	ok(true,'base getBaseData():'+sub.getBaseData());
	//ok(true,'base getBaseData():'+base.getBaseData());
});
//test("AD.define",function(){
//
//	AD.define('AD.test',{
//		extend: 'AD.Web.Packages.Package',
//		name:'value',
//		getParams:function(){
//			var tmp = this.base();
//			return {name:'value'};
//		},
//		constructor: function (config) {
//         this.base(arguments); // calls My.app.Panel's constructor
//         //...
//     }
//	});
//	test = AD.create('AD.test');
//	test.getParams();
//	ok(test.name == "value",'OK!');
//});
//
//
//
//test("url:",function(){
//
//	var testUrl = function(url){
//		var tmpUrl = encodeURIComponent(url);
//		equal(1,1,tmpUrl);
//		equal(url,decodeURIComponent(tmpUrl),'ok');
//	};
//	
//	testUrl('http://localhost:8080/web/');
//	testUrl('http://localhost:8080/web/?name=a+b');
//	testUrl('eyJsb2NhdGlvbiI6Ii9jbG91ZC9hY3Rpb24vY29tbSF0ZXN0LmFjdGlvbiIsInRpbWVzdGFtcCI6' +
//			'MzQyMzQyMzQyMywic2VxdWV1ZWlkIjozNDIzMDQ4MDIzLCJ2ZXJzaW9uIjp7Im1ham9yIjowLCJt' +
//			'aW5vciI6MH0sImRhdGEiOnsiZGF0YSI6Iua1i+ivleS4reaWh++8gSJ9fQ==');
//});
//
//test("base 64",function(){
//	
//	str = "test data!";
//	equal(1,1,str);
//	var base64Str = AD.encode64(str);
//	equal(1,1,base64Str);
//	base64Str = AD.decode64(base64Str);
//	equal(1,1,base64Str);
//	
//	str = "测试中文！";
//	equal(1,1,str);
//	var base64Str = AD.encode64(str);
//	equal(1,1,base64Str);
//	base64Str = AD.decode64(base64Str);
//	equal(1,1,base64Str);
//});
//
test("HTTP Request:",function(){

	AD.define('tmp.TestPackage',{
		extend:'AD.Web.Packages.Package',
		location:'/cloud/action/comm!test.action',
        getParams:function()
        {
            var param = {};
            param.data = this.data;
            return param;
        },
        constructor : function(){
			this.base(arguments);
			this.version.major = 0;
	        this.version.minor = 0;
		}
	});
	pack = new tmp.TestPackage();
	pack.data = '测试中文！';
	AD.Web.Http.HttpCommunicate.request(pack,{result:function(result,warning){
		equal(1,1,result);
	},
	fault:function(error){
	
	},
	async:false,
	scope:{pack:pack,name:'ddd'}
	});
	
	AD.Web.Http.HttpCommunicate.request(pack,{result:function(result,warning){
		equal(1,1,result);
	},
	fault:function(error){
	
	},
	async:false
	});

});
//
//test('context request',function(){
//	equal(1,1,'context request');
//	
//	var scripts = document.getElementsByTagName('script');
//
//
//    for (i = 0, ln = scripts.length; i < ln; i++) {
//        scriptSrc = scripts[i].src;
//
//        match = scriptSrc.match(/http\.js$/);
//
//        if (match) {
//            path = scriptSrc.substring(0, scriptSrc.length - match[0].length);
//            break;
//        }
//    }
//
//	AD.Web.Http.Ajax.request(path + 'context.jsp',{
//		async:false,
//		result:function(r){
//			equal(1,1,r);
//		}
//	})
//	equal(1,1,AD.Context);
//	equal(1,1,AD.Context["ContextPath"]);
//	
//});