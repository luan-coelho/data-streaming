import { Video } from '@/types/Video';
import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';

export default function Streaming() {
  const router = useRouter();
  const [video, setVideo] = useState<Video | null>(null);

  function fetchVideo() {
    const id = router.query.id;
    fetch(`http://localhost:8080/api/video/${id}`)
      .then((response) => response.json())
      .then((data) => {
        setVideo(data);
      })
      .catch((error) => {
        console.log(error);
      });
  }

  function streaming() {
    fetch(
      `http://localhost:8080/api/video/streaming?videopatch=${video?.resolutionPaths}`
    )
      .then((response) => response.json())
      .then((data) => {
        setVideo(data);
      })
      .catch((error) => {
        console.log(error);
      });
  }

  useEffect(() => {
    fetchVideo();
  }, []);

  if (!video) {
    return <div>Carregando...</div>;
  }

  return (
    <div>
      <h1>{video.title}</h1>
    </div>
  );
}
