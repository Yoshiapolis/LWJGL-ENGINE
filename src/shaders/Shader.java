package shaders;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import files.TextFile;
import math.Matrix;
import math.Vector3f;

public abstract class Shader {
	
	protected int vertexShaderID, fragmentShaderID, programID;
	private TextFile vertexFile;
	private TextFile fragmentFile;
	private HashMap<String, Integer> uniforms;

	public Shader(String vertexFile, String fragmentFile) {
		this.uniforms = new HashMap<String, Integer>();
		this.vertexFile = new TextFile(vertexFile);
		this.fragmentFile = new TextFile(fragmentFile);
	}
	
	public void createUniform(String name) {
		int id = glGetUniformLocation(this.programID, name);
		this.uniforms.put(name, id);
	}
	
	public void create() {
		this.programID = GL20.glCreateProgram();
		this.vertexShaderID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		GL20.glShaderSource(this.vertexShaderID, vertexFile.read());
		GL20.glCompileShader(this.vertexShaderID);
		
		if(GL20.glGetShaderi(this.vertexShaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Error compiling vertex shader - " + GL20.glGetShaderInfoLog(this.vertexShaderID));
		}
		
		this.fragmentShaderID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(this.fragmentShaderID, fragmentFile.read());
		GL20.glCompileShader(this.fragmentShaderID);
		
		if(GL20.glGetShaderi(this.fragmentShaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Error compiling fragment shader - " + GL20.glGetShaderInfoLog(this.fragmentShaderID));
		}
		
		GL20.glAttachShader(this.programID, this.vertexShaderID);
		GL20.glAttachShader(this.programID, this.fragmentShaderID);
		
		bindAllAttributes();
		createUniforms();
		
		GL20.glLinkProgram(this.programID);
		
		if(GL20.glGetProgrami(this.programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			System.err.println("Error program validating" + GL20.glGetShaderInfoLog(this.programID));
		}
		
		GL20.glValidateProgram(this.programID);
		
		if(GL20.glGetProgrami(this.programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Error program validating" + GL20.glGetShaderInfoLog(this.programID));
		}
	}
	
	public void bind() {
		GL20.glUseProgram(this.programID);
	}
	
	public void unbind() {
		GL20.glUseProgram(0);
	}
	
	public void remove() {
		GL20.glDetachShader(this.programID, this.vertexShaderID);
		GL20.glDetachShader(this.programID, this.fragmentShaderID);
		GL20.glDeleteShader(this.vertexShaderID);
		GL20.glDeleteShader(this.fragmentShaderID);
		GL20.glDeleteProgram(this.programID);
	}
	
	public void bindAttribute(int index, String path) {
		GL20.glBindAttribLocation(this.programID, index, path);
	}
	
	public void setUniform(String name, Matrix value) {
		float[] vals = value.getFloats();
		glUniformMatrix4fv(uniforms.get(name), false, vals);
	}
	
	public void setUniform(String name, float value) {
		glUniform1f(this.uniforms.get(name), value);
	}
	
	public void setUniformi(String name, int value) {
		glUniform1i(this.uniforms.get(name), value);
	}
	
	public void setUniform3f(String name, Vector3f value) {
		glUniform3f(this.uniforms.get(name), value.getX(), value.getY(), value.getZ());
	}
	
	public abstract void bindAllAttributes();
	public abstract void createUniforms();
	
}
