package com.wegotrip;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.wegotrip.data.Locale;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class CheckAvailabilityTest {

        @BeforeAll
        static void setUp() {
                Configuration.browserSize = "1920x1080";
                Configuration.holdBrowserOpen = true;
        }

        //ТЕСТОВЫЕ ДАННЫЕ: ["Catacombs of Paris: Explore the Underground Realm", "Tower of London: 950 Years of History"]
        @ValueSource(strings = {"Catacombs of Paris: Explore the Underground Realm", "Tower of London: 950 Years of History"})
        @ParameterizedTest(name = "Проверка работоспособности поиска на сайте wegotrip")
        // [test_data] == (String testData)
        void wegotripSearchCommonTest(String testData) {
        open("https://wegotrip.com");
        $(".Search__input").setValue(testData);
        $(".SearchItem__title").click();
        $(".Product-page__title")
                .shouldHave(text(testData));
        }

        @CsvSource(value = {
        "https://wegotrip.com, What will you discover next?",
        "https://wegotrip.ru, Куда вы отправитесь дальше?"
        })
        @ParameterizedTest(name = "Проверка работы сайта в разных локалях")
        void wegotripWorkDifferentLocaleText(String url, String expectedText) {
        open(url);
        $(".TopBar__title")
                .shouldHave(text(expectedText));
        }

        static Stream<Arguments> kapitalSiteButtonsTextDataProvider() {
        return Stream.of(
        Arguments.of(List.of("О банке", "Пресс-центр", "Интерактивные услуги", "Законы", "Контакты", "Закупки", "Активы на продажу"), Locale.Рус),
        Arguments.of(List.of("About", "Press-center", "Interactive services", "Legislation", "Contacts", "Purchases", "Assets for sale"), Locale.Eng)
        );
        }

        @MethodSource("kapitalSiteButtonsTextDataProvider")
        @ParameterizedTest(name = "Проверка отображения названия кнопок для локали: {1}")
        void kapitalSiteButtonsText(List<String> buttonsTexts, Locale locale) {
        open("https://kapitalbank.uz/");
        $("button[id='dropdownLangMenu']").click();
        $$(".dropdown-menu-right span").find(text(locale.name())).click();
        $$(".horizontal-multilevel-menu a").filter(visible)
        .shouldHave(CollectionCondition.texts(buttonsTexts));
        }

        @EnumSource(Locale.class)
        @ParameterizedTest
        void checkLocaleTest(Locale locale) {
        open("https://kapitalbank.uz/");
        $("button[id='dropdownLangMenu']").click();
        $$(".dropdown-menu-right span").find(text(locale.name())).shouldBe(visible);
        }
}