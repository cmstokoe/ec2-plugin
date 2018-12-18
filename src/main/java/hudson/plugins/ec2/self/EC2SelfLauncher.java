package hudson.plugins.ec2.self;

import java.util.logging.Level;
import java.util.logging.Logger;

import hudson.model.TaskListener;
import hudson.plugins.ec2.EC2AbstractSlave;
import hudson.plugins.ec2.EC2Cloud;
import hudson.plugins.ec2.EC2Computer;
import hudson.slaves.JNLPLauncher;
import hudson.slaves.SlaveComputer;

public class EC2SelfLauncher extends JNLPLauncher {
    
    private static final Logger LOGGER = Logger.getLogger(EC2SelfLauncher.class.getName());
    
    protected void log(Level level, EC2Computer computer, TaskListener listener, String message) {
        EC2Cloud cloud = computer.getCloud();
        if (cloud != null)
            EC2Cloud.log(LOGGER, level, listener, message);
    }

    protected void logException(EC2Computer computer, TaskListener listener, String message, Throwable exception) {
        EC2Cloud cloud = computer.getCloud();
        if (cloud != null)
            EC2Cloud.log(LOGGER, Level.WARNING, listener, message, exception);
    }

    protected void logInfo(EC2Computer computer, TaskListener listener, String message) {
        log(Level.INFO, computer, listener, message);
    }

    protected void logWarning(EC2Computer computer, TaskListener listener, String message) {
        log(Level.WARNING, computer, listener, message);
    }
    
    @Override
    public boolean isLaunchSupported() {
    	return true;
    }

    @Override
    public void launch(SlaveComputer slaveComputer, TaskListener listener) {
        try {
            EC2Computer computer = (EC2Computer) slaveComputer;
            final long timeout = computer.getNode().getBootDelay();
            final long startTime = System.currentTimeMillis();
            while (true) {
                    long waitTime = System.currentTimeMillis() - startTime;
                    if (computer.isOnline()) {
                        logInfo(computer, listener, "Computer is connected after "  + (waitTime / 1000) + " seconds.");
                        return;
                    }
                    if (timeout > 0 && waitTime > timeout) {
                        logWarning(computer, listener, "Timed out after " + (waitTime / 1000)
                                + " seconds of waiting for the agent to connect itself to the Master. (maximum timeout configured is "
                                + (timeout / 1000) + ")");
                        return;                
                    }
                    else Thread.sleep(5000);        
            } 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace(listener.error(e.getMessage()));
            if (slaveComputer.getNode() instanceof  EC2AbstractSlave) {
                LOGGER.log(Level.FINE, String.format("Terminating the ec2 agent %s due a problem launching or connecting to it", slaveComputer.getName()), e);
                EC2AbstractSlave ec2AbstractSlave = (EC2AbstractSlave) slaveComputer.getNode();
                if (ec2AbstractSlave != null) {
                    ec2AbstractSlave.terminate();
                }
            }
        }

    }
}
