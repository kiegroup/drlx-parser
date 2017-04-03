/*
 * Copyright 2005 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.drlx;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.runtime.KieContainer;

import static com.github.javaparser.printer.PrintUtil.toDrl;
import static com.github.javaparser.printer.PrintUtil.toJava;
import static org.junit.Assert.assertTrue;

public class DrlxCompiler {

    public static CompiledUnit compile(InputStream source) {
        return compile(new InputStreamReader( source ));
    }

    public static CompiledUnit compile(Reader source) {
        CompilationUnit compilationUnit = JavaParser.parse( source );

        ClassOrInterfaceDeclaration unitClass = (ClassOrInterfaceDeclaration) compilationUnit.getType( 0 );
        String pkg = compilationUnit.getPackageDeclaration().map( PackageDeclaration::getNameAsString ).orElse( "defaultpkg" );
        String unit = unitClass.getNameAsString();

        KieServices ks = KieServices.get();
        ReleaseId releaseId = ks.newReleaseId( pkg, unit, "1.0" );

        KieModuleModel kproj = ks.newKieModuleModel();
        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.writeKModuleXML(kproj.toXML());
        kfs.writePomXML(getPom(releaseId));

        String unitPath = pkg.replace( ".", "/" ) + "/" + unit;
        String javaPath = "src/main/java/" + unitPath + ".java";
        String drlPath = "src/main/resources/" + unitPath + ".drl";

        kfs.write(drlPath, toDrl( compilationUnit ))
           .write(javaPath, toJava( compilationUnit ));

        KieBuilder kieBuilder = ks.newKieBuilder( kfs );
        assertTrue(kieBuilder.buildAll().getResults().getMessages().isEmpty());
        KieContainer kieContainer = ks.newKieContainer( releaseId );

        return new CompiledUnit(pkg, unit, kieContainer);
    }

    private static String getPom(ReleaseId releaseId) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
               "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
               "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
               "  <modelVersion>4.0.0</modelVersion>\n" +
               "\n" +
               "  <groupId>" + releaseId.getGroupId() + "</groupId>\n" +
               "  <artifactId>" + releaseId.getArtifactId() + "</artifactId>\n" +
               "  <version>" + releaseId.getVersion() + "</version>\n" +
               "</project>\n";
    }
}
