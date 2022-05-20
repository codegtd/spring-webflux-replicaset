package com.mongo.rs;

import com.mongo.rs.modules.CommonReplicasetNoAuthTest;
import com.mongo.rs.modules.TransactionsReplicasetNoAuthTest;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("DevThreeNodesReplicasetNoAuthSuiteTests")
@SelectClasses({
     CommonReplicasetNoAuthTest.class,
     TransactionsReplicasetNoAuthTest.class})
@IncludeTags({"replicaset"})
public class DevThreeNodesReplicasetNoAuthSuiteTests {
}