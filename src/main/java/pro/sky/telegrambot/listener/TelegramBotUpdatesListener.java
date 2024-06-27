package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final NotificationTaskRepository repository;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationTaskRepository repository) {
        this.telegramBot = telegramBot;
        this.repository = repository;
    }

    // pattern of data format "dd.mm.yyyy hh:mm textOfNotification"
    private static final Pattern PATTERN = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            LOGGER.info("Processing update: {}", update);
            // if there is any message
            if (update.message() != null) {
                String messageText = update.message().text();
                if (messageText != null) {
                    if (messageText.equals("/start")) { // send message "Добро пожаловать"
                        SendMessage message = new SendMessage(update.message().chat().id(), "Добро пожаловать");
                        SendResponse response = telegramBot.execute(message);
                    } else {
                        var matcher = PATTERN.matcher(messageText);
                        if (matcher.matches()) { // if new message is like "dd.mm.yyyy hh:mm textOfNotification"
                            var noteTime = parseDate(matcher.group(1));
                            if (noteTime == null) {
                                telegramBot.execute(new SendMessage(update.message().chat().id(), "Неправильный формат даты"));
                                return;
                            }
                            // then save notification task
                            var task = new NotificationTask();
                            task.setNoteText(matcher.group(3));
                            task.setChatId(update.message().chat().id());
                            task.setNoteTime(noteTime);
                            repository.save(task);
                            LOGGER.info("Task saved: {}", task);
                        }
                    }
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private LocalDateTime parseDate(String date) {
        try {
            return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        } catch (DateTimeParseException e) {
            LOGGER.error("Incorrect date format: {}", date);
        }
        return null;
    }
}
