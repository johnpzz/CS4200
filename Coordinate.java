/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4200;

/**
 *
 * @author John
 */
public class Coordinate {
    public int x;
    public int y;
    
    Coordinate() {
        
    }

    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
         
    
        public static Coordinate getCorrectPosition(int pos) {
        if (pos == 0)
            return new Coordinate(0,0);
        if (pos == 1)
            return new Coordinate(0,1);
        if (pos == 2)
            return new Coordinate(0,2);
         if (pos == 3)
            return new Coordinate(1,0);
        if (pos == 4)
            return new Coordinate(1,1);
        if (pos == 5)
            return new Coordinate(1,2);       
        if (pos == 6)
            return new Coordinate(2,0);
        if (pos == 7)
            return new Coordinate(2,1);
        if (pos == 8)
            return new Coordinate(2,2);
        
        return new Coordinate(-1,-1);
    }
    
}
