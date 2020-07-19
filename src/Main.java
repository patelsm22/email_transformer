import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        String filePath = "132443512.txt";
        String outputFilePath = "transformed-"+filePath;

        // Step 1. Read file content.

        List<String> linesList = readFileContent(filePath);


        //Step 2. Process for 'rules' triggger.
        boolean rulesFound = isMatchingRulesFound(linesList);

        if(!rulesFound){
            System.out.println("No rules match found in file: " + filePath);
            //Save untransformed file.
            saveToFile(linesList, outputFilePath);
            System.out.println("content Written to file: " + outputFilePath);
            return;
        }

        //Step 3 Transform File
        List<String> transformedList = transformFile(linesList);

        //Step 4 Save transformed file.
        saveToFile(transformedList, outputFilePath);
        System.out.println("Transformation completed for the file: " + filePath);
        System.out.println("Processed content Written to file: " + outputFilePath);
    }

    //save the lines to the file.
    private static void saveToFile(List<String> transformedList, String outputFilePath) {
        try {
            FileWriter fw = new FileWriter(outputFilePath);
            for (String line : transformedList) {
                fw.write(line);
                fw.write("\n");
            }
            fw.flush();
            fw.close();
        }catch (Exception e){
            System.out.println("Error writing to file: " + outputFilePath);
            System.exit(0);
        }
    }

    //Transform the given file content as per the rules.
    private static List<String> transformFile(List<String> linesList) {
        //        To:user@domain.net,user2@domain2.org
//        From:origin@somewhere.tv
//        Subject:Saying hi
//        Body:

        List<String> transformedList = new ArrayList<String>();
        boolean inBody = false;
        String transformed;
        for(String line: linesList){
           if(line.startsWith("Body:")){
               transformedList.add(line);
                inBody = true;
               continue;
            }

            if(inBody){
                transformed = transformLine(line);
                transformedList.add(transformed);
            }
            else{
                transformedList.add(line);
            }
        }

        return transformedList;
    }

    //Transform the given line as per the rules.
    private static String transformLine(String line) {
        //replace rule
        line = line.replaceAll("$", "e");
        line = line.replaceAll("^", "y");
        line = line.replaceAll("&", "u");

        //reverse rule
        char[] carr = line.toCharArray();
        int charsCount = 0;
        StringBuffer lineBuf = new StringBuffer();
        StringBuffer wordBuf = new StringBuffer();
        for (int i = 0; i < carr.length; i++) {
            //check if the current character is space
            if(carr[i] == ' '){
                //Check if any chars found before this space
                if(wordBuf.length() > 0){
                    //reverse the chars and add it to the final output
                    lineBuf.append(wordBuf.reverse());
                    //reset the word buffer.
                    wordBuf = new StringBuffer();
                }
                lineBuf.append(carr[i]);
            }
            else{
                //collect characters after the last space
                //append char to word buffer
                wordBuf.append(carr[i]);
            }
        }

        //Check if any characters left to be reversed
        if(wordBuf.length() > 0){
            //reverse the chars and add it to the final output
            lineBuf.append(wordBuf.reverse());
        }
        return lineBuf.toString();
    }

    //returns true if any of the rules matches.
    private static boolean isMatchingRulesFound(List<String> linesList) {
//        To:
//        From:
//        Subject:SECURE:
//        Body:

        boolean inBody = false;
        for(String line: linesList){
            if(line.startsWith("To:") && line.contains("domain.com")){
                    return true;
            }
            else  if(line.startsWith("Subject:SECURE:")){
                return true;
            }
            else if(line.startsWith("Body:")){
                inBody = true;
            }
            if(inBody){
                if(isLineWith10ConsecutiveNumbers(line)){
                    return true;
                }
            }
        }//for

        return false;
    }

    //returns true of the line contains 10 numbers consecutively
    private static boolean isLineWith10ConsecutiveNumbers(String line) {
        char[] carr = line.toCharArray();
        int charsCount = 0;
        for (int i = 0; i < carr.length; i++) {
            if(Character.isDigit(carr[i])){
                charsCount++;
                if(charsCount == 10){
                    return true;
                }
            }
            else{
                charsCount = 0;
            }
        }
        return  false;
    }

    //read the file content and return the list of lines.
    private static List<String> readFileContent(String filePath) {
        List<String> linesList = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = null;
            while(true){
                line = br.readLine();
                if(line == null){
                    break;
                }
                linesList.add(line);
            }
            br.close();
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error reading from file.");
            System.exit(0);
        }

        return linesList;
    }

}
