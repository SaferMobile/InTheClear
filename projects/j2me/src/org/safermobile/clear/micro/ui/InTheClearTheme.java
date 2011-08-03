package org.safermobile.clear.micro.ui;

import java.awt.Color;
import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.j4me.ui.*;
import org.j4me.ui.components.HorizontalRule;
import org.j4me.ui.components.Label;
import org.j4me.ui.components.Picture;

/**
 * A green on white theme.
 */
public class InTheClearTheme
        extends Theme
{
	
		Image imgTitle;
		Font fntMenu;
		
		int titleHeight = 0;
		
		public final static int BLUE_STEEL = 0x004682B4;
		public final static int DARK_ORANGE = 0x00FF8C00;
		public InTheClearTheme ()
		{
			fntMenu = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
			
             try {
             	imgTitle = Image.createImage("/sm3cropped.png");
             	
				
				} catch (IOException e) {
					
					e.printStackTrace();
				}
             
		}
        /**
         * @see Theme#getBackgroundColor()
         */
        public int getBackgroundColor ()
        {
                return WHITE;
        }

        /**
         * @see Theme#getFontColor()
         */
        public int getFontColor ()
        {
                return BLACK;
        }

        
        public Font getFont() {
		
			return Font.getFont(Font.FACE_PROPORTIONAL, 
                    Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
		}

        public Font getTitleFont() {
    		
			return Font.getFont(Font.FACE_PROPORTIONAL, 
                    Font.STYLE_BOLD, Font.SIZE_LARGE);
		}
		/**
         * @see Theme#getBorderColor()
         */
        public int getBorderColor ()
        {
                return LIGHT_GRAY;
        }

        /**
         * @see Theme#getHighlightColor()
         */
        public int getHighlightColor ()
        {
                return DARK_ORANGE;
        }

        /**
         * @see Theme#getMenuBarBackgroundColor()
         */
        public int getMenuBarBackgroundColor ()
        {
                return SILVER;
        }

        /**
         * @see Theme#getMenuBarHighlightColor()
         */
        public int getMenuBarHighlightColor ()
        {
                return LIGHT_GRAY;
        }

        /**
         * @see Theme#getMenuBarBorderColor()
         */
        public int getMenuBarBorderColor ()
        {
                return LIGHT_GRAY;
        }

        /**
         * @see Theme#getMenuFontColor()
         */
        public int getMenuFontColor ()
        {
                return BLACK;
        }
        
        /**
         * @see Theme#getMenuFontHighlightColor()
         */
        public int getMenuFontHighlightColor ()
        {
                return DARK_ORANGE;
        }
        
        /**
         * @see Theme#getTitleBarBackgroundColor()
         */
        public int getTitleBarBackgroundColor ()
        {
                return BLACK;
        }

        /**
         * @see Theme#getTitleBarHighlightColor()
         */
        public int getTitleBarHighlightColor ()
        {
                return WHITE;
        }

        /**
         * @see Theme#getTitleBarBorderColor()
         */
        public int getTitleBarBorderColor ()
        {
                return SILVER;
        }

        /**
         * @see Theme#getTitleFontColor()
         */
        public int getTitleFontColor ()
        {
                return WHITE;
        }

        /**
         * @see Theme#getScrollbarBackgroundColor()
         */
        public int getScrollbarBackgroundColor ()
        {
                return SILVER;
        }

        /**
         * @see Theme#getScrollbarHighlightColor()
         */
        public int getScrollbarHighlightColor ()
        {
                return SILVER;
        }

        /**
         * @see Theme#getScrollbarBorderColor()
         */
        public int getScrollbarBorderColor ()
        {
                return Theme.BLACK;
        }

        /**
         * @see Theme#getScrollbarTrackbarColor()
         */
        public int getScrollbarTrackbarColor ()
        {
                return LIGHT_GRAY;
        }

		public int getTitleHeight() {
			
			return imgTitle.getHeight();
		}

		public void paintTitleBar(Graphics g, String title, int width,
				int height) {

				g.setColor(getTitleBarBackgroundColor());
				g.fillRect(0, 0, width, height);
				g.setColor(getTitleBarBorderColor());
				g.drawRect(1, 1, width-2, height-2);
							
				g.drawImage(imgTitle, 0, 0, Graphics.LEFT | Graphics.TOP);
				g.setColor(this.getTitleFontColor());
				g.setFont(this.getTitleFont());
				
				int centerY = height/2-getMenuFont().getHeight()/2;
				g.drawString(title, imgTitle.getWidth(), centerY, Graphics.LEFT | Graphics.TOP);
			
		}
		
		public Font getMenuFont() {
			
			return fntMenu;
		}
}