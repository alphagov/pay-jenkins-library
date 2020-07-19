import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import static com.lesfurets.jenkins.unit.MethodCall.callArgsToString
import static org.junit.Assert.assertTrue

/**
 * Tests of the step deploy.
 * @author <a href="mailto:VictorMartinezRubio@gmail.com">Victor Martinez</a>
 */
class DeployStepTests extends BasePipelineTest {
  static final String scriptName = 'vars/deploy.groovy'
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
    script.call('microservice', 'aws_profile', 'tag', false, true, 'smoke_tags', false)
    printCallStack()

    // Then triggers the deploy-pipeline-microservice job
    assertTrue(helper.callStack.findAll { call ->
      call.methodName == 'build'
    }.any { call ->
      callArgsToString(call).contains('job=deploy-pipeline-microservice')
    })

    // with parameters
    assertTrue(helper.callStack.findAll { call ->
      call.methodName == 'build'
    }.any { call ->
      callArgsToString(call).contains('parameters=[{name=MICROSERVICE, value=microservice}, {name=CONTAINER_VERSION, value=tag}, {name=AWS_PROFILE, value=aws_profile}, {name=TAG_AFTER_DEPLOYMENT, value=false}, {name=RUN_TESTS, value=true}, {name=SMOKE_TAGS, value=smoke_tags}, {name=PROMOTED_ENV, value=false}]')
    })

    assertJobStatusSuccess()
  }
}
