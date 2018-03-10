package com.yourorganization.maven_sample;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;
import com.yourorganization.maven_sample.support.DirExplorer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import java.io.File;
import java.io.IOException;

import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.javadoc.Javadoc;
import java.util.Optional;

public class MethodCallsExample {

    public static void listMethodCalls(File projectDir) {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            System.out.println(Strings.repeat("=", path.length()));
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(MethodDeclaration n, Object arg) {
                        super.visit(n, arg);
                        String name = n.getNameAsString();
                        n.getJavadoc();
                        Optional comment_options  =  n.getJavadocComment();
                        System.out.println(" [L " + n.getBegin().get().line + "] " + n);
                        System.out.println("+++++++++name:" + name);
                        System.out.println("+++++++++declarae:" + n.getDeclarationAsString());
                        if(comment_options.isPresent()) {
                            JavadocComment commet_object = (JavadocComment) comment_options.get();
                            String commnet_str = commet_object.getContent();
                            System.out.println("+++++++++注释:" + commnet_str);
                        }

                    }
                }.visit(JavaParser.parse(file), null);
                System.out.println(); // empty line
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);
    }

    public static void main(String[] args) {
        File projectDir = new File("source_to_parse/springBootIntegertion/src/main/java/com/hdw/springboot/contorller");
        listMethodCalls(projectDir);
    }
}
