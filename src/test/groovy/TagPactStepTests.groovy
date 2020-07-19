import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import static com.lesfurets.jenkins.unit.MethodCall.callArgsToString
import static org.junit.Assert.assertTrue

/**
 * Tests of the step tagPact.
 * @author <a href="mailto:VictorMartinezRubio@gmail.com">Victor Martinez</a>
 */
class TagPactStepTests extends BasePipelineTest {
  static final String scriptName = 'vars/tagPact.groovy'
  Map env = [:]

  @Override
  @Before
  void setUp() throws Exception {
    super.setUp()

    binding.setVariable('env', env)

    helper.registerAllowedMethod('error', [String.class], { s ->
      updateBuildStatus('FAILURE')
      throw new Exception(s)
    })
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
    helper.registerAllowedMethod('sh', [Map.class], { '200' })
    script.call('service', '1.0', 'v1.0')
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
      callArgsToString(call).contains('https://pact-broker-test.cloudapps.digital/pacticipants')
    })

    assertJobStatusSuccess()
  }

  @Test
  void test_with_wrong_http_code() throws Exception {
    def script = loadScript(scriptName)
    helper.registerAllowedMethod('sh', [Map.class], { '404' })
    try{
      script.call('service', '1.0', 'v1.0')
      //NOOP
    } catch(e){
      //NOOP
    }
    printCallStack()
    // then an error is thrown
    assertTrue(helper.callStack.findAll { call ->
      call.methodName == 'error'
    }.any { call ->
      callArgsToString(call).contains('Tagging consumer pact did not return 200 OK or 201 CREATED')
    })
    assertJobStatusFailure()
  }
}
