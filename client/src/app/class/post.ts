import { Commentaire } from "./commentaire";
import { User } from "./user";

export class Post {
    id!: number;
    url!: string;
    description!: string;
    date!: string;
    utilisateur!: User;
    comments!: Commentaire[];
    isLiked!: boolean;
}
