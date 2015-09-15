package net.apryx.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Test {

	public static void main(String[] args) {
		Window window = new Window(1024,768,false);
		
		window.setTitle("Hello OpenGL!");

		window.setVisible(true);

		int vertexCount = 3;
		int floatSize = 4;
		
		FloatBuffer buffer = createFloatBuffer(vertexCount * 3 + vertexCount * 4);
		buffer.put(0).put(0.5f).put(0);
		buffer.put(1).put(0).put(0).put(1);
		
		buffer.put(-0.5f).put(-0.5f).put(0);
		buffer.put(0).put(1).put(0).put(1);
		
		buffer.put(0.5f).put(-0.5f).put(0);
		buffer.put(0).put(0).put(1).put(1);
		buffer.flip();
		
		/*IntBuffer indices = ByteBuffer.allocateDirect(3 * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
		indices.put(0).put(1).put(2);
		indices.flip();*/
		
		int vbo = glGenBuffers();
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		
		
		while (!window.isCloseRequested()) {
			window.pollEvents();

			glEnableClientState(GL_VERTEX_ARRAY);
			glEnableClientState(GL_COLOR_ARRAY);

			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			
			glVertexPointer(3, GL_FLOAT, 7 * floatSize, 0);
			glColorPointer(4, GL_FLOAT, 7 * floatSize, 3 * floatSize);
			
			glDrawArrays(GL_TRIANGLES, 0, 3);

			window.swap();
			window.sleep(16);
		}

		window.destroy();
	}
	
	public static FloatBuffer createFloatBuffer(int floatAmounts){
		return ByteBuffer.allocateDirect(floatAmounts * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
	}
}
