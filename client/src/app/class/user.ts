import { Post } from "./post";

export class User {
    nom!: String;
    prenom!: String;
    username!: String;
    posts!: Post[];
    avatar!: String | null;
    postCount!: Number;
    followerCount!: Number;
    followingCount!:Number;
    followed!: boolean;
}
