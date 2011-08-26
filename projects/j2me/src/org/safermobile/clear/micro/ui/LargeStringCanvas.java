package org.safermobile.clear.micro.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import org.safermobile.micro.utils.CharTokenizer;
import org.safermobile.micro.utils.StringTokenizer;

public class LargeStringCanvas extends Canvas
{
	  private String _largeString;
	  
	  private final static String NEWLINE = "\n";
	  
	  public LargeStringCanvas (String largeString)
	  {
		  _largeString = processString(largeString);
		  
	  }
	  
	  private static String processString (String input)
	  {

		  int nlIdx = input.indexOf("\\n");
		  if (nlIdx !=-1)
		  {
			  input = input.substring(0,nlIdx) + '\n' + input.substring(nlIdx+2);
		  }
		  
		  return input;
	  }
	  protected void paint(Graphics graphics)
	  {
	    graphics.setColor(0,0,0);
	    graphics.fillRect(0, 0, getWidth(), getHeight());
	    graphics.setColor(255,255,255);
	    graphics.setFont(Font.getFont(Font.FACE_PROPORTIONAL, 
	                          Font.STYLE_BOLD, Font.SIZE_LARGE));
	    
	    StringTokenizer st = new StringTokenizer(_largeString,NEWLINE);
	    
	    int startY = getHeight()/2;
	    String line;
	    Font font = graphics.getFont();
	    int screenWidth = this.getWidth();
	    int lineWidth;
	    int lineHeight = graphics.getFont().getHeight() + 3; //padding
	    
	    while (st.hasMoreTokens())
	    {
	    	line = st.next();
	    
	    	lineWidth = font.charsWidth(line.toCharArray(), 0, line.length());
	    	
	    	if (lineWidth > screenWidth)
	    	{
	    		
	    		startY+=lineHeight;
	    		
	    		graphics.drawString(line.substring(0, line.length()/2), getWidth()/2, startY, Graphics.HCENTER|Graphics.BASELINE);
	    		startY+=lineHeight;
	    		
	    		graphics.drawString(line.substring(line.length()/2), getWidth()/2, startY, Graphics.HCENTER|Graphics.BASELINE);
	    		startY+=lineHeight;
	    		
	    	}
	    	else
	    	{
	    		graphics.drawString(line, getWidth()/2, startY, Graphics.HCENTER|Graphics.BASELINE);
	    		startY+=lineHeight;
	    	}
	    	
	    	
	    }
	   }
	  
	  public void setLargeString (String largeString)
	  {
		  _largeString = processString(largeString);
		  repaint();
	  }
	    
	}