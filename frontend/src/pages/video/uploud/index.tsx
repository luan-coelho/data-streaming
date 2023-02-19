import React, { useState } from 'react';
import App from '../../../App';
import axios from 'axios';

export default function VideoUploud() {
  const [file, setFile] = useState({} as File);

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const formData = new FormData();
    formData.append('file', file);
    formData.append('fileName', file.name);
    const response = await axios.post(
      'http://localhost:8080/api/video/uploud?videoId=1',
      formData,
      {
        headers: {
          'content-type': 'multipart/form-data',
        },
      }
    );
  };

  return (
    <App>
      <div className="w-full flex items-center justify-center mt-3">
        <form
          onSubmit={handleSubmit}
          className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
          <div className="mb-4">
            <label
              className="block text-gray-700 text-sm font-bold mb-2"
              htmlFor="username">
              Video file
            </label>
            <input
              onChange={(e) => setFile(e.target.files[0])}
              className="appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              id="title"
              type="file"
              placeholder="Enter the title"
            />
          </div>
          <div className="flex items-center justify-end">
            <button
              className="bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
              type="submit">
              Send
            </button>
          </div>
        </form>
      </div>
    </App>
  );
}
