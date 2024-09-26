import { Client } from '@stomp/stompjs';

const client = new Client({
    brokerURL: 'ws://localhost:8080/spring/wschat', // Cambia a tu broker URL correcto
    onConnect: () => {
        client.subscribe('/topic/messages', message =>
            showMessage(message.body)
        );
        document.getElementById('sendButton').disabled = false; // Habilitar el botón al conectar
    },
});

client.activate();

function sendMessage() {
    const message = document.getElementById('messageInput').value;
    if (client.connected) {
        client.publish({ destination: '/app/sendMessage', body: message });
        document.getElementById('messageInput').value = '';
    } else {
        console.error("No se puede enviar el mensaje. WebSocket no está conectado.");
    }
}

function showMessage(message) {
    const messagesDiv = document.getElementById('messages');
    messagesDiv.innerHTML += `<p>${message}</p>`;
}

// Asignar el evento de click al botón
document.getElementById('sendButton').addEventListener('click', sendMessage);
