
import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
// import filehandling.WriteToFile;
import java.io.FileWriter;
import java.util.Formatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static String rawCountryCapital = getRandomCountryCapital();
    public static String capital = getCapital(rawCountryCapital).toLowerCase();
    public static String country = getCountry(rawCountryCapital);
    public static ArrayList<String> notInWord = new ArrayList<>();
    public static ArrayList<String> inWord = new ArrayList<>();
    public static ArrayList<String> dashWord = makeDashWord(capital);
    public static int lifes = 10;

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        
        do {
            cls();
            System.out.println("WELCOME TO HANGMAN\n");
            System.out.println(capital + "\n");
            System.out.println(printDashWord(dashWord));
            System.out.println(String.format("\nYour life points: %d \n", lifes));
            System.out.print("Your unguessed letters: " + printDashWord(notInWord) + "\n");
            if (lifes < 3) {
                System.out.println(String.format("I see you need a little help...this is capitol of %s\n", country));
            }
            System.out.print("Enter a letter or guess the answer: ");
            String userInput = scanner.nextLine();
            String answer = password(userInput, dashWord, capital);

            if (answer.equalsIgnoreCase(capital)) {
                cls();
                System.out.println(String.format("\nBravo! :) you guessed the capital of %s:\n", country));
                System.out.println(capital.toUpperCase() + "\n");
                long totalTime = (System.currentTimeMillis() - start) / 1000;
                int totalLetters = (notInWord.size() + inWord.size());
                System.out.println(String.format("You guessed after %d letters, it took you %d seconds.\n",
                        totalLetters, totalTime));
                System.out.print("Enter your name: ");
                String userName = scanner.nextLine();
                writeReadFile(userName, capital, totalLetters, totalTime);
                System.out.println("Do you want to play again? (Y) or press 'Enter' to exit.");
                String userAnswer = scanner.nextLine();
                if (userAnswer.equalsIgnoreCase("Y")) {
                    main(new String[] { "Hello" });
                } else {
                    System.out.println("Thank you and bye.");
                    System.exit(0);
                }
            } else if (userInput.length() > 1 && !answer.equalsIgnoreCase(capital)) {
                lifes -= 2;
            } else if (userInput.length() == 1 && capital.contains(userInput.toLowerCase())) {
                inWord.add(userInput);
                lifes += 0;
            } else {
                notInWord.add(userInput + ", ");
                lifes--;
            }
        } while (lifes > 0);

    }

    static String getRandomCountryCapital() {

        ArrayList<String> lista = new ArrayList<>();

        try {
            File file = new File("capitals.txt");
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                lista.add(line);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            }
            
        int randomIndex = (int) (Math.random() * lista.size());
        return lista.get(randomIndex);
            
    }
        
        
     

    static void writeReadFile(String capital, String userName, int totalLetters, long totalTime) {
        LocalDate localDate = LocalDate.now();// For reference
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedString = localDate.format(formatter);
        File f = new File("scores.txt");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (f.canWrite()) {
            try {
                FileWriter fw = new FileWriter(f, true);
                Formatter fm = new Formatter(fw);
                Scanner sf = new Scanner(f);

                String lettersToString = Integer.toString(totalLetters);
                String timeToStr = Long.toString(totalTime);

                ArrayList<String> userData = new ArrayList<>();
                userData.add(userName);
                userData.add(formattedString.strip());
                userData.add(capital);
                userData.add(lettersToString);
                userData.add(timeToStr);

                String userDataString = String.join("*", userData);

                fm.format("%s \r\n", userDataString);

                fm.close();
                fw.close();

                System.out.println("\nTEN BEST SCORES:\n");
                int i = 0;
                while (sf.hasNextLine() && i < 10) {
                    System.out.println(sf.nextLine());
                    i++;
                }
                System.out.println();
                sf.close();

                List<String> rawData = new ArrayList<String>();
                while (sf.hasNextLine()) {
                    rawData.add(sf.nextLine());
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static String getCapital(String rawCountryCapital) {
        String[] arrSplit = rawCountryCapital.split("[|]+");
        return arrSplit[1].trim();
    }

    static String getCountry(String rawCountryCapital) {
        String[] arrSplit = rawCountryCapital.split("[|]+");
        return arrSplit[0].trim();
    }

    static ArrayList<String> makeDashWord(String capital) {
        String[] arr = capital.split("");

        ArrayList<String> dashWord = new ArrayList<String>();

        for (String e : arr) {
            if (e.equals(" ")) {
                dashWord.add(" ");
            } else {
                dashWord.add("_ ");
            }
        }
        return dashWord;
    }

    static String password(String userInput, ArrayList<String> dashWord, String capital) {
        String[] arrCapital = capital.split("");
        StringBuilder capitalStrBuilder = new StringBuilder();

        if (userInput.length() > 1 && userInput.equalsIgnoreCase(capital)) {
            return capital;
        } else {
            for (int i = 0; i < arrCapital.length; i++) {
                if (userInput.equalsIgnoreCase(arrCapital[i])) {
                    dashWord.set(i, userInput.toUpperCase());
                }
            }
            for (String e : dashWord) {
                capitalStrBuilder.append(e);
            }
            return capitalStrBuilder.toString();
        }
    }

    static String printDashWord(ArrayList<String> dashword) {
        StringBuilder result = new StringBuilder();
        for (Object item : dashword) {
            result.append(item);
        }
        return result.toString();
    }

    public static void cls() {
        try {
            new ProcessBuilder("clear").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}