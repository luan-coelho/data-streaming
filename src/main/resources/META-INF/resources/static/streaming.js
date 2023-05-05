document.addEventListener('DOMContentLoaded', function () {
  const videoPlayer = document.getElementById('video-player');
  const quality720Btn = document.getElementById('quality-720');
  const quality1280Btn = document.getElementById('quality-1280');
  const src720 = document.getElementById('720x480');
  const src1280 = document.getElementById('1280x720');

  function changeQuality(srcElement, buttonElement) {
    const currentTime = videoPlayer.currentTime;
    const isPaused = videoPlayer.paused;

    videoPlayer.src = srcElement.src;
    videoPlayer.load();
    videoPlayer.currentTime = currentTime;

    if (!isPaused) {
      videoPlayer.play();
    }

    quality720Btn.hidden = buttonElement === quality720Btn;
    quality1280Btn.hidden = buttonElement === quality1280Btn;
  }

  quality720Btn.addEventListener('click', function () {
    changeQuality(src720, quality720Btn);
  });

  quality1280Btn.addEventListener('click', function () {
    changeQuality(src1280, quality1280Btn);
  });

  if (navigator.connection) {
    navigator.connection.addEventListener('change', function () {
      updateQualityByNetworkSpeed(navigator.connection.downlink);
    });
  }
});

function updateQualityByNetworkSpeed(downlinkSpeed) {
  if (downlinkSpeed >= 5) {
    changeQuality(src1280, quality1280Btn);
  } else {
    changeQuality(src720, quality720Btn);
  }
}
