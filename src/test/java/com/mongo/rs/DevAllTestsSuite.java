package com.mongo.rs;


import com.mongo.rs.modules.CommonTest;
import com.mongo.rs.modules.TransactionsTest;
import org.junit.platform.suite.api.*;

@Suite
@SuiteDisplayName("DevAllTestsSuite")
@SelectClasses({CommonTest.class, TransactionsTest.class})
@IncludeTags({"standalone","replicaset-transaction"})
public class DevAllTestsSuite {
}