package compiler;

import gen.COOLLexer;
import gen.COOLListener;
import gen.COOLParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;

public class Compiler{
    public static void main(String[] args)  throws IOException {
        CharStream stream = CharStreams.fromFileName("./sample_code/sample1.cl");
        COOLLexer lexer = new COOLLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        COOLParser coolParser = new COOLParser(tokens);
        coolParser.setBuildParseTree(true);

        ParseTree parseTree = coolParser.program();
        ParseTreeWalker walker = new ParseTreeWalker();
        COOLListener coolListener = new ProgramPrinter(coolParser);
        walker.walk(coolListener, parseTree);

    }

}
