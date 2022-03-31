package com.mongo.rs;


import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.mongo.rs.modules")
@IncludeTags("replicaset-transaction")
public class ReplicasetSuiteTests {
}