public class User {
String userName;
    String email;
    int age;
    String firstName;
    String lastName;
    String phone;

     User(UserBuilder b){
         this.userName=b.userName;
         this.age=b.age;
         this.firstName=b.firstName;
         this.lastName=b.lastName;
         this.phone=b.phone;
         this.email=b.email;
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
