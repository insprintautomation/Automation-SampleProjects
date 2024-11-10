package helpers;

import com.github.javafaker.Faker;
import net.minidev.json.JSONObject;

import java.security.SecureRandom;

public class DataGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
    private static final int PASSWORD_LENGTH = 8;

    public static String getRandomEmail() {
        Faker faker = new Faker();
        return faker.name().firstName().toLowerCase() + faker.random().nextInt(0, 100) + "@text.com";
    }

    public static String getRandomUserName(){
        Faker faker = new Faker();
        return faker.name().username();
    }

    public static String getRandomText() {
        Faker faker = new Faker();
        return faker.book() + " " + faker.address() + " " + faker.random().nextInt(0, 100);
    }

    private static String getRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        return password.toString();
    }

    public static JSONObject getUserRequestJson() {
        Faker faker = new Faker();
        JSONObject json = new JSONObject();
        json.put("id", faker.number().randomNumber());
        json.put("username", faker.name().username());
        json.put("firstName", faker.name().firstName());
        json.put("lastName", faker.name().lastName());
        json.put("email", faker.internet().emailAddress());
        json.put("password", getRandomPassword());
        json.put("phone", faker.phoneNumber().cellPhone());
        json.put("userStatus", 0);
        return json;
    }

}
