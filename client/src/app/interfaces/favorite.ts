import { Post } from "./post";
import { User } from "./user";

export interface Favorite {
    user: User;
    post: Post;
}
