document.addEventListener('DOMContentLoaded', function () {
'use strict';

var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var groupName = document.querySelector('#groupName');

var stompClient = null;
var user = JSON.parse(document.getElementById('userJson').value); 
var group = JSON.parse(document.getElementById('groupJson').value);

function connect(event) {
    if(user) {
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }
    if (event) { event.preventDefault(); }
}

function onConnected() {
    stompClient.subscribe('/topic/group/' + group.groupId, onMessageReceived);
	var joinMessage = {
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
	if (group && group.name) { 
		groupName.textContent = group.name; 
	}
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
	event.preventDefault();
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
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
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    renderMessage(message);
}

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

function renderMessage(message) { 
    var messageElement = document.createElement('li'); 
    if (message.messageType === 'JOIN') { 
        messageElement.classList.add('event-message'); 
        message.content = message.username + ' is now active in chat!'; 
    } else if (message.messageType === 'LEAVE') { 
        messageElement.classList.add('event-message'); 
        message.content = message.username + ' left!'; 
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

connect();
loadOldMessages(); 
messageForm.addEventListener('submit', sendMessage);
});