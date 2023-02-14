package ru.netology.delivery.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    public void shouldPlanAndReplanMeeting() {
        DataGenerator.UserInfo user = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 3;
        String initialDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        int daysToAddForSecondMeeting = 7;
        String secondDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='date'] input").doubleClick();
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(initialDate);
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id='agreement']").click();
        $x("//span[text()='Запланировать']").click();
        $(withText("Успешно!")).should(appear, Duration.ofSeconds(15));
        $(".notification__content").shouldBe(visible.text("Встреча успешно запланирована на " + initialDate), Duration.ofSeconds(15)).shouldBe(visible);
        $("[data-test-id='date'] input").doubleClick();
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(secondDate);
        $(withText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content").shouldBe(visible.text("У вас уже запланирована встреча на другую дату. Перепланировать?"), Duration.ofSeconds(15)).shouldBe(visible);
        $x("//span[text()='Перепланировать']").click();
        $(".notification__content").shouldBe(visible.text("Встреча успешно запланирована на " + secondDate), Duration.ofSeconds(15)).shouldBe(visible);
    }
}
