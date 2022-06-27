package com.mongo.rs;

import com.mongo.rs.modules.ReplicasetTest;
import com.mongo.rs.modules.ReplicasetTransactionsTest;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("SingleNode|ThreeNodes: Rs-Suite")
@SelectClasses({
     ReplicasetTest.class,
     ReplicasetTransactionsTest.class})
@IncludeTags({"replicaset"})
public class ReplicasetSuite {
}