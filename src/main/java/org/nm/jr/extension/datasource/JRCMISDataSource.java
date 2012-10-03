package org.nm.jr.extension.datasource;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.design.JRDesignField;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.nm.jr.extension.constants.JRCMISConstants;
import org.nm.jr.extension.mapper.JRCMISTypeClassMapper;

public class JRCMISDataSource implements JRDataSource 
{

	public static final String PARAM_CMIS_URL			= "CMIS_URL";
	public static final String PARAM_CMIS_USER 			= "CMIS_USER";
	public static final String PARAM_CMIS_PASS			= "CMIS_PASS";
	public static final String PARAM_CMIS_QUERY			= "CMIS_QUERY";
	public static final String PARAM_CMIS_ALLVERSIONS 	= "CMIS_ALLVERSIONS";
	
	private ItemIterable<QueryResult> results;
	private Iterator<QueryResult> it;
	private QueryResult current;
	private Session session;
	private JRCMISTypeClassMapper tcm = new JRCMISTypeClassMapper();
	
	/**
	 * Constructor for the CMIS DataSource.  This DataSource enables querying a CMIS
	 * repository from JasperReports.
	 * 
	 * @param repoUrl
	 * @param user
	 * @param pass
	 * @param query
	 */
	public JRCMISDataSource(String cmisUrl, String user, String pass, String query, boolean allVersions)
	{
		session = createCMISSession(cmisUrl, user, pass);
		results = session.query(query, allVersions);
		it = results.iterator();
	}
	
	/**
	 * Disposes of this data source.  Nothing to do here, yet
	 */
	public void dispose()
	{
		//nothing to do, yet
	}
	
	@Override
	public Object getFieldValue(JRField field) throws JRException {

		Object retval;
		PropertyData<?> pd = current.getPropertyByQueryName(PropertyIds.OBJECT_ID);
		ObjectId objId = session.createObjectId(pd.getFirstValue().toString());
		CmisObject obj = session.getObject(objId);
		
		//if the property requested is the content stream, get it and return
		if(field.getName().equalsIgnoreCase(JRCMISConstants.FIELD_CONTENT_STREAM))
		{
			return tcm.getObjectByMimeType(obj);
		}
		
		Property<?> prop = obj.getProperty(field.getName());
		List<?> values = prop.getValues();
		
		if(values.size() == 1)
		{
			retval = values.get(0);
		}
		else if(values.size() > 1)
		{
			retval = values;
		}
		else
		{
			retval = null;
		}
		
		return retval;
	}
	
	@Override
	public boolean next() throws JRException {
		
		boolean hasNext = false;
		
		if(it != null && it.hasNext())
		{
			current = it.next();
			hasNext = true;
		}
		
		return hasNext;
	}

	/**
	 * Creates a CMIS session from the provided CMIS url, using the provide user and
	 * password.
	 * 
	 * @param cmisUrl
	 * @param user
	 * @param pass
	 * @return
	 */
	private Session createCMISSession(String cmisUrl, String user, String pass)
	{
		SessionFactory factory = SessionFactoryImpl.newInstance();
		Map<String, String> params = new HashMap<String, String>();
		params.put(SessionParameter.USER, user);
		params.put(SessionParameter.PASSWORD, pass);
		params.put(SessionParameter.ATOMPUB_URL, cmisUrl);
		params.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
		
		List<Repository> repositories = factory.getRepositories(params);
		
		// if we dont' get a valid list back, throw exception
		if(repositories == null || repositories.size() < 1)
		{
			throw new RuntimeException("No repository found in CMIS server");
		}
		
		return repositories.get(0).createSession();
	}
	
	/**
	 * Gets a list of all the available fields from the current query
	 * 
	 * @return
	 */
	public JRField[] getAllFields()
	{
		List<JRField> allFields = new ArrayList<JRField>();
		
		try 
		{
			//move to first row
			next();
			
			PropertyData<?> pd = current.getPropertyByQueryName(PropertyIds.OBJECT_ID);
			ObjectId objId = session.createObjectId(pd.getFirstValue().toString());
			CmisObject obj = session.getObject(objId);
			List<Property<?>> props = obj.getProperties();
			
			for(Property<?> prop : props)
			{
				JRDesignField field = new JRDesignField();
				field.setDescription(prop.getDisplayName());
				field.setName(prop.getQueryName());
				field.setValueClass(tcm.getClassForCMISType(prop.getType()));
				field.setValueClassName(tcm.getClassForCMISType(prop.getType()).getName());
				allFields.add(field);
			}
			
			// now add a "special" field for the content stream itself if this is a document
			if(obj.getBaseType().getId().equals(ObjectType.DOCUMENT_BASETYPE_ID))
			{
				
				//get the stream object based on its mime type
				Object streamObject = tcm.getObjectByMimeType(obj);
				
				if(streamObject != null)
				{
					JRDesignField field = new JRDesignField();
					field.setDescription("CMIS Content Stream");
					field.setName(JRCMISConstants.FIELD_CONTENT_STREAM);
					
					//value class and class name is determined by mime-type of stream
					field.setValueClass(streamObject.getClass());
					field.setValueClassName(streamObject.getClass().getName());
					
					allFields.add(field);
				}
			}
		}
		catch(JRException ex)
		{
			return new JRField[0];
		}
		
		

		JRField[] fields = new JRField[allFields.size()];
		for(int i = 0; i < allFields.size(); i++) fields[i] = allFields.get(i);
		return fields;
	}
}
