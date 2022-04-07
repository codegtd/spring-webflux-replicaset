package com.mongo.rs;


import com.mongo.rs.modules.CommonTestcontainerTest;
import com.mongo.rs.modules.TransactionsTestcontainerTest;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Tc-Compose: TestcontainerSuite")
@SelectClasses({
     CommonTestcontainerTest.class,
     TransactionsTestcontainerTest.class})
@IncludeTags({"testcontainer"})
public class TestcontainerSuiteTests {
}