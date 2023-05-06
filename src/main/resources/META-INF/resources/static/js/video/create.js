function create() {
  const title = document.getElementById('title').value;
  const description = document.getElementById('description').value;

  const requestOptions = {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ title, description })
  };

  fetch('http://localhost:8080/video', requestOptions)
    .then((response) => response.json('http://localhost:8080/video'))
    .then(() => {
      window.location.replace('');
    })
    .catch((error) => console.error(error));
}
