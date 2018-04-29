package gui;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class ImageLoader
{
   private static ImageLoader il = new ImageLoader();
   private Toolkit toolkit;
   private int count = 0;

   //singleton design pattern hides the constructor
   private ImageLoader() 
   {
      toolkit = Toolkit.getDefaultToolkit();
   }

   //the only way to get at the singleton reference
   public static ImageLoader getImageLoader()
   {
      return il;
   }
   
   public Image getImage(String file_name)
   {
      int len = file_name.length();
      Image img = null;   

      try
      {
		  BufferedImage bi = ImageIO.read(new File(file_name));
         //BufferedImage bi = ImageIO.read(getClass().getResource(file_name));
         img = toolkit.createImage(bi.getSource());
      } 
      catch (IOException e){}
      
      return img;
   }
   
   public Image getHighLightImage(Image img)
   {
	   count++;
	   if(count <= 20)
	   { 
			return getFilteredImage(img, new MyFilter(true, false, false));
	   }
	   else if(count <= 30)
	   {
		   return getFilteredImage(img, new MyFilter(true, true, false));
	   }
	   else if(count <= 40)
	   {
		  return getFilteredImage(img, new MyFilter(true, false, true));
	   }
	   else if(count <= 50)
	   {																		// convoluted image filter stuff
		   return getFilteredImage(img, new MyFilter(false, false, true));
	   }
	    else if(count <= 60)
	   {
		    return getFilteredImage(img, new MyFilter(false, true, false));
	   }
	     else if(count <= 70)
	   {
		    return getFilteredImage(img, new MyFilter(false, true, true));
	   }
	   else if (count == 71){count = 0;}

	   return getFilteredImage(img, new MyFilter(false, false, false));
   }
   
   public Image getFilteredImage(Image img, ImageFilter img_filter)
   {
		FilteredImageSource fis = new FilteredImageSource(img.getSource(), img_filter);
		return toolkit.createImage(fis);
   }
}

 class AlphaFilter extends RGBImageFilter {
    int alphaLevel;
 
    public AlphaFilter(int alpha) {
      alphaLevel = alpha;
      canFilterIndexColorModel = true;
    }
  
    public int filterRGB(int x, int y, int rgb) {
     // Adjust the alpha value
     int alpha = (rgb >> 24) & 0xff;
     alpha = (alpha * alphaLevel) / 255;
 
     // Return the result
     return ((rgb & 0x00fffff) | (alpha << 24));
   }
 }

  class MyFilter extends RGBImageFilter {
   boolean red, green, blue;
   
   public MyFilter(boolean r, boolean g, boolean b) {
     red = r;
     green = g;
     blue = b;
     canFilterIndexColorModel = true;
   }
 
   public int filterRGB(int x, int y, int rgb) {
   // Filter the colors
     int r = red ? 0: ((rgb >> 16) & 0xff);
     int g = green ? 0: ((rgb >> 8) & 0xff);
     int b = blue ? 0: ((rgb >> 0) & 0xff);
 
     // Return the result
     return (rgb & 0xff000000) | (r << 16) | (g << 8) | (b << 0);
   }
 }
 
class HighLightFilter extends ImageFilter 
{
   //has alpha, not premultiplied
   private static ColorModel defaultRGB = ColorModel.getRGBdefault();

   public HighLightFilter() 
   {
      super();
   }

   public void setPixels(int x, int y, int w, int h, ColorModel model, int[] pixels, int off, int scansize) 
   {
      int count = 0;

      for (int yp = 0; yp < h; yp++) 
      {
         for (int xp = 0; xp < w; xp++) 
         {
             int alpha = pixels[count] >>> 24;  //alpha is the high 8 bits
 
             //to isolate the red pixels, left shift 8 bits (eliminate alpha)
             //then right shift 24 bits (red is in the low 8 bits)
             int red = pixels[count] << 8;
             red = red >>> 24;

             int green = pixels[count] << 16;
             green = green >>> 24;

             int blue = pixels[count] << 24;
             blue = blue >>> 24;

             red = red + 100;
             green = green + 100;

             red = clamp(red);
             green = clamp(green);
             blue = clamp(blue);

             pixels[count] = 255;  //opaque image
             pixels[count] = pixels[count] << 8;
             pixels[count] = pixels[count] | red;
             pixels[count] = pixels[count] << 8;
             pixels[count] = pixels[count] | green;
             pixels[count] = pixels[count] << 8;
             pixels[count] = pixels[count] | blue;

             count++;
	 }
      }

      consumer.setPixels(x, y, w, h, defaultRGB, pixels, 0, 1);
   }

   private int clamp(int color)
   {
      if (color > 255) color = 255;
      else if (color < 0) color = 0;
      return color;
   }
}

class TransparencyFilter extends RGBImageFilter
{
    // the color we are looking for... alpha bits are set to opaque
    private int marker_rgb;
 
    //the transparent color is specified through the constructor
    public TransparencyFilter(Color color)  
    {
       marker_rgb = (color.getRGB() | 0xFF000000);
    }

	//on a pixel by pixel basis, determine if alpha should be set to zero
    public int filterRGB(int x, int y, int rgb) 
    {
        if ((rgb | 0xFF000000) == marker_rgb) 
        {
           // Mark the alpha bits as zero - transparent
           return 0x00FFFFFF & rgb;
        }
		
		return rgb;
    }
}
