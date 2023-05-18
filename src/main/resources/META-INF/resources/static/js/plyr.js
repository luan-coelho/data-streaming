document.addEventListener('DOMContentLoaded', function () {
  const playerElement = document.getElementById('player');

  let videoSources = [];
  let videoResources = document.getElementById('video-resources');
  if(videoResources){
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

  console.log(videoSources);
  
  player.source = {
    type: 'video',
    title: 'Example title',
    sources: videoSources
  };
});
