document.addEventListener('DOMContentLoaded', function () {
  console.log('Change');
  const videoPlayer = document.getElementById('video-player');
  const quality720Btn = document.getElementById('quality-720');
  const quality1280Btn = document.getElementById('quality-1280');
  const src720 = document.getElementById('720x480');
  const src1280 = document.getElementById('1280x720');
  console.log('Change');

  function changeQuality(srcElement) {
    const currentTime = videoPlayer.currentTime;
    const isPaused = videoPlayer.paused;

    videoPlayer.src = srcElement.src;
    videoPlayer.load();
    videoPlayer.currentTime = currentTime;

    if (!isPaused) {
      videoPlayer.play();
    }
  }

  console.log('Change');

  quality720Btn.addEventListener('click', function () {
    changeQuality(src720);
  });

  quality1280Btn.addEventListener('click', function () {
    changeQuality(src1280);
  });
});
