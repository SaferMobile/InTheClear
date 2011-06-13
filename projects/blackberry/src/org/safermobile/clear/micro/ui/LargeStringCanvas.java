package org.safermobile.clear.micro.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

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
	    
	    graphics.drawString(_largeString, getWidth()/2, getHeight()/2, Graphics.HCENTER|Graphics.BASELINE);  
	   }
	  
	  public void setLargeString (String largeString)
	  {
		  _largeString = largeString;
		  repaint();
	  }
	    
	}