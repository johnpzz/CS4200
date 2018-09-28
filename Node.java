/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4200;

import static cs4200.Coordinate.getCorrectPosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author John
 */

public class Node {
    
    public int[][] board;
    public static final int[][] solutionBoard = {{0,1,2},{3,4,5},{6,7,8}};
    public int heuristic;
    public int gn;
    Node parent;    //This is just to get the trail back so we can trace our solution steps..  Not really necessary to just "solve"
    
    public Node() {
        this.board = new int[3][3];
        this.heuristic = -1;
        this.parent = null;
        this.gn = 0;
    }
    
    
    //Had a bug here where i said this.board = board and so the children Nodes boards were referencing the parent board and so any changes to the child changed the parent and an infinite loop occurred.
    public Node(int[][] board, int heuristic, Node parent) {
        this.board = new int[3][3];
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                this.board[i][j] = board[i][j];
            }
        }
        
        this.heuristic = heuristic;
        this.parent = parent;
    }
    
    public static int[][] copyBoard(int[][] board) {
        int[][] clone = new int[3][3];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                clone[i][j] = board[i][j];
            }
        }
        
        
        return clone;
    }
   
    public int calculateHeuristic1() {
        int heuristic = 0;
        
        for (int i = 0; i < this.board.length; i++) {
            
            for (int j = 0; j < this.board[i].length; j++) {
                
                if (this.board[i][j] != solutionBoard[i][j]) {
                    heuristic += 1;
                }
            }
        }
        
        //this.heuristic = heuristic;
        
        return heuristic;
    }
    
    
    public int calculateHeuristic2() {
        int heuristic = 0;
        
        for (int i = 0; i < this.board.length; i++) {
            
            for (int j = 0; j < this.board[i].length; j++) {
                
                if (this.board[i][j] != solutionBoard[i][j]) {
                    
                    Coordinate coordinate = getCorrectPosition(this.board[i][j]);
                    int x = Math.abs(j - coordinate.x);
                    int y = Math.abs(i - coordinate.y);
                    heuristic += (x+y);
 
                }
   
            }
            
        }
        
        //this.heuristic = heuristic;
     
        return heuristic;
    }
    
    public int getPathCost() {
        return heuristic + gn;
    }

    
    
    
    
    public List<Node> calculateChildren() {
        // Maximum of four possible children states
        List<Node> childrenList = new ArrayList<Node>();
        
        for (int i = 0; i < this.board.length; i++) {
            
            for (int j = 0; j < this.board[i].length; j++) {
                
                if (this.board[i][j] == 0 && j <= 1 ) {
                    //can move right
                    Node child = new Node(this.board, -1, this);
                    int temp = child.board[i][j+1];
                    child.board[i][j+1] = 0;
                    child.board[i][j] = temp;
                    
                    child.heuristic = calculateHeuristic1();
                    child.gn = this.gn + 1;
                    
                    childrenList.add(child);
                    
                } 
                
                if (this.board[i][j] == 0 && j >= 1) {
                    //can move left
                    Node child = new Node(this.board, -1, this);
                    int temp = child.board[i][j-1];
                    child.board[i][j-1] = 0;
                    child.board[i][j] = temp;
                    
                    child.heuristic = calculateHeuristic1();
                    child.gn = this.gn + 1;
                    
                    childrenList.add(child);
                    
                } 
                
                if (this.board[i][j] == 0 && i <= 1) {
                    //can move down
                    Node child = new Node(this.board, -1, this);
                    int temp = child.board[i+1][j];
                    child.board[i+1][j] = 0;
                    child.board[i][j] = temp;
                    
                    child.heuristic = calculateHeuristic1();
                    child.gn = this.gn + 1;
                    
                    childrenList.add(child);
                     
                }
                
                if (this.board[i][j] == 0 && i >= 1) {
                    //can move up
                    Node child = new Node(this.board, -1, this);
                    int temp = child.board[i-1][j];
                    child.board[i-1][j] = 0;
                    child.board[i][j] = temp;
                    
                    child.heuristic = calculateHeuristic1();
                    child.gn = this.gn + 1;
                    
                    childrenList.add(child);
                    
                }
                
            }
        }

        return childrenList;
    }
    
    

    // Returns a solution (true), or failure (false)
    public Node aStar() {
        Node initial = this;
        
        
        //Had a bug here, didn't pass in my comparator
        Queue<Node> frontier = new PriorityQueue<>(new NodeComparator());   //PriorityQueue ordered by f(n) = g(n) + h(n) cost
        frontier.add(initial);
        Set<Node> explored = new HashSet<Node>();
        
        boolean isSolved = false;
        while (!isSolved) {
            
            if (frontier.isEmpty()) {
                // Failure // 
                //return explored;            // FAILURE
                return null;
            }
            
            Node next = frontier.poll();    // choose lowest f(n)=h(n)+g(n) node in frontier
            
            
            
            //next.printBoard();
            //System.out.println("---");
            
            
            //Had a bug right here comparing arrays with next.board.equals(solutionBoard)
            if (Arrays.deepEquals(next.board, solutionBoard)) {
                isSolved = true;
                //next.printSolution();
                //System.out.println("Puzzle has been solved!");
                return next;
                //return explored;    // SOLUTION
                
            }
            
            explored.add(next);
            
            List<Node> children = next.calculateChildren();
            
            for (Node n : children) {
                if (!explored.contains(n) || !frontier.contains(n)) {
                    frontier.add(n);
                    
                } else if (frontier.contains(n.board)) {
                    
                    for (Node a : frontier) {
                        if (a.heuristic < n.heuristic) {
                            frontier.remove(a);
                            frontier.add(a);
                        }
                        
                    }
                    
                }
                    
                    
            }
            
            
            
            
            
        }
        
        //return explored;
        return null;
    }
    
    public int[][] generateRandomBoard() {
        int[][] board = new int[3][3];
        
        List<Integer> numList = new ArrayList<Integer>();
        //Integer[] intArray = new Integer[9];
   
        for (int i = 0; i < 9; i++) {
            numList.add(i);
        }
       
        Collections.shuffle(numList);
        
        for (int i = 0; i < board.length; i++) {
            
            for (int j = 0; j < board[i].length; j++) {
                
                        Collections.shuffle(numList);
                        board[i][j] = numList.remove(0);
            }
        }
        
        // Board is now created, now we must check if it is solvable.

        return board;
    }
    
    public boolean isSolvable() {
        int[][] board = this.board;
        Integer[] intArray = new Integer[9];
        
        int index = 0;
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                intArray[index] = board[i][j];
                index++;
            }
        }
        // Our 2D board is now represented as a 1D Integer array called intArray
        
        int inversionCount = 0;
        for (int i = 0; i < intArray.length-1; i++) {
         
            for (int j = i+1; j < intArray.length; j++) {
                
                if (intArray[i] != 0 && intArray[j] != 0) {
                
                    if (intArray[i] > intArray[j]) {
                        inversionCount++;
          
                    }
                }
            }
        }
        
       if (inversionCount % 2 == 0) {
            return true;
       } else
           return false;
    }
    
    public void printBoard() {
        System.out.println("Depth: " + this.gn);
        for (int i = 0; i < 3; i++) {
            //System.out.println();
            for (int j = 0; j < 3; j++) {
                System.out.print(this.board[i][j]);
            }
            System.out.println();
        }
        
        //System.out.println("Depth: " + this.gn);
        //System.out.println();

    }
    
    public void printSolution() {
        List<Node> solutionList = new ArrayList<Node>();
        solutionList.add(this);
       
        
        Node answerParent = this.parent;
        solutionList.add(answerParent);
        
        while (answerParent.parent != null) {
            answerParent = answerParent.parent;
            solutionList.add(answerParent);
        }
        
        Collections.reverse(solutionList);
        
        for (Node n : solutionList) {
            n.printBoard();
            System.out.println("--------------");
        }
        
        
    }

    
    
}
