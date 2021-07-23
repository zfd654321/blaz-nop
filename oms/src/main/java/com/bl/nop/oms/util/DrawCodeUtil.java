package com.bl.nop.oms.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class DrawCodeUtil {
	private static final int WIDTH = 65;//设置图片的宽�?

    private static final int HEIGHT = 22;//设置图片的高�?
    
    public static BufferedImage draw(char[] rands) {
    	BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
    		    BufferedImage.TYPE_INT_RGB);
    		    //初始化画�?
    		  Graphics g = image.getGraphics();

    		    //写入背景
    		  drawBackground(g);
    		    //写入�?
    		  drawRands(g, rands);
    		  g.dispose();
    	return image;
    }
    
    private static void drawBackground(Graphics g) {
		  g.setColor(new Color(0xDCDCDC));
		  g.fillRect(0, 0, WIDTH, HEIGHT);
		  for (int i = 0; i < 120; i++) {
		   int x = (int) (Math.random() * WIDTH);
		   int y = (int) (Math.random() * HEIGHT);
		   int red = (int) (Math.random() * 255);
		   int green = (int) (Math.random() * 255);
		   int blue = (int) (Math.random() * 255);
		      //图片颜色组成
		   g.setColor(new Color(red, green, blue));
		   g.drawOval(x, y, 1, 0);
		  }
		   }

   //随机生成字符或�?数字
	private static void drawRands(Graphics g, char[] rands) {
	    // g.setColor(Color.BLUE);
	    Random random = new Random();
	    int red = random.nextInt(110);
	    int green = random.nextInt(50);
	    int blue = random.nextInt(50);
	    g.setColor(new Color(red, green, blue));
	    g.setFont(new Font(null, Font.ITALIC | Font.BOLD, 18));
	    g.drawString("" + rands[0], 1, 17);
	    g.drawString("" + rands[1], 16, 15);
	    g.drawString("" + rands[2], 31, 18);
	    g.drawString("" + rands[3], 46, 16);
	}

	public static char[] generateCheckCode() {
	    String chars = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
	    char[] rands = new char[4];
	    for (int i = 0; i < 4; i++) {
	     int rand = (int) (Math.random() * 32);
	     rands[i] = chars.charAt(rand);
	    }
	    return rands;
	}
}
