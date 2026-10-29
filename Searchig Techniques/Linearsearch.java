import java.util.Scanner;

public class Linearsearch {
    static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the size of the Array:");
        int n = sc.nextInt();
        int[] arr = new int[n];
        System.out.println("Enter the array elements");
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        System.out.println("Enter the Target Element:");
        int target = sc.nextInt();
        int result = linearSearch(arr, target);
        if (result != -1) {
            System.out.println("Element fount at index:" + result);

        } else {
            System.out.println("Element not found in the array");
        }
        sc.close();
    }
}