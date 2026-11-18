public class UserBuilder {

 String userName;
    String email;
    int age;
    String firstName;
    String lastName;
    String phone;

    public UserBuilder(String userName,String email) {
        this.userName=userName;
        this.email=email;
    }

    UserBuilder firstName(String firstName){
        this.firstName=firstName;
        return this;
    }
     UserBuilder lastName(String lastName){
        this.lastName=lastName;
        return this;
    }
     UserBuilder phone(String phone){
        this.phone=phone;
        return this;
    }
     UserBuilder age(int age){
        this.age=age;
        return this;
    }

    User build(){
        return new User(this);
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }
    
}
