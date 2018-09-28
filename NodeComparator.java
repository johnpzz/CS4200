/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4200;

import java.util.Comparator;

/**
 *
 * @author John
 */
public class NodeComparator implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) {
        int function1 = o1.heuristic + o1.gn;
        int function2 = o2.heuristic + o2.gn;
        
        return function1 - function2;
        
        /*
        if (function1 > function2) 
            return 1;
        else if (function1 < function2) 
            return -1;
        else if (function1 == function2)
            return 0;
        
        return -1;
        */
    }
    
}
