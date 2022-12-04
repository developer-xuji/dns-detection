package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String DNS_FILE = "E:\\NYIT\\domains.txt";
        String FAMOUS_LIST = "E:\\NYIT\\top-1000-websites.txt";
        String CSV_FILE = "E:\\NYIT\\dd2.csv";
        // Define csv header
        List<String> header = new ArrayList<>();
        header.add("DNS");
        header.add("Len");
        header.add("MLCN");
        header.add("Vowel");
        header.add("MLCC");
        header.add("NS");
        header.add("NP");
        header.add("Deceptive");
        header.add("Entropy");
        header.add("IP Distance");

        List<List<String>> data = new ArrayList<>();
        data.add(header);

        List<String> dnsList = FileUtils.getDnsListFromTxt(DNS_FILE);
        List<String> famousList = FileUtils.getDnsListFromTxt(FAMOUS_LIST);

        IpSolver ipSolver = new IpSolver(dnsList);
        List<String> ipResult = ipSolver.getResult();

        Detector detector = new Detector();
        for(String d: dnsList){
            List<String> dnsDetection = new ArrayList<>();
            dnsDetection.add(d);
            dnsDetection.add(String.valueOf(getLen(d)));
            dnsDetection.add(String.valueOf(findLongestNumStr(d)));
            dnsDetection.add(String.valueOf(findVowel(d)));
            dnsDetection.add(String.valueOf(findMLCC(d)));
            dnsDetection.add(String.valueOf(findNS(d)));
            dnsDetection.add(String.valueOf(findNP(d)));
            dnsDetection.add(isDeceptive(d, famousList));
            dnsDetection.add(String.valueOf(detector.entropy(d)));
            dnsDetection.add(ipResult.get(dnsList.indexOf(d)));
            data.add(dnsDetection);
        }

        FileUtils.writeToCSV(CSV_FILE, data);
    }

    public static int getLen(String str) {
        return str.trim().length();
    }


    public static int findLongestNumStr(String str){
        int maxStartIndex = 0, maxLen = 0;
        int tempStartIndex = 0, tempMaxLen = 0;

        for(int i = 0;i < str.length();i++){
            if (Character.isDigit(str.charAt(i))){
                if(i ==0 ||!Character.isDigit(str.charAt(i-1))){
                    tempStartIndex = i;
                    tempMaxLen = 0;
                    tempMaxLen ++;
                }else if(i!=0 && Character.isDigit(str.charAt(i-1))){
                    tempMaxLen ++;
                }
                if(i == str.length() -1 || !Character.isDigit(str.charAt(i+1))){
                    if(tempMaxLen >= maxLen){
                        maxStartIndex = tempStartIndex;
                        maxLen = tempMaxLen;
                    }
                }
            }
        }
        return maxLen;
    }

    public static double findVowel(String str) {
        int count = 0;
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == 'a' || str.charAt(i) == 'e' || str.charAt(i) == 'i' || str.charAt(i) == 'o'
                    || str.charAt(i) == 'u' || str.charAt(i) == 'y' || str.charAt(i) == 'w') {
                count++;
            }
        }
        int len = str.trim().length();
        double test = 1.0 * count / len;
        return  test;
    }

    public static int findNS(String str) {
        int count = 0;
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == '-') {
                count++;
            }
        }
        return  count;
    }

    public static int findNP(String str) {
        int count = 0;
        String[] arrOfStr = str.split("[0123456789]");
        for(int i = 0; i < arrOfStr.length; i++) {
            if(!arrOfStr[i].equals("")){
                count++;
            }
        }
        String lasString = arrOfStr[arrOfStr.length - 1];
        String[] test = lasString.split(".");

        return count;
    }

    public static int findMLCC(String str){
        int mlcc = 0;
        int curr = 0;
        for(int i = 0; i < str.length(); i++) {
            //if the char is consonant, the number of continuous consonant(curr) plus 1
            //else compare with the previous max number of continuous consonants, if it is longer then change the mlcc to curr
            if(str.charAt(i) == 'b' || str.charAt(i) == 'c'|| str.charAt(i) == 'd'|| str.charAt(i) == 'f'|| str.charAt(i) == 'g'|| str.charAt(i) == 'h'|| str.charAt(i) == 'j'|| str.charAt(i) == 'k'|| str.charAt(i) == 'l'|| str.charAt(i) == 'm'|| str.charAt(i) == 'n'|| str.charAt(i) == 'p'|| str.charAt(i) == 'q'|| str.charAt(i) == 'r'|| str.charAt(i) == 's'|| str.charAt(i) == 't'|| str.charAt(i) == 'v'|| str.charAt(i) == 'w'|| str.charAt(i) == 'x'|| str.charAt(i) == 'z') {
                curr++;
            }
            else{
                if(mlcc < curr){
                    mlcc = curr;
                }
                curr = 0;
            }
        }
        return mlcc;
    }

    public static String isDeceptive(String str, List<String> famousList) {
        String result = containSensitive(str);
        if(!result.equals("No")){
            return result;
        }

        result = containFamous(str, famousList);
        if(!result.equals("No")){
            return result;
        }

        result = getLevenshtein(str, famousList);
        if(!result.equals("No")){
            return result;
        }

        return "No";
    }

    public static String containSensitive(String str) {
        String[] sensitiveWords = {"bank", "ebay", "webscr", "account", "secure", "user", "login", "confirm"};
        for(String word : sensitiveWords){
            if(str.contains(word)){
                return word;
            }
        }
        return "No";
    }

    public static String containFamous(String str, List<String> famousList) {
        for(String famous: famousList){
            String[] parts = famous.split("\\.");
            String name = parts[0];
            if(name.length() > 4 && str.contains(name)){
                return name;
            }
        }
        return "No";
    }

    public static String getLevenshtein(String str, List<String> famousList) {
        for(String famous : famousList) {
            int result = getDistance(str, famous);
            if(result > 0 && result < 3) {
                return famous;
            }
        }

        return "No";
    }

    public static int getDistance(String s1, String s2)
    {
        int len1 = s1.length();
        int len2 = s2.length();
        int[][] d = new int[len1+1][len2+1];
        int i=0, j=0;
        for(i=0; i<=len1; i++)
            d[i][0] = i;
        for(j=0; j<=len2; j++)
            d[0][j] = j;
        for (i = 1; i < len1+1; i++)
            for (j = 1; j < len2+1; j++)
            {
                int cost = 1;
                if(s1.charAt(i-1) == s2.charAt(j-1))
                {
                    cost = 0;
                }
                int delete = d[i - 1][j] + 1;
                int insert = d[i][j - 1] + 1;
                int substitution = d[i - 1][j - 1] + cost;
                d[i][j] = min(delete, insert, substitution);
            }
        return (d[len1][len2]);
    }

    public static int min(int d,int i,int s)
    {
        int temp = 0;
        if(d>i)
            temp = i;
        else
            temp = d;
        return s<temp?s:temp;
    }
}
