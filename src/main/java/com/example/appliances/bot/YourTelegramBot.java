//package com.example.appliances.bot;
//
//import com.example.appliances.model.response.*;
//import com.example.appliances.service.StorageService;
//import com.example.appliances.service.SupplierService;
//import com.example.appliances.service.SupplyService;
//import com.example.appliances.service.WishListService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Component
//public class YourTelegramBot extends TelegramLongPollingBot {
//    private final SupplyService supplyService;
//    private final BotConfig botConfig;
//    private final SupplierService supplierService;
//    private final WishListService wishListService;
//    private final StorageService storageService; // Добавляем зависимость для StorageService
//
//    private final Map<Long, String> userStates = new HashMap<>();
//    private final Map<Long, String> userPins = new HashMap<>();
//    private final Map<Long, String> userPasswords = new HashMap<>();
//
//    @Autowired
//    public YourTelegramBot(SupplyService supplyService, BotConfig botConfig, SupplierService supplierService, WishListService wishListService, StorageService storageService) {
//        this.supplyService = supplyService;
//        this.botConfig = botConfig;
//        this.supplierService = supplierService;
//        this.wishListService = wishListService;
//        this.storageService = storageService;
//    }
//
//    @Override
//    public String getBotUsername() {
//        return botConfig.getBotUsername();
//    }
//
//    @Override
//    public String getBotToken() {
//        return botConfig.getBotToken();
//    }
//
//    @Override
//    public void onUpdateReceived(Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String messageText = update.getMessage().getText();
//            long chatId = update.getMessage().getChatId();
//
//            if (messageText.equals("/start")) {
//                sendLoginPrompt(chatId);
//            } else if (messageText.startsWith("/login")) {
//                handleLogin(chatId, messageText);
//            } else if (userStates.containsKey(chatId) && userStates.get(chatId).equals("AUTHORIZED")) {
//                if (messageText.equals("/лист заявок")) {
//                    handleWishList(chatId);
//                } else if (messageText.equals("/лист поставок")) {
//                    handleViewSupplies(chatId);
//                } else {
//                    sendMessage(chatId, "Unknown command. Use /wishlist to view the wish list or /supplylist to view your supplies.");
//                }
//            } else {
//                sendLoginPrompt(chatId);
//            }
//        } else if (update.hasCallbackQuery()) {
//            handleCallbackQuery(update);
//        }
//    }
//
//    private void sendLoginPrompt(long chatId) {
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//
//        List<InlineKeyboardButton> rowInline = new ArrayList<>();
//        InlineKeyboardButton button = new InlineKeyboardButton();
//        button.setText("Логин");
//        button.setCallbackData("login_prompt");
//        rowInline.add(button);
//        rowsInline.add(rowInline);
//
//        inlineKeyboardMarkup.setKeyboard(rowsInline);
//
//        SendMessage message = new SendMessage();
//        message.setChatId(String.valueOf(chatId));
//        message.setText("Добро пожаловать! Пожалуйста введите /login <pin> <password>");
//        message.setReplyMarkup(inlineKeyboardMarkup);
//
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void handleViewSupplies(long chatId) {
//        String pin = userPins.get(chatId);
//        if (pin == null) {
//            sendMessage(chatId, "Пожалуйста сначала логин.");
//            return;
//        }
//
//        try {
//            List<SupplyItemResponse> supplies = supplyService.findAllBySupplierPin(pin);
//
//            if (supplies == null || supplies.isEmpty()) {
//                sendMessage(chatId, "Вы еще не совершили поставки.");
//            } else {
//                StringBuilder sb = new StringBuilder();
//                sb.append("Ваши поставки:\n");
//                supplies.forEach(supply -> {
//                    sb.append(" - ID: ").append(supply.getId())
//                            .append(", Количество: ").append(supply.getQuantity())
//                            .append(", Продукт: ").append(supply.getProduct().getName())
//                            .append("\n");
//                });
//
//                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//
//                List<InlineKeyboardButton> rowInline = new ArrayList<>();
//                InlineKeyboardButton buttonBack = new InlineKeyboardButton();
//                buttonBack.setText("Назад в главное меню");
//                buttonBack.setCallbackData("Назад в главное меню");
//                rowInline.add(buttonBack);
//
//                rowsInline.add(rowInline);
//                inlineKeyboardMarkup.setKeyboard(rowsInline);
//
//                SendMessage message = new SendMessage();
//                message.setChatId(String.valueOf(chatId));
//                message.setText(sb.toString());
//                message.setReplyMarkup(inlineKeyboardMarkup);
//
//                try {
//                    execute(message);
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                    sendMessage(chatId, "Error occurred while sending the message: " + e.getMessage());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            sendMessage(chatId, "Error occurred while retrieving supplies: " + e.getMessage());
//        }
//    }
//
//    private void handleLogin(long chatId, String messageText) {
//        String[] parts = messageText.split(" ");
//        if (parts.length == 3) {
//            String pin = parts[1];
//            String password = parts[2];
//            if (supplierService.validateLogin(pin, password)) {
//                userStates.put(chatId, "AUTHORIZED");
//                userPins.put(chatId, pin);
//                userPasswords.put(chatId, password);
//                sendMainMenu(chatId);
//            } else {
//                sendMessage(chatId, "Invalid pin or password. Please try again.");
//            }
//        } else {
//            sendMessage(chatId, "Invalid login command. Use /login <pin> <password>");
//        }
//    }
//
//    private void sendMainMenu(long chatId) {
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//
//        List<InlineKeyboardButton> rowInline = new ArrayList<>();
//        InlineKeyboardButton buttonWishList = new InlineKeyboardButton();
//        buttonWishList.setText("Заявки");
//        buttonWishList.setCallbackData("view_wishlists");
//        rowInline.add(buttonWishList);
//
//        InlineKeyboardButton buttonProfile = new InlineKeyboardButton();
//        buttonProfile.setText("Показать профиль");
//        buttonProfile.setCallbackData("view_profile");
//        rowInline.add(buttonProfile);
//
//        InlineKeyboardButton buttonSupplyList = new InlineKeyboardButton(); // Добавляем кнопку для просмотра поставок
//        buttonSupplyList.setText("Поставлено");
//        buttonSupplyList.setCallbackData("view_supplies");
//        rowInline.add(buttonSupplyList);
//
//        rowsInline.add(rowInline);
//        inlineKeyboardMarkup.setKeyboard(rowsInline);
//
//        SendMessage message = new SendMessage();
//        message.setChatId(String.valueOf(chatId));
//        message.setText("Приветствую в Appliances!\n" +
//                "\n" +
//                "Я рад, что ты здесь!\n" +
//                "\n" +
//                "Appliances - твой верный помощник в мире бытовой техники и электроники.\n" +
//                "\n" +
//                "Чем я могу тебе помочь?:");
//        message.setReplyMarkup(inlineKeyboardMarkup);
//
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void handleWishList(long chatId) {
//        List<WishListResponse> wishLists = wishListService.findAll().stream()
//                .filter(wishList -> !wishList.getIsServed())
//                .collect(Collectors.toList());
//
//        if (wishLists.isEmpty()) {
//            sendMessage(chatId, "No pending wish lists.");
//        } else {
//            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//
//            wishLists.forEach(wishList -> {
//                List<InlineKeyboardButton> rowInline = new ArrayList<>();
//                InlineKeyboardButton button = new InlineKeyboardButton();
//                button.setText("Заявка № : " + wishList.getId());
//                button.setCallbackData("wishlist_" + wishList.getId());
//                rowInline.add(button);
//                rowsInline.add(rowInline);
//            });
//
//            inlineKeyboardMarkup.setKeyboard(rowsInline);
//
//            SendMessage message = new SendMessage();
//            message.setChatId(String.valueOf(chatId));
//            message.setText("Выберите заявку:");
//            message.setReplyMarkup(inlineKeyboardMarkup);
//
//            try {
//                execute(message);
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void handleCallbackQuery(Update update) {
//        String callbackData = update.getCallbackQuery().getData();
//        long chatId = update.getCallbackQuery().getMessage().getChatId();
//        int messageId = update.getCallbackQuery().getMessage().getMessageId();
//
//        if (callbackData.startsWith("wishlist_")) {
//            long wishListId = Long.parseLong(callbackData.split("_")[1]);
//            handleWishListSelection(chatId, messageId, wishListId);
//        } else if (callbackData.equals("view_wishlists")) {
//            handleWishList(chatId);
//        } else if (callbackData.equals("view_profile")) {
//            handleViewProfile(chatId);
//        } else if (callbackData.equals("view_storage")) {
//            handleViewAllStorage(chatId); // Добавляем обработку запроса на просмотр всех хранилищ
//        } else if (callbackData.startsWith("back_to_wishlists")) {
//            handleWishList(chatId);
//        } else if (callbackData.startsWith("back_to_main_menu")) { // Обрабатываем запрос на возврат в главное меню
//            sendMainMenu(chatId);
//        } else if (callbackData.startsWith("supply_")) {
//            long wishListId = Long.parseLong(callbackData.split("_")[1]);
//            handleSupplyWishList(chatId, wishListId);
//        }
//    }
//
//    private void handleWishListSelection(long chatId, int messageId, long wishListId) {
//        WishListResponse wishList = wishListService.findById(wishListId);
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("Заявка №: ").append(wishList.getId()).append("\n");
//        sb.append("Склад: ").append(wishList.getStorage().getName()).append("\n");
//        sb.append("Список необходимых продуктов:\n");
//        wishList.getWishListItems().forEach(item -> {
//            sb.append(" - ").append(item.getProduct().getName()).append(": ").append(item.getQuantity()).append("\n");
//        });
//
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//
//        List<InlineKeyboardButton> rowInline = new ArrayList<>();
//        InlineKeyboardButton buttonSupply = new InlineKeyboardButton();
//        buttonSupply.setText("Поставить");
//        buttonSupply.setCallbackData("supply_" + wishList.getId());
//        rowInline.add(buttonSupply);
//
//        InlineKeyboardButton buttonBack = new InlineKeyboardButton();
//        buttonBack.setText("Обратно к заявкам");
//        buttonBack.setCallbackData("back_to_wishlists");
//        rowInline.add(buttonBack);
//
//        InlineKeyboardButton buttonBackToMenu = new InlineKeyboardButton();
//        // Добавляем кнопку возврата в главное меню
//        buttonBackToMenu.setText("Главное меню");
//        buttonBackToMenu.setCallbackData("back_to_main_menu");
//        rowInline.add(buttonBackToMenu);
//
//        rowsInline.add(rowInline);
//        inlineKeyboardMarkup.setKeyboard(rowsInline);
//
//        EditMessageText message = new EditMessageText();
//        message.setChatId(String.valueOf(chatId));
//        message.setMessageId(messageId);
//        message.setText(sb.toString());
//        message.setReplyMarkup(inlineKeyboardMarkup);
//
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void handleSupplyWishList(long chatId, long wishListId) {
//        WishListResponse wishList = wishListService.findById(wishListId);
//        String pin = userPins.get(chatId);
//        String password = userPasswords.get(chatId);
//        SupplyResponse supplyResponse = supplyService.createFromWishList(wishListId, pin, password);
//        sendMessage(chatId, "Поставка успешно оформлена под номером: " + supplyResponse.getId());
//    }
//
//    private void handleViewProfile(long chatId) {
//        String pin = userPins.get(chatId);
//        SupplierResponse supplier = supplierService.findByUsername(pin);
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("Информация о профиле:\n");
//        sb.append("ПИН: ").append(supplier.getPin()).append("\n");
//        sb.append("имя: ").append(supplier.getName()).append("\n");
//        sb.append("фамилия: ").append(supplier.getSurname()).append("\n");
//        sb.append("Email: ").append(supplier.getEmail()).append("\n");
//        sb.append("номер телефона: ").append(supplier.getPhoneNumber()).append("\n");
//
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//
//        List<InlineKeyboardButton> rowInline = new ArrayList<>();
//        InlineKeyboardButton buttonBack = new InlineKeyboardButton();
//        buttonBack.setText("Back to Main Menu");
//        buttonBack.setCallbackData("back_to_main_menu"); // Обновляем callback data для возврата в главное меню
//        rowInline.add(buttonBack);
//
//        rowsInline.add(rowInline);
//        inlineKeyboardMarkup.setKeyboard(rowsInline);
//
//        SendMessage message = new SendMessage();
//        message.setChatId(String.valueOf(chatId));
//        message.setText(sb.toString());
//        message.setReplyMarkup(inlineKeyboardMarkup);
//
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void handleViewAllStorage(long chatId) {
//        List<StorageResponseBot> storageList = storageService.findAllBot(); // Вызов вашего метода для получения всех хранилищ
//        String storageInfo = storageList.stream()
//                .map(StorageResponseBot::toString) // Преобразуйте по необходимости
//                .collect(Collectors.joining("\n"));
//
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//
//        List<InlineKeyboardButton> rowInline = new ArrayList<>();
//        InlineKeyboardButton buttonBack = new InlineKeyboardButton();
//        buttonBack.setText("Back to Main Menu");
//        buttonBack.setCallbackData("back_to_main_menu"); // Обновляем callback data для возврата в главное меню
//        rowInline.add(buttonBack);
//
//        rowsInline.add(rowInline);
//        inlineKeyboardMarkup.setKeyboard(rowsInline);
//
//        SendMessage message = new SendMessage();
//        message.setChatId(String.valueOf(chatId));
//        message.setText("Storages:\n" + storageInfo);
//        message.setReplyMarkup(inlineKeyboardMarkup);
//
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void sendMessage(long chatId, String text) {
//        SendMessage message = new SendMessage();
//        message.setChatId(String.valueOf(chatId));
//        message.setText(text);
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//}