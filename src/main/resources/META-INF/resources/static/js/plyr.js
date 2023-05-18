document.addEventListener("DOMContentLoaded", function () {
  const playerElement = document.getElementById("player");

  let videoSources = [];
  let videoResources = document.getElementById("video-resources");
  const videoId = videoResources.getAttribute("videoId");

  if (videoResources) {
    let resources = videoResources.getElementsByTagName("div");

    for (let i = 0; i < resources.length; i++) {
      let rwidthValue = resources[i].getAttribute("rwidth");
      let pathValue = resources[i].getAttribute("path");

      if (rwidthValue && pathValue) {
        videoSources.push({
          size: rwidthValue,
          type: "video/mp4",
          src: `http://localhost:8080/video/streaming?videopath=${pathValue}`,
        });
      }
    }
  }

  const configs = {
    controls: [
      "play-large",
      "play",
      "progress",
      "current-time",
      "mute",
      "volume",
      "settings",
      "fullscreen",
    ],
    settings: ["captions", "quality", "speed", "loop"],
    i18n: {
      qualityLabel: "Qualidade",
    },
  };

  const player = new Plyr(playerElement, configs);

  player.source = {
    type: "video",
    title: "Example title",
    sources: videoSources,
  };

  fetch(`http://localhost:8080/video/getWatchTime/${videoId}`)
    .then((response) => response.json())
    .then((data) => {
      player.currentTime = data;
    });

  let isPlayEventFired = false;

  player.on("play", function () {
    if (!isPlayEventFired) {
      isPlayEventFired = true;
      fetch(`http://localhost:8080/video/incrementView?videoId=${videoId}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });
    }
  });

  let lastSavedTime = 0;
  const saveInterval = 10;

  player.on("timeupdate", function () {
    if (Math.floor(player.currentTime) - lastSavedTime >= saveInterval) {
      lastSavedTime = Math.floor(player.currentTime);

      fetch("http://localhost:8080/video/updateWatchTime", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          videoId: videoId,
          watchTime: player.currentTime,
        }),
      });
    }
  });
});
