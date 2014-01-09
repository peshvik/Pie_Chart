import java.util.Date;


public class StockPrice {
	private String symbol;
	private Date date;
	private float price;
	public StockPrice(String symbol, Date date, float price) {
		this.symbol = symbol;
		this.date = date;
		this.price = price;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public Date getDate() {
		return date;
	}

	public float getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return "<" + this.symbol + " | " + this.date + " | " + this.price + ">";
	}
}
