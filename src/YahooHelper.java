import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;


public class YahooHelper {
	private static String urlString = "http://finance.yahoo.com/d/quotes.csv?s=YHOO+EBAY+AAPL+GOOG+XOM+JNJ+MSFT+GE&f=sd1l";
	public static List<StockPrice> getData()
	{
		List<StockPrice> result = new ArrayList<StockPrice>();
		try {
			URL url = new URL(urlString);
			InputStream is = url.openStream();
			Scanner scanner = new Scanner(new BufferedInputStream(is));
			
			while (scanner != null && scanner.hasNextLine()) {
				String[] line = scanner.nextLine().split(",");
				String symbol = line[0].substring(1, line[0].length() - 1);
				String dateString = line[1].substring(1, line[1].length() - 1);
				String priceString = line[2];
				
				Calendar calendar = Calendar.getInstance();
				String[] dateArray = dateString.split("/");
				calendar.set(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]));
				
				result.add(new StockPrice(symbol, calendar.getTime(), Float.parseFloat(priceString)));
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
