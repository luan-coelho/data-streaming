import React from 'react';
import {Video} from "@/types/Video";
import {GetServerSideProps} from "next";

interface Props {
    videos: Video[];
}

export default function Videos({videos}: Props) {
    return (
        <div>
            <h1>Vídeos</h1>
            {videos.map((video) => (
                <div key={video.id}
                     className="block max-w-sm p-6 bg-white border border-gray-200 rounded-lg shadow hover:bg-gray-100 dark:bg-gray-800 dark:border-gray-700 dark:hover:bg-gray-700">
                    <h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white">{video.title}</h5>
                    <p className="font-normal text-gray-700 dark:text-gray-400">{video.description}</p>
                </div>
            ))}
        </div>
    );
}

export const getServerSideProps: GetServerSideProps = async () => {
    {
        const res = await fetch('http://localhost:8080/api/video')
        console.log(res)
        const videos = await res.json() as Video[]

        return {
            props: {
                videos,
            },
        }
    }
}

