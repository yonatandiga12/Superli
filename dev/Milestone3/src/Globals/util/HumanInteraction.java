package Globals.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Static functions for human interactions
 */
public class HumanInteraction {
    public static class OperationCancelledException extends Exception {
    }

    public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static final Scanner scanner = new Scanner(System.in);

    public static void operationCancelled() throws OperationCancelledException {
        System.out.println("Operation Canceled");
        throw new OperationCancelledException();
    }

    public static boolean areYouSure() {
        System.out.println("Are you sure?");
        return yesOrNo();
    }

    public static boolean yesOrNo() {
        int ans = 0;
        while (ans != 1 && ans != 2) {
            System.out.println("1 -- yes\n2 -- no");
            try {
                ans = Integer.parseInt(getInput());
            } catch (NumberFormatException ex) {
                System.out.println("Please enter an integer value (1 or 2)");
            } catch (Exception ex) {
                System.out.println("An unexpected error happened. Please try again");
            }
        }
        return ans == 1;
    }

    public static LocalDate buildDate() throws OperationCancelledException {
        while (true) {
            System.out.println("Enter day");
            int day = getNumber(1, 31);
            System.out.println("Enter month");
            int month = getNumber(1, 12);
            System.out.println("Enter year");
            int year = getNumber(2000);
            try {
                return LocalDate.of(year, month, day);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again");
            }
        }
    }

    public static int getNumber() throws OperationCancelledException {
        while (true) {
            try {
                int number = Integer.parseInt(getInput());
                if (number == -1) {
                    operationCancelled();
                } else {
                    return number;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Please enter an integer");
            } catch (OperationCancelledException e) {
                throw e;
            } catch (Exception ex) {
                System.out.println("Unexpected error occurred");
                System.out.println("Please try again");
            }
        }
    }

    public static int getNumber(int min) throws OperationCancelledException {
        while (true) {
            try {
                int number = Integer.parseInt(getInput());
                if (number == -1) {
                    operationCancelled();
                } else if (number < min) {
                    System.out.println("Please enter an integer bigger or equal to " + min);
                } else {
                    return number;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Please enter an integer");
            } catch (OperationCancelledException e) {
                throw e;
            } catch (Exception ex) {
                System.out.println("Unexpected error occurred");
                System.out.println("Please try again");
            }
        }
    }

    public static int getNumber(int min, int max) throws OperationCancelledException {
        while (true) {
            try {
                int number = Integer.parseInt(getInput());
                if (number == -1) {
                    operationCancelled();
                } else if (number < min || number > max) {
                    System.out.println("Please enter an integer between " + min + " and " + max);
                } else {
                    return number;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Please enter an integer");
            } catch (OperationCancelledException e) {
                throw e;
            } catch (Exception ex) {
                System.out.println("Unexpected error occurred");
                System.out.println("Please try again");
            }
        }
    }

    public static int getNumber(int min, String because) throws OperationCancelledException {
        while (true) {
            try {
                int number = Integer.parseInt(getInput());
                if (number == -1) {
                    operationCancelled();
                } else if (number < min) {
                    System.out.println("Please enter an integer bigger or equal to " + min + " because: " + because);
                } else {
                    return number;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Please enter an integer");
            } catch (OperationCancelledException e) {
                throw e;
            } catch (Exception ex) {
                System.out.println("Unexpected error occurred");
                System.out.println("Please try again");
            }
        }
    }

    public static String getString() throws OperationCancelledException {
        while (true) {
            try {
                String string = getInput();
                if (string.equals("-1"))
                    operationCancelled();
                else
                    return string;
            }
            catch (OperationCancelledException e){
                throw e;
            }
            catch (Exception e) {
                System.out.println("Unexpected error occurred");
                System.out.println("Please try again");
            }
        }
    }

    private static String getInput() {
        return scanner.nextLine().trim();
    }
}