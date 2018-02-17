package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;


public class Controller {
    @FXML
    protected void locateFile(ActionEvent event) {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open maze file");
        File file = chooser.showOpenDialog(new Stage());
        String path = file.getPath();
        ArrayList<String> maze = readFile(path);
        //int lineCounter = maze.length;
        //System.out.println(maze.length);
        //System.out.println(maze[0].length);

        FileChooser chooser2 = new FileChooser();
        chooser2.setTitle("Open Player file");
        File file2 = chooser2.showOpenDialog(new Stage());
        String path2 = file2.getPath();
        ArrayList<String> moves = readFile(path2);

        String[][] maze2dArray = new String[maze.size()][];
        for (int i = 0; i < maze.size(); i++) {
            System.out.println(maze.get(i));
            maze2dArray[i] = maze.get(i).split("(?!^)");
            //int movesInTheLine = moves2dArray[i].length;
        }
        boolean isNotDone = true;
        int d = 0;
        maze2dArray[0][0] = Integer.toString(d);
        boolean hadD;
        int [][] coordinateChange = new int[moves.size()][2];
        for (int i = 0; i<moves.size(); i++){
            coordinateChange[i] = calcMovesFor1Line(moves.get(i));
        }
        while (isNotDone) {
            hadD = false;
            for (int e = 0; e < maze2dArray.length; e++) {
                for (int j = 0; j < maze2dArray[e].length; j++) {//for each maze2[e][j];
                    if (maze2dArray[e][j].equals(Integer.toString(d))) {
                        hadD = true;
                        for (int i = 0; i < coordinateChange.length; i++) {
                            if ((e + coordinateChange[i][0] >= 0) && (e + coordinateChange[i][0] < maze2dArray.length) && (j + coordinateChange[i][1] >= 0) &&
                                    (j + coordinateChange[i][1] < maze2dArray[e].length) &&
                                    (!(maze2dArray[e + coordinateChange[i][0]][j + coordinateChange[i][1]].equals("N"))) &&
                                    ((maze2dArray[e + coordinateChange[i][0]][j + coordinateChange[i][1]].equals("Y")))) {
                                maze2dArray[e + coordinateChange[i][0]][j + coordinateChange[i][1]] = Integer.toString(d + 1);
                            }
                        }
                    }  //if we have d
                } //maze2dArray[e][j]
            } //maze2dArray[e]

            d++;
            if (!hadD || (!(maze2dArray[maze2dArray.length-1][maze2dArray[0].length-1].equals("Y")))) {
                isNotDone = false;
                System.out.println("ITS OVER");



                Stack<Integer> pathStack = new Stack<Integer>();

                d = Integer.parseInt(maze2dArray[maze2dArray.length-1][maze2dArray[maze2dArray.length-1].length-1]);



                pathStack.push(maze2dArray.length-1);
                pathStack.push(maze2dArray[maze2dArray.length-1].length-1);
                while(d!=0) {
                    for (int i = maze2dArray.length - 1; i >= 0; i--) {
                        for (int j = maze2dArray[i].length - 1; j >= 0; j--) {
                            for (int e = 0; e < coordinateChange.length; e++) {
                                if ((i + coordinateChange[e][0] >= 0) && (i + coordinateChange[e][0] < maze2dArray.length) && (j + coordinateChange[e][1] >= 0) &&
                                        (j + coordinateChange[e][1] < maze2dArray[e].length)) {
                                    if (maze2dArray[i + coordinateChange[e][0]][j + coordinateChange[e][1]].equals(Integer.toString(d - 1))) {
                                        d--;
                                        pathStack.push(i + coordinateChange[e][0]);
                                        pathStack.push(j + coordinateChange[e][1]);
                                    }
                                }
                            }
                        }
                    }
                }

                    FileWriter writer = null;
                    try {
                        writer = new FileWriter("E:\\aMAZEing_output.txt");
                        writer.write(maze2dArray[maze2dArray.length - 1][maze2dArray[maze2dArray.length - 1].length - 1]);


                        System.out.println("Решенный лабиринт:");
                        for (int i = 0; i < maze2dArray.length; i++) {
                            for (int j = 0; j < maze2dArray.length; j++) {
                                System.out.print(maze2dArray[i][j]);
                            }
                            writer.write("\n");
                            System.out.println("");
                        }


                while (!pathStack.empty()) {
                    int i = pathStack.pop();
                   int j= pathStack.pop();
                    System.out.print(i);
                    System.out.print(j + " ");
                    writer.write(Integer.toString(i)+ " ");
                    writer.write(Integer.toString(j) + "  ");

                }
                        writer.close();
                }catch(IOException e) {
                    e.printStackTrace();
                }
            } //ending sequence
        } //isNotDone
    }


    private ArrayList<String> readFile (String path) {
        String thisLine;

        ArrayList<String> list = new ArrayList<String>();
        int lineCounter = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((thisLine = br.readLine()) != null) {
                list.add(lineCounter, thisLine);
                lineCounter++;
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

    private int countInString(String line, String str){
        int count = line.length() - line.replace(str, "").length();
        return (count/ str.length());
    }

    private int[] calcMovesFor1Line(String moves){
        int[] coordinateChange = new int[2];
            //System.out.println("\n" + moves);

                if (moves.contains("r")) {
                    int occurrence = countInString(moves, "r");
                    coordinateChange[0] = coordinateChange[0] + occurrence;
                }
                if (moves.contains("l")) {
                    int occurrence = countInString(moves, "l");
                    coordinateChange[0] = coordinateChange[0] - occurrence;
                }
                if (moves.contains("u")){
                    int occurrence = countInString(moves, "u");
                    coordinateChange[1] = coordinateChange[1] - occurrence;
                }
                if (moves.contains("d")) {
                    int occurrence = countInString(moves, "d");
                    coordinateChange[1] = coordinateChange[1] + occurrence;
                }
                //System.out.print(coordinateChange[0]);
                //System.out.print(coordinateChange[1]);

        return coordinateChange;
    }
}
