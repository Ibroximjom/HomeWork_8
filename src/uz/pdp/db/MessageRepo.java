package uz.pdp.db;

import util.Utils;
import uz.pdp.entity.User;
import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MessageRepo {
    private List<Message> messages;
    private static MessageRepo singleton;
    private MessageRepo(List<Message> messages) {
        this.messages = messages;
    }
    public static MessageRepo getInstance(){
        if (singleton == null){
            singleton = new MessageRepo(loadData());
        }
        return singleton;
    }


    public static void chat(User currentUser, User friendUser) {
        MessageRepo messageRepo = MessageRepo.getInstance();
        List<Message> messages = messageRepo.findAll();

        for (int i = 0; i < messages.size(); i++) {
            String formatTemp = messages.get(i).getZonedDateTime()
                    .withZoneSameInstant(ZoneId.of(currentUser.getTimeZone().getZone().getId()))
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);


            LocalDateTime parse = LocalDateTime.parse(formatTemp.substring(0, 19));
            String minute = parse.getMinute() < 10 ? "0" + parse.getMinute() : parse.getMinute() + "";
            String format = parse.getDayOfMonth() + "- " + parse.getHour() + ":" + minute;

            boolean isCurrentUserSender = messages.get(i).getFromId().equals(currentUser.getId());
            boolean isFriendUserReceiver = messages.get(i).getToId().equals(friendUser.getId());
            boolean isFriendUserSender = messages.get(i).getFromId().equals(friendUser.getId());
            boolean isCurrentUserReceiver = messages.get(i).getToId().equals(currentUser.getId());

            if ((isCurrentUserSender && isFriendUserReceiver) || (isFriendUserSender && isCurrentUserReceiver)) {
                String senderName = isCurrentUserSender ? currentUser.getName() : friendUser.getName();
                String receiverName = isCurrentUserReceiver ? currentUser.getName() : friendUser.getName();

                String messageText = messages.get(i).getText();
                System.out.println(senderName + " -> " + receiverName + ": " + messageText + "  " + format);
            }
        }

        if (Utils.getNum("1- " + ("Habar jonatish ") + "\n" +
                "2- " +("EXIT") + "\n" + ("Tanlash")) == 1) {
            sendMessage(currentUser, friendUser);
        }else {
            return;
        }
    }


    private static void sendMessage(User from, User to) {
       MessageRepo messageRepo = MessageRepo.getInstance();
        String message = Utils.getStr("Habar kiriting");
        System.out.println("LOADING");
        Thread saveMessage = new Thread(() -> {
            messageRepo.save(new Message(from.getId(), to.getId(), message));
        });
        saveMessage.start();
        try {
            saveMessage.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        CompletableFuture<Boolean> completableFuture = CompletableFuture.completedFuture(true);
        completableFuture.thenRun(() -> chat(from, to));
    }

    public void save(Message message) {
        messages.add(message);
        uploadData();
    }

    public List<Message> findAll() {
        return messages;
    }

    @SuppressWarnings("unchecked")
    private static List<Message> loadData() {
        try (
                InputStream inputStream = new FileInputStream("src/uz/pdp/DB/messages_db.txt");
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ) {
            return (List<Message>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private void uploadData() {
        try (
                OutputStream outputStream = new FileOutputStream("src/uz/pdp/DB/messages_db.txt");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        ) {
            objectOutputStream.writeObject(messages);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}