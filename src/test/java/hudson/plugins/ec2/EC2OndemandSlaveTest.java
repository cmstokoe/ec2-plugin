package hudson.plugins.ec2;

import hudson.model.Node;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import hudson.slaves.NodeProperty;
import static org.junit.Assert.assertEquals;
import java.util.Collections;

public class EC2OndemandSlaveTest {

    @Rule
    public JenkinsRule r = new JenkinsRule();

    @Test
    public void testSpecifyMode() throws Exception {
      EC2OndemandSlave slaveNormal = new EC2OndemandSlave("description (instanceId)", "instanceId", "description", "templateDescription", null, "remoteFS", 1, "labelString", Node.Mode.NORMAL, "initScript", "tmpDir", Collections.<NodeProperty<?>> emptyList(), "remoteAdmin", "jvmopts", false, "30", "publicDNS", "privateDNS", null, "cloudName", false, false, 0, new UnixData("a", null, null, "b"));  
      assertEquals(Node.Mode.NORMAL, slaveNormal.getMode());
        
      EC2OndemandSlave slaveExclusive = new EC2OndemandSlave("description (instanceId)", "instanceId", "description", "templateDescription", null, "remoteFS", 1, "labelString", Node.Mode.EXCLUSIVE, "initScript", "tmpDir", Collections.<NodeProperty<?>> emptyList(), "remoteAdmin", "jvmopts", false, "30", "publicDNS", "privateDNS", null, "cloudName", false, false, 0, new UnixData("a", null, null, "b"));
      assertEquals(Node.Mode.EXCLUSIVE, slaveExclusive.getMode());
    }

}
