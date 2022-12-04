package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Detector {
    private static Logger logger = Logger.getLogger("Detector");

    public static double entropy(String dns) {
        int len = dns.length();
        List<Character> charList = new String(dns.toCharArray()).chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        List<Integer> frequence = charList.stream().collect(Collectors.toSet())
                .stream().map(c -> (int) charList.stream().filter(ch -> ch.equals(c)).count()).collect(Collectors.toList());
        logger.log(Level.INFO, dns + ": "+ frequence);

        //frequence.stream().reduce( (sum, f) -> sum - (f / len) * Math.log(f / len));
        double sum = 0.0;
        for(int i=0; i!=frequence.size(); ++i)
            sum = sum - (frequence.get(i) / (double)len) * Math.log(frequence.get(i) / (double)len)/Math.log(2);

        logger.log(Level.INFO, dns + ": "+ sum);
        return sum;
    }

    public static double jaccard(String s1, String s2) {
        List<Character> words1 = new String(s1.toCharArray()).chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        List<Character> words2 = new String(s2.toCharArray()).chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());

        List<Character> temp = new ArrayList<>();
        temp.addAll(words1);
        temp.addAll(words2);

        List<Character> union =  temp.stream().distinct().collect(Collectors.toList());
        List<Character> intersect = new ArrayList<>();
        List<Character> a = words1.stream().
                map(x -> words2.contains(x) ? x : null).
                collect(Collectors.toList());

        List<Character> b = words2.stream().
                map(x -> words1.contains(x) ? x : null).
                collect(Collectors.toList());

        intersect.addAll(a);
        intersect.addAll(b);

        intersect = intersect.stream().distinct().collect(Collectors.toList());
        intersect.removeAll(Collections.singleton(null));

        return 1.0 * intersect.size() / union.size();
    }
}
