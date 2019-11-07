var fs = require('fs')
var path=require('path');

var filePath = path.resolve(__dirname + '/../client/assets/proto');
fs.readdir(filePath, 'utf8', function(err, data) {
	if(err) {
		return console.log(err);
	}

	data.forEach(function(item, index) {
		let posfixIdx = item.lastIndexOf(".");
		let fileExt = item.substr(posfixIdx);
		if(fileExt === ".js") {
			console.log("process " + item);
		
			let messagePackage = item.substr(0, posfixIdx - 3);
			fs.readFile(filePath + "/" + item, "utf8", function(err, file) {
				if(err) {
					return console.log(err);
				}

				var result = file.replace(/com.seeu.framework.websocket/g, messagePackage);
				fs.writeFile(filePath + "/" + item, result, "utf8", function(err) {
					if(err) {
						return console.log(err);
					}
				});
			});
		}
	});
});