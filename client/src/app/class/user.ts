import { Post } from "./post";

export class User {
    nom!: String;
    prenom!: String;
    username!: String;
    posts!: Post[];
    avatar!: String;
    has_avatar!: boolean;
    postCount!: Number;
    followerCount!: Number;
    followingCount!:Number;
    followed!: boolean;
}
