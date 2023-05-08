function convertSecondsToMinutes(seconds) {
  let minutes = Math.floor(seconds / 60);
  let remainingSeconds = seconds % 60;

  // Adicionar um zero à esquerda se os minutos ou segundos forem menores que 10
  minutes = minutes < 10 ? '0' + minutes : minutes;
  remainingSeconds =
    remainingSeconds < 10 ? '0' + remainingSeconds : remainingSeconds;

  return `${minutes}:${remainingSeconds}`;
}

function formatDurations() {
  // Selecionar todos os elementos span com a classe 'duration'
  const durationElements = document.querySelectorAll('.duration');
  console.log(durationElements);
  // Iterar sobre os elementos e atualizar seus valores formatados
  durationElements.forEach((element) => {
    const seconds = parseInt(element.textContent);
    element.textContent = convertSecondsToMinutes(seconds);
  });
}

// Executar a função formatDurations após o carregamento completo do DOM
document.addEventListener('DOMContentLoaded', formatDurations);
