package ru.netology.delivery.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;
import java.time.LocalDate;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    @BeforeEach
    void Setup() {
        open("http://localhost:9999/");
        //Configuration.headless = true;
    }

    @Test
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 15;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 25;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $(".icon_name_calendar").click();
        String meetingDateDay = Integer.toString(LocalDate.now().plusDays(daysToAddForFirstMeeting).getDayOfMonth());
        if (((LocalDate.now().plusDays(daysToAddForFirstMeeting)).getMonthValue()) == (LocalDate.now().plusDays(3).getMonthValue())) {
            $$(".calendar__day").find(exactText(meetingDateDay)).click();
        } else {
            $("[data-step='1']").click();
            $$(".calendar__day").find(exactText(meetingDateDay)).click();
        }
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $x("//*[text()='Запланировать']").click();
        $("[data-test-id =success-notification]")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate));
        $(".icon_name_calendar").click();
        String secondMeetingDateDay = Integer.toString(LocalDate.now().plusDays(daysToAddForSecondMeeting).getDayOfMonth());
        if (((LocalDate.now().plusDays(daysToAddForFirstMeeting)).getMonthValue()) == ((LocalDate.now().plusDays(daysToAddForSecondMeeting)).getMonthValue())) {
            $$(".calendar__day").find(exactText(secondMeetingDateDay)).click();
        } else {
            $("[data-step='1']").click();
            $$(".calendar__day").find(exactText(secondMeetingDateDay)).click();
        }
        $x("//*[text()='Запланировать']").click();
        $x("//*[text()='Перепланировать']").click();
        $("[data-test-id =success-notification]")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate));

    }

}
