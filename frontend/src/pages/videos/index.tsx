import VideoCard from '@/components/video/card';
import { Video } from '@/types/Video';
import { useRouter } from 'next/router';

interface Props {
  videos: Video[];
}

export default function Videos({ videos }: Props) {
  const router = useRouter();

  return (
    <div className="p-2">
      <span>Vídeos</span>
      <div className="mb-4 flex justify-end">
        <button
          onClick={() => router.push('/videos/create')}
          type="button"
          className="focus:outline-none text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:ring-green-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2 dark:bg-green-600 dark:hover:bg-green-700 dark:focus:ring-green-800">
          Criar
        </button>
      </div>
      <div className="flex gap-2 flex-col">
        {videos.map((video) => (
          <VideoCard video={video} />
        ))}
      </div>
    </div>
  );
}

export async function getStaticProps() {
  const res = await fetch('http://127.0.0.1:8080/api/video');
  const data = await res.json();
  return {
    props: {
      videos: data.content
    }
  };
}
