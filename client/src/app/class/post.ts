import { Commentaire } from "./commentaire";
import { User } from "./user";

export class Post {
    id!: number;
    url!: string;
    description!: string;
    date!: Date;
    utilisateur!: User;
    comments!: Commentaire[];
    liked!: boolean;
}
