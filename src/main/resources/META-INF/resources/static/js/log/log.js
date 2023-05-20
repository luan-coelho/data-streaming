var socket = new WebSocket('ws://localhost:8080/notifications');

socket.onmessage = function (event) {
  location.reload();
};
