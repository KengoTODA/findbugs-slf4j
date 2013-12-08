package pkg;

import java.util.LinkedHashSet;
import java.util.Set;

public final class Issue29 {

    public static void main(String[] args) {
        Set<String> filteredCollection = new LinkedHashSet<String>();
        for (int i = 0; i < 10000; i++) {
            filteredCollection.add(String.valueOf(i));
        }
        @SuppressWarnings("unused")
        String[] array = new String[filteredCollection.size()];
    }

}