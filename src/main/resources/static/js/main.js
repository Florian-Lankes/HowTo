// Waits until the DOM content is fully loaded
document.addEventListener('DOMContentLoaded', function () {
	'use strict';
	// Defines the needed elements from the DOM
	var messageForm = document.querySelector('#messageForm');
	var messageInput = document.querySelector('#message');
	var messageArea = document.querySelector('#messageArea');
	var connectingElement = document.querySelector('.connecting');
	var groupName = document.querySelector('#groupName');
	
	// Declares the WebSocket connection and user and group data
	var stompClient = null;
	var user = JSON.parse(document.getElementById('userJson').value); 
	var group = JSON.parse(document.getElementById('groupJson').value);
	
	// Function to establish the connection
	function connect(event) {
	    if(user) {
	        var socket = new SockJS('/ws');
	        stompClient = Stomp.over(socket);
	        stompClient.connect({}, onConnected, onError);
	    }
	    if (event) { event.preventDefault(); }
	}
	
	// Function called when the connection is successfully established
	function onConnected() {
	    stompClient.subscribe('/topic/group/' + group.groupId, onMessageReceived);
		// Creates a join message (currently not used)
		var joinMessage = {
				   content: "", 
				   messageType: 'INCHAT', 
				   messageOwnerId: user.userId, 
				   username: user.username, 
				   email: user.email, 
				   birthDate: user.birthDate, 
				   messageGroupId: group.groupId, 
				   groupname: group.name, 
				   description: group.description, 
				   creationDate: group.creationDate
		       };
			   // Send join message to /app/chat.UserOnline
	    stompClient.send("/app/chat.UserOnline", {}, JSON.stringify(joinMessage));
	    connectingElement.classList.add('hidden');
		if (group && group.name) { 
			groupName.textContent = group.name; 
		}
	}
	
	// Function called in case of a connection error
	function onError(error) {
	    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
	    connectingElement.style.color = 'red';
	}
	
	// Function to send a message
	function sendMessage(event) {
		event.preventDefault();
	    var messageContent = messageInput.value.trim();
	    if(messageContent && stompClient) {
			// Creates a chat message
	        var chatMessage = {
				   content: messageContent, 
				   messageType: 'CHAT', 
				   messageOwnerId: user.userId, 
				   username: user.username, 
				   email: user.email, 
				   birthDate: user.birthDate, 
				   messageGroupId: group.groupId, 
				   groupname: group.name, 
				   description: group.description, 
				   creationDate: group.creationDate
	        };
			// Sende chat message to /app/chat.sendMessage
	        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
	        messageInput.value = '';
	    }
	}
	
	// Function called when a message is received
	function onMessageReceived(payload) {
	    var message = JSON.parse(payload.body);
	    renderMessage(message);
	}
	
	// Function to load old messages
	function loadOldMessages() { 
	    fetch('/chat/messages/' + group.groupId) 
	        .then(response => {
				return response.json();
			}) 
	        .then(messages => { 
	            for (const message of messages) { 
	                renderMessage(message); 
	            } 
	        }) 
	        .catch(error => { 
	        }); 
	} 
	
	// Function to render a message
	function renderMessage(message) { 
	    var messageElement = document.createElement('li'); 
	    if (message.messageType === 'JOIN') { 
	        messageElement.classList.add('event-message'); 
	        message.content = message.username + ' joined the Group!'; 
	    } else if (message.messageType === 'LEAVE') { 
	        messageElement.classList.add('event-message'); 
	        message.content = message.username + ' left the Group!'; 
	    } else if (message.messageType === 'INCHAT') { 
			//SHOW THAT USER ONLINE
			console.log("Display user online")
			return
		} else if (message.messageType === 'CREATE') { 
			messageElement.classList.add('event-message'); 
			message.content = message.username + ' created the Group!'; 
		} else { 
	        messageElement.classList.add('chat-message'); 
			
			var avatarElement = document.createElement('i');
	        var avatarText = document.createTextNode(message.username[0]);
	        avatarElement.appendChild(avatarText);
	        avatarElement.style['background-color'] = '#2196F3';
	
	        messageElement.appendChild(avatarElement);
			
	        var usernameElement = document.createElement('span'); 
	        var usernameText = document.createTextNode(message.username); 
	        usernameElement.appendChild(usernameText); 
	        messageElement.appendChild(usernameElement); 
	    }
	    var textElement = document.createElement('p'); 
	    var messageText = document.createTextNode(message.content); 
	    textElement.appendChild(messageText); 
	    messageElement.appendChild(textElement); 
	    messageArea.appendChild(messageElement); 
	    messageArea.scrollTop = messageArea.scrollHeight;
	}
	
	// Connects when the page loads
	connect();
	// Loads old messages when the page loads
	loadOldMessages(); 
	// Adds the event listener to send messages
	messageForm.addEventListener('submit', sendMessage);
});