package pkg;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class Issue29 {

    public static void main(String[] args) {
        for (int cycle = 1; cycle < 2; cycle++) {
            List<String> orderedCollection = new ArrayList<String>();
            Set<String> filteredCollection = new LinkedHashSet<String>();
            for (int i = 0; i < 10000; i++) {
                orderedCollection.add(String.valueOf(i));
                if (Math.random() < 0.01) {
                    filteredCollection.add(String.valueOf(i));
                }
            }
            String[] strings = filteredCollection.toArray(new String[filteredCollection.size()]);
            for (int i = 0; i < strings.length; i++) {
                int index1 = (int) Math.floor(Math.random() * strings.length);
                int index2 = (int) Math.floor(Math.random() * strings.length);
                String tmp = strings[index1];
                strings[index1] = strings[index2];
                strings[index2] = tmp;
            }
            filteredCollection.clear();
            filteredCollection.addAll(Arrays.asList(strings));

            long start = System.currentTimeMillis();

            subset(orderedCollection, filteredCollection);
            System.out.println(cycle + ";" + String.valueOf(System.currentTimeMillis() - start)
                    + "");
        }


        List<String> orderedCollection = Arrays.asList("1", "2", "3", "2", "4", "5",
                "6", "7", "8", "10", "11", "12");
        Collection<String> filteredCollection = Arrays.asList("3", "1", "0", "2",
                "8", "10", "19", "9", "8");

        System.out.println(filteredCollection);
        System.out.println(orderedCollection);
        @SuppressWarnings("unused")
        long start = System.currentTimeMillis();
        System.out.println(subset(orderedCollection, filteredCollection));


    }


    public static <T> List<T> subset(List<T> orderedCollection,
            Collection<T> filteredCollection) {
        Preconditions.checkNotNull(orderedCollection);
        Preconditions.checkNotNull(filteredCollection);
        Collection<T> order = new LinkedHashSet<T>(orderedCollection);
        Collection<T> filter = new LinkedHashSet<T>(filteredCollection);
        order.retainAll(filter);
        return new ArrayList<T>(order);

    }

}