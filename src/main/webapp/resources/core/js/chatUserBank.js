import { Client } from '@stomp/stompjs';

const client = new Client({
    brokerURL: 'ws://localhost:8080/spring/wschat', // Cambia a tu broker URL correcto
    onConnect: () => {
        client.subscribe('/topic/chat', message => {
            const mensajeUsuarioBanco = JSON.parse(message.body); // Parsear el mensaje recibido
            showMessage(mensajeUsuarioBanco);
        });
        document.getElementById('sendButton').disabled = false;
    },
});

client.activate();

function sendMessage() {
    const message = document.getElementById('mensaje').value;
    const userId = document.getElementById('userId').value;
    const bankId = document.getElementById('bankId').value;
    console.log(message, userId, bankId, "los elementos")

    const chatMessage = {
        usuarioId: userId,
        bancoId: bankId,
        mensaje: message
    };

    if (client.connected) {
        client.publish({ destination: '/app/chatMessage', body: JSON.stringify(chatMessage) });
        console.log("Chat message enviado:", JSON.stringify(chatMessage));
        document.getElementById('mensaje').value = '';
    } else {
        console.error("No se puede enviar el mensaje. WebSocket no está conectado.");
    }
}

function showMessage(mensajeUsuarioBanco) {
    const messagesDiv = document.getElementById('messages');
    const isUser = mensajeUsuarioBanco.emisor === 'Usuario';

    const messageHTML = `
        <div class="d-flex justify-content-${isUser ? 'end' : 'start'} mb-2">
            <div class="p-2 ${isUser ? 'bg-primary' : 'bg-secondary'} text-white rounded">
                <span>${mensajeUsuarioBanco.mensaje}</span>
            </div>
        </div>
    `;
    messagesDiv.insertAdjacentHTML('beforeend', messageHTML);

    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

document.getElementById('sendButton').addEventListener('click', event => {
    event.preventDefault();
    console.log("holaaaaa")
    sendMessage();
});
