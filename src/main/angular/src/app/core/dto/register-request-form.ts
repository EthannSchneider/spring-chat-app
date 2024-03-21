export class RegisterRequestForm {
    username: string;
    email: string;
    phoneNumber: string;
    firstName: string;
    lastName: string;
    password: string;

    constructor(username: string, email: string, phoneNumber: string, firstName: string, lastName: string, password: string) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
}