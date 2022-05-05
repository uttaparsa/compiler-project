package compiler;

import gen.COOLListener;
import gen.COOLParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;


class SymbolData {

    ArrayList<String> properties = new ArrayList<>();

    String type;
    String parent;
    int startLine;
    int startColumn;

    public SymbolData() {

    }


    public SymbolData(String type, String parent) {
        this.type = type;
        this.parent = parent;
    }

    public void addProperty(String property) {
        properties.add(property);
    }


    public void setStartPosition(int startLine, int startColumn) {
        this.startLine = startLine;
        this.startColumn = startColumn;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getStartColumn() {
        return startColumn;
    }


    @Override
    public String toString() {
        return "SymbolData{" +
                " type='" + type + '\'' +
                ", parent='" + parent + '\'' +
                ", startLine=" + startLine +
                ", startColumn=" + startColumn +
                '}';
    }

    public String getParent() {
        return parent;
    }
}


public class ErrorFinder implements COOLListener {

    LinkedHashMap<String, SymbolData> symbols = new LinkedHashMap<String, SymbolData>();
    Stack<String> scopes = new Stack<>();
    ArrayList<UsedType> usedTypes = new ArrayList<>();

    @Override
    public void enterProgram(COOLParser.ProgramContext ctx) {
        SymbolData programSymbolData = new SymbolData();
        String programKey = "global_" + "PROGRAM";
        programSymbolData.setStartPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        symbols.put(programKey, programSymbolData);
        scopes.push(programKey);
    }

    @Override
    public void exitProgram(COOLParser.ProgramContext ctx) {
        scopes.pop();
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
        String classKey = "class_" + ctx.TYPEID(0);
        SymbolData classSymbolData = new SymbolData("class", scopes.peek());

        classSymbolData.setStartPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        if (symbols.containsKey(classKey)) {
            String errorMessage = String.format("Error101: in line [%d:%d], class %s has been defined already"
                    , ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(), ctx.TYPEID(0));
            System.out.println(errorMessage);
            classKey += "_" +ctx.getStart().getLine() + "_" +ctx.getStart().getCharPositionInLine();
        }
        symbols.put(classKey, classSymbolData);
        scopes.push(classKey);

    }

    @Override
    public void exitClassDefine(COOLParser.ClassDefineContext ctx) {
        scopes.pop();
    }


    @Override
    public void enterMethod(COOLParser.MethodContext ctx) {
        String methodKey = getMethodKey(ctx.OBJECTID().toString());
        SymbolData methodSymbolData = new SymbolData(ctx.TYPEID().toString(), scopes.peek());
        methodSymbolData.setStartPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        if (methodExistsInClass(methodKey)) {
            String errorMessage = String.format("Error102: in line [%d:%d], method %s has been defined already"
                    , ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(), ctx.OBJECTID());
            System.out.println(errorMessage);
            methodKey+=  "_"+ctx.getStart().getLine()+"_"+ctx.getStart().getCharPositionInLine();
        }
        if (ctx.formal().size() > 0) {

            for (int i = 0; i < ctx.formal().size(); i++) {

                addNewField(ctx.formal(i).OBJECTID().getText(), ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(), ctx.formal(i).TYPEID().getSymbol().getText());
            }

        }
        scopes.push(methodKey);
        symbols.put(methodKey, methodSymbolData);


    }

    private void addNewField(String fieldName, int startLine, int startColumn, String fieldType)  {
        String fieldKey = "field_" + fieldName;
        SymbolData fieldSymbolData = new SymbolData(fieldType, scopes.peek());
        try {
            checkDuplicateField(fieldName, startLine, startColumn);

        }catch (DuplicateDefinitionException exception){
            exception.printStackTrace();
            fieldKey += "_"+startLine+"_"+startColumn;
        }
        symbols.put(fieldKey, fieldSymbolData);



    }


    @Override
    public void exitMethod(COOLParser.MethodContext ctx) {
        scopes.pop();
    }

    @Override
    public void enterProperty(COOLParser.PropertyContext ctx) {

        addNewField(ctx.OBJECTID().getText(), ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(), ctx.TYPEID().toString());

        usedTypes.add(new UsedType(ctx.TYPEID().toString(), ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine()));
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

        for (int i = 0; i < ctx.OBJECTID().size(); i++) {
            addNewField(ctx.OBJECTID(i).getText(), ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(), ctx.TYPEID(i).getSymbol().getText());
        }

        String letInKey = "letin_" + ctx.getStart().getLine() + "_" + ctx.getStart().getCharPositionInLine();
        SymbolData methodSymbolData = new SymbolData("letin", scopes.peek());
        methodSymbolData.setStartPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        symbols.put(letInKey, methodSymbolData);
        scopes.push(letInKey);

    }

    @Override
    public void exitLetIn(COOLParser.LetInContext ctx) {
        scopes.pop();
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
        String whileKey = "while_" + ctx.getStart().getLine() + "_" + ctx.getStart().getCharPositionInLine();
        SymbolData methodSymbolData = new SymbolData("while", scopes.peek());
        methodSymbolData.setStartPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        symbols.put(whileKey, methodSymbolData);
        scopes.push(whileKey);
    }

    @Override
    public void exitWhile(COOLParser.WhileContext ctx) {
        scopes.pop();
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
        String blockKey = "block_" + ctx.getStart().getLine() + "_" + ctx.getStart().getCharPositionInLine();
        SymbolData methodSymbolData = new SymbolData("block", scopes.peek());
        methodSymbolData.setStartPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        symbols.put(blockKey, methodSymbolData);
        scopes.push(blockKey);
    }

    @Override
    public void exitBlock(COOLParser.BlockContext ctx) {
        scopes.pop();
    }

    @Override
    public void enterId(COOLParser.IdContext ctx) {

        checkFieldExistence(ctx.OBJECTID().getText(), ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
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
        String ifKey = "if_" + ctx.getStart().getLine() + "_" + ctx.getStart().getCharPositionInLine();
        SymbolData methodSymbolData = new SymbolData("if", scopes.peek());
        methodSymbolData.setStartPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        symbols.put(ifKey, methodSymbolData);
        scopes.push(ifKey);
    }

    @Override
    public void exitIf(COOLParser.IfContext ctx) {
        scopes.pop();
    }

    @Override
    public void enterCase(COOLParser.CaseContext ctx) {

    }

    @Override
    public void exitCase(COOLParser.CaseContext ctx) {

    }

    @Override
    public void enterOwnMethodCall(COOLParser.OwnMethodCallContext ctx) {
        checkMethodExistence(ctx.OBJECTID().toString(), ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
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

        usedTypes.add(new UsedType(ctx.TYPEID().toString(), ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine()));
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
//        checkFieldExistence(ctx.OBJECTID().getText() , ctx.getStart().getLine() , ctx.getStart().getCharPositionInLine());
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
        checkMethodExistence(ctx.OBJECTID().toString(), ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
        if (ctx.TYPEID() != null) {
            usedTypes.add(new UsedType(ctx.TYPEID().toString(), ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine()));
        }

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

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }

    void checkDuplicateField(String fieldName, int startLine, int startColumn) throws DuplicateDefinitionException {
        if (fieldDefinedInCurrentScope(fieldName)) {
            String errorMessage = String.format("Error104 : in line [%d,%d] field [%s] has been defined already"
                    , startLine, startColumn, fieldName);
            throw new DuplicateDefinitionException(errorMessage);
        }
    }

    void checkFieldExistence(String fieldName, int startLine, int startColumn) {
        if (!fieldDefinedInCurrentScope(fieldName)) {
            throw new FieldNotFoundException(
                    String.format("Error106: in line [%d:%d], cannot find variable [%s]"
                            , startLine, startColumn, fieldName));
        }
    }

    private boolean fieldDefinedInCurrentScope(String fieldName) {
        String fieldKey = "field_" + fieldName;
        String[] reservedWords = {"self"};
        if (Arrays.asList(reservedWords).contains(fieldName)) {
            return true;
        }
        if (symbols.containsKey(fieldKey)) {
            SymbolData existing = symbols.get(fieldKey);
            for (int i = scopes.size() - 1; i >= 0; i--) {
                if (existing.getParent().equals(scopes.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void printHashMap() {
        for (Map.Entry<String, SymbolData> mapElement : symbols.entrySet()) {

            String key = mapElement.getKey();

            // Finding the value
            SymbolData value = mapElement.getValue();

            // print the key : value pair
            System.out.println(key + " : " + value);
        }
    }

    private String getMethodKey(String methodName) {
        return "method_" + methodName + "_" + scopes.peek();
//        return "method_" +methodName;
    }

    private boolean methodExistsInAnyClass(String methodName) {
        String[] builtinMethods = {"abort", "length", "substr", "a2i_aux", "i2a_aux", "concat", "out_string"};
        if (Arrays.asList(builtinMethods).contains(methodName)) {
            return true;
        }
        for (Map.Entry<String, SymbolData> mapElement : symbols.entrySet()) {

            String key = mapElement.getKey();
            if (key.startsWith("method_" + methodName)) {
                return true;
            }

        }
        return false;
    }

    public void checkMethodExistence(String methodID, int startLine, int startCol) {

        if (!methodExistsInAnyClass(methodID)) {
            throw new MethodNotFoundException(
                    String.format("Error105: in line [%d:%d], cannot find method [%s]"
                            , startLine, startCol, methodID));
        }
    }

    String[] primitiveTypes = {"String", "Bool", "Int"};

    public void checkUsedTypes() {
        for (var s : usedTypes) {
            String typeId = s.getType();
            String typeKey = "class_" + typeId;
            if (!symbols.containsKey(typeKey)) {
                if (!Arrays.asList(primitiveTypes).contains(typeId)) {
                    throw new ClassNotDefinedException(String.format("Error106: in line [%d:%d], cannot find class [%s]"
                            , s.getStartLine(), s.getStartColumn(), typeId));
                }
            }
        }
    }


    private boolean methodExistsInClass(String methodKey) {
        if (symbols.containsKey(methodKey)) {
            SymbolData existing = symbols.get(methodKey);
            return existing.getParent().equals(scopes.peek());
        }
        return false;
    }

    private void printStackTop() {
        System.out.println("Stack top:");
        for (int i = scopes.size() - 1; i >= 0; i--) {
            System.out.print(String.format("scopes[%d]=%s", i, scopes.get(i)));
        }
        System.out.println("");
    }

    private boolean fieldExistsInScope(String fieldName) {
        String fieldKey = "field_" + fieldName;
        if (symbols.containsKey(fieldKey)) {
            SymbolData existing = symbols.get(fieldKey);
            SymbolData existingParent = symbols.get(existing.getParent());
            SymbolData existingParentsParent = symbols.get(existingParent.getParent());
            return existing.getParent().equals(scopes.peek()) && scopes.get(scopes.size() - 2).equals(existingParent.getParent()) ; //&& scopes.get(scopes.size() - 3).equals(existingParentsParent.getParent())
        }
        return false;
    }


}
class DuplicateDefinitionException extends RuntimeException {
    public DuplicateDefinitionException(String message) {
        super(message);
    }
}


class UsedType {
    String type;
    int startLine;
    int startColumn;

    public String getType() {
        return type;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public UsedType(String type, int startLine, int startColumn) {
        this.type = type;
        this.startLine = startLine;
        this.startColumn = startColumn;
    }

    @Override
    public String toString() {
        return "UsedType{" +
                "type='" + type + '\'' +
                ", startLine=" + startLine +
                ", startColumn=" + startColumn +
                '}';
    }
}
