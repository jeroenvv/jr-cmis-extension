package org.nm.jr.extension.query;

import java.util.ArrayList;
import java.util.Map;

import org.nm.jr.extension.datasource.JRCMISDataSource;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.query.JRQueryExecuter;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;

public class JRCMISQueryExecuterFactory implements QueryExecuterFactory
{

	@Override
	public JRQueryExecuter createQueryExecuter(JRDataset dataset,
			Map<String, ? extends JRValueParameter> params) throws JRException 
	{
		String url = params.get(JRCMISDataSource.PARAM_CMIS_URL).getValue().toString();
		String user = params.get(JRCMISDataSource.PARAM_CMIS_USER).getValue().toString();
		String pass = params.get(JRCMISDataSource.PARAM_CMIS_PASS).getValue().toString();
		String query = params.get(JRCMISDataSource.PARAM_CMIS_QUERY).getValue().toString();
		
		//create the executer
		JRCMISQueryExecuter executer = new JRCMISQueryExecuter(url,user,pass,query,false);
		return executer;
	}

	@Override
	public JRQueryExecuter createQueryExecuter(JasperReportsContext context,
			JRDataset dataset, Map<String, ? extends JRValueParameter> params)
			throws JRException 
	{
		String url = params.get(JRCMISDataSource.PARAM_CMIS_URL).getValue().toString();
		String user = params.get(JRCMISDataSource.PARAM_CMIS_USER).getValue().toString();
		String pass = params.get(JRCMISDataSource.PARAM_CMIS_PASS).getValue().toString();
		String query = params.get(JRCMISDataSource.PARAM_CMIS_QUERY).getValue().toString();
		
		//create the executer
		JRCMISQueryExecuter executer = new JRCMISQueryExecuter(url,user,pass,query,false);
		return executer;
	}

	@Override
	public Object[] getBuiltinParameters() 
	{
		/* 
		 * this array is bit weird.  Per the JR docs, this method needs to return
		 * a list of parameters required by this query, and the type of the parameter.
		 * Instead of using something like a key-value pair, this API requires the 
		 * structure shown below [param name, class name, param name, class name]
		 * 
		 * These are created as system parameters, not something editable
		*/
		ArrayList<Object> params = new ArrayList<Object>();
		/*params.add(JRCMISDataSource.PARAM_CMIS_URL);
		params.add(String.class);
		params.add(JRCMISDataSource.PARAM_CMIS_USER);
		params.add(String.class);
		params.add(JRCMISDataSource.PARAM_CMIS_PASS);
		params.add(String.class);
		params.add(JRCMISDataSource.PARAM_CMIS_QUERY);
		params.add(String.class);*/
		
		return params.toArray();
	}

	@Override
	public boolean supportsQueryParameterType(String type) 
	{
		// TODO Auto-generated method stub
		return false;
	}

}
