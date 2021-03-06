package archive;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("DockerCompose: StandaloneSuite")
@SelectClasses({
     CommonStandaloneTest.class,
     TransactionsStandaloneTest.class})
@IncludeTags({"standalone"})
public class DevStdProfileStandaloneSuiteTests {
}