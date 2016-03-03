/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mazelab;

import apcscvm.GraphicsUtilityFunctions;
import apcscvm.View;
import apcsmaze.Maze;
import apcsmazedisplay.DefaultEdgeDisplayer;
import apcsmazedisplay.MazeDisplayer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;


/**
 *
 * @author DSTIGANT
 */
public class TreasureMazeDisplay implements View<TreasureMaze>
{
    
    @Override
    public void paint(TreasureMaze m, Graphics g, int w, int h) 
    {
        g.drawImage( 
                MazeDisplayer.displayMaze(
                        m, 
                        new TreasureSquareDisplayer(),
                        new DefaultEdgeDisplayer<Object, Integer>( 
                                Color.GREEN),
                        w, 
                        h), 
                0, 0, null );
    }    
}
