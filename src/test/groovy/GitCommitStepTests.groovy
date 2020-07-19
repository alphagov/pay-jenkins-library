import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import static org.junit.Assert.assertEquals

/**
 * Tests of the step gitCommit.
 * @author <a href="mailto:VictorMartinezRubio@gmail.com">Victor Martinez</a>
 */
class GitCommitStepTests extends BasePipelineTest {
  static final String scriptName = 'vars/gitCommit.groovy'
  Map env = [:]

  @Override
  @Before
  void setUp() throws Exception {
    super.setUp()

    env.GIT_COMMIT = 'foo'
    binding.setVariable('env', env)
  }

  @Test
  void test_with_git_commit() throws Exception {
    def script = loadScript(scriptName)
    def value = script.call()
    assertEquals(value, 'foo')
    printCallStack()
    assertJobStatusSuccess()
  }

  @Test
  void test_without_git_commit() throws Exception {
    def script = loadScript(scriptName)
    env.GIT_COMMIT = null
    helper.registerAllowedMethod('sh', [Map.class], { 'bar' })
    def value = script.call()
    assertEquals(value, 'bar')
    printCallStack()
    assertJobStatusSuccess()
  }
}
