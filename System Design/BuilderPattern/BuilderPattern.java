public class BuilderPattern {
public static void main(String[] args) {
        User pracUser= new UserBuilder("prachalit13","prachalit13@gmail.com").firstName("Prachalit").age(26).lastName("Prachalit").phone("123-456-7890").build();

        System.out.println(pracUser);
    }
}
