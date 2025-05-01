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
            System.out.println("6) Custom Search");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
                    filterTransactionsByDate(firstDayOfMonth, LocalDate.now());
                    //This allows user to search by month to date
                    break;
                case "2":
                    LocalDate previousMonth = LocalDate.now().minusMonths(1);
                    filterTransactionsByDate(previousMonth, LocalDate.now());
                    // This allows user to user the previous month searching feature
                    break;
                case "3":
                    LocalDate YearToDate = LocalDate.now().withDayOfYear(1);
                    filterTransactionsByDate(YearToDate, LocalDate.now());
                    // This allows user to use the Year to Date search feature
                    break;
                case "4":
                    LocalDate PreviousYear = LocalDate.now().minusYears(1);
                    filterTransactionsByDate(PreviousYear, LocalDate.now());
                    // This allows user to search transaction from the previous year till now
                    break;
                case "5":
                    Scanner myscanner = new Scanner(System.in);
                    System.out.println("Please enter vendor: ");
                    String vendorInput = myscanner.nextLine();
                    filterTransactionsByVendor(vendorInput);
                    // This allows user to use the search by vendor option
                    break;
                case "6":
                    customSearch(transactions,scanner);
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
    //This Method allows user to use the search by start date and end date. for cases 1-4
    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        for (Transaction transaction : transactions) {
            if (!transaction.getDate().isBefore(startDate) && !transaction.getDate().isAfter(endDate)) {
                System.out.println(transaction);
            }

        }
    }
        //This method allows user to search a transaction by vendor
        private static void filterTransactionsByVendor(String vendorName) {
            for (Transaction transaction : transactions) {
                if (transaction.getVendor().equalsIgnoreCase(vendorName)) {
                    System.out.println(transaction);
                }
            }

        }
        //This is muy attempt at creating a custom search method
        private static void customSearch(ArrayList<Transaction> transactions,Scanner scanner){

            try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME));
                //Create a scanner to receive user input
                //Prompt user would the would like to search by
                System.out.println("Enter start date(yyyy-MM-dd): ");
                String start = scanner.nextLine();
                LocalDate startDate = LocalDate.parse(start, DATE_FORMATTER);
                System.out.println("Enter end date(yyyy-MM-dd): ");
                String end = scanner.nextLine();
                LocalDate endDate = LocalDate.parse(end, DATE_FORMATTER);
                System.out.println("Enter description, if you would like to search by description: ");
                String descriptionInput = scanner.nextLine();
                System.out.println("Enter vendor, if you would like to search by vendor: ");
                String vendorInput = scanner.nextLine();
                System.out.println("Enter amount, if you would like to search by amount: ");
                String amount = scanner.nextLine();
                double amountInput = Double.parseDouble(amount);
                scanner.nextLine();

                //Create a for each statement that checks what user is searching by and print appropriate transactions

                for (Transaction transaction : transactions) {
                    boolean found = true;
                    if (!transaction.getDate().isBefore(startDate)&& !transaction.getDate().isAfter(endDate)){
                        System.out.println("Your dates have been saved.");
                    }
                    if (!transaction.getDescription().equalsIgnoreCase(descriptionInput)&& !descriptionInput.isEmpty()) {
                        found = false;
                    }
                    if (!transaction.getVendor().equalsIgnoreCase(vendorInput)&& !vendorInput.isEmpty()) {
                        found = false;
                    }
                    if (transaction.getAmount() != amountInput && amountInput.isEmpty) {
                        found = false;
                    }
                    if (found){
                        System.out.println("Transactions matching your search: "+transaction);
                    }

                }bufferedReader.close();
            } catch (Exception e) {
                System.out.println("An error has occurred");
            }


        }

}

