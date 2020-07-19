import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import static com.lesfurets.jenkins.unit.MethodCall.callArgsToString
import static org.junit.Assert.assertTrue

/**
 * Tests of the step runLocalPayScriptsUpdate.
 * @author <a href="mailto:VictorMartinezRubio@gmail.com">Victor Martinez</a>
 */
class RunLocalPayScriptsUpdateStepTests extends BasePipelineTest {
  static final String scriptName = 'vars/runLocalPayScriptsUpdate.groovy'
  Map env = [:]

  @Override
  @Before
  void setUp() throws Exception {
    super.setUp()

    env.BRANCH_NAME = 'master'
    binding.setVariable('env', env)
    helper.registerAllowedMethod('build', [Map.class], { 'OK' })
  }

  @Test
  void test_default_behaviour() throws Exception {
    def script = loadScript(scriptName)
    script.call()
    printCallStack()

    // Then empty parameters
    assertTrue(helper.callStack.findAll { call ->
      call.methodName == 'build'
    }.any { call ->
      callArgsToString(call).contains('parameters=[]')
    })

    // Then triggers the run-scheduled-pay-scripts-update job
    assertTrue(helper.callStack.findAll { call ->
      call.methodName == 'build'
    }.any { call ->
      callArgsToString(call).contains('job=run-scheduled-pay-scripts-update')
    })
    assertJobStatusSuccess()
  }
}
