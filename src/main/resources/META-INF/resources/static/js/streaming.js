const videoPlayer = document.getElementById('video-player');
const qualityBtnContainer = document.getElementById('quality-btn-container');

const sources = Array.from(videoPlayer.getElementsByTagName('source')).map((source, index) => {
    const quality = source.getAttribute('data-quality');
    const button = document.createElement('button');
    button.textContent = quality;
    button.id = `quality-${quality}`;
    button.classList.add('btn-quality');
    button.onclick = () => changeQuality(source, button);
    qualityBtnContainer.appendChild(button);
    return {source, button, quality: parseInt(quality)};
});

function changeQuality(srcElement, buttonElement) {
    const currentTime = videoPlayer.currentTime;
    const isPaused = videoPlayer.paused;

    videoPlayer.src = srcElement.src;
    videoPlayer.load();
    videoPlayer.currentTime = currentTime;

    if (!isPaused) {
        videoPlayer.play();
    }

    sources.forEach(({button}) => {
        button.disabled = button === buttonElement;
    });
}

function updateQualityByNetworkSpeed(downlinkSpeed) {
    const suitableSource = sources.reduce((prev, current) => {
        if (downlinkSpeed * 1000 >= current.quality && current.quality > prev.quality) {
            return current;
        }
        return prev;
    }, {quality: 0});

    changeQuality(suitableSource.source, suitableSource.button);
}

if (navigator.connection) {
    updateQualityByNetworkSpeed(navigator.connection.downlink);

    navigator.connection.addEventListener('change', function () {
        updateQualityByNetworkSpeed(navigator.connection.downlink);
    });
}
