import java.util.*;


class Stock {
    private String symbol;
    private String companyName;
    private double price;

    public Stock(String symbol, String companyName, double price) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getPrice() {
        return price;
    }
}


class Portfolio {
    private HashMap<String, Integer> holdings = new HashMap<>();

    public void buyStock(String symbol, int quantity) {
        holdings.put(symbol, holdings.getOrDefault(symbol, 0) + quantity);
    }

    public boolean sellStock(String symbol, int quantity) {
        if (!holdings.containsKey(symbol))
            return false;

        int owned = holdings.get(symbol);

        if (owned < quantity)
            return false;

        if (owned == quantity)
            holdings.remove(symbol);
        else
            holdings.put(symbol, owned - quantity);

        return true;
    }

    public HashMap<String, Integer> getHoldings() {
        return holdings;
    }
}

class User {
    private String name;
    private double balance;
    private Portfolio portfolio;

    public User(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.portfolio = new Portfolio();
    }

    public boolean buyStock(Stock stock, int quantity) {

        double totalCost = stock.getPrice() * quantity;

        if (totalCost > balance)
            return false;

        balance -= totalCost;
        portfolio.buyStock(stock.getSymbol(), quantity);

        return true;
    }

    public boolean sellStock(Stock stock, int quantity) {

        if (portfolio.sellStock(stock.getSymbol(), quantity)) {
            balance += stock.getPrice() * quantity;
            return true;
        }

        return false;
    }

    public void showPortfolio(Map<String, Stock> market) {

        System.out.println("\n========== YOUR PORTFOLIO ==========");

        double totalValue = balance;

        if (portfolio.getHoldings().isEmpty()) {
            System.out.println("No stocks purchased yet.");
        } else {

            for (String symbol : portfolio.getHoldings().keySet()) {

                int qty = portfolio.getHoldings().get(symbol);

                Stock stock = market.get(symbol);

                double value = qty * stock.getPrice();

                totalValue += value;

                System.out.println(symbol +
                        " | Quantity: " + qty +
                        " | Current Value: ₹" + value);
            }
        }

        System.out.println("-------------------------------------");
        System.out.println("Cash Balance : ₹" + balance);
        System.out.println("Net Worth    : ₹" + totalValue);
    }

    public double getBalance() {
        return balance;
    }
}


public class StockTradingPlatform {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        
        HashMap<String, Stock> market = new HashMap<>();

        market.put("TCS", new Stock("TCS", "Tata Consultancy Services", 3650));
        market.put("INFY", new Stock("INFY", "Infosys", 1520));
        market.put("RELIANCE", new Stock("RELIANCE", "Reliance Industries", 2850));
        market.put("HDFC", new Stock("HDFC", "HDFC Bank", 1750));
        market.put("WIPRO", new Stock("WIPRO", "Wipro Ltd", 540));

        User user = new User("Investor", 100000);

        while (true) {

            System.out.println("\n====================================");
            System.out.println("     STOCK TRADING PLATFORM");
            System.out.println("====================================");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Check Balance");
            System.out.println("6. Exit");

            System.out.print("\nEnter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:

                    System.out.println("\nAvailable Stocks");

                    System.out.println("---------------------------------------------");
                    System.out.printf("%-12s %-25s %-10s\n",
                            "Symbol", "Company", "Price");
                    System.out.println("---------------------------------------------");

                    for (Stock stock : market.values()) {

                        System.out.printf("%-12s %-25s ₹%.2f\n",
                                stock.getSymbol(),
                                stock.getCompanyName(),
                                stock.getPrice());
                    }

                    break;

                case 2:

                    System.out.print("Enter Stock Symbol: ");
                    String buySymbol = sc.next().toUpperCase();

                    if (!market.containsKey(buySymbol)) {
                        System.out.println("Invalid Stock Symbol.");
                        break;
                    }

                    System.out.print("Enter Quantity: ");
                    int buyQty = sc.nextInt();

                    if (buyQty <= 0) {
                        System.out.println("Quantity must be greater than zero.");
                        break;
                    }

                    if (user.buyStock(market.get(buySymbol), buyQty))
                        System.out.println("Stock Purchased Successfully.");
                    else
                        System.out.println("Insufficient Balance.");

                    break;

                case 3:

                    System.out.print("Enter Stock Symbol: ");
                    String sellSymbol = sc.next().toUpperCase();

                    if (!market.containsKey(sellSymbol)) {
                        System.out.println("Invalid Stock Symbol.");
                        break;
                    }

                    System.out.print("Enter Quantity: ");
                    int sellQty = sc.nextInt();

                    if (user.sellStock(market.get(sellSymbol), sellQty))
                        System.out.println("Stock Sold Successfully.");
                    else
                        System.out.println("Not enough shares.");

                    break;

                case 4:

                    user.showPortfolio(market);

                    break;

                case 5:

                    System.out.println("Available Cash Balance : ₹" + user.getBalance());

                    break;

                case 6:

                    System.out.println("Thank you for using the Stock Trading Platform.");
                    sc.close();
                    System.exit(0);

                default:

                    System.out.println("Invalid Choice!");
            }
        }
    }
}