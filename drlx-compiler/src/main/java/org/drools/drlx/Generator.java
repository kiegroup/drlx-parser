package org.drools.drlx;

import static com.github.javaparser.printer.PrintUtil.toDrl;
import static com.github.javaparser.printer.PrintUtil.toJava;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

public class Generator {

    public static void main(String[] args) throws IOException {
        System.out.println("Generator...");
        Stream.of(args).forEach(System.out::println);
        Files.list(Paths.get(args[0]))
             .filter(path -> path.toFile().isFile() && path.toString().endsWith(".drlx"))
             .forEach(x -> {
                try {
                    processDRLFilePath(x, Paths.get(args[1]), Paths.get(args[2]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }

    private static void processDRLFilePath(Path fromFile, Path toDirectoryJava, Path toDirectoryDrl) throws IOException {
        CompilationUnit compilationUnit;
        try {
            compilationUnit = JavaParser.parse( new FileReader( fromFile.toFile() ) );
        } catch (FileNotFoundException e) {
            throw new RuntimeException( e );
        }
        
        ClassOrInterfaceDeclaration unitClass = (ClassOrInterfaceDeclaration) compilationUnit.getType( 0 );
        String pkg = compilationUnit.getPackageDeclaration().map( PackageDeclaration::getNameAsString ).orElse( "defaultpkg" );
        String unit = unitClass.getNameAsString();
        String unitPath = pkg.replace( ".", "/" ) + "/" + unit;
        
        Path javaDestinationPath = Paths.get(toDirectoryJava.toString(), pkg.replace( ".", "/" ));
        System.out.println("javaDestinationPath: "+javaDestinationPath);
        javaDestinationPath.toFile().mkdirs();
        
        Path drlDestinationPath = Paths.get(toDirectoryDrl.toString(), pkg.replace( ".", "/" ));
        System.out.println("drlDestinationPath: "+drlDestinationPath);
        drlDestinationPath.toFile().mkdirs();
        
        Path javaFilePath = Paths.get(toDirectoryJava.toString(), unitPath + ".java");
        Path drlFilePath = Paths.get(toDirectoryDrl.toString(), unitPath + ".drl");
        
        System.out.println("Writing java file: "+javaFilePath);
        Files.write(javaFilePath, toJava( compilationUnit ).getBytes());
        System.out.println("Writing drl file: "+drlFilePath);
        Files.write(drlFilePath, toDrl( compilationUnit ).getBytes());
    }
}
