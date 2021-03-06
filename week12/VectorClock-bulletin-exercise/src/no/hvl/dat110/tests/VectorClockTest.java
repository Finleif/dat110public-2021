package no.hvl.dat110.tests;


import java.rmi.RemoteException;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.hvl.dat110.clients.Client1;
import no.hvl.dat110.clients.Client2;
import no.hvl.dat110.clients.Client3;
import no.hvl.dat110.middleware.Config;
import no.hvl.dat110.middleware.ProcessContainer;
import no.hvl.dat110.process.iface.ProcessInterface;
import no.hvl.dat110.util.Util;

class VectorClockTest {
	

	@BeforeEach
	void setUp() throws Exception {
		
		new ProcessContainer("process1", 1001, Config.PORT1);
		new ProcessContainer("process2", 1002, Config.PORT2);
		new ProcessContainer("process3", 1003, Config.PORT3);

	}

	@Test
	void test() throws RemoteException, InterruptedException {
		/**
		 * Testing concurrency - no dependency in the way processes send and receive messages
		 */
		Client1 c1 = new Client1();
		Client2 c2 = new Client2();
		Client3 c3 = new Client3();
		
		c1.start();
		c2.start();
		c3.start();
		
		c1.join();
		c2.join();
		c3.join();
		
		ProcessInterface p1 = Util.getProcessStub("process1", Config.PORT1);
		ProcessInterface p2 = Util.getProcessStub("process2", Config.PORT2);
		ProcessInterface p3 = Util.getProcessStub("process3", Config.PORT3);
		
		Util.printClock(p1, "process1");
		Util.printClock(p2, "process2");
		Util.printClock(p3, "process3");
		
		Assert.assertArrayEquals(p1.getVectorclock().toArray(), new Object[] {1,1,0});
		Assert.assertArrayEquals(p2.getVectorclock().toArray(), new Object[] {1,1,0});
		Assert.assertArrayEquals(p3.getVectorclock().toArray(), new Object[] {1,1,1});
	}

}
