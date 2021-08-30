import { Post } from "./post";

export class User {
    nom!: string;
    prenom!: string;
    username!: string;
    posts!: Post[];
    avatar!: string;
    has_avatar!: boolean;
    postCount!: number;
    followerCount!: number;
    followingCount!:number;
    followed!: boolean;
}
