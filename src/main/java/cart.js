var domain = com.github.mpi.spring_routes.domain;
var ProductID = domain.ProductID;
var Quantity = domain.Quantity;
var BigDecimal = java.math.BigDecimal;

var app = route();
var cart = bean('cart');

app
	.produces('application/json')
	.consumes('application/json')
	.path('/app')
	.get('/cart', function(req, res){
		
		var json = [];
		
		cart.forEach(function(productID, qty){
			
			json.push({
				product: productID.toString(),
				qty: {
					amount: qty.amount(),
					uom: qty.unitOfMeasure()
				}
			});
		});
		
		res.sendJson(json);
	})
	.post('/cart', function(req, res){
		
		var json = req.body;
		
		cart.add(new ProductID(json.product), new Quantity(BigDecimal.valueOf(json.qty.amount), json.qty.uom));
	});

