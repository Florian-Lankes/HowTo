document.addEventListener('DOMContentLoaded', function () {
'use strict';

var chatPage = document.querySelector('#chat-page');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
// TODO maybe add user, group query here

var stompClient = null;
var user = JSON.parse(document.getElementById('userJson').value); 
var group = JSON.parse(document.getElementById('groupJson').value);
//let userJson = document.getElementById('userJson').value; 
//let groupJson = document.getElementById('groupJson').value;

//let user = JSON.parse(userJson); 
//let group = JSON.parse(groupJson);


function connect(event) {
	console.log("connect");
	//console.log("User from hidden input:", user); 
	//console.log("Group from hidden input:", group);
    //username = document.querySelector('#name').value.trim();
	// Take the json string and make it into an json object
	//user = JSON.parse(document.getElementById('user').value); 
	//user = /*[[${user}]]*/ {};
	//group = JSON.parse(document.getElementById('group').value);	
    if(user) {
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }
    if (event) { 
		event.preventDefault(); 
	}
}


function onConnected() {
	console.log("onConnected");
    // Subscribe to the Public Topic 
	// TODO change to private group topic
    stompClient.subscribe('/topic/group/' + group.groupId, onMessageReceived);

    // Tell your username to the server
	var joinMessage = {
			// messageId = -1, TODO maybe
			   content: "", 
			   messageType: 'JOIN', 
			   messageOwnerId: user.userId, 
			   username: user.username, 
			   email: user.email, 
			   birthDate: user.birthDate, 
			   messageGroupId: group.groupId, 
			   groupname: group.name, 
			   description: group.description, 
			   creationDate: group.creationDate
	       };
    stompClient.send("/app/chat.addUser", {}, JSON.stringify(joinMessage));
    connectingElement.classList.add('hidden');
}


function onError(error) {
	console.log("onError");
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
	event.preventDefault();
	console.log("sendMessage");
    var messageContent = messageInput.value.trim();
	console.log("messageContent: " + messageContent)
    if(messageContent && stompClient) {
        var chatMessage = {
			// messageId = -1, TODO maybe
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
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    //event.preventDefault();
}


function onMessageReceived(payload) {
    console.log("onMessageReceived");
    var message = JSON.parse(payload.body);
    console.log("message payload:", message); // Log the message payload to verify
    renderMessage(message);
}


// new
function loadOldMessages() { 
	console.log("Fetching old messages for group ID:", group.groupId);
    fetch('/chat/messages/' + group.groupId) 
        .then(response => {
			console.log("Response status:", response.status);
			return response.json();
		}) 
        .then(messages => { 
			console.log("Fetched messages:", messages);
            for (const message of messages) { 
                renderMessage(message); 
            } 
        }) 
        .catch(error => { 
            console.error('Error fetching old messages:', error); 
        }); 
} 

function renderMessage(message) { 
    var messageElement = document.createElement('li'); 
    if (message.messageType === 'JOIN') { 
        messageElement.classList.add('event-message'); 
        message.content = message.username + ' joined!!'; 
    } else if (message.messageType === 'LEAVE') { 
        messageElement.classList.add('event-message'); 
        message.content = message.username + ' left!'; 
    } else { 
        messageElement.classList.add('chat-message'); 
		
		var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.username[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = '#2196F3'; // DELETED avatar color function

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
// new


/*
window.addEventListener('load', function() { 
	console.log("Event listener triggered");
	loadOldMessages(); 
});*/

connect();
loadOldMessages(); 
messageForm.addEventListener('submit', sendMessage);
});