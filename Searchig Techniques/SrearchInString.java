import java.util.Scanner;

public class SrearchInString {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter any Sting:");
        String str = sc.nextLine();
        System.out.println("Enter the character waht to find:");
        char target = sc.next().charAt(0);
        boolean result = search(str, target);
        if (result != false)
            System.out.println("Character is found");
        else
            System.out.println("Character Not Found");
        sc.close();
    }

    static boolean search(String str, char target) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == target)
                return true;
        }
        return false;
    }
}
