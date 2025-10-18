package Homework_1;

public class Main {
    public static void main(String[] args) {

        HashMapa<Integer, String> hm = new HashMapa();
        hm.put(1, "one");
        hm.put(2, "two");
        hm.put(3, "three");
        hm.put(4, "four");
        System.out.println(hm);

        hm.remove(1);
        hm.remove( 3);
        System.out.println(hm);

        System.out.println(hm.get(2));

    }
}