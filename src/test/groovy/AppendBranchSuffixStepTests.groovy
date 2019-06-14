import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import static org.junit.Assert.assertEquals

/**
 * Tests of the step appendBranchSuffix.
 * @author <a href="mailto:VictorMartinezRubio@gmail.com">Victor Martinez</a>
 */
class AppendBranchSuffixStepTests extends BasePipelineTest {
  static final String scriptName = 'vars/appendBranchSuffix.groovy'
  Map env = [:]

  @Override
  @Before
  void setUp() throws Exception {
    super.setUp()

    env.BRANCH_NAME = 'master'
    binding.setVariable('env', env)
  }

  @Test
  void test_with_master_branch() throws Exception {
    def script = loadScript(scriptName)
    def value = script.call('.prefix')
    assertEquals(value, '.prefix.master')
    printCallStack()
  }

  @Test
  void test_with_another_branch() throws Exception {
    def script = loadScript(scriptName)
    env.BRANCH_NAME = 'foo'
    def value = script.call('.prefix')
    assertEquals(value, '.prefix.PR')
    printCallStack()
  }
}
