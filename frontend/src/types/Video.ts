export interface Video {
    id: number;
    title: string;
    description: string;
    path: string;
    duration: 0;
    views: 0;
    resolutionPaths: ResourcePath[];
}

export interface ResourcePath {
    id: number;
    resolution: Resolution;
    path: string;
}

export enum Resolution {
    HD,
    SD,
}

