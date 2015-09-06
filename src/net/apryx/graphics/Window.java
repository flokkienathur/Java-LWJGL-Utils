package net.apryx.graphics;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.Arrays;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GLContext;

public final class Window {
	
	private static boolean glfwInit = false;
	private static int windowCount = 0;
	
	private static GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWCursorPosCallback cursorCallback;
	
	private boolean[] keyStates;
	private boolean[] keyPresses;
	private boolean[] keyReleases;
	
	private float mouseX;
	private float mouseY;
	private float mouseDX;
	private float mouseDY;
	
	private int width, height;
	
	private long windowHandle;
	
	private boolean fullscreen;
	
	private String title = "";
	
	public Window(int w, int h){
		this(w,h,false);
	}
	
	public Window(int w, int h, boolean fullscreen){
		this.setWidth(w);
		this.setHeight(h);
		this.fullscreen = fullscreen;
		
		init();
	}
	
	private void init(){
		keyStates = new boolean[GLFW_KEY_LAST];
		keyPresses = new boolean[GLFW_KEY_LAST];
		keyReleases = new boolean[GLFW_KEY_LAST];
		
		if(windowCount != 0)
			System.err.println("Multiple windows active. This may result in some unexpected behaviour.");
		
		windowCount++;
		
		
		initLib();
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		
		long monitor = NULL;
		if(fullscreen)
			monitor = glfwGetPrimaryMonitor();
		
		windowHandle = glfwCreateWindow(width, height, title, monitor, NULL);
		
		if(windowHandle == NULL)
			throw new RuntimeException("Failed to create window!");
		
		keyCallback = new KeyCallback();
		
		glfwSetKeyCallback(windowHandle, keyCallback);
		
		cursorCallback = new CursorCallback();
		
		glfwSetCursorPosCallback(windowHandle, cursorCallback);
		
		setWindowPos(100,100);
		
		makeCurrent();
		
		GLContext.createFromCurrent();
	}
	
	public void setCursorHidden(boolean hidden){
		glfwSetInputMode(windowHandle, GLFW_CURSOR, hidden ? GLFW_CURSOR_HIDDEN : GLFW_CURSOR_NORMAL);
	}

	public float getMouseX(){
		return mouseX;
	}
	
	public float getMouseY(){
		return mouseY;
	}
	
	public float getMouseDX(){
		return mouseDX;
	}
	
	public float getMouseDY(){
		return mouseDY;
	}

	public boolean isKeyPressed(int keycode){
		return keyPresses[keycode];
	}
	
	public boolean isKeyDown(int keycode){
		return keyStates[keycode];
	}
	
	public boolean isKeyReleased(int keycode){
		return keyReleases[keycode];
	}
	
	public void setWindowPos(int x, int y){
		if(!fullscreen)
			glfwSetWindowPos(windowHandle, x, y);
	}
	
	public void setVisible(boolean visible){
		if(visible)
			glfwShowWindow(windowHandle);
		else
			glfwHideWindow(windowHandle);
	}
	
	public void makeCurrent(){
		glfwMakeContextCurrent(windowHandle);
	}
	
	public void update(){
		pollEvents();
		swap();
	}
	
	public void pollEvents(){
		Arrays.fill(keyPresses,false);
		Arrays.fill(keyReleases,false);
		mouseDX = 0;
		mouseDY = 0;
		
		glfwPollEvents();
	}
	
	public void swap(){
		glfwSwapBuffers(windowHandle);
	}
	
	public void setVSync(boolean vsync){
		glfwSwapInterval(vsync ? 1 : 0);
	}
	
	public boolean isCloseRequested(){
		return glfwWindowShouldClose(windowHandle) == GL_TRUE;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void destroy(){
		windowCount--;
		glfwDestroyWindow(windowHandle);
		if(windowCount == 0){
			glfwTerminate();
			glfwInit = false;
			
			if(errorCallback != null)
				errorCallback.release();
			if(keyCallback != null)
				keyCallback.release();
		}
	}
	
	public static void initLib(){
		if(!glfwInit){
			glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
			
			if (glfwInit() != GL_TRUE)
	            throw new IllegalStateException("Unable to initialize GLFW");
			
			glfwInit = true;
			
		}
	}
	
	private class CursorCallback extends GLFWCursorPosCallback{

		@Override
		public void invoke(long windowHandle, double x, double y) {
			mouseDX = (float) (x - mouseX);
			mouseDY = (float) (y - mouseY);
			
			mouseX = (float) x;
			mouseY = (float) y;
		}
		
	}
	
	private class KeyCallback extends GLFWKeyCallback{

		@Override
		public void invoke(long windowHandle, int key, int scancode, int action, int mods) {
			if(action == GLFW_PRESS){
				keyPresses[key] = true;
				keyStates[key] = true;
			}
			if(action == GLFW_RELEASE){
				keyReleases[key] = true;
				keyStates[key] = false;
			}
		}
		
	}
	
}