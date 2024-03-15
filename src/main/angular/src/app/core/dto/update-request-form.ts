export class UpdateRequestForm {
    name: string;
    lastName: string;

    constructor(name: string, lastName: string) {
        this.name = name;
        this.lastName = lastName;
    }
}