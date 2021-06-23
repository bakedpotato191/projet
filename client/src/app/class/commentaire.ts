import { User } from "./user";

export class Commentaire {
    id!: Number;
    text!: String;
    utilisateur!: User;
    date!: Date;
}
