var socket = new WebSocket('ws://localhost:8080/notifications');

socket.onmessage = function (event) {
  showToast(event.data, 'success');
  setTimeout(() => {
    location.reload();
  }, 3000);
};

function hiddenUploudButton() {
  const uploudButton = document.getElementById('uploud-button');
  const uploudInput = document.getElementById('uploud-input');
  const uploudTrashInput = document.getElementById('uploud-trash-button');
  if (uploudInput.files[0]) {
    uploudButton.classList.remove('hidden');
    uploudTrashInput.classList.remove('hidden');
  } else {
    uploudButton.classList.add('hidden');
    uploudTrashInput.classList.add('hidden');
  }
}

function removeFile() {
  const uploudForm = document.getElementById('uploud-form');
  uploudForm.reset();
  hiddenUploudButton();
}

function hiddenInput() {
  const uploudContainer = document.getElementById('uploud-container');
  uploudContainer.style.visibility = 'hidden';
}

function submitUploudFile(id) {
  const uploudInput = document.getElementById('uploud-input');
  const file = uploudInput.files[0];
  const data = new FormData();
  data.append('file', file);
  data.append('fileName', file.name);

  const requestOptions = {
    method: 'POST',
    body: data
  };

  fetch(`http://localhost:8080/video/uploud/?videoId=${id}`, requestOptions)
    .then((response) => {
      if (response.status === 200 || response.status === 201) {
        showToast('Uploud realizado com sucesso!', 'success');
        removeFile();
        hiddenInput();
        setTimeout(() => {
          location.reload();
        }, 3000);
      } else {
        showToast(data.detail, 'error');
      }
    })
    .catch((error) => console.error(error));
}

document.addEventListener('DOMContentLoaded', () => {
  const description = document.getElementById('description');
  if (description) {
    description.innerHTML = description.innerText;
  }
});
