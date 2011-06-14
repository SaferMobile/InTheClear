package org.safermobile.clear.micro.ui;

import javax.microedition.lcdui.Font;

import org.j4me.ui.*;

/**
 * A green on white theme.
 */
public class ClearTheme
        extends Theme
{
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

        /*
        public Font getFont() {
		
			return Font.getFont(Font.FACE_MONOSPACE, 
                    Font.STYLE_BOLD, Font.SIZE_MEDIUM);
		}*/

		/**
         * @see Theme#getBorderColor()
         */
        public int getBorderColor ()
        {
                return BURNT_ORANGE;
        }

        /**
         * @see Theme#getHighlightColor()
         */
        public int getHighlightColor ()
        {
                return BURNT_ORANGE;
        }

        /**
         * @see Theme#getMenuBarBackgroundColor()
         */
        public int getMenuBarBackgroundColor ()
        {
                return BURNT_ORANGE;
        }

        /**
         * @see Theme#getMenuBarHighlightColor()
         */
        public int getMenuBarHighlightColor ()
        {
                return BURNT_ORANGE;
        }

        /**
         * @see Theme#getMenuBarBorderColor()
         */
        public int getMenuBarBorderColor ()
        {
                return WHITE;
        }

        /**
         * @see Theme#getMenuFontColor()
         */
        public int getMenuFontColor ()
        {
                return WHITE;
        }
        
        /**
         * @see Theme#getMenuFontHighlightColor()
         */
        public int getMenuFontHighlightColor ()
        {
                return SILVER;
        }
        
        /**
         * @see Theme#getTitleBarBackgroundColor()
         */
        public int getTitleBarBackgroundColor ()
        {
                return BURNT_ORANGE;
        }

        /**
         * @see Theme#getTitleBarHighlightColor()
         */
        public int getTitleBarHighlightColor ()
        {
                return ORANGE;
        }

        /**
         * @see Theme#getTitleBarBorderColor()
         */
        public int getTitleBarBorderColor ()
        {
                return BURNT_ORANGE;
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
                return BURNT_ORANGE;
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
                return BURNT_ORANGE;
        }

        /**
         * @see Theme#getScrollbarTrackbarColor()
         */
        public int getScrollbarTrackbarColor ()
        {
                return ORANGE;
        }
}