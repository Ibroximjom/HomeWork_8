package uz.pdp;
import uz.pdp.db.UserServis;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scannerInt = new Scanner(System.in);
        UserServis usetServis = new UserServis();

        while (true){

        System.out.println("""
                0 Exit
                1 Register
                2 Login
                """);
        switch (scannerInt.nextInt()){
            case 0 -> {
                System.exit(0);
            }
            case 1 -> {
                usetServis.userRegister();
            }
            case 2 -> {
                usetServis.login();
            }
        }

        }

    }
}