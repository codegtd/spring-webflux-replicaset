package com.mongo.rs;

import com.mongo.rs.modules.ReplicasetAuthTest;
import com.mongo.rs.modules.ReplicasetAuthTransactionsTest;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("SingleNode-Rs-Auth-Suite")
@SelectClasses({
     ReplicasetAuthTest.class,
     ReplicasetAuthTransactionsTest.class})
@IncludeTags({"replicaset"})
public class ReplicasetAuthSuite {
}