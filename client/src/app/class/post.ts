import { Commentaire } from "./commentaire";
import { User } from "./user";

export class Post {
    id!: number;
    photo!: string;
    description!: string;
    date!: Date;
    utilisateur!: User;
    comments!: Commentaire[];
    liked!: boolean;
}
