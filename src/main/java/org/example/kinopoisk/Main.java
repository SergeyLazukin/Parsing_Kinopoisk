package org.example.kinopoisk;

import org.example.kinopoisk.util.ParsingFilmUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        context.getBean("parsingFilmUtil", ParsingFilmUtil.class).parsing("https://www.kinopoisk.ru/lists/top250/");
    }
}
