/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazelab;

import apcsmazegenerator.KruskalsAlgorithm;
import apcsmazegenerator.MazeGenerator;

/**
 *
 * @author DSTIGANT
 */
public class TreasureModel
{
    private TreasureBot bot;
    private TreasureMaze maze;
    private TreasureMazeSolver solver;
    private Thread solveThread;
    private boolean started;
    
    public TreasureModel()
    {
        reset( 20, 20 );
        
    }
    
    public void start() {
        if ( ! started )
        {
            started = true;
            solveThread.start();
        }
    }
    
    public void reset( int r, int c )
    {
        reset( r, c, new KruskalsAlgorithm() );
    }
    
    public void reset( int r, int c, MazeGenerator gen )
    {
        started = false;
        maze = new TreasureMaze(r, c, gen );
        bot = new TreasureBot( maze, r/2, c/2 );
        solver = new TreasureMazeSolver();
        solveThread = new Thread() {
            public void run() {
                solver.solve( maze, bot );
            }
        };
    }
    
    public TreasureMaze getMaze() { return maze; }
    public TreasureBot getBot() { return bot; }
    
}
