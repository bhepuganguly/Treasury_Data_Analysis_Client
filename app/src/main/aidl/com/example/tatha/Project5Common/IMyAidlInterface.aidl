// IMyAidlInterface.aidl
package com.example.tatha.Project5Common;
// IMyAidlInterface.aidl



// Declare any non-default types here with import statements

interface IMyAidlInterface {

    int[] monthlyCash(int year);
    int[] dailyCash(int day, int month,int year, int wDays);
    int yearlyAvg(int year);
}
