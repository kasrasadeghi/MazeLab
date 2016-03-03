/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazelab;

import apcsmaze.Maze;
import apcsmazegenerator.KruskalsAlgorithm;
import apcsmazegenerator.MazeGenerator;
import java.util.ArrayList;

/**
 *
 * @author DSTIGANT
 */
public class TreasureMaze extends Maze<Integer, Object>
{
    private static Integer [][] getTreasureArray( int nr, int nc )
    {
        ArrayList<Integer> treasures = new ArrayList<>();
        for ( int i = 10; i < nr*nc/4; i++ )
        {
            treasures.add(i);
        }
        
        Integer [][] scarlet = new Integer[nr][nc];
        for ( int i = 0; i < nr; i++ )
        {
            for ( int j = 0; j < nc; j++ )
            {
                if ( treasures.size() > 0 && Math.random() < 0.1 )
                {
                    int num = treasures.remove( (int)(Math.random()*treasures.size()) );
                    scarlet[i][j] = num;
                }
            }
        }
        return scarlet;
    }
    
    public TreasureMaze(int numRows, int numCols)
    {
        super( getTreasureArray(numRows, numCols) );
        initialize( new KruskalsAlgorithm() );
    }
    
    public TreasureMaze( int numRows, int numCols, MazeGenerator gen )
    {
        super( getTreasureArray( numRows, numCols ) );
        initialize( gen );
    }
    
    private void initialize( MazeGenerator gen )
    {
        MazeGenerator.raiseWalls( this, 0, 0, getNumColumns(), getNumRows() );
        gen.generate( this, 0, 0, getNumColumns(), getNumRows() );
    }
    
}
