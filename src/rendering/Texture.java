package rendering;

import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT16;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import files.PNGFile;

public class Texture {
	
	int id;
	
	public Texture() {}
	
	public void emptyDepthCubemap(int res) {

	    this.id = glGenTextures();
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_CUBE_MAP, this.id);
		for(int i = 0; i < 6; i ++) {
		    glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_DEPTH_COMPONENT16, res, res, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
		}
		GL11.glTexParameteri(GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL32.glFramebufferTexture(GL30.GL_DRAW_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, this.id, 0);
        //glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
    }
	
	public Texture(int width, int height) {
		this.id = glGenTextures();
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, this.id);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT16, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, this.id, 0);
	}
	
	public Texture(int width, int height, int format) {
		this.id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, this.id);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT16, width, height, 0, format, GL_FLOAT, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	}
	
	public void loadCubemap(String[] textureFiles) {
		
		
		ByteBuffer[] images = new ByteBuffer[6];
		int width = 0;
		int height= 0;
		
		for(int i = 0; i < 6; i ++) {
			PNGFile file = new PNGFile(textureFiles[i]);
			images[i] = file.read();
			width = file.getWidth();
			height = file.getHeight();
		}

		
		this.loadCubemapBytes(images, width, height);
		
	}
	
	private void loadCubemapBytes(ByteBuffer[] bytes, int width, int height) {
		
		this.id = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
	    glBindTexture(GL_TEXTURE_CUBE_MAP, id);

	    for(int i = 0; i < bytes.length; i ++) {
	    	glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, bytes[i]);
	    }
	    
	    glPixelStorei(GL_UNPACK_ALIGNMENT, 1); 
	    glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
	    glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
	    glGenerateMipmap(GL_TEXTURE_CUBE_MAP);
	    
	}
	
	private void loadBytes(ByteBuffer bytes, int width, int height) {
		this.id = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
	    glBindTexture(GL_TEXTURE_2D, id);

	   // glPixelStorei(GL_UNPACK_ALIGNMENT, 1); 
	    
	    if(GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
	    	float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
	    	GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, amount);
	    } else {
	    	System.out.println("Anisotropic filtering not supported");
	    }
	    
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
	    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, bytes);
	    glGenerateMipmap(GL_TEXTURE_2D);
	}
	
	public Texture(BufferedImage image) {
		
		byte[] pixelData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	    ByteBuffer buff = BufferUtils.createByteBuffer(pixelData.length);
	    buff.put(pixelData);
		this.loadBytes(buff, image.getWidth(), image.getHeight());
	    
	}
	
	public Texture(String path) {

		PNGFile file = new PNGFile(path);
		ByteBuffer image = file.read();
		this.loadBytes(image, file.getWidth(), file.getHeight());  
	    
	}
	
	public int getID() {
		return this.id;
	}
	
	public void bindAsCubemap() {
		glBindTexture(GL_TEXTURE_CUBE_MAP, this.id);
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, this.id);
	}
	
}
