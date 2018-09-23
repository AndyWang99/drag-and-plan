var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

var queue = [];

server.listen(8080, function() {
	console.log("Server is now running...");
});

io.on('connection', function(socket) {
	console.log("User Connected!");
	socket.emit('giveCurrentQueueSize', queue.length); // send them the current number of people waiting

	socket.on('disconnect', function() {
		console.log("User Disconnected.");
		for (var i = 0; i < queue.length; i++) {
			if (socket.id == queue[i].id) {
				//queue.splice(i, 1); // remove user from queue when they disconnect
				socket.broadcast.emit('userLeftQueue'); // notify other users that a user has left
				break;
			}
		}
	});

	socket.on('receieveProperties', function(data) {
		//queue.push(new person(socket.id, data.interests, data.latitude, data.longitude));
		socket.broadcast.emit('userJoinedQueue'); // notify other users that a new user has joined
		if (queue.length == 4) {
			// send them average coordinates between the people, reset the queue
		}
	});  
});

function person(id, interests, latitude, longitude) {
	this.id = id;
	this.interests = interests;
	this.latitude = latitude;
	this.longitude = longitude;
}