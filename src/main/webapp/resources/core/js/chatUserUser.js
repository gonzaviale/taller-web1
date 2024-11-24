import { Client } from '@stomp/stompjs';

const client = new Client({
    brokerURL: 'ws://localhost:8080/spring/wschat',
    onConnect: () => {
        client.subscribe('/topic/chatUser', message => {
            try {
                // Intentar leer directamente el mensaje como texto
                const mensajeUsuarioUsuario = JSON.parse(message.body);
                console.log(mensajeUsuarioUsuario);
                showMessage(mensajeUsuarioUsuario);
            } catch (error) {
                console.error("Error al procesar el mensaje:", error);
            }
        });

        document.getElementById('sendButton').disabled = false;
    },
});

client.activate();

function sendMessage() {
    const message = document.getElementById('mensaje').value;
    const usuarioEmisor = document.getElementById('usuarioEmisor').value;
    const usuarioReceptor = document.getElementById('usuarioReceptor').value;
    let chatMessage;

    chatMessage = {
        usuarioEmisor: usuarioEmisor,
        usuarioReceptor: usuarioReceptor,
        mensaje: message
    }

    console.log(chatMessage)


    if (client.connected) {
        client.publish({ destination: '/app/chatUserMessage', body: JSON.stringify(chatMessage) });
        console.log("client conectado se deberia haber hecho el publish")
        document.getElementById('mensaje').value = '';
    } else {
        console.error("No se puede enviar el mensaje. WebSocket no está conectado.");
    }
}

function showMessage(mensajeUsuarioUsuario) {
    console.log(mensajeUsuarioUsuario)
    const messagesDiv = document.getElementById('messages');

    const messageHTML = `
        <div class="d-flex justify-content-end mb-2">
            <div class="p-2 bg-primary text-white rounded">
                <span>${mensajeUsuarioUsuario.mensaje}</span>
            </div>
        </div>
    `;
    messagesDiv.insertAdjacentHTML('beforeend', messageHTML);

    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}

document.getElementById('sendButton').addEventListener('click', event => {
    event.preventDefault();
    console.log("hola")
    sendMessage();
});
