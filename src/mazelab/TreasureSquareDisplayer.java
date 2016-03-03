/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mazelab;

import apcscvm.GraphicsUtilityFunctions;
import apcsmaze.MazeSquare;
import apcsmazedisplay.MazeSquareDisplayer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author DSTIGANT
 */
public class TreasureSquareDisplayer implements MazeSquareDisplayer<Integer, Object>
{
    
    public TreasureSquareDisplayer() 
    {
    }
    
    @Override
    public BufferedImage displaySquare(MazeSquare<Integer, Object> sq, int w, int h) 
    {
        BufferedImage scarlet = new BufferedImage( w, h, BufferedImage.TYPE_4BYTE_ABGR );
        Graphics2D g = scarlet.createGraphics();
        Integer i = sq.getData();
        
        if ( i != null )
        {
            Font f = GraphicsUtilityFunctions.getFont( h * 8/10 );
            g.setColor( Color.BLACK );
            GraphicsUtilityFunctions.drawStringWithFontInRectangle(g, ""+i, f, 0, 0, w, h );
        }
        
        return scarlet;
    }
    
}
