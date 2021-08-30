import { User } from "./user";


export interface Commentaire {
    id: number;
    text: string;
    utilisateur: User;
    date: Date;
}
