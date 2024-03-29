document.addEventListener('DOMContentLoaded', () => {
  const playerElement = document.getElementById('player');

  let videoSources = [];
  let videoResources = document.getElementById('video-resources');
  let videoId;

  if (videoResources && videoResources.hasAttribute('videoId')) {
    videoId = videoResources.getAttribute('videoId');
  }

  if (videoResources) {
    let resources = videoResources.getElementsByTagName('div');

    for (let i = 0; i < resources.length; i++) {
      let rwidthValue = resources[i].getAttribute('rwidth');
      let pathValue = resources[i].getAttribute('path');

      if (rwidthValue && pathValue) {
        videoSources.push({
          size: rwidthValue,
          type: 'video/mp4',
          src: `http://localhost:8080/video/streaming?videopath=${pathValue}`
        });
      }
    }
  }

  const configs = {
    controls: [
      'play-large',
      'play',
      'progress',
      'current-time',
      'mute',
      'volume',
      'settings',
      'fullscreen'
    ],
    settings: ['captions', 'quality', 'speed', 'loop'],
    i18n: {
      qualityLabel: 'Qualidade'
    }
  };

  const player = new Plyr(playerElement, configs);

  player.source = {
    type: 'video',
    title: 'Example title',
    sources: videoSources
  };

  fetch(`http://localhost:8080/video/getWatchTime/${videoId}`)
    .then((response) => response.json())
    .then((data) => {
      player.on('loadedmetadata', () => {
        player.currentTime = data;
      });
    })
    .catch(() => {
      player.on('loadedmetadata', () => {
        player.currentTime = 0;
      });
    });

  let isPlayEventFired = false;
  let videoStarted = false;

  player.on('play', () => {
    if (!isPlayEventFired) {
      isPlayEventFired = true;
      videoStarted = true; // O vídeo começou a tocar

      fetch(`http://localhost:8080/video/incrementView?videoId=${videoId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        }
      });
    }
  });

  let lastSavedTime = 0;
  let saveTimeout;
  const saveInterval = 10000; // Intervalo de 10 segundos

  player.on('timeupdate', () => {
    if (
      videoStarted &&
      Math.floor(player.currentTime) - lastSavedTime >= saveInterval
    ) {
      // Cancela o timeout anterior, se existir
      if (saveTimeout) clearTimeout(saveTimeout);

      // Inicia um novo timeout para fazer a requisição
      saveTimeout = setTimeout(() => {
        lastSavedTime = Math.floor(player.currentTime);

        fetch('http://localhost:8080/video/updateWatchTime', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            videoId: videoId,
            watchTime: player.currentTime
          })
        });
      }, saveInterval);
    }
  });

  // Detecta quando o usuário clica na barra de progresso do vídeo
  player.on('seeked', () => {
    // Cancela o timeout anterior
    if (saveTimeout) clearTimeout(saveTimeout);

    // Imediatamente atualiza o tempo de exibição
    lastSavedTime = Math.floor(player.currentTime);

    fetch('http://localhost:8080/video/updateWatchTime', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        videoId: videoId,
        watchTime: player.currentTime
      })
    });
  });
});
