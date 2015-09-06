package net.apryx.graphics;

import net.apryx.input.Keys;

import org.lwjgl.opengl.GL11;

public class Test {

	public static void main(String[] args) throws Exception {
		Window window = new Window(1920,1080, true);

		window.setVisible(true);
		window.setCursorHidden(true);
		
		int size = 4;

		while (!window.isCloseRequested()) {
			window.pollEvents();

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			
			GL11.glLoadIdentity();
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, window.getWidth(), window.getHeight(), 0, -100, 100);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			

			float r = 1, g = 1, b = 1, s = 1;

			if(window.isKeyDown(Keys.X)){
				r = 0;
				s += 0.5f;
			}
			if(window.isKeyDown(Keys.Z)){
				g = 0;
				s += 0.5f;
			}
			
			GL11.glTranslatef(window.getMouseX(), window.getMouseY(), 0);
			GL11.glScalef(s,s,1);
			
			GL11.glBegin(GL11.GL_TRIANGLES);
			
			GL11.glColor3f(r,g,b);
			
			GL11.glVertex2f(0,0);
			GL11.glVertex2f(0, 4*size);
			GL11.glVertex2f(2*size, 3*size);
			
			GL11.glColor3f(0.9f * r, 0.9f * g, 0.9f * b);
			
			GL11.glVertex2f(0,0);
			GL11.glVertex2f(2*size, 3*size);
			GL11.glVertex2f(4*size, 4*size);

			GL11.glEnd();

			window.swap();
			
			Thread.sleep(8);
		}

		window.destroy();
	}
}
