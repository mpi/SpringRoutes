
app
	.get('/customer/jane', function(req, res){
		
		print(req.params);
	
		res.sendJson({
			name: req.params(),
			active: true,
			status: 'OK'
		});
		
	});