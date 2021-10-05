import { Commentaire } from "./commentaire";
import { User } from "./user";


export interface Publication {
    id: number;
    photo: string;
    description: string;
    date: Date;
    utilisateur: User;
    comments: Commentaire[];
    liked: boolean;
    countLike: number;
    commentsCount: number;
    author: boolean;
}
