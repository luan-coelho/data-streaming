function create(event) {
  event.preventDefault();
  const title = document.getElementById('title').value;
  const description = document.getElementById('description').value;
  const form = document.getElementById('form');

  const requestOptions = {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ title, description })
  };

  function validate() {
    if (!title) {
      showToast('Informe o titulo', 'error');
      return false;
    }

    if (!description) {
      showToast('Informe a descrição', 'error');
      return false;
    }
    return true;
  }

  if (validate()) {
    fetch('http://localhost:8080/video', requestOptions).then((response) => {
      response
        .json('http://localhost:8080/video')
        .then((data) => {
          console.log(data);
          if (
            data.status == 200 ||
            data.status == 201 ||
            response.status == 200 ||
            response.status == 201
          ) {
            showToast('Vídeo cadastrado com sucesso!', 'success');
            form.reset();
            redirect('video');
          } else {
            showToast(data.detail, 'error');
          }
        })
        .catch((error) => console.error(error));
    });
  }
}

function update(event, id) {
  event.preventDefault();
  const title = document.getElementById('title').value;
  const description = document.getElementById('description').value;
  const form = document.getElementById('form');

  const requestOptions = {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ title, description })
  };

  function validate() {
    if (!title) {
      showToast('Informe o titulo', 'error');
      return false;
    }

    if (!description) {
      showToast('Informe a descrição', 'error');
      return false;
    }
    return true;
  }

  if (validate()) {
    fetch(`http://localhost:8080/video/${id}`, requestOptions).then(
      (response) => {
        response
          .json('http://localhost:8080/video')
          .then((data) => {
            console.log(data);
            if (
              data.status == 200 ||
              data.status == 201 ||
              response.status == 200 ||
              response.status == 201
            ) {
              showToast('Vídeo atualizado com sucesso!', 'success');
            } else {
              showToast(data.detail, 'error');
            }
          })
          .catch((error) => console.error(error));
      }
    );
  }
}

function redirect(endpoint = '', timeout = 3000) {
  setTimeout(() => {
    window.location.href = `http://localhost:8080/${endpoint}`;
  }, timeout);
}
