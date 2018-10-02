/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4200;

import static cs4200.Node.copyBoard;
import static cs4200.Node.solutionBoard;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;

/**
 *
 * @author John
 */
public class Driver {
    
    public static boolean checkChoiceSelection(String input) {
        try {
            int selection = Integer.parseInt(input);
            if (selection >= 3 || selection < 1) 
                return false;
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public static boolean userNumRangeCheck(String inputBoard) {
        
        
        int[] numbers = new int[9];
        for (int i = 0; i < 9; i++) {
            numbers[i] = Integer.parseInt(String.valueOf(inputBoard.charAt(i)));
        }
        
        for (int i : numbers) {
            if (i < 0 || i > 8) 
                return false;
        }
        
        return true;
    }
    
    public static boolean checkBoard(String board) {
        Scanner input = new Scanner(System.in);
        String userBoard = board;
            
            while (userBoard.length() != 9 || hasDuplicates(userBoard) == true || userNumRangeCheck(userBoard) == false) {
                if (userBoard.length() != 9) {
                    System.out.println("Your input must have 9 values including a 0 for the empty space in the puzzle.");
                    userBoard = input.nextLine();
                } else if (hasDuplicates(userBoard) == true) {
                    System.out.println("You cannot have any duplicate values in your puzzle.  Please re-enter your values:");
                    userBoard = input.nextLine();
                }
                else if (userNumRangeCheck(userBoard) == false) {
                    System.out.println("Your values must fall between 0 and 8 inclusively.");
                    userBoard = input.nextLine();
                }
            }
            
        return true;
    }
    
    public static int[][] initializeBoard(String userBoard) {
            int[][] gameBoard = new int[3][3];
            int index = 0;
            for (int i = 0; i < gameBoard.length; i++) {
                
                for (int j = 0; j < gameBoard[i].length; j++) {
                    
                    gameBoard[i][j] = Integer.parseInt(String.valueOf(userBoard.charAt(index)));
                    index++;
   
                }
            }
            return gameBoard;
    }
    
    
    
    public static boolean hasDuplicates(String inputBoard) {
       
        int[] numbers = new int[9];
        for (int i = 0; i < 9; i++) {
            numbers[i] = Integer.parseInt(String.valueOf(inputBoard.charAt(i)));
            
        }
        
            
        Map<Integer, Integer> uniqueKeys = new HashMap<>();
            
        for (int i : numbers) {
            
            if (uniqueKeys.containsKey(i)) {
                return true;
            }
            if (!uniqueKeys.containsKey(i)) {
                //Had a bug here.. its put i not put numbers[i]
                uniqueKeys.put(i, i);
            }
        }
            
            
        return false;
    }
    
    
    public static void main(String[] args) {
        
        System.out.println("Welcome to my A* 8-Puzzle Solver Program!");
        System.out.println("For this program, there are two options..\n"
                + "(1) Generate a random solvable 8-puzzle and solve it"
                + " using both heuristics.\n(2) User inputs an 8-puzzle"
                + " configuration.");
        System.out.print("Please enter an option: ");
        
        Scanner input = new Scanner(System.in);
        String selection = input.nextLine();
       
        while (checkChoiceSelection(selection) != true) {
            System.out.println("Please enter a valid option: ");
            selection = input.nextLine();
            checkChoiceSelection(selection);
        }
        
        int option = Integer.parseInt(selection);
        
        if (option == 1) {
            Node node = new Node();
            Node node2 = new Node();
            node.board = node.generateRandomBoard();
            node2.board = copyBoard(node.board);
            System.out.println("Checking random board for solvability..");
            
            boolean solvable = node.isSolvable();
            
            /*
            if (solvable == false) {
                System.out.println("Randomized board isn't solvable.  Making new random board.. ");
            }
            */
            
            while (solvable != true) {
                    System.out.println("Generated board isn't solvable.  Randomizing new board.. ");
                    node.board = node.generateRandomBoard();
                    node2.board = copyBoard(node.board);
                    solvable = node.isSolvable();
                }
            
            System.out.println("Board is solvable!  Continuing..");
        
            node.heuristic = node.calculateHeuristic1();
            node2.heuristic = node2.calculateHeuristic2();
            long start = System.currentTimeMillis();
            Node answerNode = node.aStar(1);
            long end = System.currentTimeMillis();
            answerNode.printSolution();
            System.out.println("h1 time: " + Math.abs((start-end)) + " ms.");
            
            node2.heuristic = node2.calculateHeuristic2();
            start = System.currentTimeMillis();
            Node answerNode2 = node2.aStar(2);
            end = System.currentTimeMillis();
            //answerNode2.printSolution();
            System.out.println("h2 time: " + Math.abs(start-end) + " ms.");
            
            ///////////////////////////////////////////////////

            
        } else if (option == 2) {
            
            Node node = new Node();
            Node node2 = new Node();
            System.out.println("Enter your puzzle in the form of a String e.g. '123456780'.");
            String userBoard = input.nextLine();
            
            try {
                checkBoard(userBoard);
            } catch (NumberFormatException e) {
                System.out.println("Negative values are not allowed.  Please try again");
                userBoard = input.nextLine();
                checkBoard(userBoard);
            }
          

            System.out.println("Correct board input!  Checking for solvability..");
            
            // Make board and set it to user input //
            node.board = initializeBoard(userBoard);
            node2.board = copyBoard(node.board);
            
            
            boolean solvable = node.isSolvable();
            
            while (solvable != true) {
                    System.out.println("Generated board isn't solvable.  Enter a new board: ");
                    userBoard = input.nextLine();
                    checkBoard(userBoard);
                    node.board = initializeBoard(userBoard);
                    node2.board = copyBoard(node.board);
                    solvable = node.isSolvable();
                }
            
            
            System.out.println("Correct board input!  Continuing with board algorithm..");
            
            node.heuristic = node.calculateHeuristic1();
            node2.heuristic = node2.calculateHeuristic2();
            long start = System.currentTimeMillis();
            Node answerNode = node.aStar(1);
            long end = System.currentTimeMillis();
            answerNode.printSolution();
            System.out.println("h1 time: " + Math.abs((start-end)) + " ms.");
            
            node2.heuristic = node2.calculateHeuristic2();
            start = System.currentTimeMillis();
            Node answerNode2 = node2.aStar(2);
            end = System.currentTimeMillis();
            //answerNode2.printSolution();
            System.out.println("h2 time: " + Math.abs((start-end)) + " ms.");
            
            
        }
        

            
    }
        
        
    
        
}
    

