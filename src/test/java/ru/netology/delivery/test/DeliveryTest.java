package test;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }
            @Test
            @DisplayName("Plan")
            void shouldSuccessfulPlanAndReplanMeeting() {
                var validUser = DataGenerator.Registration.generateUser("ru");
                var daysToAddForFirstMeeting = 4;
                var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
                var daysToAddForSecondMeeting = 7;
                var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
                $("[data-test-id=city] .input__control").setValue(validUser.getCity());
                $("[data-test-id=date] .input__control").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
                $("[data-test-id=date] .input__control").setValue(firstMeetingDate);
                $("[data-test-id=name] .input__control").setValue(validUser.getName());
                $("[data-test-id=phone] .input__control").setValue(validUser.getPhone());
                $("[data-test-id=agreement] .checkbox__box").click();
                $$(".button").find(exactText("Запланировать")).click();
                $("[data-test-id=success-notification] .notification__title").shouldBe(Condition.visible, Duration.ofSeconds(15));
                $("div.notification__content").shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate));
                $("[data-test-id=success-notification] .icon-button__text").click();
                $("[data-test-id=date] .input__control").doubleClick().sendKeys(secondMeetingDate);
                $(".button__content").click();
                $("[data-test-id=replan-notification] .notification__title").shouldBe(Condition.visible, Duration.ofSeconds(15));
                $("[data-test-id=replan-notification] .notification__content").shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
                $("[data-test-id=replan-notification] .button__text").click();
                $("[data-test-id=success-notification] .notification__title").shouldBe(Condition.visible, Duration.ofSeconds(15));
                $("div.notification__content").shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate));
            }
        }
