import { Food } from "./food.model";
import { User } from "./user.model";

export interface Feedback {
    feedbackId?: number;
    userId?:number;
    feedbackText?: string;
    date?: Date;
    food?:Food;
    user?:User;
    rating?:number;
   }
