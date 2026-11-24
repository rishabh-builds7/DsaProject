# 🛒 Supermarket Billing System

A simple **Java console application** that handles supermarket billing. Customers wait in a queue, the cashier scans items, prices are found instantly, mistakes can be undone, and every bill is saved in a file.

**PS No.:** 34  
**Author:** Rishabh Kumar Sharma

---

## ✨ Features

- Add customers to a billing queue (first come, first served)
- Bill the next customer and scan items one by one
- Fast price lookup for every item
- **Undo** the last scanned item if the cashier makes a mistake
- View who is waiting in the queue
- Bills are **saved to `bills.txt`** when you exit and **loaded again** when you start
- Input checks: empty names, unknown items, and wrong quantities are rejected

## 🧰 Concepts Used

| Concept | Where it is used |
|---|---|
| Queue (`LinkedList`) | Customers waiting at checkout |
| Hashing (`HashMap`) | Item name → price |
| Stack | Scanned items, so the last one can be undone |
| Array | Shop catalog and receipt items |
| Modular methods | Each task has its own method |
| File handling | `BufferedReader` and `BufferedWriter` with `bills.txt` |

## 💰 Price List

| Item | Price |
|---|---|
| Rice | 60 |
| Milk | 30 |
| Bread | 40 |

Item names are not case sensitive (`rice`, `RICE` and `Rice` all work).

## 🧠 Data Structures: What and Why

| Structure | Java type | Why | Speed |
|---|---|---|---|
| Queue | `LinkedList<String>` | Like a line at the counter. First in, first out (FIFO). Billing is blocked when the queue is empty. | O(1) add / remove |
| HashMap | `HashMap<String, Integer>` | Like a price list. Finds any price at once and catches wrong items. | O(1) average lookup |
| Stack | `Stack<CartItem>` | Like a pile of plates. Last in, first out (LIFO), so Undo is easy. | O(1) push / pop |
| Array | `String[]`, `int[]`, `CartItem[]` | Stores the fixed catalog, and prints the bill in scanned order. | O(n) to go through items |

## ⚙️ How It Works

1. **Start:** load the price list and old bills from `bills.txt`
2. **Menu:** choose an option
3. **Check:** every input is checked
4. **Receipt:** the bill is printed and kept
5. **Exit:** all bills are saved to `bills.txt`

### Main methods

| Method | What it does |
|---|---|
| `addCustomer()` | Adds a customer to the queue |
| `billNextCustomer()` | Scans items and finds prices |
| `undoLastItem()` | Removes the last scanned item |
| `viewQueue()` | Shows who is waiting |
| `loadBillsFromFile()` | Reads old bills at start |
| `saveBillsToFile()` | Saves all bills at exit |

## ▶️ How to Run

You need the **JDK 8 or higher** installed.

```bash
git clone <your-repo-url>
cd <your-repo-folder>
javac SupermarketBillingSystem.java
java SupermarketBillingSystem
```

## 📋 Menu

```
=== SUPERMARKET BILLING SYSTEM ===
1. Add customer
2. Bill next customer
3. View queue
4. Exit
Enter option:
```

While billing, type an **item name**, `Undo`, or `Done`.

## 🧪 Test Cases and Sample Output

### Test Case 1: Normal input

```
Enter option: 1
Name: Riya
Riya added to billing queue. Position: 1
```

### Test Case 2: Smallest input (1 item)

```
Enter option: 2
Billing customer: Riya
Enter Item name (or 'Undo' / 'Done'): Rice
Quantity: 1
Added: Rice x1
Enter Item name (or 'Undo' / 'Done'): Done
Bill for Riya: Rice x1 = 60 | Total 60
Queue is now empty.
```

### Test Case 3: Invalid (wrong) item

```
Enter Item name (or 'Undo' / 'Done'): Chocolate
Item Chocolate not found in price list.
```

### Test Case 4: Many steps (queue has Riya, Aman)

```
Enter Item name (or 'Undo' / 'Done'): Undo
Last item Bread removed.
Enter Item name (or 'Undo' / 'Done'): Done
Bill for Riya: Milk x2 = 60 | Total 60
Enter option: 3
1. Aman
```

(Before the undo, Riya's cart had `Milk x2` and `Bread x1`.)

## 💾 File Handling

- **Start:** `BufferedReader` reads `bills.txt` and loads old bills
- **Billing:** new bills are kept in memory
- **Exit:** `BufferedWriter` saves all bills to `bills.txt`
- **Next run:** old bills are still there

`bills.txt` is created automatically the first time you choose **Exit**. Each line is one bill:

```
Bill for Riya: Milk x2 = 60 | Total 60
```

> ⚠️ Bills are saved only when you choose **4. Exit**. If you close the program in another way, the bills from that session are lost.

## 📁 Project Structure

```
.
├── SupermarketBillingSystem.java   # Full source code
├── bills.txt                       # Created on first exit
└── README.md
```

## 🚀 Future Enhancements

- Discount coupons
- Stock (inventory) tracking
- Simple screen (GUI)
- Save the bill to the file after every customer

## 👤 Author

**Rishabh Kumar Sharma**
