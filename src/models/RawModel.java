package models;

import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class RawModel {
	
	private HashMap<Integer, Integer> ids = new HashMap<>();
	private int vertexCount;
	
	public static final int VAO = 0;
	public static final int VBO = 1;
	public static final int IBO = 2;
	public static final int TBO = 3;
	public static final int NBO = 4;
	
	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}
	
	public int getVertexCount() {
		return this.vertexCount;
	}
	
	public void setVAO_ID(int vaoID) {
		this.ids.put(RawModel.VAO, vaoID);
	}
	
	public int getVAO_ID() {
		return this.ids.get(RawModel.VAO);
	}
	
	public void updateBuffers(float[] vertices, float[] textCoords, int[] indices) {
		
		FloatBuffer vertsBuff = BufferUtils.createFloatBuffer(vertices.length);
		vertsBuff.put(vertices);
		vertsBuff.flip();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ids.get(VBO));
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, vertsBuff, GL15.GL_DYNAMIC_DRAW);
		
		FloatBuffer textBuff = BufferUtils.createFloatBuffer(textCoords.length);
		textBuff.put(textCoords);
		textBuff.flip();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ids.get(TBO));
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, textBuff, GL15.GL_DYNAMIC_DRAW);
		
		IntBuffer indicesBuff  = BufferUtils.createIntBuffer(indices.length);
		indicesBuff.put(indices);
		indicesBuff.flip();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ids.get(IBO));
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuff, GL15.GL_DYNAMIC_DRAW);
		
	    this.vertexCount = indices.length;
	    
	}
	
	public void setBuffer(int bufferID, int type) {
		this.ids.put(type, bufferID);
	}
	
	public void remove() {
		
		if(this.ids.containsKey(RawModel.VAO))
			GL30.glDeleteVertexArrays(this.ids.get(RawModel.VAO));
		
		this.deleteBuffer(RawModel.VBO);
		this.deleteBuffer(RawModel.TBO);
		this.deleteBuffer(RawModel.IBO);
		this.deleteBuffer(RawModel.NBO);
		
	}
	
	private void deleteBuffer(int type) {
		if(this.ids.containsKey(type))
			GL15.glDeleteBuffers(this.ids.get(type));
	}
	
}