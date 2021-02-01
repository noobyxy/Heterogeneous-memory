package edu.rice.cs.hpc.test.viewer;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.rice.cs.hpc.data.experiment.Experiment;
import edu.rice.cs.hpc.data.experiment.extdata.IThreadDataCollection;
import edu.rice.cs.hpc.viewer.metric.ThreadDataCollectionFactory;

/*************************************
 * 
 * Unit test for ThreadDataCollection2 class
 *
 *************************************/
public class ThreadDataCollection2Test 
{
	protected IThreadDataCollection data;
	protected Experiment experiment ;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		String filename = System.getProperty("HPCDATA_DB");
		assertNotNull(filename);
		init(filename);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetMetrics() {
		int numMetrics = experiment.getMetricRaw().length;
		try {
			final int THREAD = 0;
			final int METRIC = 0;
			
			double []values2 = data.getScopeMetrics(THREAD, METRIC, numMetrics);
			checkScopeMetrics(values2);
			
			int cctMin = experiment.getMinCCTID();
			int cctMax = experiment.getMaxCCTID();
			int numThreads	 = data.getRankLabels().length;
			
			for (int i=cctMin; i<cctMax; i++) {
				double []values1 = data.getMetrics(i, METRIC, numMetrics);
				
				assertNotNull(values1);
				assertTrue(values1.length == numThreads);

				double valMin2 = values2[i-cctMin];
				double valMin1 = values1[THREAD];
				assertTrue(valMin1 == valMin2);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			fail("error occurs");
		}
	}

	private void checkScopeMetrics(double []values) {
		assertNotNull(values);
		
		int cctMax = experiment.getMaxCCTID();
		assertTrue(cctMax > 0);
		
		int length   = values.length;
		assertTrue(length > cctMax + 1);
	}
	
	@Test
	public void testGetScopeMetrics() {
		int numMetrics = experiment.getMetricRaw().length;
		final int THREAD = 0;
		
		for (int i=0; i<numMetrics; i++)
		{
			try {
				double []values = data.getScopeMetrics(THREAD, i, numMetrics);
				checkScopeMetrics(values);
				
			} catch (IOException e) {
				e.printStackTrace();
				fail("error occurs");
			}
		}
	}

	@Test
	public void testIsAvailable() {
		boolean is = data.isAvailable();
		assertTrue(is);
	}

	@Test
	public void testGetRankLabels() throws IOException {
		double []labels = data.getRankLabels();
		assertNotNull(labels);
		
	}

	@Test
	public void testGetParallelismLevel() throws IOException {
		int level = data.getParallelismLevel();
		assertTrue(level>0);
	}

	@Test
	public void testGetRankTitle() throws IOException {
		String title = data.getRankTitle();
		assertNotNull(title);
	}

	protected void init(String database) throws Exception
	{
		experiment = new Experiment();
		final File file = new File(database);
		assertTrue(file.canRead());
		experiment.open(file, null, false);
		data = ThreadDataCollectionFactory.build(experiment);
		data.open(experiment.getDefaultDirectory().getAbsolutePath());
	}
}
