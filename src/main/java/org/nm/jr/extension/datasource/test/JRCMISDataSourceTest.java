package org.nm.jr.extension.datasource.test;

import org.nm.jr.extension.datasource.JRCMISDataSource;

public class JRCMISDataSourceTest {

	public static void main(String args[])
	{
		String user = "admin";
		String pass = "admin";
		String url = "http://localhost:8080/alfresco/cmisatom";
		String query = "select * from cmis:document";
		
		JRCMISDataSource testSrc = new JRCMISDataSource(url, user, pass, query, false);
	}
}
