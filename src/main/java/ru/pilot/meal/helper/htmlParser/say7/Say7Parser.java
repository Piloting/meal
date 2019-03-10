package ru.pilot.meal.helper.htmlParser.say7;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.pilot.meal.entity.ComplexMeal;
import ru.pilot.meal.helper.ComponentSaver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class Say7Parser {
    
    private static final String tableOfContentUrl = "https://www.say7.info/cook/";
    private ExecutorService executor;
    private ComponentSaver saver;
    private boolean isMultiThread;
    private volatile AtomicLong pageCount = new AtomicLong(0);
    
    public Say7Parser(ComponentSaver saver, boolean isMultiThread){
        executor = Executors.newFixedThreadPool(10);
        this.saver = saver;
        this.isMultiThread = isMultiThread;
    }
    
    public void processParseAndSave() throws Exception{
        Document doc = Jsoup.connect(tableOfContentUrl).get();

        System.out.println("START");
        
        // первая загрузка
        String nextUrl = extractNextPageUrl(doc);

        while (nextUrl != null){
            // получаем адреса рецептов с текущей страницы
            List<String> urlList = getRecipeUrls(doc);
            
            // обработка страниц с сохранением
            parseAndSaveMealComponent(urlList);
            
            // получаем адрес следущей страницы
            nextUrl = extractNextPageUrl(doc);
            if (nextUrl != null){
                // загружаем следущую страницу
                doc = loadReceiptUrl(nextUrl);
            }
        }

        System.out.println("END (" + pageCount.get() + ")");
    }
    
    private Document loadReceiptUrl(String url) throws Exception {
        return Jsoup.connect(url).get();
    }
    

    private String extractNextPageUrl(Document doc) {
        Element element = doc.select("li.nav-next").first();
        Element next = element.select("a.tt").first();
        String href = null;
        if (next != null){
            href = getHref(next, "href");
        }
        return href;
    }
    
    private List<String> getRecipeUrls(Document doc) {
        Element lst = doc.select("div.lst").first();
        Elements select = lst.select("a[href]");
        List<String> urlList = new ArrayList<>(20);
        for (Element element : select) {
            urlList.add(getHref(element, "href"));
        }
        return urlList;
    }
    
    private void parseAndSaveMealComponent(List<String> urlList){
        for (String url : urlList) {
            if (isMultiThread){
                executor.execute(() -> processReceiptPage(url));
            } else {
                processReceiptPage(url);
            }
        }
    }
    
    private void processReceiptPage(String url){
        try {
            // загрузка страницы
            Document document = loadReceiptUrl(url);
            // парсинг страницы
            ComplexMeal complexMeal = new HtmlComponentParser().extractAllComponent(document);
            // сохранение объектов
            saver.save(complexMeal);

            long count = pageCount.addAndGet(1);
            System.out.println(count + " - " + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getHref(Element elem, String ettrName) {
        return "https://" + elem.attr(ettrName).replace("//", "");
    }
}
