import astar.Cell;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by phamuyvu on 11/30/16.
 */
public class TestList {
    /*public static void main(String [] args) {
        List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        System.out.println(list.subList(1, list.size()));
    }*/

    public static void main(String args[]) {
//        Pattern pattern = Pattern.compile("(.*)\\.(.*dataset)\\.dt");;
        /*Pattern pattern = Pattern.compile("(.*)\\.dataset\\.dt");
        String string = "retaildate.retail454_dataset.dt";
        final Matcher matcher = string == null ? null : pattern.matcher(string);
        if (matcher == null || !matcher.matches()) {
            System.out.println("not matched: " + string);
        } else {
//            System.out.println("matched group 0: " + matcher.group(0));
            System.out.println("matched group 1: " + matcher.group(1));
        }*/

        PriorityQueue<Integer> queue = new PriorityQueue(16, (o1, o2) -> {
            Integer c1 = (Integer) o1;
            Integer c2 = (Integer) o2;

            return c1.intValue() < c2.intValue() ? -1 :
                    c1.intValue() > c2.intValue() ? 1 : 0;
        });

        Integer v1 = new Integer(1);
        Integer v2 = new Integer(2);
        Integer v3 = new Integer(3);
        queue.add(v2);
        queue.add(v1);
        queue.add(v3);

        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
    }
}
