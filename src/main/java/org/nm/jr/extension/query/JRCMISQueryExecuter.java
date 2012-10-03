package org.nm.jr.extension.query;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.query.JRQueryExecuter;

import org.nm.jr.extension.datasource.JRCMISDataSource;

public class JRCMISQueryExecuter implements JRQueryExecuter
{
	private JRCMISDataSource dataSource;
	
	/**
	 * Constructor for the CMIS DataSource.  This DataSource enables querying a CMIS
	 * repository from JasperReports.
	 * 
	 * @param repoUrl
	 * @param user
	 * @param pass
	 * @param query
	 */
	public JRCMISQueryExecuter(String cmisUrl, String user, String pass, String query, boolean allVersions)
	{
		dataSource = new JRCMISDataSource(cmisUrl, user, pass, query, allVersions);
	}
	@Override
	public boolean cancelQuery() throws JRException 
	{
		return false;
	}

	@Override
	public void close() 
	{
		
	}

	@Override
	public JRDataSource createDatasource() throws JRException 
	{
		return dataSource;
	}

}
