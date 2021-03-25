package compiler;

import gen.COOLListener;
import gen.COOLParser;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ProgramPrinter  implements COOLListener {
    private static final int CLASS_INDENTATION = 1;
    private static final int METHOD_INDENTATION = 2;
    private static final int METHOD_PARAMETERS_INDENTATION = 3;
    private static final int PROPERTY_INDENTATION = 2;
    private static final int METHOD_FIELD_INDENTATION = 3;

    String[] ruleNames;

    private Parser parser;

    public ProgramPrinter(Parser parser){
        this.parser = parser;
        ruleNames = parser.getRuleNames();
    }

    @Override
    public void enterProgram(COOLParser.ProgramContext ctx) {
        System.out.printf("program start {");
    }

    @Override
    public void exitProgram(COOLParser.ProgramContext ctx) {
        System.out.printf("\n}");
    }

    @Override
    public void enterClasses(COOLParser.ClassesContext ctx) {

    }

    @Override
    public void exitClasses(COOLParser.ClassesContext ctx) {

    }

    @Override
    public void enterEof(COOLParser.EofContext ctx) {

    }

    @Override
    public void exitEof(COOLParser.EofContext ctx) {

    }

    @Override
    public void enterClassDefine(COOLParser.ClassDefineContext ctx) {
        System.out.printf("\n%s"," ".repeat(4*CLASS_INDENTATION));
        System.out.print("class : "+ctx.TYPEID(0)+ "/ class parents: ");
        for (int i = 1 ; i < ctx.TYPEID().size() ; i++ )
            System.out.printf("%s, ",ctx.TYPEID(i));
        if (ctx.TYPEID().size() == 1)
            System.out.printf("%s, ","object");
        System.out.printf("{");
    }

    @Override
    public void exitClassDefine(COOLParser.ClassDefineContext ctx) {
        System.out.printf("\n%s"," ".repeat(4*CLASS_INDENTATION));
        System.out.printf("}");
    }

    @Override
    public void enterMethod(COOLParser.MethodContext ctx) {

        System.out.printf("\n%s"," ".repeat(4*METHOD_INDENTATION));
        System.out.printf("class method: %s/return type=%s {",ctx.OBJECTID(),ctx.TYPEID());
        if(ctx.formal().size() > 0){
            System.out.printf("\n%s"," ".repeat(4*METHOD_PARAMETERS_INDENTATION));
            System.out.printf("parameters list= [");

            for (int i = 0 ; i < ctx.formal().size() ; i++ )
                System.out.printf("%s %s, ",ctx.formal(i).TYPEID().getSymbol().getText(),ctx.formal(i).OBJECTID());
            System.out.printf("]");
        }


    }

    @Override
    public void exitMethod(COOLParser.MethodContext ctx) {
        System.out.printf("\n%s"," ".repeat(4*METHOD_INDENTATION));
        System.out.printf("}");
    }

    @Override
    public void enterProperty(COOLParser.PropertyContext ctx) {
        System.out.printf("\n%s"," ".repeat(4*PROPERTY_INDENTATION));
        System.out.printf("field: %s/ type=%s",ctx.OBJECTID(),ctx.TYPEID());
    }

    @Override
    public void exitProperty(COOLParser.PropertyContext ctx) {

    }

    @Override
    public void enterFormal(COOLParser.FormalContext ctx) {

    }

    @Override
    public void exitFormal(COOLParser.FormalContext ctx) {

    }

    @Override
    public void enterLetIn(COOLParser.LetInContext ctx) {
        System.out.printf("\n%s"," ".repeat(4*METHOD_FIELD_INDENTATION));
        System.out.printf("field: %s/ type=%s",ctx.OBJECTID(0),ctx.TYPEID(0));
    }

    @Override
    public void exitLetIn(COOLParser.LetInContext ctx) {

    }

    @Override
    public void enterMinus(COOLParser.MinusContext ctx) {

    }

    @Override
    public void exitMinus(COOLParser.MinusContext ctx) {

    }

    @Override
    public void enterString(COOLParser.StringContext ctx) {

    }

    @Override
    public void exitString(COOLParser.StringContext ctx) {

    }

    @Override
    public void enterIsvoid(COOLParser.IsvoidContext ctx) {

    }

    @Override
    public void exitIsvoid(COOLParser.IsvoidContext ctx) {

    }

    @Override
    public void enterWhile(COOLParser.WhileContext ctx) {

    }

    @Override
    public void exitWhile(COOLParser.WhileContext ctx) {

    }

    @Override
    public void enterDivision(COOLParser.DivisionContext ctx) {

    }

    @Override
    public void exitDivision(COOLParser.DivisionContext ctx) {

    }

    @Override
    public void enterNegative(COOLParser.NegativeContext ctx) {

    }

    @Override
    public void exitNegative(COOLParser.NegativeContext ctx) {

    }

    @Override
    public void enterBoolNot(COOLParser.BoolNotContext ctx) {

    }

    @Override
    public void exitBoolNot(COOLParser.BoolNotContext ctx) {

    }

    @Override
    public void enterLessThan(COOLParser.LessThanContext ctx) {

    }

    @Override
    public void exitLessThan(COOLParser.LessThanContext ctx) {

    }

    @Override
    public void enterBlock(COOLParser.BlockContext ctx) {

    }

    @Override
    public void exitBlock(COOLParser.BlockContext ctx) {

    }

    @Override
    public void enterId(COOLParser.IdContext ctx) {

    }

    @Override
    public void exitId(COOLParser.IdContext ctx) {

    }

    @Override
    public void enterMultiply(COOLParser.MultiplyContext ctx) {

    }

    @Override
    public void exitMultiply(COOLParser.MultiplyContext ctx) {

    }

    @Override
    public void enterIf(COOLParser.IfContext ctx) {

    }

    @Override
    public void exitIf(COOLParser.IfContext ctx) {

    }

    @Override
    public void enterCase(COOLParser.CaseContext ctx) {

    }

    @Override
    public void exitCase(COOLParser.CaseContext ctx) {

    }

    @Override
    public void enterOwnMethodCall(COOLParser.OwnMethodCallContext ctx) {

    }

    @Override
    public void exitOwnMethodCall(COOLParser.OwnMethodCallContext ctx) {

    }

    @Override
    public void enterAdd(COOLParser.AddContext ctx) {

    }

    @Override
    public void exitAdd(COOLParser.AddContext ctx) {

    }

    @Override
    public void enterNew(COOLParser.NewContext ctx) {

    }

    @Override
    public void exitNew(COOLParser.NewContext ctx) {

    }

    @Override
    public void enterParentheses(COOLParser.ParenthesesContext ctx) {

    }

    @Override
    public void exitParentheses(COOLParser.ParenthesesContext ctx) {

    }

    @Override
    public void enterAssignment(COOLParser.AssignmentContext ctx) {

    }

    @Override
    public void exitAssignment(COOLParser.AssignmentContext ctx) {

    }

    @Override
    public void enterFalse(COOLParser.FalseContext ctx) {

    }

    @Override
    public void exitFalse(COOLParser.FalseContext ctx) {

    }

    @Override
    public void enterInt(COOLParser.IntContext ctx) {

    }

    @Override
    public void exitInt(COOLParser.IntContext ctx) {

    }

    @Override
    public void enterEqual(COOLParser.EqualContext ctx) {

    }

    @Override
    public void exitEqual(COOLParser.EqualContext ctx) {

    }

    @Override
    public void enterTrue(COOLParser.TrueContext ctx) {

    }

    @Override
    public void exitTrue(COOLParser.TrueContext ctx) {

    }

    @Override
    public void enterLessEqual(COOLParser.LessEqualContext ctx) {

    }

    @Override
    public void exitLessEqual(COOLParser.LessEqualContext ctx) {

    }

    @Override
    public void enterMethodCall(COOLParser.MethodCallContext ctx) {

    }

    @Override
    public void exitMethodCall(COOLParser.MethodCallContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

//        if (parserRuleContext.getParent() != null)
//            System.out.println("rule name "+ ruleNames[parserRuleContext.getRuleIndex()]
//                    +", parent rule : "+ruleNames[parserRuleContext.getParent().getRuleIndex()]
//                    +", rule text : "+parserRuleContext.getText());

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}
