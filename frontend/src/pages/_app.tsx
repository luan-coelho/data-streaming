import '@/styles/globals.css'
import type {AppProps} from 'next/app'
import {config} from "@fortawesome/fontawesome-svg-core";
import Layout from "@/components/Layout";

config.autoAddCss = false;

export default function App({Component, pageProps}: AppProps) {
    return (<Layout>
        <Component {...pageProps} />
    </Layout>)
}
