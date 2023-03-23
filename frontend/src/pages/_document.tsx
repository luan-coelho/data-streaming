import {Head, Html, Main, NextScript} from 'next/document'
import AppHeader from "@/components/AppHeader";

export default function Document() {
    return (
        <Html lang="pt-br">
            <Head/>
            <body>
            <AppHeader/>
            <Main/>
            <NextScript/>
            </body>
        </Html>
    )
}
