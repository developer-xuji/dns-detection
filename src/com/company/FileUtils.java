package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtils {
    private static Logger logger = Logger.getLogger("FileUtils");

    public static List<String> getDnsListFromTxt(String fileName) {
        List<String> dnsList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)), "UTF-8"));
            String lineTxt = null;
            int count = 0;
            // read line by line
            while ((lineTxt = br.readLine()) != null) {
                logger.log(Level.INFO, lineTxt);
                dnsList.add(lineTxt);
                count++;
            }
            br.close();
            logger.log(Level.INFO,"count=" + count);
        } catch (Exception e) {
            logger.log(Level.WARNING,"Error Message :", e);
        }

        return dnsList;
    }

    public static void writeToTxt(String fileName, List<String> data) {
        try{
            File dataFile = new File(fileName);
            dataFile.delete();
            dataFile.createNewFile();

            FileOutputStream fos = new FileOutputStream(dataFile,true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            for (String d: data){
                bw.write(d);
                bw.newLine();
            }
            bw.flush();
            bw.close();
            osw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToCSV(String fileName, List<List<String>> data) {
        try {
            BufferedWriter out =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName),"UTF-8"));
            for (int i = 0; i < data.size(); i++)
            {
                List<String> onerow= data.get(i);

                for (int j = 0; j < onerow.size(); j++)
                {
                    out.write(onerow.get(j));
                    out.write(",");
                }
                out.newLine();
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
