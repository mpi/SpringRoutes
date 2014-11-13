
//var bean = function(beanName){
//	
//	return {
//		__noSuchMethod__: function() {
//			
//			var method = [].splice.call(arguments, 0, 1);
//
//			print(beanName + "." + method + '(' + JSON.stringify(arguments) + ')');
//			
//			var bean = _app.bean(beanName);
//			print('bean: ' + bean);
//			
//			print('result: ' + bean.hello('Dupa', 10));
//			
//			print('method: ' + bean[method]);
//			return target.apply(bean, arguments);
//		}		
//	};
//};

var bean = function(beanName){
	return _app.bean(beanName);
};

var route = function(_r){

	var _route = _r || _app.route();
	
	var req = function(_req){
		
		try{
			var bodyJson = JSON.parse(_req.body());
		} catch(e){
			// not a json
		}
		
		return {
			param: _req.param,
			params: _req.params,
			body: bodyJson || {}
		};
	};

	var res = function(_res){
		
		return {
			send: function(obj){
				return _res.send(obj);
			},
			sendJson: function(obj){
				return _res.send(JSON.stringify(obj));
			}
		};
	};
	
	var delegateTo = function(callback){
		return function(_req, _res){
			callback(new req(_req), new res(_res));
		};
	};
	
	var r = {
		
		get: function(url, callback){
			return route(_route.get(url, delegateTo(callback))); 
		},
		post: function(url, callback){
			return route(_route.post(url, delegateTo(callback))); 
		},
		path: function(url){
			return route(_route.path(url));
		},
		produces: function(contentType){
			return route(_route.produces(contentType));
		},
		consumes: function(contentType){
			return route(_route.consumes(contentType));
		},
		options: function(options){
			var _r = _route;
			if(options.consumes) _r = _r.consumes(options.consumes);
			if(options.produces) _r = _r.consumes(options.produces);
			return route(_r);
		}
	};
	
	return r;
};
