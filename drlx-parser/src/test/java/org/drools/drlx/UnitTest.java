package org.drools.drlx;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.Assert;
import org.junit.Test;

import static com.github.javaparser.printer.PrintUtil.toDrl;

public class UnitTest {
    @Test
    public void toDrlTest() {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("unit1/AdultUnit.java");
        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
        CompilationUnit compilationUnit = JavaParser.parse(inputStreamReader );

        String drl = toDrl(compilationUnit);
        Assert.assertEquals("package org.unit1;\n" +
                                    "\n" +
                                    "import org.kie.api.runtime.rule.RuleUnit;\n" +
                                    "import org.kie.api.runtime.rule.DataSource;\n" +
                                    "\n" +
                                    "rule Adult when \n" +
                                    "p : /persons[age > 18]\n" +
                                    "then \n" +
                                    "System.out.println(p.getName());\n" +
                                    "end\n" +
                                    "\n", drl);
    }

}
