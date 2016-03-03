/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazelab;

import apcscvm.DefaultControl;
import apcscvm.GraphicsUtilityFunctions;
import apcscvm.View;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

/**
 *
 * @author DSTIGANT
 */
public class TreasureGameView extends DefaultControl<TreasureModel> implements View<TreasureModel>
{
    private TreasureMazeDisplay md;
    private int timeToNextStep;
    
    private int STEP_DURATION = 100;
    
    public TreasureGameView()
    {
        md = new TreasureMazeDisplay();
        timeToNextStep = STEP_DURATION;
    }
    
    public void setStepDuration( int dt )
    {
        STEP_DURATION = dt;
    }
    
    @Override
    public void paint(TreasureModel m, Graphics g, int width, int h )
    {
        int w = 9*width/10;
        
        md.paint( m.getMaze(), g, w, h );
        
        TreasureMaze maze = m.getMaze();
        int nr = maze.getNumRows();
        int nc = maze.getNumColumns();
        
        double sqw = 1.0*w/nc;
        double sqh = 1.0*h/nr;
        
        TreasureBot bot = m.getBot();
        int br = bot.getRow();
        int bc = bot.getColumn();
        
        Graphics2D g2D = (Graphics2D)g;
        Stroke s = g2D.getStroke();
        g2D.setStroke( new BasicStroke( 4 ));
        g.setColor( Color.BLUE );
        g.drawOval( (int)(bc*sqw), (int)(br*sqh), (int)(sqw), (int)(sqh) );
        g2D.setStroke( s );
        
        double th = Math.min( sqh, 1.0*h/bot.getNumTreasures() );
        Font f = GraphicsUtilityFunctions.getFont( (int)(0.9*th) );
        for ( int i = 0; i < bot.getNumTreasures(); i++ )
        {
            GraphicsUtilityFunctions.drawStringWithFontInRectangle(
                    g, "" + bot.getTreasure(i), f, 
                    w, (int)(i*th), (int)sqw, (int)th);
        }
    }
    
    public void handleTimePassage( TreasureModel m, int ea, int dt )
    {
        timeToNextStep -= dt;
        while ( timeToNextStep <= 0 )
        {
            timeToNextStep += STEP_DURATION;
            m.getBot().step();
        }
    }
    
    public void handleMouseClick( TreasureModel m, int ea, MouseEvent me )
    {
        m.start();
    }
    
    
}
