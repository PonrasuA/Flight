package com.flight;

import java.text.MessageFormat;
import java.util.HashMap;

public class AircraftSeat {

	public static HashMap<Integer, Integer> updateMap(HashMap<Integer, Integer> oldMap, int k) {
		if (oldMap.containsKey(k)) {
			oldMap.put(k, oldMap.get(k) + 1);
		} else {
			oldMap.put(k, 1);
		}
		return oldMap;
	}

	public static HashMap<Object, HashMap<Integer, HashMap<Integer, Integer>>> updateMapOfMap(
			HashMap<Object, HashMap<Integer, HashMap<Integer, Integer>>> mapOfMap, HashMap<Integer, Integer> oldMap,
			int k, int j, int i) {
		if (mapOfMap.containsKey(k)) {
			HashMap<Integer, HashMap<Integer, Integer>> temp1 = mapOfMap.get(k);
			if (temp1.containsKey(i)) {
				HashMap<Integer, Integer> temp2 = temp1.get(i);
				temp2.put(j, oldMap.get(k));
			} else {
				HashMap<Integer, Integer> temp2 = new HashMap<Integer, Integer>();
				temp2.put(j, oldMap.get(k));
				temp1.put(i, temp2);
			}
		} else {
			HashMap<Integer, HashMap<Integer, Integer>> temp1 = new HashMap<Integer, HashMap<Integer, Integer>>();
			HashMap<Integer, Integer> temp2 = new HashMap<Integer, Integer>();
			temp2.put(j, oldMap.get(k));
			temp1.put(i, temp2);
			mapOfMap.put(k, temp1);
		}
		return mapOfMap;
	}

	public static int getValue(HashMap<Object, HashMap<Integer, HashMap<Integer, Integer>>> mapOfMap,
			HashMap<Integer, Integer> hm, int k, int j, int i) {
		HashMap<Integer, HashMap<Integer, Integer>> temp1 = mapOfMap.get(k);
		HashMap<Integer, Integer> temp2 = temp1.get(i);
		Integer mapGet = 0;
		if (hm.containsKey(k - 1)) {
			mapGet = hm.get(k - 1);
		}
		Integer indexGet = 0;
		if (temp2.containsKey(j)) {
			indexGet = temp2.get(j);
		}
		return mapGet + indexGet;
	}

	public static HashMap<Integer, Integer> accumulate(HashMap<Integer, Integer> oldMap) {
		for (int i = 1; i < oldMap.size(); ++i) {
			oldMap.put(i, oldMap.get(i) + oldMap.get(i - 1));
		}
		return oldMap;
	}

	public static Integer getSum(HashMap<Integer, Integer> dataMap) {
		Integer sumValue = 0;
		for (int i = 0; i < dataMap.size(); ++i) {
			sumValue += dataMap.get(i);
		}
		return sumValue;
	}

	private void seatBooking(int[][] input, int number) {
		HashMap<Integer, Integer> aisleMap = new HashMap<Integer, Integer>();
		HashMap<Object, HashMap<Integer, HashMap<Integer, Integer>>> aisleContains = new HashMap<>();
		HashMap<Integer, Integer> windowMap = new HashMap<Integer, Integer>();
		HashMap<Object, HashMap<Integer, HashMap<Integer, Integer>>> windowContains = new HashMap<>();
		HashMap<Integer, Integer> middleMap = new HashMap<Integer, Integer>();
		HashMap<Object, HashMap<Integer, HashMap<Integer, Integer>>> middleContains = new HashMap<>();

		for (int i = 0; i < input.length; ++i) {
			int numCols = input[i][0];
			int aisleValue = input[i][0] - 1;

			for (int j = 0; j < numCols; ++j) {
				if ((i == 0 && j == 0) || (i == input.length - 1 && j == aisleValue)) {
					for (int k = 0; k < input[i][1]; ++k) {
						windowMap = updateMap(windowMap, k);
						windowContains = updateMapOfMap(windowContains, windowMap, k, j, i);
					}
				} else if (j == 0 || j == aisleValue) {
					for (int k = 0; k < input[i][1]; ++k) {
						aisleMap = updateMap(aisleMap, k);
						aisleContains = updateMapOfMap(aisleContains, aisleMap, k, j, i);
					}
				} else {
					for (int k = 0; k < input[i][1]; ++k) {
						middleMap = updateMap(middleMap, k);
						middleContains = updateMapOfMap(middleContains, middleMap, k, j, i);
					}
				}
			}
		}

		Integer aisleSum = getSum(aisleMap);
		Integer windowSum = getSum(windowMap);

		aisleMap = accumulate(aisleMap);
		windowMap = accumulate(windowMap);
		middleMap = accumulate(middleMap);

		for (int i = 0; i < input.length; ++i) {
			String string = new String();
			int numCols = input[i][0];
			int aisleValue = input[i][0] - 1;
			for (int k = 0; k < input[i][1]; ++k) {
				for (int j = 0; j < numCols; ++j) {
					String temp = new String("**");
					if ((i == 0 && j == 0) || (i == input.length - 1 && j == aisleValue)) {
						Integer windowMapGet = getValue(windowContains, windowMap, k, j, i);
						Integer windowValue = windowMapGet + aisleSum;
						if (windowValue <= number) {
							temp = MessageFormat.format("{0}W", windowValue);
						}
					} else if (j == 0 || j == aisleValue) {
						Integer aisleMapGet = getValue(aisleContains, aisleMap, k, j, i);
						if (aisleMapGet <= number) {
							temp = MessageFormat.format("{0}A", aisleMapGet);
						}
					} else {
						Integer middleMapGet = getValue(middleContains, middleMap, k, j, i);
						Integer middleValue = middleMapGet + aisleSum + windowSum;
						if (middleValue <= number) {
							temp = MessageFormat.format("{0}C", middleMapGet + aisleSum + windowSum);
						}
					}
					string = string + temp + "\t";
				}
				string = string + "\n";
			}
			System.out.println(string);
		}
	}

	public static void main(String[] args) {
		int[][] input = { { 3, 2 }, { 4, 3 }, { 2, 3 }, { 3, 4 } };
		int number = 30;
		new AircraftSeat().seatBooking(input, number);
	}
}
