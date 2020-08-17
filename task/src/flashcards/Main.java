package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;


public class Main {
    private static Map<String, String> flashcards = new LinkedHashMap<>();
    private static ArrayList<String> log = new ArrayList<>();
    private static final Random rand = new Random();
    private static String[] correct;
    private static Map<String, Integer> hc = new TreeMap<>();

    public static String newCard(String term, String def) {
        flashcards.put(term, def);
        return String.format("The pair (\"%s\": \"%s\") has been added.", term, def);
    }

    public static String remove(String card) {
        if (flashcards.containsKey(card)) {
            flashcards.remove(card);
            return "The card has been removed.";
        }
        return String.format("Can't remove \"%s\": there is no such card.", card);
    }

    public static String impo(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scan = new Scanner(file);
        int count = 0;
        while (scan.hasNextLine()) {
            String[] card = scan.nextLine().split(":");
            if(flashcards.containsKey(card[0])) {
                flashcards.replace(card[0], card[1]);
            }
            else{
                flashcards.put(card[0], card[1]);
            }
            count++;
        }
        return String.format("%d cards have been loaded.", count);
    }

    public static String expo(String fileName) throws IOException {
        File file = new File(fileName);
        int count = 0;
        FileWriter fileWriter = new FileWriter(file, false);
        for (Map.Entry<String, String> entry : flashcards.entrySet()) {
            fileWriter.write(entry.toString().replace("=", ":") + '\n');
            count++;
        }
        fileWriter.close();
        return String.format("%d cards have been saved", count);
    }

    private static void defAnswer(){
        correct = String.valueOf(flashcards.entrySet().toArray()[rand.nextInt(flashcards.size())]).split("=");
    }


    public static String ask(String answer) {
        if (answer.equals(correct[1])) {
            return "Correct answer";
        } else {
            if(!hc.containsKey(correct[0])){
                hc.put(correct[0], 1);
            }
            else{
                for(Map.Entry<String, Integer> entry : hc.entrySet()){
                    if(entry.getKey().equals(correct[0])){
                        entry.setValue(entry.getValue()+1);
                    }
                }
            }
            if (flashcards.containsValue(answer)) {
                String temp = "";
                for (Map.Entry<String, String> e : flashcards.entrySet()) {
                    if (answer.equals(e.getValue())) {
                        temp = e.getKey();
                    }
                }

                return String.format("Wrong answer. The correct one is \"%s\", you've just written the definition of \"%s\".", correct[1], temp);
            } else {
                return String.format("Wrong answer. The correct one is \"%s\".", correct[1]);
            }
        }

    }

    public static String log(String filename) throws IOException {
        File file = new File(filename);
        FileWriter fileWriter = new FileWriter(file);
        for(String s: log){
            fileWriter.write(s + '\n');
        }
        fileWriter.close();
        return "The log has been saved.";
    }


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String term;
        String def;
        String answer;
        String resultLog;
        while (true) {
            System.out.println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats) :");
            log.add("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats) :");
            String action = scan.next();
            log.add(action);
            scan.nextLine();
            switch (action) {
                case "log":
                    System.out.println("File name: ");
                    log.add("File name: ");
                    answer = scan.next();
                    log.add(answer);
                    try {
                        resultLog = log(answer);
                        log.add(resultLog);
                        System.out.println(resultLog);
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.add(e.getMessage());
                    }
                    break;
                case "add":
                    System.out.println("The card: ");
                    log.add("The card: ");
                    term = scan.nextLine();
                    if (flashcards.containsKey(term)) {
                        resultLog = String.format("The card \"%s\" already exists.", term);
                        System.out.println(resultLog);
                        log.add(resultLog);
                    } else {
                        System.out.println("The definition of the card: ");
                        log.add("The definition of the card: ");
                        def = scan.nextLine();
                        log.add(def);
                        if (flashcards.containsValue(def)) {
                            resultLog = String.format("The definition \"%s\" already exists.", def);
                            System.out.println(resultLog);
                            log.add(resultLog);
                        } else {
                            resultLog = newCard(term, def);
                            log.add(resultLog);
                            System.out.println(resultLog);
                        }
                    }

                    break;
                case "remove":
                    System.out.println("The card: ");
                    log.add("The card: ");
                    answer = scan.nextLine();
                    log.add(answer);
                    resultLog = remove(answer);
                    log.add(resultLog);
                    System.out.println(resultLog);
                    break;
                case "exit":
                    System.out.println("Bye bye!");
                    log.add("Bye bye!");
                    System.exit(0);
                    break;
                case "import":
                    System.out.println("File name: ");
                    log.add("File name: ");
                    try {
                        answer = scan.next();
                        log.add(answer);
                        resultLog = impo(answer);
                        log.add(resultLog);
                        System.out.println(resultLog);
                    } catch (FileNotFoundException e) {
                        System.out.println("File not found");
                        log.add("File not found");
                    }
                    break;
                case "export":
                    System.out.println("File name: ");
                    log.add("File name: ");
                    try {
                        answer = scan.next();
                        log.add(answer);
                        resultLog = expo(answer);
                        log.add(resultLog);
                        System.out.println(resultLog);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "ask":
                    System.out.println("How many time to ask?");
                    log.add("How many time to ask?");
                    int times = scan.nextInt();
                    scan.nextLine();
                    log.add(String.valueOf(times));
                        for (int i = 0; i < times; i++) {
                            defAnswer();
                            resultLog = String.format("Print the definition of \"%s\":", correct[0]);
                            System.out.println(resultLog);
                            log.add(resultLog);
                            answer = scan.nextLine();
                            log.add(answer);
                            resultLog = ask(answer);
                            System.out.println(resultLog);
                            log.add(resultLog);

                        }
                    break;
            }
        }
        
    }
}
