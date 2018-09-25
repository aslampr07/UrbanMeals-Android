package com.urbanmeals.client.urbanmeals.tools;

import android.graphics.Color;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class PieGraphTool {
    public static PieData SetPieGraphData(float value, float max, int colour){

        ArrayList<PieEntry> Entrylist = new ArrayList<>();
        Entrylist.add(new PieEntry(value, 0));
        Entrylist.add(new PieEntry(max - value, 1));
        PieDataSet dataSet = new PieDataSet(Entrylist, "");
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(colour);
        colors.add(Color.parseColor("#f3f4f7"));
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);
        return new PieData(dataSet);
    }
}