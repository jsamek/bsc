package sam.bsc;

import static java.lang.System.exit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sam.bsc.data.PackageInfo;
import sam.bsc.generator.OutputGenerator;
import sam.bsc.service.PackageService;

@SpringBootApplication
public class SpringBootBscApp implements CommandLineRunner {

    /** job trigger period in seconds */
    private static final long PERIOD = 60L;

    /** storage */
    private static final Map<String, PackageInfo> storage = new HashMap<>();
    public static final String QUIT_COMMAND = "quit";

    /** service for handling package info and storage */
    private final PackageService packageService;

    @Autowired
    public SpringBootBscApp(PackageService packageService) {
        this.packageService = packageService;
    }

    public static void main(String[] args) {

        SpringApplication.run(SpringBootBscApp.class, args);

    }

    /**
     * Entry point.
     * @param args command line arguments
     */
    @Override
    public void run(String... args) {

        if (args.length == 0) {
            System.out.println("No args. Program will exit.");
            exit(-1);
        }

        String location = args[0];
        Path filePath = Paths.get(location);
        if (!Files.exists(filePath)) {
            System.out.println("Error: " + location + " file not found");
            exit(-1);
        }

        packageService.processInitialData(storage, filePath);

        scheduleOutputGenerationTask();

        handleConsoleInput();

        exit(0);


    }

    /**
     * Creates and schedules output generation task.
     */
    private void scheduleOutputGenerationTask() {
        Runnable runnableTask = () -> {
            System.out.println();
            OutputGenerator.generateOutput(storage).forEach(System.out::println);
            System.out.println();
        };

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(runnableTask, 0, PERIOD, TimeUnit.SECONDS
        );
    }

    /**
     * Handles console inputs. Processes new package entries and quit command.
     */
    private void handleConsoleInput() {
        System.out.println("Enter parcel info:");

        Scanner scanner = new Scanner(System.in);

        boolean cont = true;
        while(cont) {

            String line = null;

            if (scanner.hasNext()) {
                line = scanner.nextLine();
            }

            if (line != null) {
                if (QUIT_COMMAND.equals(line.trim())) {
                    cont = false;
                } else {
                    packageService.storePackageEntry(storage, line);
                }
            }
        }
        scanner.close();
    }

}
