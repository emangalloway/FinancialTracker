package com.pluralsight;

import javax.sound.midi.Soundbank;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String fileName) {
        String line;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0]);
                LocalTime time = LocalTime.parse(parts[1]);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);
                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                transactions.add(transaction);
            }
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println("An error has occurred: "+e);
        }
        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>|<time>|<description>|<vendor>|<amount>
        // For example: 2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.
    }

    private static void addDeposit(Scanner scanner) {
        //Create buffered writer
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true));

            //Asking user to fill in information about deposit
            System.out.println("Enter Date (yyyy-MM-dd): ");
            String date = scanner.nextLine();
            LocalDate dateInput = LocalDate.parse(date, DATE_FORMATTER);//Auto changes user date input to yyyy-MM-dd format
            System.out.println("Enter time (HH:mm:ss): ");
            String time = scanner.nextLine();
            LocalTime timeInput = LocalTime.parse(time, TIME_FORMATTER);//Auto changes user time input to HH:mm:ss format
            System.out.println("Enter Description: ");
            String description = scanner.nextLine();
            System.out.println("Enter Vendor: ");
            String vendor = scanner.nextLine();
            System.out.println("Enter Amount: ");
            double amountOfDeposit = scanner.nextDouble();
            scanner.nextLine();//Consume

            //Create new transaction and add it to list
            Transaction transaction = new Transaction(dateInput, timeInput, description, vendor, amountOfDeposit);
            System.out.println("Successful deposit: " + transaction);
            transactions.add(transaction);

            //Append to file

            bufferedWriter.write(transaction.toString());
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (Exception e) {
            System.out.println("An error has occurred");
        }
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.
    }

    private static void addPayment(Scanner scanner) {
        //Create buffered writer
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true));

            //Asking user to fill in information about deposit
            System.out.println("Enter Date (yyyy-MM-dd): ");
            String date = scanner.nextLine();
            LocalDate dateInput = LocalDate.parse(date, DATE_FORMATTER);//Auto changes user date input to yyyy-MM-dd format
            System.out.println("Enter time (HH:mm:ss): ");
            String time = scanner.nextLine();
            LocalTime timeInput = LocalTime.parse(time, TIME_FORMATTER);//Auto changes user time input to HH:mm:ss format
            System.out.println("Enter Description: ");
            String description = scanner.nextLine();
            System.out.println("Enter Vendor: ");
            String vendor = scanner.nextLine();
            System.out.println("Enter Amount: ");
            double amountOfPayment = scanner.nextDouble();
            scanner.nextLine();//Consume

            //Change amount to negative
            if (amountOfPayment <= 0) {
                amountOfPayment *= -1;
            }
            //Create new transaction and add it to list
            Transaction transaction = new Transaction(dateInput, timeInput, description, vendor, amountOfPayment);
            System.out.println("Successful payment: " + transaction);
            transactions.add(transaction);

            //Append to file
            bufferedWriter.write(transaction.toString());
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (Exception e) {
            System.out.println("An error has occurred");
        }
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) A`ll");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        for (Transaction transaction : transactions) {
            System.out.println(" ");
            System.out.println(transaction);
        }
    }
    // This method should display a table of all transactions in the `transactions` ArrayList.
    // The table should have columns for date, time, description, vendor, and amount.

    private static void displayDeposits() {
        for (Transaction transaction : transactions) {
            System.out.println(" ");
            if (transaction.getAmount() > 0)
                System.out.println(transaction);
        }
    }
    // This method should display a table of all deposits in the `transactions` ArrayList.
    // The table should have columns for date, time, description, vendor, and amount.

    private static void displayPayments() {
        for (Transaction transaction : transactions) {
            System.out.println(" ");
            if (transaction.getAmount() < 0)
                System.out.println(transaction);
        }
    }
    // This method should display a table of all payments in the `transactions` ArrayList.
    // The table should have columns for date, time, description, vendor, and amount.

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
                    filterTransactionsByDate(firstDayOfMonth, LocalDate.now());
                    // Generate a report for all transactions within the current month,
                    // including the date, time, description, vendor, and amount for each transaction.
                    break;
                case "2":
                    LocalDate previousMonth = LocalDate.now().minusMonths(1);
                    filterTransactionsByDate(previousMonth, LocalDate.now());
                    // Generate a report for all transactions within the previous month,
                    // including the date, time, description, vendor, and amount for each transaction.
                    break;
                case "3":
                    LocalDate YearToDate = LocalDate.now().withDayOfYear(1);
                    filterTransactionsByDate(YearToDate, LocalDate.now());
                    // Generate a report for all transactions within the current year,
                    // including the date, time, description, vendor, and amount for each transaction.
                    break;
                case "4":
                    LocalDate PreviousYear = LocalDate.now().minusYears(1);
                    filterTransactionsByDate(PreviousYear, LocalDate.now());
                    // Generate a report for all transactions within the previous year,
                    // including the date, time, description, vendor, and amount for each transaction.
                    break;
                case "5":
                    Scanner myscanner = new Scanner(System.in);
                    System.out.println("Please enter vendor: ");
                    String vendorInput = myscanner.nextLine();
                    filterTransactionsByVendor(vendorInput);
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, time, description, vendor, and amount for each transaction.
                    break;
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    /* private static void displayMonthToDate(){
         LocalDate today = LocalDate.now();
         LocalDate firstDayOfMonth = today.withDayOfMonth(1);
         for (Transaction transaction : transactions) {
             LocalDate transactionDate = transaction.getDate();
             if (transactionDate.isAfter(firstDayOfMonth) && transactionDate.isBefore(today)){
                 System.out.println(transaction);
             }
         }
     }
     private static void displayPreviousMonth(){
         LocalDate today = LocalDate.now();
         LocalDate firstDayOfPreviousMonth = today.minusMonths(1).withDayOfMonth(1);
         LocalDate lastDayOfPreviousMonth = today.withDayOfMonth(1).minusDays(1);
         for (Transaction transaction : transactions) {
             LocalDate transactionDate = transaction.getDate();
             if (transactionDate.isBefore(lastDayOfPreviousMonth) && transactionDate.isAfter(firstDayOfPreviousMonth)){
                 System.out.println(transaction);
             }
         }
     }
     private static void displayYearToDate(){
         LocalDate today = LocalDate.now();
         LocalDate firstDayOfYear = today.withDayOfYear(1);
         for (Transaction transaction : transactions) {
             LocalDate transactionDate = transaction.getDate();
             if (transactionDate.isAfter(firstDayOfYear)&& transactionDate.isBefore(today)){
                 System.out.println(transaction);
             }
         }
     }
     private static void displayPreviousYear(){
         LocalDate today = LocalDate.now();
         LocalDate firstDayOfYear = today.withDayOfYear(1);
         LocalDate firstDayOfPreviousYear = today.minusYears(1);
         for (Transaction transaction : transactions) {
             LocalDate transactionDate = transaction.getDate();
             if (transactionDate.isAfter(firstDayOfPreviousYear) && transactionDate.isBefore(firstDayOfYear)){
                 System.out.println(transaction);
             }
         }
     }*/
    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        for (Transaction transaction : transactions) {
            if (!transaction.getDate().isBefore(startDate) && !transaction.getDate().isAfter(endDate)) {
                System.out.println(transaction);//Search by date
            }

            // This method filters the transactions by date and prints a report to the console.
            // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
            // The method loops through the transactions list and checks each transaction's date against the date range.
            // Transactions that fall within the date range are printed to the console.
            // If no transactions fall within the date range, the method prints a message indicating that there are no results.
        }
    }

        private static void filterTransactionsByVendor(String vendorName) {
            for (Transaction transaction : transactions) {
                if (transaction.getVendor().equalsIgnoreCase(vendorName)) {
                    System.out.println(transaction);
                }
            }

            // This method filters the transactions by vendor and prints a report to the console.
            // It takes one parameter: vendor, which represents the name of the vendor to filter by.
            // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
            // Transactions with a matching vendor name are printed to the console.
            // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
        }

}

