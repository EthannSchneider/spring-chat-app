import { Injectable } from "@angular/core";

@Injectable({
providedIn: 'root'
})
export class ConversationService {
    getConversation(): string[] {
        return JSON.parse(localStorage.getItem('conversation') ?? '[]');
    }

    addConversation(conversation: string) {
        let conversations = this.getConversation();
        conversations.push(conversation);
        localStorage.setItem('conversation', JSON.stringify(conversations));
    }

    removeConversation(conversation: string) {
        let conversations = this.getConversation();
        conversations = conversations.filter(c => c !== conversation);
        localStorage.setItem('conversation', JSON.stringify(conversations));
    }
}
