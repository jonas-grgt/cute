package io.toolisticon.cute.integrationtest.junit5;


import io.toolisticon.cute.CompileTestBuilder;
import io.toolisticon.cute.UnitTest;
import io.toolisticon.cute.extension.api.AssertionSpiServiceLocator;
import io.toolisticon.cute.extension.junit5.JUnit5Assertion;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * Test class to test testng extension.
 */
public class Junit5Test {

    @Test
    public void testServiceLocator() {

        MatcherAssert.assertThat(AssertionSpiServiceLocator.locate().getClass(), Matchers.is((Class) JUnit5Assertion.class));

    }

    @Test
    public void warningMessageTest() {

        CompileTestBuilder
                .unitTest()
                .defineTest(new UnitTest<Element>() {
                    @Override
                    public void unitTest(ProcessingEnvironment processingEnvironment, Element element) {
                        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.WARNING, "WARNING!");
                    }
                })
                .expectWarningMessageThatContains("WARNING!")
                .compilationShouldSucceed()
                .executeTest();


    }

    @Test
    public void successfullFailingCompilationTest_ByErrorMessage() {

        CompileTestBuilder
                .unitTest()
                .defineTest(new UnitTest<Element>() {
                    @Override
                    public void unitTest(ProcessingEnvironment processingEnvironment, Element element) {
                        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, "ERROR!");
                    }
                })
                .expectErrorMessageThatContains("ERROR!")
                .compilationShouldFail()
                .executeTest();


    }

    @Test
    public void failingCompilationTest_ByErrorMessage() {

        try {
            CompileTestBuilder
                    .unitTest()
                    .defineTest(new UnitTest<Element>() {
                        @Override
                        public void unitTest(ProcessingEnvironment processingEnvironment, Element element) {
                            processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, "ERROR!");
                        }
                    })
                    .compilationShouldSucceed()
                    .executeTest();

            Assertions.fail("Should have failed");
        } catch (AssertionError error) {
            Assertions.assertEquals(error.getMessage(), "Compilation should have succeeded but failed");
        }

    }

}
