package com.web.client;

import DataModel.EventModel;
import Logger.Logger;
import com.web.service.WebInterface;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.Scanner;

public class Client {
    public static final int USER_TYPE_CUSTOMER = 1;
    public static final int USER_TYPE_MANAGER = 2;
    public static final int CUSTOMER_BOOK_EVENT = 1;
    public static final int CUSTOMER_GET_BOOKING_SCHEDULE = 2;
    public static final int CUSTOMER_CANCEL_EVENT = 3;
    public static final int CUSTOMER_SWAP_EVENT = 4;
    public static final int CUSTOMER_LOGOUT = 5;
    public static final int MANAGER_ADD_EVENT = 1;
    public static final int MANAGER_REMOVE_EVENT = 2;
    public static final int MANAGER_LIST_EVENT_AVAILABILITY = 3;
    public static final int MANAGER_BOOK_EVENT = 4;
    public static final int MANAGER_GET_BOOKING_SCHEDULE = 5;
    public static final int MANAGER_CANCEL_EVENT = 6;
    public static final int MANAGER_SWAP_EVENT = 7;
    public static final int MANAGER_LOGOUT = 8;
//    public static final int SHUTDOWN = 0;
    public static Service montrealService;
    public static Service sherbrookService;
    public static Service quebecService;
    private static WebInterface obj;

    static Scanner input;

    public static void main(String[] args) throws Exception {
        URL montrealURL = new URL("http://localhost:8080/montreal?wsdl");
        QName montrealQName = new QName("http://implementaion.service.web.com/", "EventManagementService");
        montrealService = Service.create(montrealURL, montrealQName);

        URL quebecURL = new URL("http://localhost:8080/quebec?wsdl");
        QName quebecQName = new QName("http://implementaion.service.web.com/", "EventManagementService");
        quebecService = Service.create(quebecURL, quebecQName);

        URL sherbrookURL = new URL("http://localhost:8080/sherbrook?wsdl");
        QName sherbrookQName = new QName("http://implementaion.service.web.com/", "EventManagementService");
        sherbrookService = Service.create(sherbrookURL, sherbrookQName);
        init();
    }

    public static void init() throws Exception {
        input = new Scanner(System.in);
        String userID;
        System.out.println("*************************************");
        System.out.println("*************************************");
        System.out.println("Please Enter your UserID(For Concurrency test enter 'ConTest'):");
        userID = input.next().trim().toUpperCase();
        if (userID.equalsIgnoreCase("ConTest")) {
            startConcurrencyTest();
        } else {
            Logger.clientLog(userID, " login attempt");
            switch (checkUserType(userID)) {
                case USER_TYPE_CUSTOMER:
                    try {
                        System.out.println("Customer Login successful (" + userID + ")");
                        Logger.clientLog(userID, " Customer Login successful");
                        customer(userID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case USER_TYPE_MANAGER:
                    try {
                        System.out.println("Manager Login successful (" + userID + ")");
                        Logger.clientLog(userID, " Manager Login successful");
                        manager(userID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("!!UserID is not in correct format");
                    Logger.clientLog(userID, " UserID is not in correct format");
                    Logger.deleteALogFile(userID);
                    init();
            }
        }
    }

    private static void startConcurrencyTest() throws Exception {
        System.out.println("Concurrency Test Starting for BookEvent");
        System.out.println("Connecting Montreal Server...");
        String eventType = EventModel.CONFERENCES;
        String eventID = "MTLE101020";
        WebInterface servant = montrealService.getPort(WebInterface.class);
        System.out.println("adding " + eventID + " " + eventType + " with capacity 2 to Montreal Server...");
        String response = servant.addEvent(eventID, eventType, 2);
        System.out.println(response);
        Runnable task1 = () -> {
            String customerID = "MTLC2345";
//            System.out.println("Connecting Montreal Server for " + customerID);
            String res = servant.bookEvent(customerID, eventID, eventType);
            System.out.println("Booking response for " + customerID + " " + res);
            res = servant.cancelEvent(customerID, eventID, eventType);
            System.out.println("Canceling response for " + customerID + " " + res);
        };
        Runnable task2 = () -> {
            String customerID = "MTLC3456";
//            System.out.println("Connecting Montreal Server for " + customerID);
            String res = servant.bookEvent(customerID, eventID, eventType);
            System.out.println("Booking response for " + customerID + " " + res);
            res = servant.cancelEvent(customerID, eventID, eventType);
            System.out.println("Canceling response for " + customerID + " " + res);
        };
        Runnable task3 = () -> {
            String customerID = "MTLC4567";
//            System.out.println("Connecting Montreal Server for " + customerID);
            String res = servant.bookEvent(customerID, eventID, eventType);
            System.out.println("Booking response for " + customerID + " " + res);
            res = servant.cancelEvent(customerID, eventID, eventType);
            System.out.println("Canceling response for " + customerID + " " + res);
        };
        Runnable task4 = () -> {
            String customerID = "MTLC6789";
//            System.out.println("Connecting Montreal Server for " + customerID);
            String res = servant.bookEvent(customerID, eventID, eventType);
            System.out.println("Booking response for " + customerID + " " + res);
            res = servant.cancelEvent(customerID, eventID, eventType);
            System.out.println("Canceling response for " + customerID + " " + res);
        };
        Runnable task5 = () -> {
            String customerID = "MTLC7890";
//            System.out.println("Connecting Montreal Server for " + customerID);
            String res = servant.bookEvent(customerID, eventID, eventType);
            System.out.println("Booking response for " + customerID + " " + res);
            res = servant.cancelEvent(customerID, eventID, eventType);
            System.out.println("Canceling response for " + customerID + " " + res);
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        Thread thread3 = new Thread(task3);
        Thread thread4 = new Thread(task4);
        Thread thread5 = new Thread(task5);
//        synchronized (thread1) {
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
//        }
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        thread5.join();
//        if (!thread1.isAlive() && !thread2.isAlive() && !thread3.isAlive() && !thread4.isAlive() && !thread5.isAlive()) {
        System.out.println("Concurrency Test Finished for BookEvent");
        init();
//        }
    }

    private static String getServerID(String userID) {
        String branchAcronym = userID.substring(0, 3);
        if (branchAcronym.equalsIgnoreCase("MTL")) {
            obj = montrealService.getPort(WebInterface.class);
            return branchAcronym;
        } else if (branchAcronym.equalsIgnoreCase("SHE")) {
            obj = sherbrookService.getPort(WebInterface.class);
            return branchAcronym;
        } else if (branchAcronym.equalsIgnoreCase("QUE")) {
            obj = quebecService.getPort(WebInterface.class);
            return branchAcronym;
        }
        return "1";
    }

    private static int checkUserType(String userID) {
        if (userID.length() == 8) {
            if (userID.substring(0, 3).equalsIgnoreCase("MTL") ||
                    userID.substring(0, 3).equalsIgnoreCase("QUE") ||
                    userID.substring(0, 3).equalsIgnoreCase("SHE")) {
                if (userID.substring(3, 4).equalsIgnoreCase("C")) {
                    return USER_TYPE_CUSTOMER;
                } else if (userID.substring(3, 4).equalsIgnoreCase("M")) {
                    return USER_TYPE_MANAGER;
                }
            }
        }
        return 0;
    }

    private static void customer(String customerID) throws Exception {
        String serverID = getServerID(customerID);
        if (serverID.equals("1")) {
            init();
        }
        boolean repeat = true;
        printMenu(USER_TYPE_CUSTOMER);
        int menuSelection = input.nextInt();
        String eventType;
        String eventID;
        String serverResponse;
        switch (menuSelection) {
            case CUSTOMER_BOOK_EVENT:
                eventType = promptForEventType();
                eventID = promptForEventID();
                Logger.clientLog(customerID, " attempting to bookEvent");
                serverResponse = obj.bookEvent(customerID, eventID, eventType);
                System.out.println(serverResponse);
                Logger.clientLog(customerID, " bookEvent", " eventID: " + eventID + " eventType: " + eventType + " ", serverResponse);
                break;
            case CUSTOMER_GET_BOOKING_SCHEDULE:
                Logger.clientLog(customerID, " attempting to getBookingSchedule");
                serverResponse = obj.getBookingSchedule(customerID);
                System.out.println(serverResponse);
                Logger.clientLog(customerID, " bookEvent", " null ", serverResponse);
                break;
            case CUSTOMER_CANCEL_EVENT:
                eventType = promptForEventType();
                eventID = promptForEventID();
                Logger.clientLog(customerID, " attempting to cancelEvent");
                serverResponse = obj.cancelEvent(customerID, eventID, eventType);
                System.out.println(serverResponse);
                Logger.clientLog(customerID, " bookEvent", " eventID: " + eventID + " eventType: " + eventType + " ", serverResponse);
                break;
            case CUSTOMER_SWAP_EVENT:
                System.out.println("Please Enter the OLD event to be replaced");
                eventType = promptForEventType();
                eventID = promptForEventID();
                System.out.println("Please Enter the NEW event to be replaced");
                String newEventType = promptForEventType();
                String newEventID = promptForEventID();
                Logger.clientLog(customerID, " attempting to swapEvent");
                serverResponse = obj.swapEvent(customerID, newEventID, newEventType, eventID, eventType);
                System.out.println(serverResponse);
                Logger.clientLog(customerID, " swapEvent", " oldEventID: " + eventID + " oldEventType: " + eventType + " newEventID: " + newEventID + " newEventType: " + newEventType + " ", serverResponse);
                break;
            case CUSTOMER_LOGOUT:
                repeat = false;
                Logger.clientLog(customerID, " attempting to Logout");
                init();
                break;
        }
        if (repeat) {
            customer(customerID);
        }
    }

    private static void manager(String eventManagerID) throws Exception {
        String serverID = getServerID(eventManagerID);
        if (serverID.equals("1")) {
            init();
        }
        boolean repeat = true;
        printMenu(USER_TYPE_MANAGER);
        String customerID;
        String eventType;
        String eventID;
        String serverResponse;
        int capacity;
        int menuSelection = input.nextInt();
        switch (menuSelection) {
            case MANAGER_ADD_EVENT:
                eventType = promptForEventType();
                eventID = promptForEventID();
                capacity = promptForCapacity();
                Logger.clientLog(eventManagerID, " attempting to addEvent");
                serverResponse = obj.addEvent(eventID, eventType, capacity);
                System.out.println(serverResponse);
                Logger.clientLog(eventManagerID, " addEvent", " eventID: " + eventID + " eventType: " + eventType + " eventCapacity: " + capacity + " ", serverResponse);
                break;
            case MANAGER_REMOVE_EVENT:
                eventType = promptForEventType();
                eventID = promptForEventID();
                Logger.clientLog(eventManagerID, " attempting to removeEvent");
                serverResponse = obj.removeEvent(eventID, eventType);
                System.out.println(serverResponse);
                Logger.clientLog(eventManagerID, " removeEvent", " eventID: " + eventID + " eventType: " + eventType + " ", serverResponse);
                break;
            case MANAGER_LIST_EVENT_AVAILABILITY:
                eventType = promptForEventType();
                Logger.clientLog(eventManagerID, " attempting to listEventAvailability");
                serverResponse = obj.listEventAvailability(eventType);
                System.out.println(serverResponse);
                Logger.clientLog(eventManagerID, " listEventAvailability", " eventType: " + eventType + " ", serverResponse);
                break;
            case MANAGER_BOOK_EVENT:
                customerID = askForCustomerIDFromManager(eventManagerID.substring(0, 3));
                eventType = promptForEventType();
                eventID = promptForEventID();
                Logger.clientLog(eventManagerID, " attempting to bookEvent");
                serverResponse = obj.bookEvent(customerID, eventID, eventType);
                System.out.println(serverResponse);
                Logger.clientLog(eventManagerID, " bookEvent", " customerID: " + customerID + " eventID: " + eventID + " eventType: " + eventType + " ", serverResponse);
                break;
            case MANAGER_GET_BOOKING_SCHEDULE:
                customerID = askForCustomerIDFromManager(eventManagerID.substring(0, 3));
                Logger.clientLog(eventManagerID, " attempting to getBookingSchedule");
                serverResponse = obj.getBookingSchedule(customerID);
                System.out.println(serverResponse);
                Logger.clientLog(eventManagerID, " getBookingSchedule", " customerID: " + customerID + " ", serverResponse);
                break;
            case MANAGER_CANCEL_EVENT:
                customerID = askForCustomerIDFromManager(eventManagerID.substring(0, 3));
                eventType = promptForEventType();
                eventID = promptForEventID();
                Logger.clientLog(eventManagerID, " attempting to cancelEvent");
                serverResponse = obj.cancelEvent(customerID, eventID, eventType);
                System.out.println(serverResponse);
                Logger.clientLog(eventManagerID, " cancelEvent", " customerID: " + customerID + " eventID: " + eventID + " eventType: " + eventType + " ", serverResponse);
                break;
            case MANAGER_SWAP_EVENT:
                customerID = askForCustomerIDFromManager(eventManagerID.substring(0, 3));
                System.out.println("Please Enter the OLD event to be swapped");
                eventType = promptForEventType();
                eventID = promptForEventID();
                System.out.println("Please Enter the NEW event to be swapped");
                String newEventType = promptForEventType();
                String newEventID = promptForEventID();
                Logger.clientLog(eventManagerID, " attempting to swapEvent");
                serverResponse = obj.swapEvent(customerID, newEventID, newEventType, eventID, eventType);
                System.out.println(serverResponse);
                Logger.clientLog(eventManagerID, " swapEvent", " customerID: " + customerID + " oldEventID: " + eventID + " oldEventType: " + eventType + " newEventID: " + newEventID + " newEventType: " + newEventType + " ", serverResponse);
                break;
            case MANAGER_LOGOUT:
                repeat = false;
                Logger.clientLog(eventManagerID, "attempting to Logout");
                init();
                break;
        }
        if (repeat) {
            manager(eventManagerID);
        }
    }

    private static String askForCustomerIDFromManager(String branchAcronym) {
        System.out.println("Please enter a customerID(Within " + branchAcronym + " Server):");
        String userID = input.next().trim().toUpperCase();
        if (checkUserType(userID) != USER_TYPE_CUSTOMER || !userID.substring(0, 3).equals(branchAcronym)) {
            return askForCustomerIDFromManager(branchAcronym);
        } else {
            return userID;
        }
    }

    private static void printMenu(int userType) {
        System.out.println("*************************************");
        System.out.println("Please choose an option below:");
        if (userType == USER_TYPE_CUSTOMER) {
            System.out.println("1.Book Event");
            System.out.println("2.Get Booking Schedule");
            System.out.println("3.Cancel Event");
            System.out.println("4.Swap Event");
            System.out.println("5.Logout");
        } else if (userType == USER_TYPE_MANAGER) {
            System.out.println("1.Add Event");
            System.out.println("2.Remove Event");
            System.out.println("3.List Event Availability");
            System.out.println("4.Book Event");
            System.out.println("5.Get Booking Schedule");
            System.out.println("6.Cancel Event");
            System.out.println("7.Swap Event");
            System.out.println("8.Logout");
        }
    }

    private static String promptForEventType() {
        System.out.println("*************************************");
        System.out.println("Please choose an eventType below:");
        System.out.println("1.Conferences");
        System.out.println("2.Seminars");
        System.out.println("3.Trade Shows");
        switch (input.nextInt()) {
            case 1:
                return EventModel.CONFERENCES;
            case 2:
                return EventModel.SEMINARS;
            case 3:
                return EventModel.TRADE_SHOWS;
        }
        return promptForEventType();
    }

    private static String promptForEventID() {
        System.out.println("*************************************");
        System.out.println("Please enter the EventID (e.g MTLM190120)");
        String eventID = input.next().trim().toUpperCase();
        if (eventID.length() == 10) {
            if (eventID.substring(0, 3).equalsIgnoreCase("MTL") ||
                    eventID.substring(0, 3).equalsIgnoreCase("SHE") ||
                    eventID.substring(0, 3).equalsIgnoreCase("QUE")) {
                if (eventID.substring(3, 4).equalsIgnoreCase("M") ||
                        eventID.substring(3, 4).equalsIgnoreCase("A") ||
                        eventID.substring(3, 4).equalsIgnoreCase("E")) {
                    return eventID;
                }
            }
        }
        return promptForEventID();
    }

    private static int promptForCapacity() {
        System.out.println("*************************************");
        System.out.println("Please enter the booking capacity:");
        return input.nextInt();
    }
}
