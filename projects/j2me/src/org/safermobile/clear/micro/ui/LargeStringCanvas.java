package org.safermobile.clear.micro.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import org.safermobile.micro.utils.StringTokenizer;

public class LargeStringCanvas extends Canvas
{
	  private String _largeString;
	  
	  public LargeStringCanvas (String largeString)
	  {
		  _largeString = largeString;
	  }
	  
	  protected void paint(Graphics graphics)
	  {
	    graphics.setColor(0,0,0);
	    graphics.fillRect(0, 0, getWidth(), getHeight());
	    graphics.setColor(255,255,255);
	    graphics.setFont(Font.getFont(Font.FACE_PROPORTIONAL, 
	                          Font.STYLE_BOLD, Font.SIZE_LARGE));
	    
	    StringTokenizer st = new StringTokenizer(_largeString,"\n");
	    
	    int startY = getHeight()/2;
	    
	    while (st.hasMoreTokens())
	    {
	    	graphics.drawString(st.nextToken(), getWidth()/2, startY, Graphics.HCENTER|Graphics.BASELINE);
	    	startY+=graphics.getFont().getHeight()+3;
	    }
	   }
	  
	  public void setLargeString (String largeString)
	  {
		  _largeString = largeString;
		  repaint();
	  }
	    
	}