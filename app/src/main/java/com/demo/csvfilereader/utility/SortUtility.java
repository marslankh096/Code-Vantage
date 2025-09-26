package com.demo.csvfilereader.utility;

import com.demo.csvfilereader.model.Conversion;

import java.util.ArrayList;


public class SortUtility {
    public static ArrayList<Conversion> reverseConversions(ArrayList<Conversion> arrayList) {
        ArrayList<Conversion> arrayList2 = new ArrayList<>();
        int size = arrayList.size() - 1;
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(arrayList.get(size));
            size--;
        }
        return arrayList2;
    }
}
