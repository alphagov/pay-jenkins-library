import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import static com.lesfurets.jenkins.unit.MethodCall.callArgsToString
import static org.junit.Assert.assertTrue

/**
 * Tests of the step deployEcs.
 * @author <a href="mailto:VictorMartinezRubio@gmail.com">Victor Martinez</a>
 */
class DeployEcsStepTests extends BasePipelineTest {
  static final String scriptName = 'vars/deployEcs.groovy'
  Map env = [:]

  @Override
  @Before
  void setUp() throws Exception {
    super.setUp()

    env.BRANCH_NAME = 'master'
    binding.setVariable('env', env)

    helper.registerAllowedMethod('booleanParam', [Map.class], { s -> s })
    helper.registerAllowedMethod('build', [Map.class], { 'OK' })
    helper.registerAllowedMethod('gitCommit', [ ], { 'foo' })
    helper.registerAllowedMethod('string', [Map.class], { s -> s })
  }

  @Test
  void test_default_behaviour() throws Exception {
    def script = loadScript(scriptName)
    script.call('microservice', 'aws_profile', 'tag')
    printCallStack()

    // Then triggers the deploy-ecs-microservice job
    assertTrue(helper.callStack.findAll { call ->
      call.methodName == 'build'
    }.any { call ->
      callArgsToString(call).contains('job=deploy-ecs-microservice')
    })

    // with parameters
    assertTrue(helper.callStack.findAll { call ->
      call.methodName == 'build'
    }.any { call ->
      callArgsToString(call).contains('parameters=[{name=MICROSERVICE, value=microservice}, {name=MICROSERVICE_IMAGE_TAG, value=tag}, {name=AWS_PROFILE, value=aws_profile}]')
    })

    assertJobStatusSuccess()
  }
}
