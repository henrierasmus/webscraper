import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.awt.*;
import java.io.File;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;

public class Scraper {
    public static void main(String[] args) {
        FileWriter fileWriter = new FileWriter();
        String searchQuery = "Iphone 6s";
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        try {
            String searchUrl = "https://newyork.craigslist.org/search/sss?sort=rel&query=" + URLEncoder.encode(searchQuery, "UTF-8");
            HtmlPage page = client.getPage(searchUrl);

            List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//li[@class='result-row']");
            if(items.isEmpty())
                System.out.println("No Items Found!!!");
            else {
                for(HtmlElement htmlElement : items) {
                    HtmlElement itemNameFromHtml = ((HtmlElement) htmlElement.getFirstByXPath(".//h3[@class='result-heading']"));
                    HtmlElement spanPrice = ((HtmlElement) htmlElement.getFirstByXPath(".//span[@class='result-price']"));
                    HtmlAnchor itemAnchor = ((HtmlAnchor) htmlElement.getFirstByXPath(".//a[@class='result-image gallery']"));
                    HtmlAnchor itemAnchorNoImg = ((HtmlAnchor) htmlElement.getFirstByXPath(".//a[@class='result-image gallery empty']"));

                    Item item = new Item();

                    String itemName = itemNameFromHtml.asText();
                    item.setTitle(itemName);

                    String itemUrl = itemAnchor == null ? itemAnchorNoImg.getHrefAttribute() : itemAnchor.getHrefAttribute();
                    item.setUrl(itemUrl);

                    String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();
                    String cleanedPrice = itemPrice.replaceAll("[, $;]", "");
                    item.setPrice(new BigDecimal(cleanedPrice));

                    fileWriter.addItem(item);
                    item.print();

                    ObjectMapper mapper = new ObjectMapper();
                    String jsonString = mapper.writeValueAsString(item);
                    //System.out.println(jsonString);
                }
                fileWriter.writeToFile();
            }

            Desktop desktop = Desktop.getDesktop();
            desktop.open(new File("data.csv"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}