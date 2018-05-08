package org.protokruft

import kotlin.jvm.JvmField
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS
import static org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE

class TestRealBuild extends GroovyTestCase {
    @Rule
    @JvmField
    def root = new TemporaryFolder()
    File simpleProjectDir
    File configuredProjectDir

    def pluginClasspathResource = getClass().classLoader.findResource("plugin-classpath.txt")
    def pluginClasspath = pluginClasspathResource.readLines().collect { new File(it) }

    void setUp() {
        root.create()
        simpleProjectDir = root.newFolder("simple")
        configuredProjectDir = root.newFolder("configured")
        new File(simpleProjectDir, "build.gradle").write(new File(getClass().getResource("/simple.gradle").getFile()).text)
        new File(configuredProjectDir, "build.gradle").write(new File(getClass().getResource("/configured.gradle").getFile()).text)
    }

    void tearDown() {
        setUp()
    }

    void testDealWithIt() {
        def result = GradleRunner.create()
                .withProjectDir(simpleProjectDir)
                .withPluginClasspath(pluginClasspath)
                .withArguments("dealwithit")
                .build()

        assertEquals(UP_TO_DATE, result.task(":dealwithit").getOutcome())
        assertTrue(result.output.contains("(•_•) ( •_•)>⌐■-■ (⌐■_■)"))
    }

    void testMyTask() {
        def result = GradleRunner.create()
                .withProjectDir(simpleProjectDir)
                .withPluginClasspath(pluginClasspath)
                .withArguments("mytask")
                .build()

        assertEquals(SUCCESS, result.task(":mytask").getOutcome())
        assertTrue((new File(simpleProjectDir, "build/myfile.txt")).exists())
    }

    void testMyOtherTask() {
        def result = GradleRunner.create()
                .withProjectDir(simpleProjectDir)
                .withPluginClasspath(pluginClasspath)
                .withArguments("myothertask")
                .build()

        assertEquals(SUCCESS, result.task(":myothertask").getOutcome())
        assertTrue((new File(simpleProjectDir, "build/otherfile.txt")).exists())
    }

    void testConfiguration() {
        def testFile = new File(configuredProjectDir, "build/myfile.txt")

        def result = GradleRunner.create()
                .withProjectDir(configuredProjectDir)
                .withPluginClasspath(pluginClasspath)
                .withArguments("mytask")
                .build()

        assertEquals(SUCCESS, result.task(":mytask").getOutcome())
        assertTrue(testFile.exists())
        assertEquals("CONFIGURED", testFile.text)
    }
}