package com.example.appliances.bot;

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
@Component
public class YourTelegramBot extends TelegramLongPollingBot {

    final BotConfig botConfig;

    public YourTelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
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
            Long chatId = update.getMessage().getChatId();
            if (messageText.equals("/start")) {
                sendStartMenu(chatId);
            } else {
                // Обработка нажатий на кнопки
                String buttonText = messageText.toLowerCase(); // приводим текст к нижнему регистру для удобства сравнения
                switch (buttonText) {
                    case "о нас":
                        sendAboutUs(chatId);
                        break;
                    case "наши товары/услуги":
                        sendProductsServices(chatId);
                        break;
                    case "контакты":
                        sendContacts(chatId);
                        break;
                    case "адреса филиалов":
                        sendBranchAddresses(chatId);
                        break;
                    case "акции и специальные предложения":
                        sendMessageFeedback(chatId);
                        break;
                    case "поддержка/обратная связь":
                        sendSupport(chatId);
                        break;
                    case "партнерство/сотрудничество":
                        sendPartnershipInfo(chatId);
                        break;
//                    case "отправить сообщение на почту":
//                        sendEmailPrompt(chatId);
//                        break;
                    default:
                        // Возможно, здесь стоит добавить обработку неизвестной команды или текста
                        break;
                }
            }
        }
    }
    private void sendAboutUs(Long chatId) {
        String infoAboutUs  = "Информация о нас";

        sendMessage(chatId, infoAboutUs);
    }

    private void sendProductsServices(Long chatId) {
        String websiteLink = "Вот ссылка на наш сайт: glavsnab.kg ";
        sendMessage(chatId, websiteLink);
    }

    private void sendContacts(Long chatId) {
        String contactInfo = "Для связи с нашими специалистами, позвоните по номеру: +99655555555";
        sendMessage(chatId, contactInfo);
    }

    private void sendBranchAddresses(Long chatId) {
        String branchAddress = "Адрес нашего филиала: [Адрес_филиала]";
        sendMessage(chatId, branchAddress);
    }

//    private void sendPromotions(Long chatId) {
//        String promotionsInfo = "У нас действуют следующие акции и специальные предложения: [Информация_о_акциях]";
//        sendMessage(chatId, promotionsInfo);
//    }

    private void sendSupport(Long chatId) {
        // Форма обратной связи
        sendMessage(chatId, "Пожалуйста, введите ваше сообщение для поддержки:");
    }

    private void sendPartnershipInfo(Long chatId) {
        // Информация о возможностях партнерства или сотрудничества
    }

    private void sendMessageFeedback(Long chatId) {
        String feedbackForm = "Для отправки обратной связи, пожалуйста, заполните форму по ссылке: [Ссылка_на_форму_обратной_связи]";
        sendMessage(chatId, feedbackForm);
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void sendStartMenu(Long chatId) {
        ReplyKeyboardMarkup replyMarkup = new ReplyKeyboardMarkup();
        replyMarkup.setResizeKeyboard(true);

        KeyboardRow row1 = new KeyboardRow();
        row1.add("О нас");
        row1.add("Наши товары/услуги");
        row1.add("Контакты");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("Адреса филиалов");
//        row2.add("Акции и специальные предложения");
        row2.add("Поддержка/Обратная связь");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("Партнерство/Сотрудничество");
        row3.add("Отправить сообщение на почту");

        List<KeyboardRow> keyboard = Arrays.asList(row1, row2, row3);
        replyMarkup.setKeyboard(keyboard);
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите действие:");
        message.setReplyMarkup(replyMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
