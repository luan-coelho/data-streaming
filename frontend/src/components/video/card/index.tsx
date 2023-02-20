import React, { useState } from 'react';
import Video from '../../../types/Video';
import VideoUploudButton from '../uploudbutton';
import api from '../../../utils/api';
import { notifyError, notifySucess } from '../../../utils/toast';

type Prop = {
  video: Video;
};

export default function VideoCard({ video }: Prop) {
  const [file, setFile] = useState(({} as File) || undefined);

  const handleSubmit = async () => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('fileName', file.name);
    const response = await api.post(
      `/video/uploud?videoId=${video.id}`,
      formData,
      {
        headers: {
          'content-type': 'multipart/form-data',
        },
      }
    );

    if (response.status == 200) {
       notifySucess('Upload done successfully!');
    } else {
      notifyError(response.data?.detail);
    }
  };

  function handleChangeVideoFile(file: File) {
    setFile(file);
  }

  return (
    <div className="w-full">
      <div className="shadow-2xl bg-white rounded-2xl p-1 flex flex-col justify-between">
        <div className="mb-8">
          <div className="p-2 text-xl text-gray-900 font-bold text-xl">
            {video.title}
          </div>
          <p className="p-2 ext-sm text-gray-700 text-base">
            {video.description}
          </p>
          {video.resolutionPaths.length == 0 && (
            <div>
              <VideoUploudButton
                notifyFather={handleChangeVideoFile}
                submit={handleSubmit}
              />
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
