import com.google.common.base.Objects;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by phamuyvu on 3/24/17.
 */
public class HashCodeWithOr {
    public static void main(String[] args) {
        ExtendIdentifyKey key1 = new ExtendIdentifyKey("a1", "b1");
        ExtendIdentifyKey key2 = new ExtendIdentifyKey("a2", "b2");
        ExtendIdentifyKey key3 = new ExtendIdentifyKey("a3", "b3");
        Map<ExtendIdentifyKey, ExtendIdentifyKey> map = new LinkedHashMap<ExtendIdentifyKey, ExtendIdentifyKey>();
        map.put(key1, key1);
        map.put(key2, key2);
        map.put(key3, key3);

        System.out.println("1. should be true: " + map.containsKey(new ExtendIdentifyKey("a1", "b11")));
        System.out.println("2. should be true: " + map.containsKey(new ExtendIdentifyKey("a11", "b1")));
        System.out.println("3. should be true: " + map.containsKey(new ExtendIdentifyKey("a1", "b1")));
        System.out.println("4. should be false: " + map.containsKey(new ExtendIdentifyKey("a4", "b4")));
    }

    private static class ExtendIdentifyKey {
        private String name;
        private String identifierPrefix;

        public ExtendIdentifyKey(String name, String identifierPrefix) {
            this.name = name;
            this.identifierPrefix = identifierPrefix;
        }

        public String getName() {
            return name;
        }

        public String getIdentifierPrefix() {
            return identifierPrefix;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != getClass()) {
                return false;
            }
            final ExtendIdentifyKey other = (ExtendIdentifyKey)obj;
            // the date dimension will be identical by name or identifierPrefix
            return Objects.equal(name, other.name) || Objects.equal(identifierPrefix, other.identifierPrefix);
        }

        @Override
        public int hashCode() {
            // the date dimension will be identical by name or identifierPrefix
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((identifierPrefix == null) ? 0 : identifierPrefix.hashCode());
            return result;
        }
    }
}
