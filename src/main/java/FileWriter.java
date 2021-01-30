import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;

public class FileWriter {

    ArrayList<Item> items;

    public FileWriter() {
        this.items = new ArrayList<Item>();
    }

    public void addItem(Item item) {
        System.out.println("item added");
        items.add(item);
    }

    public void writeToFile() throws Exception {
        PrintWriter writer = new PrintWriter("data.csv");
        writer.println("Title,Price,URL");

        for(Item item: items) {
            String title = item.getTitle().replaceAll("[,]", "_");
            writer.println(String.format("%s,%s,%s",
                title,
                item.getPrice(),
                item.getUrl()
            ));
        }

        writer.close();
    }

    public void printContent() {
        for (Item item: items) {
            item.print();
        }
    }

}
