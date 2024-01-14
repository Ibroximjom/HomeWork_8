package uz.pdp.db;

import util.Utils;
import uz.pdp.entity.User;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserServis {

    private User user = new User();
    private static List<User> USERS;

    public UserServis() {
        USERS = loadData();
    }

    public void userRegister() {
        user = new User();
        user.setName(Utils.getStr("Name kiriting => "));
        user.setPhoneNumber(Utils.getStr("PhoneNumber kiriting => "));
        timeZone();
        user.setPassword(Utils.getStr("Parolni kiriting => "));
        System.out.print("Parolni yana bir bor kiriting => ");
        if (user.getPassword().equals(Utils.getStr())) {
            check();
        } else {
            System.out.println("Parolni qaytadan kiritishda xatolik yuz berdi");
        }
    }

    private void timeZone() {
        System.out.println("""
                1 O'zbekiston (Uzbekistan).
                2 Amerika (Eastern Time).
                3 Xitoy (China).
                 """);
        LocalDateTime localDateTime = LocalDateTime.now();
        switch (Utils.getNum("TimeZonalardan birini tanlang => ")) {
            case 1 -> {
                ZoneId uzTashkentZone = ZoneId.of("Asia/Tashkent");
                ZonedDateTime uzTashkentDateTime = ZonedDateTime.of(localDateTime, uzTashkentZone);
                user.setTimeZone(uzTashkentDateTime);
            }
            case 2 -> {
                ZoneId easternTimeZone = ZoneId.of("America/New_York");
                ZonedDateTime easternDateTime = ZonedDateTime.of(localDateTime, easternTimeZone);
                user.setTimeZone(easternDateTime);
            }
            case 3 -> {
                ZoneId chinaZone = ZoneId.of("Asia/Shanghai");
                ZonedDateTime chinaDateTime = ZonedDateTime.of(localDateTime, chinaZone);
                user.setTimeZone(chinaDateTime);
            }
        }
    }

    public void login() {
        List<User> bosqaUserslar = new ArrayList<>();

        if (USERS.isEmpty()) {
            System.out.println("Hali Userlar mavjud emas ");
            return;
        }

        String phoneNumber = Utils.getStr("Telefon raqamini kiriting => ");
        String password = Utils.getStr("Parolni kiriting => ");

        boolean foydalanuvchiTopildi = false;
        User hozirgiUser = null;
        for (User foydalanuvchi : USERS) {
            if (foydalanuvchi.getPhoneNumber().equals(phoneNumber) && foydalanuvchi.getPassword().equals(password)) {
                hozirgiUser = foydalanuvchi;
                foydalanuvchiTopildi = true;
                System.out.println("Xush kelibsiz " + hozirgiUser.getName());

                for (User boshqaFoydalanuvchi : USERS) {
                    if (!boshqaFoydalanuvchi.getPhoneNumber().equals(phoneNumber)) {
                        bosqaUserslar.add(boshqaFoydalanuvchi);
                    }
                }

                while (true) {
                    for (int i = 0; i < bosqaUserslar.size(); i++) {
                        System.out.println(i + " " + bosqaUserslar.get(i).getName());
                    }

                    Integer num = Utils.getNum("Qaysi User ga habar jonatmoqshi siz (Chiqish -1)");
                    if (num.equals(-1)) {
                        return;
                    } else {
                        System.out.println("Tanlash");
                        MessageRepo.chat(hozirgiUser, bosqaUserslar.get(num));
                    }
                }
            }
        }

        if (!foydalanuvchiTopildi) {
            System.out.println("Bunday foydalanuvchi mavjud emas");
        }
    }

    private void check() {
        if (name() && phoneNumber() && password()) {
            System.out.println("Foydalanuvchi qo'shildi 👍👍👍");
            USERS.add(user);
            uploadData();
        } else {
            System.out.println("Ma'lumotlarni tekshirib qaytadan kiriting");
        }
    }

    private boolean name() {
        Pattern patternName = Pattern.compile("([A-Za-z]{3,})");
        Matcher matcher = patternName.matcher(user.getName());
        return matcher.matches();
    }

    private boolean phoneNumber() {
        Pattern patternPhoneNumber = Pattern.compile("((\\+998)(88|33|9[01234579)])(\\d){7})");
        Matcher matcher = patternPhoneNumber.matcher(user.getPhoneNumber());
        return matcher.matches();
    }

    private boolean password() {
        Pattern patternPassword = Pattern.compile("(?=.*[A-Z]+)(?=.*[a-z]+)(?=.*\\d+)(?=.*\\W+).{8,}");
        Matcher matcher = patternPassword.matcher(user.getPassword());
        return matcher.matches();
    }

    private void uploadData() {
        try (
                OutputStream outputStream = new FileOutputStream("src/uz/pdp/DB/users_db.txt");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)
        ) {
            objectOutputStream.writeObject(USERS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static List<User> loadData() {
        try (
                InputStream inputStream = new FileInputStream("src/uz/pdp/DB/users_db.txt");
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)
        ) {
            return (List<User>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
