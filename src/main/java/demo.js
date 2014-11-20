
	var clock = bean('clock');

	var app = route();
	
	app
		.path('/xyz')
		.produces('application/json+xyz')
		.get('/hello/{name}', function(req, res){
			res.send('Hello ' + req.params.name + '!');
		})
		.post('/hello/{name}', function(req, res){
		
			res
				.status(201)
				.sendJson({
					who: req.params.name,
					message: req.body,
					time: '' + clock.time()
				});
		});