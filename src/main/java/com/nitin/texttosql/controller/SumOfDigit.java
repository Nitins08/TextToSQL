package com.nitin.texttosql.controller;
import java.util.*;
public class SumOfDigit {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //System.out.print("Enter a number: ");
        //int num = sc.nextInt();
        //int sum = 0;
        //while (num != 0) {
        //    sum += num % 10;
        //    num /= 10;
        //}
        //System.out.println("Sum of digits: " + sum);
        int n=sc.nextInt();
        int rev=0;
        while(n!=0){
            int rem=n%10;
            rev=rev*10+rem;
            n/=10;
        }
        System.out.println(n==rev? "Palindrome":"Not Palindrome   ");
    }
}
