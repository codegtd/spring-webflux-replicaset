package com.mongo.rs;

import com.mongo.rs.modules.CommonReplicasetAuthTest;
import com.mongo.rs.modules.TransactionsReplicasetAuthTest;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("DevSingleNodeReplicasetAuthSuiteTests")
@SelectClasses({
     CommonReplicasetAuthTest.class,
     TransactionsReplicasetAuthTest.class})
@IncludeTags({"replicaset"})
public class DevSingleNodeReplicasetAuthSuiteTests {
}