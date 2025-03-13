import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class CppCodeFormatter {

    public static String formatCppCode(String code) {
        // 清除分号和括号前后的多余空格
        code = code.replaceAll("\\s+;", ";");
        code = code.replaceAll("\\s+\\(", "(");
        code = code.replaceAll("\\s+\\)", ")");

        StringBuilder formattedCode = new StringBuilder();
        Stack<Integer> indentStack = new Stack<>();
        indentStack.push(0);

        String[] lines = code.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }

            int currentIndent = indentStack.peek();
            if (line.contains("using namespace std;")) {
                formattedCode.append(" ".repeat(currentIndent)).append(line.replace("using namespace std;", "").trim());
                if (!formattedCode.toString().endsWith("\n")) {
                    formattedCode.append("\n");
                }
                formattedCode.append(" ".repeat(currentIndent)).append("using namespace std;").append("\n");
                continue;
            }

            if (line.startsWith("for") || line.startsWith("while") || line.startsWith("if")) {
                formattedCode.append(" ".repeat(currentIndent)).append(line);
                if (!line.endsWith("{")) {
                    formattedCode.append(" {").append("\n");
                } else {
                    formattedCode.append("\n");
                }
                indentStack.push(currentIndent + 2);

                // 处理循环体或条件体
                if (line.contains("{")) {
                    int startIndex = line.indexOf("{") + 1;
                    String body = line.substring(startIndex).trim();
                    if (body.endsWith("}")) {
                        body = body.substring(0, body.length() - 1).trim();
                    }
                    if (!body.isEmpty()) {
                        String[] statements = splitStatements(body);
                        for (String statement : statements) {
                            if (!statement.isEmpty()) {
                                formattedCode.append(" ".repeat(currentIndent + 2)).append(statement);
                                if (statement.endsWith(";")) {
                                    formattedCode.append("\n");
                                }
                            }
                        }
                    }
                }
            } else if (line.startsWith("else")) {
                indentStack.pop();
                currentIndent = indentStack.peek();
                formattedCode.append(" ".repeat(currentIndent)).append(line);
                if (!line.endsWith("{")) {
                    formattedCode.append(" {").append("\n");
                } else {
                    formattedCode.append("\n");
                }
                indentStack.push(currentIndent + 2);

                // 处理 else 块的内容
                if (line.contains("{")) {
                    int startIndex = line.indexOf("{") + 1;
                    String body = line.substring(startIndex).trim();
                    if (body.endsWith("}")) {
                        body = body.substring(0, body.length() - 1).trim();
                    }
                    if (!body.isEmpty()) {
                        String[] statements = splitStatements(body);
                        for (String statement : statements) {
                            if (!statement.isEmpty()) {
                                formattedCode.append(" ".repeat(currentIndent + 2)).append(statement);
                                if (statement.endsWith(";")) {
                                    formattedCode.append("\n");
                                }
                            }
                        }
                    }
                }
            } else if (line.startsWith("}")) {
                indentStack.pop();
                currentIndent = indentStack.peek();
                formattedCode.append(" ".repeat(currentIndent)).append("}").append("\n");
            } else if (line.endsWith("{")) {
                formattedCode.append(" ".repeat(currentIndent)).append(line).append("\n");
                indentStack.push(currentIndent + 2);
            } else {
                String[] statements = splitStatements(line);
                for (String statement : statements) {
                    if (!statement.isEmpty()) {
                        formattedCode.append(" ".repeat(currentIndent)).append(statement);
                        if (statement.endsWith(";")) {
                            formattedCode.append("\n");
                        }
                    }
                }
            }
        }

        return formattedCode.toString();
    }

    private static String[] splitStatements(String line) {
        StringBuilder temp = new StringBuilder();
        java.util.ArrayList<String> statements = new java.util.ArrayList<>();
        boolean inParentheses = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '(') {
                inParentheses = true;
            } else if (c == ')') {
                inParentheses = false;
            }
            if (c == ';' && !inParentheses) {
                temp.append(c);
                statements.add(temp.toString().trim());
                temp.setLength(0);
            } else {
                temp.append(c);
            }
        }
        if (temp.length() > 0) {
            statements.add(temp.toString().trim());
        }
        return statements.toArray(new String[0]);
    }

    public static void formatFile(String filePath) {
        StringBuilder code = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                code.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("读取文件时出错: " + e.getMessage());
            return;
        }

        String formattedCode = formatCppCode(code.toString());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(formattedCode);
        } catch (IOException e) {
            System.err.println("写入文件时出错: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String filePath = "E:\\myProjects\\test\\example.cpp";
        formatFile(filePath);
    }
}