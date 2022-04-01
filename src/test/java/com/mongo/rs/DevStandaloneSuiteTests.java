package com.mongo.rs;

import com.mongo.rs.modules.CommonTest;
import com.mongo.rs.modules.TransactionsTest;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("DevStandaloneSuiteTests")
@SelectClasses({CommonTest.class, TransactionsTest.class})
@IncludeTags("standalone")
public class DevStandaloneSuiteTests {
}