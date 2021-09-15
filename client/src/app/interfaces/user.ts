import { Publication } from "./publication";

export interface User {
    nom: string;
    prenom: string;
    username: string;
    posts: Publication[];
    avatar: string;
    has_avatar: boolean;
    postCount: number;
    followerCount: number;
    followingCount: number;
    followed: boolean;
}
