package edu.rice.cs.hpc.test.viewer;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ThreadDataCollection3Test extends ThreadDataCollection2Test {


	@Before
	public void setUp() throws Exception {
		String filename = System.getProperty("HPCDATA_DB_NEW");
		assertNotNull(filename);
		init(filename);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testIsAvailable() {
		super.testIsAvailable();
	}

	@Test
	public void testGetRankLabels() throws IOException {
		super.testGetRankLabels();
	}

	@Test
	public void testGetParallelismLevel() throws IOException {
		super.testGetParallelismLevel();
	}

	@Test
	public void testGetRankTitle() throws IOException {
		super.testGetRankTitle();
	}

	@Test
	public void testGetMetrics() {
		super.testGetMetrics();
	}

	@Test
	public void testGetScopeMetrics() {
		super.testGetScopeMetrics();
	}

}
