import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import static com.lesfurets.jenkins.unit.MethodCall.callArgsToString
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

/**
 * Tests of the step checkPactCompatibility.
 * @author <a href="mailto:VictorMartinezRubio@gmail.com">Victor Martinez</a>
 */
class CheckPactCompatibilityStepTests extends BasePipelineTest {
  static final String scriptName = 'vars/checkPactCompatibility.groovy'
  Map env = [:]

  @Override
  @Before
  void setUp() throws Exception {
    super.setUp()

    env.BRANCH_NAME = 'master'
    binding.setVariable('env', env)

    helper.registerAllowedMethod('echo', [String.class], { s -> s })
    helper.registerAllowedMethod('sh', [Map.class], { m -> m.script })
    helper.registerAllowedMethod('string', [Map.class], { s -> s })
    helper.registerAllowedMethod("withCredentials", [List.class, Closure.class], { list, closure ->
      list.each{ map ->
        map.each{ key, value ->
          if ('variable'.equals(key)) {
            binding.setVariable(value, 'defined')
          }
        }
      }
      def res = closure.call()
      list.each{ map ->
        map.each{ key, value ->
          if ('variable'.equals(key)) {
            binding.setVariable(value, null)
          }
        }
      }
      return res
    })
  }

  @Test
  void test_default_behaviour() throws Exception {
    def script = loadScript(scriptName)
    script.call('service', 'abcdef', 'v1.0')

    printCallStack()

    // Then it uses the right credentialsId
    assertTrue(helper.callStack.findAll { call ->
      call.methodName == 'string'
    }.any { call ->
      callArgsToString(call).contains('pact_broker_username')
    })

    // Then it uses the right broker URL
    assertTrue(helper.callStack.findAll { call ->
      call.methodName == 'sh'
    }.any { call ->
      callArgsToString(call).contains('https://pact-broker-test.cloudapps.digital/')
    })
    assertJobStatusSuccess()
  }
}
