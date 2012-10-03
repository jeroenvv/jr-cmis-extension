package org.nm.jr.extension.datasource.provider;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataSourceProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;

import org.nm.jr.extension.datasource.JRCMISDataSource;

public class JRCMISDataSourceProvider implements JRDataSourceProvider 
{
		
	@Override
	public JRDataSource create(JasperReport report) throws JRException 
	{
		String url, user, pass, query;
		
		//get the data source parameters from the report
		Map<String, JRParameter> params = buildParamMap(report.getParameters());
	
		//get the CMIS repo URL, user, pass and query
		url = params.get(JRCMISDataSource.PARAM_CMIS_URL).toString();
		user = params.get(JRCMISDataSource.PARAM_CMIS_USER).toString();
		pass = params.get(JRCMISDataSource.PARAM_CMIS_PASS).toString();
		query = params.get(JRCMISDataSource.PARAM_CMIS_QUERY).toString();
		
		//Create a new CMIS data source
		JRCMISDataSource dataSource = new JRCMISDataSource(url, user, pass, query, false);
		
		return dataSource;
	}

	@Override
	public void dispose(JRDataSource dataSource) throws JRException 
	{
		((JRCMISDataSource)dataSource).dispose();
	}

	@Override
	public JRField[] getFields(JasperReport report) throws JRException,
			UnsupportedOperationException 
	{
		return null;
	}

	@Override
	public boolean supportsGetFieldsOperation() 
	{
		return true;
	}

	/**
	 * Builds a map of parameters from the array that JR passes in with the report
	 * 
	 * @param params
	 * @return
	 */
	private Map<String, JRParameter> buildParamMap(JRParameter[] params)
	{
		Map<String, JRParameter> paramMap = new HashMap<String, JRParameter>();
		
		for(JRParameter param : params)
		{
			paramMap.put(param.getName(), param);
		}
		
		return paramMap;
	}
}
