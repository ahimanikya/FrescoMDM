package com.sun.mdm.index.loader.clustersynchronizer;

import java.sql.Connection;
import java.sql.SQLException;

import junit.framework.TestCase;

import com.sun.mdm.index.loader.clustersynchronizer.dao.DAOFactory;

public class ConnectionTest extends TestCase {

	public ConnectionTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testConnection() {

		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		} catch (Exception e) {
			// logger.info(e.getMessage());
		}
		
		for (int i = 0; i < 10000; i++) {

			try {
				Connection c = DAOFactory.getConnection();
				
				c.close();
				
				if(i==10000){
					assertTrue(false);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("runs out of connection in iteration  " + i);
				assertTrue(true);
				break;
				
			}
		}
	}

}
