package files;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;
import rendering.Texture;

public class PNGFile implements File {

	PNGDecoder decoder = null;
	String path;
	
	public PNGFile(String path) {
		this.path = path;
		this.createPNGDecoder();
	}
	
	private void createPNGDecoder() {
		try {
			this.decoder = new PNGDecoder(Texture.class.getResourceAsStream(path));
		} catch (IOException e1) {
			System.err.println("Couldn't find texture");
		}
	}
	
	@Override
	public ByteBuffer read() {

	    ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

	    try {
			decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
		} catch (IOException e) {
			System.err.println("Couldn't decode PNG texture");
		}
	    
	    buffer.flip();
	    
	    return buffer;
		
	}
	
	@Override
	public void close() {

	}
	
	public int getWidth() {	
		return this.decoder.getWidth();	
	}
	
	public int getHeight() {
		return this.decoder.getHeight();
	}
}
