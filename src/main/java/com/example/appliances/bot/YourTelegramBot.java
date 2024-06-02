package com.example.appliances.bot;

import com.example.appliances.model.response.WishListResponse;
import com.example.appliances.service.SupplierService;
import com.example.appliances.service.WishListService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class YourTelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final SupplierService supplierService;
    private final WishListService wishListService;

    private final Map<Long, String> userStates = new HashMap<>();
    private final Map<Long, String> userLogins = new HashMap<>();

    @Autowired
    public YourTelegramBot(BotConfig botConfig, SupplierService supplierService, WishListService wishListService) {
        this.botConfig = botConfig;
        this.supplierService = supplierService;
        this.wishListService = wishListService;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                sendMessage(chatId, "Welcome! Please login using /login <username> <password>");
            } else if (messageText.startsWith("/login")) {
                handleLogin(chatId, messageText);
            } else if (userStates.containsKey(chatId) && userStates.get(chatId).equals("AUTHORIZED")) {
                if (messageText.equals("/wishlist")) {
                    handleWishList(chatId);
                } else {
                    sendMessage(chatId, "Unknown command. Use /wishlist to view the wish list.");
                }
            } else {
                sendMessage(chatId, "Please login using /login <username> <password>");
            }
        }
    }

    private void handleLogin(long chatId, String messageText) {
        String[] parts = messageText.split(" ");
        if (parts.length == 3) {
            String pin = parts[1];
            String password = parts[2];
            // Assuming SupplierService has a method for validating login
            if (supplierService.validateLogin(pin, password)) {
                userStates.put(chatId, "AUTHORIZED");
                userLogins.put(chatId, pin);
                sendMessage(chatId, "Login successful!");
            } else {
                sendMessage(chatId, "Invalid username or password. Please try again.");
            }
        } else {
            sendMessage(chatId, "Invalid login command. Use /login <username> <password>");
        }
    }

    private void handleWishList(long chatId) {
        List<WishListResponse> wishLists = wishListService.findAll().stream()
                .filter(wishList -> !wishList.getIsServed())
                .collect(Collectors.toList());

        if (wishLists.isEmpty()) {
            sendMessage(chatId, "No pending wish lists.");
        } else {
            wishLists.forEach(wishList -> {
                StringBuilder sb = new StringBuilder();
                sb.append("WishList ID: ").append(wishList.getId()).append("\n");
                sb.append("Storage: ").append(wishList.getStorageId()).append("\n");
                sb.append("Items:\n");
                wishList.getWishListItems().forEach(item -> {
                    sb.append(" - ").append(item.getProduct().getName()).append(": ").append(item.getQuantity()).append("\n");
                });
                sendMessage(chatId, sb.toString());
            });
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
