import React, { useState } from 'react';
import App from '../../../App';
import Video from '../../../types/Video';
import axios from 'axios';
import { Link } from 'react-router-dom';

export default function VideoCreate() {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const response = await axios.post("http://localhost:8080/api/video", {
      title: title,
      description: description
    } as Video);
    if(response.status == 200){
      (document.querySelector("#videoForm") as HTMLFormElement).reset();
    }
  }

  return (
    <App>
      <Link to="/video/uploud">Uploud Video</Link>
      <div className="w-full flex items-center justify-center mt-3">
        <form id="videoForm" onSubmit={handleSubmit} className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
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
              htmlFor="password">
              Description
            </label>
            <input
              onChange={(e) => setDescription(e.target.value)}
              className="appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
              id="description"
              type="description"
              placeholder="Enter the description"
            />
          </div>
          <div className="flex items-center justify-end">
            <button
              className="bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
              type="submit">
              Create
            </button>
          </div>
        </form>
      </div>
    </App>
  );
}
