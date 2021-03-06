package particles;

import models.Mesh;
import models.ModelLoader;
import models.RawModel;
import rendering.Material;
import rendering.Texture;

public class ParticleMesh extends Mesh {

	private static final float[] rectVerts = new float[] {
			1, 1,
			1, -1,
			-1, -1,
			-1, 1
	};
	
	private static final int[] rectIndices = new int[] {
			0, 1, 2,
			0, 3, 2
	};
	
	private static final RawModel rectangle = ModelLoader.getInstancedParticles(rectVerts, rectIndices);
	
	private ParticleAnimation animation;
	
	public ParticleMesh(ParticleAnimation animation, Texture t) {
		
		this.animation = animation;
		
	}
	
	public ParticleMesh() {
		
		super(Mesh.PARTICLE);
		super.setModel(rectangle);
		
	} 
	
	public ParticleMesh(Texture t) {
		
		super(Mesh.PARTICLE);
		super.setModel(rectangle);
		Material material = new Material();
		material.setTexture(t);
		super.setMaterial(material);
		
	}
	
	public void setTexture(Texture t) {
		Material material = new Material();
		material.setTexture(t);
		super.setMaterial(material);
	}
	
	public ParticleAnimation getAnimation() {
		return this.animation;
	}
	
	public void setAnimation(ParticleAnimation animation) {
		this.animation = animation;
	}

}
