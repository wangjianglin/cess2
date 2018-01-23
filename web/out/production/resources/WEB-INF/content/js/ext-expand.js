
Ext.define('AD.data.reader.Reader',{
	extend:'Ext.data.reader.Reader',
	alias:'reader.ad.json',
	root: '',

    /**
     * @cfg {String} record The optional location within the JSON response that the record data itself can be found at.
     * See the JsonReader intro docs for more details. This is not often needed.
     */

    /**
     * @cfg {Boolean} useSimpleAccessors True to ensure that field names/mappings are treated as literals when
     * reading values.
     *
     * For example, by default, using the mapping "foo.bar.baz" will try and read a property foo from the root, then a property bar
     * from foo, then a property baz from bar. Setting the simple accessors to true will read the property with the name
     * "foo.bar.baz" direct from the root object.
     */
    useSimpleAccessors: false,

    /**
     * Reads a JSON object and returns a ResultSet. Uses the internal getTotal and getSuccess extractors to
     * retrieve meta data from the response, and extractData to turn the JSON data into model instances.
     * @param {Object} data The raw JSON data
     * @return {Ext.data.ResultSet} A ResultSet containing model instances and meta data about the results
     */
    readRecords: function(data) {
        //this has to be before the call to super because we use the meta data in the superclass readRecords
        if (data.metaData) {
            this.onMetaChange(data.metaData);
        }

        /**
         * @property {Object} jsonData
         * A copy of this.rawData.
         * @deprecated Will be removed in Ext JS 5.0. This is just a copy of this.rawData - use that instead.
         */
        this.jsonData = data;
        return this.callParent([data]);
    },

    //inherit docs
    getResponseData: function(response) {
        var data, error;
 
        try {
        	var resultString = response.responseText;
	    	resultString = decodeURIComponent(resultString);
	    	resultString = AD.decode64(resultString);
	    	var r = Ext.decode(resultString);
    		if(r != null && r !== 'undefined'){
    			if(r.code != 0){
    				throw new Error(r.code,r.message);
    			}
    		}else{
    			throw new Error(-1,"没有返回数据！");
    		}

            data = r.result;
            return this.readRecords(data);
        } catch (ex) {
            error = new Ext.data.ResultSet({
                total  : 0,
                count  : 0,
                records: [],
                success: false,
                message: ex.message
            });

            this.fireEvent('exception', this, response, error);

            Ext.Logger.warn('Unable to parse the JSON returned by the server');

            return error;
        }
    },

    //inherit docs
    buildExtractors : function() {
        var me = this;

        me.callParent(arguments);

        if (me.root) {
            me.getRoot = me.createAccessor(me.root);
        } else {
            me.getRoot = function(root) {
                return root;
            };
        }
    },

    /**
     * @private
     * We're just preparing the data for the superclass by pulling out the record objects we want. If a {@link #record}
     * was specified we have to pull those out of the larger JSON object, which is most of what this function is doing
     * @param {Object} root The JSON root node
     * @return {Ext.data.Model[]} The records
     */
    extractData: function(root) {
        var recordName = this.record,
            data = [],
            length, i;

        if (recordName) {
            length = root.length;
            
            if (!length && Ext.isObject(root)) {
                length = 1;
                root = [root];
            }

            for (i = 0; i < length; i++) {
                data[i] = root[i][recordName];
            }
        } else {
            data = root;
        }
        return this.callParent([data]);
    },

    /**
     * @private
     * @method
     * Returns an accessor function for the given property string. Gives support for properties such as the following:
     *
     * - 'someProperty'
     * - 'some.property'
     * - 'some["property"]'
     * 
     * This is used by buildExtractors to create optimized extractor functions when casting raw data into model instances.
     */
    createAccessor: (function() {
        var re = /[\[\.]/;

        return function(expr) {
            if (Ext.isEmpty(expr)) {
                return Ext.emptyFn;
            }
            if (Ext.isFunction(expr)) {
                return expr;
            }
            if (this.useSimpleAccessors !== true) {
                var i = String(expr).search(re);
                if (i >= 0) {
                    return Ext.functionFactory('obj', 'return obj' + (i > 0 ? '.' : '') + expr);
                }
            }
            return function(obj) {
                return obj[expr];
            };
        };
    }()),

    /**
     * @private
     * @method
     * Returns an accessor expression for the passed Field. Gives support for properties such as the following:
     * 
     * - 'someProperty'
     * - 'some.property'
     * - 'some["property"]'
     * 
     * This is used by buildExtractors to create optimized on extractor function which converts raw data into model instances.
     */
    createFieldAccessExpression: (function() {
        var re = /[\[\.]/;

        return function(field, fieldVarName, dataName) {
            var me     = this,
                hasMap = (field.mapping !== null),
                map    = hasMap ? field.mapping : field.name,
                result,
                operatorSearch;

            if (typeof map === 'function') {
                result = fieldVarName + '.mapping(' + dataName + ', this)';
            } else if (this.useSimpleAccessors === true || ((operatorSearch = String(map).search(re)) < 0)) {
                if (!hasMap || isNaN(map)) {
                    // If we don't provide a mapping, we may have a field name that is numeric
                    map = '"' + map + '"';
                }
                result = dataName + "[" + map + "]";
            } else {
                result = dataName + (operatorSearch > 0 ? '.' : '') + map;
            }
            return result;
        };
    }()),
    getRoot : function(data){
    	return data.list;
    },
     readRecords: function(data) {
		var me = this,
		success,
		recordCount,
		records,
		root,
		total,
		value,
		message;
		/*
		* We check here whether fields collection has changed since the last read.
		* This works around an issue when a Model is used for both a Tree and another
		* source, because the tree decorates the model with extra fields and it causes
		* issues because the readers aren't notified.
		*/
		if (me.lastFieldGeneration !== me.model.prototype.fields.generation) {
			me.buildExtractors(true);
		}
		/**
		* @property {Object} rawData
		* The raw data object that was last passed to {@link #readRecords}. Stored for further processing if needed.
		*/
		me.rawData = data;
		data = me.getData(data);
		success = true;
		recordCount = 0;
		records = [];
//		if (me.successProperty) {
//			value = me.getSuccess(data);
//			if (value === false || value === 'false') {
//				success = false;
//			}
//		}
		if (me.messageProperty) {
			message = me.getMessage(data);
		}
		// Only try and extract other data if call was successful
		if (me.readRecordsOnFailure || success) {
		// If we pass an array as the data, we dont use getRoot on the data.
		// Instead the root equals to the data.
			root = Ext.isArray(data) ? data : data.list;
			if (root) {
				total = root.length;
			}
			if (me.totalProperty) {
				value = parseInt(me.getTotal(data), 10);
				if (!isNaN(value)) {
					total = value;
				}
			}
			if (root) {
				records = me.extractData(root);
				recordCount = records.length;
			}else if(data){
				records = me.extractData([data]);
				recordCount = records.length;
			}
		}
		return new Ext.data.ResultSet({
			total : total || recordCount,
			count : recordCount,
			records: records,
			success: true,
			message: message
		});
	}
});


Ext.define('AD.data.proxy.Web', {
    extend: 'Ext.data.proxy.Server',
    alias: 'proxy.ad.web',
    alternateClassName: 'AD.data.Web',
    constructor: function(config) {
    	this.startParam = 'pageStart';
    	this.limitParam = 'pageSize';
    	this.pageParam = "pageNo";
    	this.idParam = "id";
        this.callParent([config]);
        this.setPackages();
    },
    setPackages:function(){
    	if(this.packages == null || this.packages === 'undefined'){
    		return;
    	}
    	var me = this;
    	var tmp = new Object();
		for(var key in this.packages){
			if(key === 'version'){
				continue;
			}
			var p = this.packages[key];
            needsCopy = true;
	        if (p === undefined || typeof p == 'string') {
	            p = {
	                type: p
	            };
	            needsCopy = false;
	        }
	
	        if(typeof p == 'object' && p.type && typeof p.type === 'string'){
	        //if (reader.isReader) {
	           // reader.setModel(me.model);
	        //} else {
	            if (needsCopy) {
	                p = Ext.apply({}, p);
	            }
	            p = AD.create(p.type,p);
	            tmp[key] = p;
	        }

		}
		//this.packages = tmp;
		AD.apply(this.packages,tmp);
    },
    actionMethods: {
        create : 'POST',
        read   : 'GET',
        update : 'POST',
        destroy: 'POST'
    },
    doRequest: function(operation, callback, scope) {
        var writer  = this.getWriter();
        
        var pack = this.packages[operation.action];
        if(pack == null || pack === 'undefined'){
        	//throw new Error('-2','没有数据包。');
        	callback(operation);
        	return;
        }
        
//        operation["url"] = "http://localhost:8080/web/cloud/action/comm!send.action";
        //operation["url"] = "/web/cloud/action/comm!send.action";
        operation["url"] = AD.Context["ContextPath"] + "/cloud/action/comm!send.action";
        
        request = this.buildRequest(operation, callback, scope);
        var jsonParam ='{' +
    		'\"location\":' +
    		'\"' + pack.location + '\"' +
    				',\"timestamp\":' +
    				'3423423423' +//Utils.Timestamp);
                	',\"sequeueid\":' +
                	'3423048023' +//Utils.Sequeue);
					',\"version\":{\"major\":' +
					'' + pack.version.major +'' +
							',\"minor\":' +
							''+pack.version.minor+'' +
									'}' +
									',\"data\":';
        var packParams = pack.getParams();
        Ext.apply(packParams,request.params)
        if(packParams.pageNo){
        	packParams.pageNo = packParams.pageNo - 1;
        }
        //sb.Append(JsonUtil.Serialize(dict));
        jsonParam += AD.toJSONString(packParams);
        jsonParam += "}";
        jsonParam = AD.encode64(jsonParam);
        jsonParam = encodeURIComponent(jsonParam);
    	request.params["jsonParam"]=jsonParam;
        request.params['coding'] = AD.getPageCharset();
        if (operation.allowWrite()) {
            request = writer.write(request);
        }
        
        Ext.apply(request, {
            headers       : this.headers,
            timeout       : this.timeout,
            scope         : this,
            callback      : this.createRequestCallback(request, operation, callback, scope),
            method        : this.getMethod(request),
            disableCaching: true // explicitly set it to false, ServerProxy handles caching
        });
        
        Ext.Ajax.request(request);
        
        return request;
    },
    
    /**
     * Returns the HTTP method name for a given request. By default this returns based on a lookup on
     * {@link #actionMethods}.
     * @param {Ext.data.Request} request The request object
     * @return {String} The HTTP method to use (should be one of 'GET', 'POST', 'PUT' or 'DELETE')
     */
    getMethod: function(request) {
        //return this.actionMethods[request.action];
    	return "POST";
    },
    
    /**
     * @private
     * TODO: This is currently identical to the JsonPProxy version except for the return function's signature. There is a lot
     * of code duplication inside the returned function so we need to find a way to DRY this up.
     * @param {Ext.data.Request} request The Request object
     * @param {Ext.data.Operation} operation The Operation being executed
     * @param {Function} callback The callback function to be called when the request completes. This is usually the callback
     * passed to doRequest
     * @param {Object} scope The scope in which to execute the callback function
     * @return {Function} The callback function
     */
    createRequestCallback: function(request, operation, callback, scope) {
        var me = this;
        
        return function(options, success, response) {
            me.processResponse(success, operation, request, response, callback, scope);
        };
    },
    buildRequest: function(operation) {
		var me = this,
		params = Ext.applyIf(operation.params || {}, me.extraParams || {}),
		request;
		//copy any sorters, filters etc into the params so they can be sent over the wire
		params = Ext.applyIf(params, me.getParams(operation));
		if (operation.id !== undefined && params.id === undefined) {
			params[this.idParam] = operation.id;
		}
		request = new Ext.data.Request({
			params : params,
			action : operation.action,
			records : operation.records,
			operation: operation,
			url : operation.url,
			// this is needed by JsonSimlet in order to properly construct responses for
			// requests from this proxy
			proxy: me
		});
		request.url = me.buildUrl(request);
		/*
		* Save the request on the Operation. Operations don't usually care about Request and Response data, but in the
		* ServerProxy and any of its subclasses we add both request and response as they may be useful for further processing
		*/
		operation.request = request;
		return request;
	}
});


AD.define('AD.data.Model',{
	extend:'Ext.data.Model',
	load: function(id, config) {
		config = Ext.apply({}, config);
		config = Ext.applyIf(config, {
			action: 'get',
			id : id
		});
		var operation = new Ext.data.Operation(config),
		scope = config.scope || this,
		record = null,
		callback;
		callback = function(operation) {
			if (operation.wasSuccessful()) {
				record = operation.getRecords()[0];
				Ext.callback(config.success, scope, [record, operation]);
			} else {
				Ext.callback(config.failure, scope, [record, operation]);
			}
			Ext.callback(config.callback, scope, [record, operation]);
		};
		this.proxy.read(operation, callback, this);
	}
	
});
AD.define('TestPackage',{
		extend: 'AD.Web.Packages.Package',
		data:'',
		version : new AD.Web.Packages.Version(0,0),
		location:'/cloud/action/comm!test.action',
		getParams:function(){
			//var tmp = this.callParent();
			return {data:this.data};
		},
		constructor: function (config) {
         this.base(arguments); // calls My.app.Panel's constructor
         //...
     }
	});
	
	
AD.apply(AD,{
 
	window:function(content,config,animateTarget, callback, scope){
		
		var renderToWindow = function(){
			window.open(config,url);
		};
		
		var renderToPopup = function(){
			delete config.renderTarget;
			delete config.renderTo;
			var top = window;
		
			AD.applyIf(config, {
                title: 'Window',
                closable: true,
                closeAction: 'destroy',
                width: 600,
                //minWidth: 350,
                modal:true,
                height: 350,
                maxHeight:document.body.clientHeight,
                constrainHeader: true,
                //constrainTo:document.body,
                //contentEl:document.body,
                layout: {
                    type: 'border',
                    padding: 0
                }
                //,
                //renderTo:top.document.body
            });
            if(content !== undefined){
            	if(typeof(content) === 'string'){
            		while(top.parent && top !== top.parent && top.parent.Ext){
						top = top.parent;
					}
					var id = Ext.id('iframe','iframe-id-');
            		AD.apply(config,{
	            			region :'center',
	            			listeners:{
	            				render:function(){
	            					//alert('dd');
	            				},
	            				show:function(){
	            					//alert('dddd');
	            					var d = top.Ext.getDom(id);
	            					d.contentWindow.close = function(){
	            						//alert('dddd');
	            						win.close();
	            					}
	            				}
	            			},
				    	 	//html:'<iframe src="'+content+'" style="width: 100%; height: 100%; border: 0">正在加载页面</iframe>'
				    	 	html:'<iframe BORDER="0px" id="' + id + '" frameBorder="0" src="'+content+'" style="border-width:0px;border:0px;width: 100%; height: 100%; border: 0"></iframe>'
	    				
				    	});
					
					
//					AD.apply(config,{
//	            			//region :'center',
//				    	 	url:content
//				    	});
					
            		content = Ext.widget('container',config);
//            		content = Ext.widget('htmlContainer',{
//            			region :'center',
//            			url:content
//            		});
//            		AD.applyIf(config,{
//						items: [content]
//					});
            	}else{
            		content.region = 'center';
					AD.applyIf(config,{
						items: [content]
					});
            	}
            	
			   content.close = function(){
			   	win.close();
			   };
            }
		   win = top.Ext.create('widget.window',config);
           
            win.show(animateTarget || config.animateTarget, callback || config.callback, scope || config.scope);
		};
		
		var renderToControl = function(){
			var renderTo = config.renderTo;
			delete config.renderTarget;
			delete config.renderTo;
			
			//Ext.getDom(renderTo).innerHTML = "";
			
			if(typeof(content) === 'string'){
			AD.apply(config,{
	            			region :'center',
				    	 	html:'<iframe src="'+content+'" style="width: 100%; height: 100%; border: 0"><html><body>正在加载页面</doby></html></iframe>'
				    	});
			}else{
				AD.apply(config,{
	            			region :'center',
				    	 	items:[content]
				    	});
			}
			if(Ext.getClassName(renderTo) !== ""){
				renderTo.removeAll();
            	renderTo.add(Ext.widget('container',config));
			}else{
				config.renderTo =renderTo;
            	Ext.widget('container',config);
			}
            		
			
            
		}
		var renderTarget = config.renderTarget || 'popup';
		
		if(renderTarget === 'popup'){
			renderToPopup();
		}else if(renderTarget === 'control'){
			renderToControl();
		}else if(renderTarget === 'window'){
			renderToWindow();
		}
	}
});


Ext.define('AD.form.Panel', {
    extend:'Ext.form.Panel',
		alias: 'widget.ad.form',
	 basicFormConfigs: [
		'packages',
		'paramName',
		'isResult',
        'api', 
        'baseParams', 
        'errorReader', 
        'method', 
        'paramOrder',
        'paramsAsHash',
        'reader',
        'standardSubmit',
        'timeout',
        'trackResetOnLoad',
        'url',
        'waitMsgTarget',
        'waitTitle'
    ]
});
Ext.define('AD.form.action.ADFormOperation', {
    extend:'Ext.form.action.Action',
    alternateClassName: 'Ext.form.Action.Submit',
    alias: 'formaction.web.form',

    type: 'ad.web.form',

    /**
     * @cfg {Boolean} [clientValidation=true]
     * Determines whether a Form's fields are validated in a final call to {@link Ext.form.Basic#isValid isValid} prior
     * to submission. Pass false in the Form's submit options to prevent this.
     */

    // inherit docs
    run : function(){
        var form = this.form;
        if (this.clientValidation === false || form.isValid()) {
            this.doSubmit();
        } else {
            // client validation failed
            this.failureType = Ext.form.action.Action.CLIENT_INVALID;
            form.afterAction(this, false);
        }
    },

    
    /**
     * @private
     * Performs the submit of the form data.
     */
    doSubmit: function() {
    	if(this.form.packages){
    		var pack;
    		var action = this.action;
    		if(this.action){
    			action = this.action;
    		}else{
    			action = 'submit';
    		}
    		if(action == 'load'){
    			this.isResult = this.isResult || true;
    		}else{
    			this.isResult = this.isResult || false;
    		}
    		pack = this.form.packages[action];
    		if(pack){
    			
    			if(!pack.$classname){
    				needsCopy = true;
			        if (typeof pack == 'string') {
			            pack = {
			                type: pack
			            };
			            needsCopy = false;
			        }
			
			        if(typeof pack == 'object' && pack.type && typeof pack.type === 'string'){
			        //if (reader.isReader) {
			           // reader.setModel(me.model);
			        //} else {
			            if (needsCopy) {
			                pack = Ext.apply({}, pack);
			            }
			            pack = AD.create(pack.type,pack);
			            this.form.packages[action] = pack;
			        }
    			}
    			if(this.form.paramName){
    				pack[this.form.paramName] = pack[this.form.paramName] || {};
    				AD.apply(pack[this.form.paramName],this.getParams());
    			}else{
    				AD.apply(pack,this.getParams());
    			}
				AD.Web.Http.HttpCommunicate.request(pack,{
					result:function(result,warning){
						this.onSuccess(result,warning);
					},
					fault:function(error){
					//this.
					},scope:this
				});
    			
    		}
    	}
//        var formEl;
//            ajaxOptions = Ext.apply(this.createCallback(), {
//                url: this.getUrl(),
//                method: this.getMethod(),
//                headers: this.headers
//            });
//
//        // For uploads we need to create an actual form that contains the file upload fields,
//        // and pass that to the ajax call so it can do its iframe-based submit method.
//        if (this.form.hasUpload()) {
//            formEl = ajaxOptions.form = this.buildForm();
//            ajaxOptions.isUpload = true;
//        } else {
//            ajaxOptions.params = this.getParams();
//        }
//
//        Ext.Ajax.request(ajaxOptions);
//
//        if (formEl) {
//            Ext.removeNode(formEl);
//        }
    },

    /**
     * @private
     * Builds the full set of parameters from the field values plus any additional configured params.
     */
    getParams: function() {
        var nope = false,
            configParams = this.callParent(),
            fieldParams = this.form.getValues(nope, nope, this.submitEmptyText !== nope);
        return Ext.apply({}, fieldParams, configParams);
    },

    /**
     * @private
     * Builds a form element containing fields corresponding to all the parameters to be
     * submitted (everything returned by {@link #getParams}.
     *
     * NOTE: the form element is automatically added to the DOM, so any code that uses
     * it must remove it from the DOM after finishing with it.
     *
     * @return {HTMLElement}
     */
    buildForm: function() {
        var fieldsSpec = [],
            formSpec,
            formEl,
            basicForm = this.form,
            params = this.getParams(),
            uploadFields = [],
            fields = basicForm.getFields().items,
            f,
            fLen   = fields.length,
            field, key, value, v, vLen,
            u, uLen;

        for (f = 0; f < fLen; f++) {
            field = fields[f];

            if (field.isFileUpload()) {
                uploadFields.push(field);
            }
        }

        function addField(name, val) {
            fieldsSpec.push({
                tag: 'input',
                type: 'hidden',
                name: name,
                value: Ext.String.htmlEncode(val)
            });
        }

        for (key in params) {
            if (params.hasOwnProperty(key)) {
                value = params[key];

                if (Ext.isArray(value)) {
                    vLen = value.length;
                    for (v = 0; v < vLen; v++) {
                        addField(key, value[v]);
                    }
                } else {
                    addField(key, value);
                }
            }
        }

        formSpec = {
            tag: 'form',
            action: this.getUrl(),
            method: this.getMethod(),
            target: this.target || '_self',
            style: 'display:none',
            cn: fieldsSpec
        };

        // Set the proper encoding for file uploads
        if (uploadFields.length) {
            formSpec.encoding = formSpec.enctype = 'multipart/form-data';
        }

        // Create the form
        formEl = Ext.DomHelper.append(Ext.getBody(), formSpec);

        // Special handling for file upload fields: since browser security measures prevent setting
        // their values programatically, and prevent carrying their selected values over when cloning,
        // we have to move the actual field instances out of their components and into the form.
        uLen = uploadFields.length;

        for (u = 0; u < uLen; u++) {
            field = uploadFields[u];
            if (field.rendered) { // can only have a selected file value after being rendered
                formEl.appendChild(field.extractFileInput());
            }
        }

        return formEl;
    },



    /**
     * @private
     */
    onSuccess: function(result) {
        //var form = this.form,
        //    success = true,
        //    result = this.processResponse(response);
        //if (result !== true && !result.success) {
        //    if (result.errors) {
         //       form.markInvalid(result.errors);
         //   }
         //   this.failureType = Ext.form.action.Action.SERVER_INVALID;
         //   success = false;
       // }
    	var isResult = this.isResult;
    	if(isResult == true){
    		this.form.clearInvalid();
        	this.form.setValues(result);
    	}
        this.form.afterAction(this, true);
    },

    /**
     * @private
     */
    handleResponse: function(response) {
        var form = this.form,
            errorReader = form.errorReader,
            rs, errors, i, len, records;
        if (errorReader) {
            rs = errorReader.read(response);
            records = rs.records;
            errors = [];
            if (records) {
                for(i = 0, len = records.length; i < len; i++) {
                    errors[i] = records[i].data;
                }
            }
            if (errors.length < 1) {
                errors = null;
            }
            return {
                success : rs.success,
                errors : errors
            };
        }
        return Ext.decode(response.responseText);
    }
});


Ext.define('AD.form.action.Submit', {
	extend:'Ext.form.action.Submit',
	//xtype:'ad.upload',
	alias: 'formaction.web.upload',
	
	onSuccess: function(response) {
        var form = this.form,
            success = true
        
	    	var resultString = response.responseText;
	    	resultString = decodeURIComponent(resultString);
	    	resultString = AD.decode64(resultString);
	    	var r = eval('(' + resultString + ')');
		    
           // result = this.processResponse(response);
//        if (result !== true && !result.success) {
//            if (result.errors) {
//                form.markInvalid(result.errors);
//            }
//            this.failureType = Ext.form.action.Action.SERVER_INVALID;
//            success = false;
//        }
	    this.result = r.result;
        form.afterAction(this, true);
    }
	
});

Ext.define('AD.container.Container', {
    extend: 'Ext.container.Container',
    xtype:'htmlContainer',
	url:'',
//	constructor:function(){
//		Ext.getBody().onload = function(){
//			//alert('00');
//		};
//	
//		this.callParent(arguments);
//	},
	layout:{
		type:'vbox',
		align:'stretch'
	},
	autoHeight:false,
	autoWidth:false,
	//height:500,
	listeners:{
//		added:function(){
//			debugger  
//		},
		render:function(){
				//debugger
			var me = this;
	    		var frameId = Ext.id('iframe','IframeId');
	    	var tmp = Ext.widget('container',{
	    		flex:1,
	    		html:'<iframe BORDER="0px" SCROLLING="no" id="'+frameId+'" frameBorder="0" src="'+this.url+'" style="border-width:0px;border:0px;width: 100%; height: 100%; border: 0"></iframe>',
    			listeners:{
    				render:function(){
    					//debugger
    					var d = top.Ext.getDom(frameId);
    					
    					
//    					d.contentWindow.document.body.onresizeend = function(){
//    						//debugger
//    						//alert('dddd');
    						if(me.autoWidth == true){
    							try{
    								//me.setHeight(d.contentWindow.document.body.scrollHeight);
    							}catch(e){
    							}
    							var widthInterval;
    							window['setInterval'+frameId+'SetWidth'] = function(){
    								try{
	    								if(me.getWidth() != d.contentWindow.document.body.scrollWidth){
	    									me.setWidth(d.contentWindow.document.body.scrollWidth);
	    								}
    								}catch(e){
	    								if(widthInterval){
	    									window.clearInterval(widthInterval)
	    								}
    								}
    							};
    							try{
    								widthInterval = window.setInterval('(function(){window["setInterval'+frameId+'SetWidth"]();})();',10);
    							}catch(e){
    								if(widthInterval){
    									window.clearInterval(widthInterval);
    								}
    							}
    						}
//    						
    						if(me.autoHeight == true){
    							try{
    								//me.setWidth(d.contentWindow.document.body.scrollWidth);
    							}catch(e){
    							}
    							window['setInterval'+frameId+'SetHeight'] = function(){
    								try{
	    								if(me.getHeight() != d.contentWindow.document.body.scrollHeight){
	    									me.setHeight(d.contentWindow.document.body.scrollHeight);
	    								}
    								}catch(e){
	    								if(heightInterval){
	    									window.clearInterval(heightInterval)
	    								}
    								}
    							};
    							//d.contentWindow.setInterval('ch()',800);
    							var heightInterval;
    							try{
    								heightInterval = window.setInterval('(function(){window["setInterval'+frameId+'SetHeight"]();})();',10);
    							}catch(e){
    								if(heightInterval){
    									window.clearInterval(heightInterval)
    								}
    							}
    						}
//    						//win.close();
//    					}
    					//alert('dd');
    				}
//    				,
//    				show:function(){
//    					debugger
//    					//alert('dddd');
//    					var d = top.Ext.getDom(id);
//    					d.contentWindow.onresize  = function(){
//    						debugger
//    						//alert('dddd');
//    						//win.close();
//    					}
//    				}
    			}
	    	});
	    	this.removeAll();
            	this.add(tmp);
		}
	},
	 initComponent: function() {
    	var me=this;
//    	if(me.rendered){
//    		me.controlRender();
//    	}else{
//    		this.un('render',me.controlRender,this);
//    	}

//    	 	Ext.onDocumentReady(function(){
//		
//    	 		debugger
//	    	var tmp = Ext.widget('container',{
//	    		flex:1,
//	    		html:'<iframe BORDER="0px" frameBorder="0" src="'+this.url+'" style="border-width:0px;border:0px;width: 100%; height: 100%; border: 0"></iframe>' +
//	    				'<div style=""></div>'
//	    	});
//	    	this.removeAll();
//            	this.add(tmp);
//    	},this);
//    	me.items=[
//    	{xtype:'container',
//    	flex:1,
//    	html:'<iframe BORDER="0px" frameBorder="0" src="'+this.url+'" style="border-width:0px;border:0px;width: 100%; height: 100%; border: 0"></iframe>' +
//	    				'<div style=""></div>'
//    	}
//    	];
    	me.callParent(arguments);
	 }
	 
});
	
/**
 * 
 */
Ext.define('AD.app.Application', {
    extend: 'Ext.app.Application',
	
   widget: function(widgetName, controllerName,cfg) {
        var ctrl = this.getController(controllerName);
        var wg = Ext.widget(widgetName, cfg);
        ctrl.init();
        return wg;
    },
    create: function(name, controllerName,cfg) {
        var ctrl = this.getController(controllerName);
        var wg = Ext.create(name, cfg);
        ctrl.init();
        return wg;
    },
    createByAlias: function(alias, controllerName,cfg) {
        var ctrl = this.getController(controllerName);
        var wg = Ext.createByAlias(alias, cfg);
        ctrl.init();
        return wg;
    }
});