import React, { useState } from 'react';

type Props = {
  notifyFather: (file: File) => void;
  submit: () => void;
};

export default function VideoUploudButton(props: Props) {
  const [file, setFile] = useState({} as File);

  return (
    <div>
      <div className="flex items-center justify-end gap-2 flex-col">
        <span>This video does not have any files yet</span>
        <input className="max-w-full"
          onChange={(e) => {
            // @ts-ignore
            props.notifyFather(e.target.files[0]);
            // @ts-ignore
            setFile(e.target.files[0]);
          }}
          id="title"
          type="file"
          placeholder="Enter the title"
          accept="video/mp4"
        />
      </div>
      {file?.size > 0 && (
        <div className="flex items-center justify-center mt-2">
            <button
              onClick={props.submit}
              className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-full"
              type="submit">
              Send video file
            </button>
        </div>
      )}
    </div>
  );
}
