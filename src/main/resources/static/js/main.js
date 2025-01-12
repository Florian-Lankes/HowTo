'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
// TODO maybe add user, group query here

var stompClient = null;
var username = null;
//let userJson = document.getElementById('userJson').value; 
//let groupJson = document.getElementById('groupJson').value;

//let user = JSON.parse(userJson); 
//let group = JSON.parse(groupJson);

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];



function connect(event) {
	console.log("connect");
	
    //username = document.querySelector('#name').value.trim();
	// Take the json string and make it into an json object
	//user = JSON.parse(document.getElementById('user').value); 
	//user = /*[[${user}]]*/ {};
	//group = JSON.parse(document.getElementById('group').value);	
	console.log("User from hidden input:", user); 
	console.log("Group from hidden input:", group);
	
    if(user) {
		// Change view
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
		console.log("2");
    }
    event.preventDefault();
	console.log("3");
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
			   messageOwner: user,
			   messageGroup: group
	       };
    stompClient.send("/app/chat.addUser", {}, JSON.stringify(joinMessage));
	
    connectingElement.classList.add('hidden');
	console.log("4");
}


function onError(error) {
	console.log("onError");
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
	console.log("5 Error");
}


function sendMessage(event) {
	console.log("sendMessage");
    var messageContent = messageInput.value.trim();
	console.log("messageContent: " + messageContent)
    if(messageContent && stompClient) {
        var chatMessage = {
			// messageId = -1, TODO maybe
			   content: messageContent,
			   messageType: 'CHAT',
			   messageOwner: user,
			   messageGroup: group
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
	console.log("onMessageReceived");
    var message = JSON.parse(payload.body);
	console.log("message payload: " + payload);
    var messageElement = document.createElement('li');

    if(message.messageType === 'JOIN') {
        messageElement.classList.add('event-message');
        //message.content = message.messageOwner.username + ' joined!'; // TODO need to change responsive
		message.content = 'User joined!';
    } else if (message.messageType === 'LEAVE') {
        messageElement.classList.add('event-message');
        //message.content = message.messageOwner.username + ' left!'; //TODO need to change responsive
		message.content = 'User left!';
    } else {
        messageElement.classList.add('chat-message');

        //var avatarElement = document.createElement('i');
        //var avatarText = document.createTextNode(message.sender[0]);
        //avatarElement.appendChild(avatarText);
        //avatarElement.style['background-color'] = '#2196F3'; // DELETED avatar color function

        //messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode('TEXTNODE'); // TODO message.sender
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

// new




usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)