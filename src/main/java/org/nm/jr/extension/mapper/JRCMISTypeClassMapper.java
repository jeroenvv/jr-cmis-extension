package org.nm.jr.extension.mapper;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.enums.PropertyType;

public class JRCMISTypeClassMapper {

	public static final String IMAGE_JPEG = "image/jpeg";
	public static final String IMAGE_PNG = "image/png";
	public static final String IMAGE_GIF = "image/gif";
	public static final String IMAGE_BMP = "image/bmp";

	public Class getClassForCMISType(PropertyType type)
	{
		switch(type)
		{
			case INTEGER:
				return Long.class;
				
			case BOOLEAN:
				return Boolean.class;
				
			case STRING:
				return String.class;
			
			case DATETIME:
				return java.util.Date.class;
				
			case DECIMAL:
				return Double.class;
			
			default:
				return Object.class;
		}
	}
	
	/**
	 * Creates a Java object for embedding in JasperReports from the mimetype and
	 * content stream of the CMIS object
	 * 
	 * @param obj
	 * @return
	 */
	public Object getObjectByMimeType(CmisObject obj)
	{
		Object o = new Object();
		
		Property<?> mimeProp = obj.getProperty(PropertyIds.CONTENT_STREAM_MIME_TYPE);
		String mimetype = mimeProp.getFirstValue().toString();
		
		if(IMAGE_JPEG.equalsIgnoreCase(mimetype) || IMAGE_PNG.equalsIgnoreCase(mimetype)
				|| IMAGE_GIF.equalsIgnoreCase(mimetype) || IMAGE_BMP.equalsIgnoreCase(mimetype))
		{
			return getImage(obj);
		}
		else if(mimetype.startsWith("text"))
		{
			return getText(obj);
		}
		
		return o;
	}
	
	/**
	 * Gets a java.awt.Image object that is suitable for embedding in JasperReports, constructed
	 * from the CMIS object's content stream
	 * 
	 * @param obj
	 * @return
	 */
	private Image getImage(CmisObject obj)
	{
		try 
		{
			InputStream stream = ((Document)obj).getContentStream().getStream();
			return ImageIO.read(stream);
		}
		catch(IOException ioex)
		{
			return new BufferedImage(1,1,BufferedImage.TYPE_BYTE_GRAY);
		}
	}
	
	/**
	 * Gets a string object containing the content stream from the CMIS object
	 * 
	 * @param obj
	 * @return
	 */
	private String getText(CmisObject obj)
	{
		try
		{
			InputStream stream = ((Document)obj).getContentStream().getStream();
			return streamToString(stream);
		}
		catch(IOException ioex)
		{
			return "Error retrieving text stream";
		}
	}
	
	/**
	 * Don't want to bundle IOUtils or another dependency....
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private String streamToString(InputStream in) throws IOException {
		  StringBuilder out = new StringBuilder();
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  for(String line = br.readLine(); line != null; line = br.readLine()) 
		    out.append(line);
		  br.close();
		  return out.toString();
	}
}
