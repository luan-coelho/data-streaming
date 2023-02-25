import React, { useState } from 'react';
import Video from '../../../types/Video';
import VideoUploudButton from '../uploudbutton';
import api from '../../../utils/api';
import { notifyError, notifySucess } from '../../../utils/toast';
import { Link } from 'react-router-dom';
import Modal from '../../modal';

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

  async function handleDeleteVideo(id: number) {
    await api
      .delete(`/video/${id}`)
      .then(() => {})
      .catch((error) => {
        notifyError(error.data?.detail);
      });
  }

  return (
    <div className="w-full">
      <div className="shadow-2xl bg-white rounded-2xl p-1 flex flex-col justify-between">
        <div className="mb-8">
          <div className="p-2 text-xl text-gray-900 font-bold text-xl">This
            {video.title}
          </div>
          <p className="p-2 ext-sm text-gray-700 text-base">
            {video.description}
          </p>
          {video.resolutionPaths.length == 0 ? (
            <>
              <div>
                <VideoUploudButton
                  notifyFather={handleChangeVideoFile}
                  submit={handleSubmit}
                />
              </div>
              <div className="flex items-center justify-end">
                <button
                  data-modal-target="confirmDelete"
                  data-modal-toggle="confirmDelete"
                  className="bg-red-600 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
                  type="button">
                  Delete
                </button>
                <Modal
                  identifier={'confirmDelete'}
                  title={'Are you sure you want to do this?'}
                  onClickSubmitButton={() => handleDeleteVideo(video.id)}
                  onClickSubmitLabel={'Confirm'}
                  onClickCloseLabel={'Cancel'}>
                  This video will be deleted along with its files, if any.
                </Modal>
              </div>
            </>
          ) : (
            <div>
              <Link
                to={video.id.toString()}
                className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-full"
                type="button">
                Reproduce
              </Link>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
