import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CppCodeFormatter {

    public static String formatCppCode(String code) {
        // 去除多余的空白行
        String[] lines = code.split("\n");
        List<String> nonEmptyLines = new ArrayList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                nonEmptyLines.add(line);
            }
        }

        // 统一缩进为 4 个空格
        int indentLevel = 0;
        StringBuilder formattedCode = new StringBuilder();
        for (String line : nonEmptyLines) {
            line = line.trim();
            if (line.endsWith("{")) {
                formattedCode.append("    ".repeat(indentLevel)).append(line).append("\n");
                indentLevel++;
            } else if (line.startsWith("}")) {
                indentLevel--;
                formattedCode.append("    ".repeat(indentLevel)).append(line).append("\n");
            } else {
                formattedCode.append("    ".repeat(indentLevel)).append(line).append("\n");
            }
        }

        String result = formattedCode.toString();
        // 清除分号前的空格
        result = result.replaceAll("\\s+;", ";");
        // 清除左括号前的空格
        result = result.replaceAll("\\s+\\(", "(");
        // 清除右括号前的空格
        result = result.replaceAll("\\s+\\)", ")");

        return result;
    }

    public static void formatFile(String filePath) {
        StringBuilder code = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            System.out.println("开始读取文件: " + filePath);
            String line;
            while ((line = reader.readLine()) != null) {
                code.append(line).append("\n");
            }
            System.out.println("文件读取成功，代码长度: " + code.length());
        } catch (IOException e) {
            System.err.println("读取文件时出错: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            String formattedCode = formatCppCode(code.toString());
            System.out.println("开始写入格式化后的代码，长度: " + formattedCode.length());
            writer.write(formattedCode);
            System.out.println("文件 " + filePath + " 格式化成功！");
        } catch (IOException e) {
            System.err.println("写入文件时出错: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String filePath ="" ;
        formatFile(filePath);
    }
}