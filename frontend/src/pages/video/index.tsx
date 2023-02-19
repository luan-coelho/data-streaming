import React, { useEffect, useState } from 'react';
import axios from 'axios';
import App from '../../App';
import Video from '../../types/Video';
import { Link } from 'react-router-dom';

export default function VideoIndex() {
  const [videos, setVideos] = useState([] as Video[]);

  const fetchVideos = async () => {
    const { data } = await axios.get('http://localhost:8080/api/video');
    setVideos(data);
  };

  useEffect(() => {
    fetchVideos();
  }, []);

  return (
    <App>
      <Link className="text-lg" to="create">
        Create Video
      </Link>

      <table className="table-auto">
        <thead>
          <tr>
            <th>Id</th>
            <th>Title</th>
            <th>Description</th>
            <th>Resolutions</th>
          </tr>
        </thead>
        <tbody>
          {videos.map((video, index) => {
            return (
              <tr key={index}>
                <td>{video.id}</td>
                <td>{video.title}</td>
                <td>{video.description}</td>
                <td>
                  {video.resolutionPaths.map((r) => {
                    return (
                      <>
                        <p>{r.path}</p>
                        <p>{r.resolution}</p>
                      </>
                    );
                  })}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </App>
  );
}
