import { User } from "./user";

export class Commentaire {
    id!: number;
    text!: string;
    utilisateur!: User;
    date!: Date;
}
