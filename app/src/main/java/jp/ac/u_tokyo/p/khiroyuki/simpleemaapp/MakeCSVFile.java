package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class MakeCSVFile {
    public void saveCSV(String folderPath, ArrayList<String[]> outputArray){
        Date currentDate = new Date();
        try {
            File output_file = new File(folderPath, "EMA_output"+currentDate.getTime()+".csv");
            FileWriter fw = new FileWriter(output_file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String[] outputLine :outputArray){
                bw.write(outputLine[0]+',');
                bw.write(outputLine[1]+',');
                bw.write(outputLine[2]);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
