import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MainTest {

    @Test
    public void testObjectsEqual() {
        Employee employeeA = new Employee("Leo", 123d);
        Employee employeeB = new Employee("Leo", 123d);
        Employee employeeC = new Employee("Elsa", 500d);
        Employee employeeD = new Employee("Elsa", 999d);

        Assertions.assertTrue(checkObjectsEqual(employeeA, employeeB));
        Assertions.assertFalse(checkObjectsEqual(employeeB, employeeC));
        Assertions.assertFalse(checkObjectsEqual(employeeC, employeeD));
        Assertions.assertFalse(checkObjectsEqual(employeeA, employeeD));
    }

    public boolean checkObjectsEqual(Object a, Object b) {
        Map<String, Object> mapA;
        Map<String, Object> mapB;
        try {
            mapA = convertUsingReflection(a);
            mapB = convertUsingReflection(b);
        } catch (IllegalAccessException e) {
            System.err.println("Error accessing object fields via reflection : " + e.getMessage());
            return false;
        }

        Set<String> setA = mapToSet(mapA);
        Set<String> setB = mapToSet(mapB);

        if (!setA.equals(setB)) {
            setA.removeAll(setB);
            System.err.println(String.join("\n", "\nNot equal", setA.toString(), setB.toString()));
            return false;
        }
        System.out.println(String.join("\n", "\nEqual", setA.toString(), setB.toString()));
        return true;
    }

    private Map<String, Object> convertUsingReflection(Object object) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field: fields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(object));
        }

        return map;
    }


    private Set<String> mapToSet(Map<String, Object> map) {
        Set<String> set = new HashSet<>();
        map.forEach((key, value) -> set.add(String.join(":", key, value.toString())));
        return set;
    }

    private record Employee(String name, Double salary) { }

}
