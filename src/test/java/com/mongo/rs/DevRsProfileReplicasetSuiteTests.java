package com.mongo.rs;

import com.mongo.rs.modules.CommonReplicasetTest;
import com.mongo.rs.modules.TransactionsReplicasetTest;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("DockerCompose: ReplicasetSuite")
@SelectClasses({
     CommonReplicasetTest.class,
     TransactionsReplicasetTest.class})
@IncludeTags({"replicaset"})
public class DevRsProfileReplicasetSuiteTests {
}