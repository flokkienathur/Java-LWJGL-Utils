package net.apryx.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Test {

	public static void main(String[] args) {
		Window window = new Window(1024,768,false);
		
		window.setTitle("Hello OpenGL!");

		window.setVisible(true);

		FloatBuffer buffer = ByteBuffer.allocateDirect(3 * 4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		buffer.put(0).put(0.5f).put(0);
		buffer.put(-0.5f).put(-0.5f).put(0);
		buffer.put(0.5f).put(-0.5f).put(0);
		buffer.flip();
		

		FloatBuffer colorBuffer = ByteBuffer.allocateDirect(4 * 4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		colorBuffer.put(1).put(0).put(0).put(1);
		colorBuffer.put(0).put(1).put(0).put(1);
		colorBuffer.put(0).put(0).put(1).put(1);
		colorBuffer.flip();
		
		IntBuffer indices = ByteBuffer.allocateDirect(3 * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
		indices.put(0).put(1).put(2);
		indices.flip();
		
		
		while (!window.isCloseRequested()) {
			window.pollEvents();

			glEnableClientState(GL_VERTEX_ARRAY);
			glEnableClientState(GL_COLOR_ARRAY);
			
			glVertexPointer(3, 0, buffer);
			glColorPointer(4, 0, colorBuffer);
			glDrawElements(GL_TRIANGLES, indices);

			window.swap();
			window.sleep(16);
		}

		window.destroy();
	}
}
