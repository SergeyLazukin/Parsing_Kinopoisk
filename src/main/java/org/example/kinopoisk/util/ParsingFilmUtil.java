package org.example.kinopoisk.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.example.kinopoisk.model.Film;
import org.example.kinopoisk.repository.FilmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.List;

/**
 * Утилита для парсинга рейтинга Кинопоиска
 */
public class ParsingFilmUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ParsingFilmUtil.class);

    @Resource
    private FilmRepository filmRepository;

    private WebClient client;

    public void initialize() {
        client = new WebClient(BrowserVersion.CHROME);
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
    }

    public void parsing(String url) {
        Instant instant = Instant.now();
        //производился ли парсинг в этот день
        boolean existTop = filmRepository.existByDate(instant);
        if(existTop) {
            LOG.info("Информация за эту дату уже собрана");
            return;
        }
        initialize();
        while (url != null) {
            try {

                HtmlPage page = client.getPage(url);
                List<?> list = page.getByXPath("//div[@class='desktop-rating-selection-film-item']");

                for (Object object : list) {
                    HtmlElement element = (HtmlElement) object;
                    HtmlElement position = element.getFirstByXPath(".//span[@class='film-item-rating-position__position']");
                    HtmlElement name = element.getFirstByXPath(".//p[@class='selection-film-item-meta__name']");
                    HtmlElement originalNameAndYear = element.getFirstByXPath(".//p[@class='selection-film-item-meta__original-name']");
                    HtmlElement rating = element.getFirstByXPath(".//span[@class='rating__value rating__value_positive']");
                    HtmlElement votes = element.getFirstByXPath(".//span[@class='rating__count']");

                    Film film = new Film();
                    film.setPosition(Integer.parseInt(position.asText()));
                    boolean containOnlyYear = originalNameAndYear.asText().matches("[0-9]+");
                    if (containOnlyYear) {
                        film.setName(name.asText());
                        film.setYear(Integer.parseInt(originalNameAndYear.asText()));
                    } else {
                        String[] tokens = originalNameAndYear.asText().split(",");
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < tokens.length - 1; i++) {
                            stringBuilder.append(tokens[i]).append((i < tokens.length - 2) ? "," : "");
                        }
                        film.setName(stringBuilder.toString());
                        film.setYear(Integer.parseInt(tokens[tokens.length - 1].trim()));
                    }
                    film.setRating(Double.parseDouble(rating.asText()));
                    film.setNumberOfVotes(Integer.parseInt(votes.asText().replaceAll(" ", "")));
                    film.setCreateInstant(instant);
                    filmRepository.save(film);
                }
                //берём элементы с ссылками "Назад" и "Вперёд"
                List<?> pagination = page.getByXPath(".//a[@class='paginator__page-relative']");
                for(Object object : pagination) {
                    HtmlElement element = (HtmlElement) object;
                    if(element.asText().equals("Вперёд")) {
                        url = "https://www.kinopoisk.ru" + element.getAttribute("href");
                    } else {
                        url = null;
                    }
                }
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }
    }
}
