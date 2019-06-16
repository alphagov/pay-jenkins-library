import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

/**
 * Tests of the step cypress.
 * @author <a href="mailto:VictorMartinezRubio@gmail.com">Victor Martinez</a>
 */
class CypressTests extends BasePipelineTest {
  static final String scriptName = 'vars/cypress.groovy'
  Map env = [:]

  @Override
  @Before
  void setUp() throws Exception {
    super.setUp()

    env.BRANCH_NAME = 'master'
    env.BUILD_NUMBER = 1
    env.BUILD_TAG = 'tag'
    binding.setVariable('env', env)
    helper.registerAllowedMethod('build', [Map.class], { 'OK' })
    helper.registerAllowedMethod('gitCommit', [ ], { 'foo' })
    helper.registerAllowedMethod('sh', [Map.class], { 'bar' })
  }

  @Test
  void test_default_behaviour() throws Exception {
    def script = loadScript(scriptName)
    script.call()
    printCallStack()
    assertJobStatusSuccess()
  }
}
