package org.nm.jr.extension.field.provider;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.nm.jr.extension.datasource.JRCMISDataSource;

import com.jaspersoft.ireport.designer.FieldsProvider;
import com.jaspersoft.ireport.designer.FieldsProviderEditor;
import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.data.ReportQueryDialog;

public class JRCMISFieldsProvider implements FieldsProvider 
{

	@Override
	public String designQuery(IReportConnection conn, String arg1,
			ReportQueryDialog queryDialog) throws JRException,
			UnsupportedOperationException {
		
		//don't have a query designer yet
		throw new UnsupportedOperationException("A CMIS query designer is not available");
	}

	@Override
	public FieldsProviderEditor getEditorComponent(ReportQueryDialog queryDialog) {
		//Don't have a custom editor yet
		return null;
	}

	@Override
	public JRField[] getFields(IReportConnection conn, JRDataset dataset, Map params)
			throws JRException, UnsupportedOperationException {
		
		// construct a datasource and get the list of fields
		String url = params.get(JRCMISDataSource.PARAM_CMIS_URL).toString();
		String user = params.get(JRCMISDataSource.PARAM_CMIS_USER).toString();
		String pass = params.get(JRCMISDataSource.PARAM_CMIS_PASS).toString();
		String query = params.get(JRCMISDataSource.PARAM_CMIS_QUERY).toString();
		
		JRCMISDataSource dataSource = new JRCMISDataSource(url, user, pass, query, false);
		
		return dataSource.getAllFields();
	}

	@Override
	public boolean hasEditorComponent() 
	{
		//don't have any custom field editor components
		return false;
	}

	@Override
	public boolean hasQueryDesigner() 
	{
		//don't have a query designer yet
		return false;
	}

	@Override
	public boolean supportsAutomaticQueryExecution() 
	{
		//do support automatic query execution if all parameters are provided
		return false;
	}

	@Override
	public boolean supportsGetFieldsOperation() 
	{
		//do support getting the available fields
		return true;
	}

}
