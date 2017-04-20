package org.drools.drlx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

import static com.github.javaparser.printer.PrintUtil.toDrl;
import static com.github.javaparser.printer.PrintUtil.toJava;

public class Generator {

    public static void main(String[] args) {
        System.out.println("Generator...");
        Stream.of(args).forEach(System.out::println);
        try {
            Files.list(Paths.get(args[0]))
                 .filter(path -> path.toFile().isFile() && path.toString().endsWith(".drlx"))
                 .forEach(x -> {
                    processDRLFilePath(x, Paths.get(args[1]), Paths.get(args[2]));
                });
        } catch (IOException e) {
            throw new RuntimeException( e );
        }
    }

    private static void processDRLFilePath(Path fromFile, Path toDirectoryJava, Path toDirectoryDrl) {
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
        writeFile(javaFilePath, toJava( compilationUnit ));
        System.out.println("Writing drl file: "+drlFilePath);
        writeFile(drlFilePath, toDrl( compilationUnit ));
    }

    private static void writeFile(Path path, String content) {
        /*
         Following code fails when generating sources with Intellij IDEA with this error:
         java.nio.channels.ClosedByInterruptException
         at java.nio.channels.spi.AbstractInterruptibleChannel.end(AbstractInterruptibleChannel.java:202)
         at sun.nio.ch.FileChannelImpl.write(FileChannelImpl.java:216)
         at java.nio.channels.Channels.writeFullyImpl(Channels.java:78)
        */
//        try {
//            Files.write(path, content.getBytes());
//        } catch (IOException e) {
//            throw new RuntimeException( e );
//        }

        File file = new File(path.toString());
        try (FileOutputStream fop = new FileOutputStream(file)) {
            if (!file.exists()) {
                file.createNewFile();
            }
            fop.write(content.getBytes());
            fop.flush();
        } catch (IOException e) {
            throw new RuntimeException( e );
        }
    }
}
