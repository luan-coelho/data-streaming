import Link from 'next/link';

export default function Sidebar() {
  return (
    <header className="bg-black text-white p-4 flex items-center justify-around font-bold">
      <span className="text-lg">Data Streaming</span>
      <ul>
        <li>
          <Link href="/videos">Vídeos</Link>
        </li>
      </ul>
    </header>
  );
}
