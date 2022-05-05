import compiler.ClassNotDefinedException;
import compiler.ErrorFinder;
import compiler.FieldNotFoundException;
import compiler.MethodNotFoundException;
import gen.COOLLexer;
import gen.COOLParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;


public class ErrorFinderTest {


    public static Collection<Object[]> getErrorFinderTests() {
        return Arrays.asList(new Object[][] {
                {"./error_finder_test/sample1_method_not_found.cl", MethodNotFoundException.class},
                {"./error_finder_test/sample1_class_not_found.cl", ClassNotDefinedException.class},
                {"./error_finder_test/sample1_field_not_found.cl", FieldNotFoundException.class},
                {"./error_finder_test/sample1_duplicate_class.cl",null},
                {"./error_finder_test/sample1_duplicate_field.cl",null},
                {"./error_finder_test/sample1_duplicate_method.cl",null},
        });
    }

    @ParameterizedTest
    @MethodSource("getErrorFinderTests")
    public void testErrorFinder( String inputName , Class<? extends Exception> expectedException )  throws IOException {
        CharStream stream = CharStreams.fromFileName(inputName);
        COOLLexer lexer = new COOLLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        COOLParser coolParser = new COOLParser(tokens);
        coolParser.setBuildParseTree(true);
        coolParser.removeErrorListeners();
        ParseTree parseTree = coolParser.program();
        ParseTreeWalker walker = new ParseTreeWalker();


        ErrorFinder errorFinder = new ErrorFinder();
        if(expectedException != null){
            Exception exception = Assertions.assertThrows(expectedException , () -> {

                walker.walk(errorFinder, parseTree);

                errorFinder.checkUsedTypes();
            });

        }else{
            walker.walk(errorFinder, parseTree);

            errorFinder.checkUsedTypes();
        }



    }
}
