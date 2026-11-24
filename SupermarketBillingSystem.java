import java.io.*;
import java.util.*;

public class SupermarketBillingSystem {

    static class Customer {
        String name;
        String contactNumber;

        Customer(String name, String contactNumber) {
            this.name = name;
            this.contactNumber = contactNumber;
        }
    }

    static class Product {
        String id;
        String name;
        int price;

        Product(String id, String name, int price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
    }

    static class CartItem {
        String id;
        String name;
        int quantity;
        int unitPrice;

        CartItem(String id, String name, int quantity, int unitPrice) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        int getTotal() {
            return quantity * unitPrice;
        }
    }

    private static final Queue<Customer> billingQueue = new LinkedList<>();
    private static final HashMap<String, Product> priceList = new HashMap<>();
    private static final List<String> billHistory = new ArrayList<>();
    private static final String FILE_NAME = "bills.txt";

    public static void main(String[] args) {
        initializePriceList();
        loadBillsFromFile();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== SUPERMARKET BILLING SYSTEM ===");
            System.out.println("1. Add customer");
            System.out.println("2. Bill next customer");
            System.out.println("3. View queue");
            System.out.println("4. Exit");
            System.out.print("Enter option: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    addCustomer(sc);
                    break;
                case "2":
                    billNextCustomer(sc);
                    break;
                case "3":
                    viewQueue();
                    break;
                case "4":
                    saveBillsToFile();
                    System.out.println("Exiting application.");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid option. Please enter 1-4.");
            }
        }
    }

    private static void initializePriceList() {
        String[] ids = {"101", "102", "103"};
        String[] items = {"Rice", "Milk", "Bread"};
        int[] prices = {60, 30, 40};

        for (int i = 0; i < ids.length; i++) {
            priceList.put(ids[i], new Product(ids[i], items[i], prices[i]));
        }
    }

    private static void addCustomer(Scanner sc) {
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Customer name cannot be empty.");
            return;
        }

        System.out.print("Contact Number: ");
        String contact = sc.nextLine().trim();
        if (contact.isEmpty()) {
            System.out.println("Contact number cannot be empty.");
            return;
        }

        billingQueue.add(new Customer(name, contact));
        System.out.println(name + " added to billing queue. Position: " + billingQueue.size());
    }

    private static void billNextCustomer(Scanner sc) {
        if (billingQueue.isEmpty()) {
            System.out.println("Billing queue is empty. No customer to bill.");
            return;
        }

        Customer customer = billingQueue.poll();
        Stack<CartItem> cartStack = new Stack<>();
        System.out.println("\nBilling customer: " + customer.name + " (Contact: " + customer.contactNumber + ")");
        System.out.println("Available items: [101] Rice (60) | [102] Milk (30) | [103] Bread (40)");

        while (true) {
            System.out.print("Enter Item ID (or 'Undo' / 'Done'): ");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("Done")) {
                break;
            } else if (input.equalsIgnoreCase("Undo") || input.equalsIgnoreCase("Undo last item")) {
                undoLastItem(cartStack);
            } else {
                if (!priceList.containsKey(input)) {
                    System.out.println("Item ID " + input + " not found in price list.");
                } else {
                    System.out.print("Quantity: ");
                    int qty;
                    try {
                        qty = Integer.parseInt(sc.nextLine().trim());
                        if (qty <= 0) {
                            System.out.println("Quantity must be greater than 0.");
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity.");
                        continue;
                    }

                    Product product = priceList.get(input);
                    cartStack.push(new CartItem(product.id, product.name, qty, product.price));
                    System.out.println("Added: [" + product.id + "] " + product.name + " x" + qty);
                }
            }
        }

        generateAndRecordBill(customer, cartStack);
    }

    private static void undoLastItem(Stack<CartItem> cartStack) {
        if (cartStack.isEmpty()) {
            System.out.println("No items in cart to undo.");
        } else {
            CartItem removed = cartStack.pop();
            System.out.println("Last item " + removed.name + " removed.");
        }
    }

    private static void generateAndRecordBill(Customer customer, Stack<CartItem> cartStack) {
        int grandTotal = 0;
        StringBuilder itemsSummary = new StringBuilder();

        CartItem[] items = cartStack.toArray(new CartItem[0]);

        System.out.println("\n=============================================");
        System.out.println("              SUPERMARKET BILL               ");
        System.out.println("=============================================");
        System.out.println("Customer Name  : " + customer.name);
        System.out.println("Contact Number : " + customer.contactNumber);
        System.out.println("---------------------------------------------");
        System.out.printf("%-6s %-12s %-8s %-6s %-8s%n", "ID", "Item", "Price", "Qty", "Total");
        System.out.println("---------------------------------------------");

        if (items.length == 0) {
            System.out.println("No items purchased.");
        } else {
            for (int i = 0; i < items.length; i++) {
                int itemTotal = items[i].getTotal();
                grandTotal += itemTotal;

                System.out.printf("%-6s %-12s %-8d %-6d %-8d%n",
                        items[i].id,
                        items[i].name,
                        items[i].unitPrice,
                        items[i].quantity,
                        itemTotal);

                itemsSummary.append(items[i].name)
                            .append(" x")
                            .append(items[i].quantity)
                            .append(" = ")
                            .append(itemTotal);
                if (i < items.length - 1) {
                    itemsSummary.append(", ");
                }
            }
        }

        System.out.println("---------------------------------------------");
        System.out.printf("GRAND TOTAL :                       Rs. %d%n", grandTotal);
        System.out.println("=============================================");

        String billRecord;
        if (items.length > 0) {
            billRecord = "Bill for " + customer.name + " (" + customer.contactNumber + "): " + itemsSummary + " | Total " + grandTotal;
        } else {
            billRecord = "Bill for " + customer.name + " (" + customer.contactNumber + "): Total 0";
        }

        System.out.println(billRecord);
        billHistory.add(billRecord);

        if (billingQueue.isEmpty()) {
            System.out.println("Queue is now empty.");
        }
    }

    private static void viewQueue() {
        if (billingQueue.isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }
        int position = 1;
        for (Customer customer : billingQueue) {
            System.out.println(position + ". " + customer.name + " (" + customer.contactNumber + ")");
            position++;
        }
    }

    private static void loadBillsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    billHistory.add(line);
                }
            }
            System.out.println("Loaded " + billHistory.size() + " previous bill(s) from " + FILE_NAME + ".");
        } catch (IOException e) {
            System.out.println("Error loading bills from file: " + e.getMessage());
        }
    }

    private static void saveBillsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String record : billHistory) {
                writer.write(record);
                writer.newLine();
            }
            System.out.println("All bills saved to " + FILE_NAME + ".");
        } catch (IOException e) {
            System.out.println("Error saving bills to file: " + e.getMessage());
        }
    }
}
