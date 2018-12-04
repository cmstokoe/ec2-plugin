package hudson.plugins.ec2;

import java.util.concurrent.TimeUnit;

import hudson.Extension;
import hudson.model.Descriptor;

import hudson.util.Secret;
import org.kohsuke.stapler.DataBoundConstructor;

public class SelfConnectingData extends AMITypeData {

    private final String bootDelay;

    @DataBoundConstructor
    public SelfConnectingData(String password, boolean useHTTPS, String bootDelay) {
        this.bootDelay = bootDelay;
    }

    @Override
    public boolean isWindows() {
        return false;
    }

    @Override
    public boolean isUnix() {
        return false;
    }
    
    @Override
    public boolean isSelfConnecting() {
        return true;
    }

    public String getBootDelay() {
        return bootDelay;
    }

    public int getBootDelayInMillis() {
        try {
            return (int) TimeUnit.SECONDS.toMillis(Integer.parseInt(bootDelay));
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<AMITypeData> {
        @Override
        public String getDisplayName() {
            return "self_connecting";
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bootDelay == null) ? 0 : bootDelay.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final SelfConnectingData other = (SelfConnectingData) obj;
        if (bootDelay == null) {
            if (other.bootDelay != null)
                return false;
        }       
        return bootDelay.equals(other.bootDelay);
    }
}
