import { Post } from "./post";

export class User {
    nom!: String;
    prenom!: String;
    username!: String;
    posts!: Post[];
    avatar!: String;
    postCount!: String;
    followerCount!: String;
    followed!: boolean;
}
