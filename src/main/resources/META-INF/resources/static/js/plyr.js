document.addEventListener('DOMContentLoaded', function () {
  const playerElement = document.getElementById('player');

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
    sources: [
      {
        src: 'http://localhost:8080/static/video/video.mp4',
        type: 'video/mp4',
        size: 720
      },
      {
        src: 'http://localhost:8080/static/video/video.mp4',
        type: 'video/mp4',
        size: 1080
      }
    ]
  };
});
