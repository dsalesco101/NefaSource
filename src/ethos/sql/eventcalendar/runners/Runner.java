package ethos.sql.eventcalendar.runners;

import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import ethos.sql.DatabaseConfiguration;
import ethos.sql.DatabaseManager;
import ethos.sql.eventcalendar.queries.AddWinnerQuery;

public class Runner {

    public static void main(String...args) throws InterruptedException, ExecutionException {
        new Runner(DatabaseConfiguration.EVENT_CALENDAR).start();
    }

    enum Type {
        PARTICIPANTS,
        WINNERS,
        ADD_WINNER
        ;

        static Optional<Type> forString(String string) {
            return Arrays.stream(Type.values()).filter(type -> type.matchesString(string)).findFirst();
        }

        boolean matchesString(String string) {
            String name = toString();
            return name.toLowerCase().equals(string)
                    || name.equals(string)
                    || name.toLowerCase().replaceAll("_", " ").equals(string)
                    || name.replaceAll("_", " ").equals(string);
        }
    }

    private final DatabaseManager manager = new DatabaseManager();
    private final DatabaseConfiguration configuration;

    public Runner(DatabaseConfiguration configuration) {
        this.configuration = configuration;
    }

    public void start() throws ExecutionException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        // Display the actions
        System.out.println();
        System.out.println("Please enter the type of action. Types of actions are as follows: ");
        Arrays.stream(Type.values()).forEach(type -> System.out.print(type.toString() + " "));
        System.out.println();

        // Read input
        String action = scanner.nextLine();
        Optional<Type> type = Type.forString(action);
        if (!type.isPresent()) {
            System.err.println("No type found for " + action);
            start();
        } else {
            switch (type.get()) {
                case PARTICIPANTS:
                    manager.execute(configuration, new PrintParticipants()).get();
                    start();
                    break;
                case WINNERS:
                    manager.execute(configuration, new PrintWinners()).get();
                    start();
                    break;
                case ADD_WINNER:
                    System.out.println("Please enter the name of the user you want to add as a winner.");
                    String username = scanner.nextLine();
                    System.out.println("Please enter the day the winner should be inserted at.");
                    int day = scanner.nextInt();
                    AddWinnerQuery.addWinner(manager, configuration, username, day);
                    start();
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
