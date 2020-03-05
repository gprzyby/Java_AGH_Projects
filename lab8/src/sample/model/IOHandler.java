package sample.model;

import javafx.collections.ObservableList;
import sample.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class IOHandler {

    public static void save(File file) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file)) )
        {
            out.writeObject(new ArrayList<>(MainFrameModel.getToDoList()));
            out.writeObject(new ArrayList<>(MainFrameModel.getInProgressList()));
            out.writeObject(new ArrayList<>(MainFrameModel.getDoneList()));
        }
    }

    public static void load(File file) throws IOException, ClassNotFoundException {
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            loadToList(in, MainFrameModel.getToDoList());
            loadToList(in, MainFrameModel.getInProgressList());
            loadToList(in, MainFrameModel.getDoneList());
        }
    }

    public static void exportToCSV(File file) throws FileNotFoundException {
        try(PrintWriter out = new PrintWriter(file)) {
            out.println("List;" + new Task().getCodedTitle());
            for(MainFrameModel.ListID lists : MainFrameModel.ListID.values()) {
                for(Task task : lists.getList()) {
                    out.println(lists.getKey() + ';' + task.toCSVFormat(';'));
                }
            }
        }
    }

    public static void importFromCSV(File file) throws FileNotFoundException, CSVFromatException {
        List<Task> loadedToDo = new ArrayList<>();
        List<Task> loadedInProgress = new ArrayList<>();
        List<Task> loadedDone = new ArrayList<>();

        try(Scanner scanner = new Scanner(file)) {
            //deleting first line of title
            int numberOfColumns;
            if(scanner.hasNextLine()) {
                numberOfColumns = scanner.nextLine().split(";").length;
                if(numberOfColumns != Task.getCellUsageAmount() + 1) throw new CSVFromatException("Unsupported header of CSV");
            } else {
                throw new CSVFromatException("CSV file was empty");
            }

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.split(";").length != numberOfColumns) throw new CSVFromatException("Columns amount mismatch");

                String listName = (line.split(";"))[0];
                MainFrameModel.ListID list = MainFrameModel.ListID.getInstance(listName);
                if(list == null) throw new CSVFromatException("Wrong list name");

                Task taskFromFile = new Task();
                taskFromFile.getFromCSVLine(line,';',1);

                if(list == MainFrameModel.ListID.TO_DO) {
                    loadedToDo.add(taskFromFile);
                } else if (list == MainFrameModel.ListID.DONE) {
                    loadedDone.add(taskFromFile);
                } else if (list == MainFrameModel.ListID.IN_PROGRESS) {
                    loadedInProgress.add(taskFromFile);
                }

            }

            MainFrameModel.clearAllLists();
            MainFrameModel.getInProgressList().addAll(loadedInProgress);
            MainFrameModel.getDoneList().addAll(loadedDone);
            MainFrameModel.getToDoList().addAll(loadedToDo);
        }

    }

    private static void loadToList(ObjectInputStream in, ObservableList<Task> loadDest) throws IOException, ClassNotFoundException {
        Object rawList = in.readObject();
        if(rawList instanceof List) {
            List list = (List)rawList;
            if(list.isEmpty()) {
                loadDest.clear();
                return;
            }
            if(list.get(0) instanceof Task) {
                List<Task> realList = (List<Task>)list;
                loadDest.clear();
                loadDest.addAll(realList);
            } else {
                throw new ClassNotFoundException();
            }
        } else {
            throw new ClassNotFoundException();
        }
    }


}
