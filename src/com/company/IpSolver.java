package com.company;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IpSolver {
    private static Logger logger = Logger.getLogger("IpSolver");
    private static String IP_FILE = "ipList.txt";
    private static String RESULT_FILE = "dnsResult.txt";
    private static String INVAILD = "invaild";
    private List<String> dnsList = null;
    private List<Integer> result = new ArrayList<>();

    public IpSolver(List<String> dnsList) {
        this.dnsList = dnsList;

        List<String> lastResult = FileUtils.getDnsListFromTxt(RESULT_FILE);
        if (lastResult.size() == dnsList.size())
            for (String r: lastResult)
                result.add(Integer.valueOf(r));
        else
            for (int i = 0; i < dnsList.size(); ++i)
                result.add(-1);
        solve();
    }

    public List<String> getResult() {
        List<String> stringList = new ArrayList<>();
        for (Integer r: result)
            stringList.add(r.toString());

        return stringList;
    }

    private void solve() {
        if (dnsList == null){
            logger.log(Level.WARNING,"Error Message :", "Set dns list first");
            return;
        }

        //Read last ip address
        List<String> lastIpList = FileUtils.getDnsListFromTxt(IP_FILE);
        List<String> currentIpList = new ArrayList<>();

        if (lastIpList.size() < dnsList.size()){
            lastIpList.clear();
            for (int i = 0; i < dnsList.size(); ++i)
                lastIpList.add(INVAILD);
        }

        for (int i = 0; i < dnsList.size(); ++i){
            System.out.println(i + ". " + dnsList.get(i));
            currentIpList.add(INVAILD);
            try{
                InetAddress[] ipList = InetAddress.getAllByName(dnsList.get(i));
                String ipString = "";
                for (InetAddress ip : ipList)
                    ipString += ip.toString();

                currentIpList.set(i, ipString);
                if (!ipString.equals(lastIpList.get(i)))
                    result.set(i, result.get(i) + 1);

            } catch (Exception e) {
                //logger.log(Level.WARNING, "Error Message :", e);
            }
        }
        FileUtils.writeToTxt(RESULT_FILE, getResult());
        FileUtils.writeToTxt(IP_FILE, currentIpList);
    }
}
