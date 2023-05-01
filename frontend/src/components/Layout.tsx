import React from "react";

type LayoutProps = {
    children: React.ReactNode,
};

export default function Layout({children}: LayoutProps) {
    return (
        <div className="flex">
            <main>{children}</main>
        </div>
    )
}
