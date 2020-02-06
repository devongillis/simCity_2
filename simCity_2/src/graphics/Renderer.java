package graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.VolatileImage;

import game.Game;
import input.Input;
import resources.Resources;
import world.World;

public class Renderer {
	
	private static Frame frame;
	public static Canvas canvas;
	
	private static int canvasWidth = 0;
	private static int canvasHeight = 0;
	
	private static final int GAME_WIDTH = 480; // 256 * 7; // how many units you want displayed horizontally
	private static final int GAME_HEIGHT = 432; // 232 * 7; // how many units you want displayed vertically
	
	private static final float screenRatio = 0.8f;
	
	private static long lastFpsCheck = 0;
	private static int currentFPS = 0;
	private static int frameCount = 0;
	private static int tickCount = 0;
	private static final int tickCycle = 360; // a single year is 360 ticks each day is one tick, once the fps loops over the tick count is incremented
	private static int targetFPS = 30;
	private static int targetTime = 1000000000 / targetFPS;
	
	public static World world = null;
	
	private static void getBestSize() {
		Toolkit tookKit = Toolkit.getDefaultToolkit();
		Dimension screenSize = tookKit.getScreenSize();
		
		float w = (float)screenSize.width/GAME_WIDTH;
		float h = (float)screenSize.height/GAME_HEIGHT;
		
		if(w < h) {
			canvasWidth = (int) (GAME_WIDTH * w * screenRatio);
			canvasHeight = (int) (GAME_HEIGHT * w * screenRatio);
		}
		else {
			canvasWidth = (int) (GAME_WIDTH * h * screenRatio);
			canvasHeight = (int) (GAME_HEIGHT * h * screenRatio);
		}
	}
	
	private static void makeFullScreen() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = env.getDefaultScreenDevice();
		
		if(gd.isFullScreenSupported()) {
			frame.setUndecorated(true);
			gd.setFullScreenWindow(frame);
		}
	}
	
	public static void init() {
		getBestSize();
		
		frame = new Frame();
		canvas = new Canvas();
		
		canvas.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
	
		frame.add(canvas);
		
		//makeFullScreen();
		
		frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		
		
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Game.quit();
			}
		});
		
		frame.setVisible(true);
		
		canvas.addKeyListener(new Input());
		
		startRendering();
	}
	
	private static void startRendering() {
		Thread thread = new Thread() {
			public void run() {
				
				GraphicsConfiguration gc = canvas.getGraphicsConfiguration();
				VolatileImage vImage = gc.createCompatibleVolatileImage(GAME_WIDTH, GAME_HEIGHT);
				
				while(true) {
					long startTime = System.nanoTime();
					
					//FPS counter
					frameCount++;
					if(System.nanoTime() > lastFpsCheck + 1000000000) {
						lastFpsCheck = System.nanoTime();
						currentFPS = frameCount;
						frameCount = 0;
						tickCount++;
						if(tickCount >= tickCycle) {
							tickCount = 0;
						}
					}
					
					if(vImage.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
						vImage = gc.createCompatibleVolatileImage(GAME_WIDTH, GAME_HEIGHT);
					}
					
					Graphics g = vImage.getGraphics();
					
					g.setColor(new Color(255, 242, 180));
					g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
					//RENDER STUFF
					
					if(Resources.allLoaded && world.worldLoaded) {
						if(frameCount == 0) {
							// a new day
							world.update(tickCount, true);
						}
						else {
							// update on same day
							world.update(tickCount, false);
						}
						// want to render every frame
						world.render(g, tickCount);
					}
					
					
					// Draw FPS counter
					g.setColor(Color.lightGray);
					g.drawString(String.valueOf(currentFPS), 2, GAME_HEIGHT - 2);
					
					g.dispose();
					
					g = canvas.getGraphics();
					g.drawImage(vImage, 0, 0, canvasWidth, canvasHeight, null);
					
					g.dispose();
					
					long totalTime = System.nanoTime() - startTime;
					if(totalTime < targetTime) {
						try {
							Thread.sleep((targetTime - totalTime) / 1000000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
				}
			}
		};
		thread.setName("Rendering Thread");
		thread.start();
	}
}
