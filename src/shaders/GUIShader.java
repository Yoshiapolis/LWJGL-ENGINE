package shaders;

import math.Matrix;
import math.Vector2f;
import math.Vector3f;

public class GUIShader extends Shader {

	private static final String VERTEX_FILE = "/shaders/GUIVertShader.glsl";
	private static final String FRAGMENT_FILE = "/shaders/GUIFragShader.glsl";
	
	public GUIShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	public void bindAllAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	public void createUniforms() {
		super.createUniform("color");
		super.createUniform("transformationMatrix");
		super.createUniform("depth");
	}
	
	public void setDepth(float depth) {
		super.setUniform("depth", depth);
	}
	
	public void setTransformationMatrix(Matrix matrix) {
		super.setUniform("transformationMatrix", matrix);
	}
	
	public void setColor(Vector3f color) {
		super.setUniform3f("color", color);
	}

}