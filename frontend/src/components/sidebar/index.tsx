import Image from "next/image";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faFilm} from "@fortawesome/free-solid-svg-icons";
import Link from "next/link";

export default function Sidebar() {
    return (
        <aside id="logo-sidebar"
               className="absolute top-0 left-0 z-40 w-64 h-full transition-transform -translate-x-full sm:translate-x-0"
               aria-label="Sidebar">
            <div className="h-full px-3 py-4 overflow-y-auto bg-gray-50 dark:bg-gray-800">
                <div className="flex items-center pl-2.5 mb-5">
                    <Image className="mr-3" src="/logo-unitins.png" alt="Logo da Unitins" width={40} height={20}/>
                    <span
                        className="self-center text-xl font-semibold whitespace-nowrap dark:text-white">Streaming</span>
                </div>
                <ul className="space-y-2 font-medium">
                    <li>
                        <Link href="/Videos"
                              className="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700">
                            <FontAwesomeIcon icon={faFilm}
                                             className="w-6 h-6 text-gray-500 transition duration-75 dark:text-gray-400 group-hover:text-gray-900 dark:group-hover:text-white"/>
                            <span className="ml-3">Vídeos</span>
                        </Link>
                    </li>
                </ul>
            </div>
        </aside>
    )
}
