import React, { useState } from 'react';
import Video from '../../../types/Video';
import api from '../../../utils/api';
import { notifyError, notifySucess } from '../../../utils/toast';
import Modal from '../../modal';

type Props = {
  dataModal: string;
  notifyParent: () => void;
};

export default function VideoCreateForm({ dataModal, notifyParent }: Props) {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');

  const handleSubmit = async () => {
    await api
      .post('/video', {
        title: title,
        description: description,
      } as Video)
      .then(() => {
        setTitle('');
        setDescription('');
        notifySucess('Video created sucessfully');
        notifyParent();
      })
      .catch((error) => {
        notifyError(error.response.data.detail);
      });
  };

  return <>
      <Modal identifier={dataModal} onClickSubmitButton={handleSubmit} title={"Create Vídeo"}>
        <form id="videoForm" onSubmit={handleSubmit}>
          <div className="mb-4">
            <label
              className="block text-gray-700 text-sm font-bold mb-2"
              htmlFor="username">
              Title
            </label>
            <input
              onChange={(e) => setTitle(e.target.value)}
              className="appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              id="title"
              type="text"
              placeholder="Enter the title"
            />
          </div>
          <div className="mb-6">
            <label
              className="block text-gray-700 text-sm font-bold mb-2"
              htmlFor="description">
              Description
            </label>
            <input
              onChange={(e) => setDescription(e.target.value)}
              className="appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              id="description"
              type="text"
              placeholder="Enter the description"
            />
          </div>
        </form>
      </Modal>
  </>;
}
