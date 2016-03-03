/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazelab;

import apcsmaze.MazeDirection;
import apcsmaze.MazeEdge;
import apcsmaze.MazeSquare;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DSTIGANT
 */
public class TreasureBot
{
    private ArrayList<Integer> treasures;
    private int row, col;
    private TreasureMaze maze;
    private Semaphore lock;
    
    public TreasureBot( TreasureMaze m, int sr, int sc )
    {
        row = sr;
        col = sc;
        maze = m;
        
        treasures = new ArrayList<>();
        lock = new Semaphore( 1 );
        try
        {
            lock.acquire();
        } catch (InterruptedException ex)
        {
            Logger.getLogger(TreasureBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getRow() { return row; }
    public int getColumn() { return col; }
    public int getNumTreasures() { return treasures.size(); }
    public int getTreasure( int i ) { return treasures.get(i); }
    
    public void move( MazeDirection dir )
    {
        try
        {
            lock.acquire();
            MazeSquare<Integer, Object> sq = maze.getSquare( row, col );
            MazeEdge<Object, Integer> ed = sq.getEdge( dir );
            MazeSquare<Integer, Object> nextSq = ed.getSquare( dir );
            if ( !ed.isWall() && nextSq != null )
            {
                row += dir.getRowChange();
                col += dir.getColumnChange();
            }
        } catch (InterruptedException ex)
        {
            Logger.getLogger(TreasureBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void pickUp()
    {
        try
        {
            lock.acquire();
            MazeSquare<Integer, Object> sq = maze.getSquare( row, col );
            if ( sq.getData() != null )
            {
                treasures.add( sq.getData() );
                sq.setData( null );
            }
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(TreasureBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void step()
    {
        if ( lock.availablePermits() == 0 )
        {
            lock.release();
        }
    }
}
