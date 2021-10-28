package com.datasource.multiconnect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

@SpringBootTest
class MulticonnectApplicationTests {

	private Context initContext;

	@BeforeEach
	public void init() throws Exception {
		SimpleNamingContextBuilder.emptyActivatedContextBuilder();
		this.initContext = new InitialContext();
	}

	@Test
	public void whenMockJndiDataSource_thenReturnJndiDataSource() throws Exception {
		// Primary Datasouce
		
		//Binding to the test context primary datasource global name
		this.initContext.bind("java:comp/env/jdbc/primary", new DriverManagerDataSource("jdbc:h2:mem:testdb"));
		//Looking up the datasource with global name
		DataSource ds = (DataSource) this.initContext.lookup("java:comp/env/jdbc/primary");
		// Asserting whether getting the datasource with given name
		assertNotNull(ds.getConnection());

		
		// Secondary Datasource
		
		this.initContext.bind("java:comp/env/jdbc/secondary", new DriverManagerDataSource("jdbc:h2:mem:testdb2"));
		DataSource ds2 = (DataSource) this.initContext.lookup("java:comp/env/jdbc/secondary");
		assertNotNull(ds2.getConnection());
		
		// Tertiary Datasource
		
		this.initContext.bind("java:comp/env/jdbc/tertiary", new DriverManagerDataSource("jdbc:h2:mem:testdb2"));
		DataSource ds3 = (DataSource) this.initContext.lookup("java:comp/env/jdbc/tertiary");
		assertNotNull(ds3.getConnection());

	}

}