
package com.program;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solution {
	
	//this method generated randomly shuffled preference list for men and woman
	public static List<Object> preflist(int n) {
		int men = n + 10;
		int woman = n + 20;
		List<Integer> menlist = IntStream.rangeClosed(11, men).boxed().collect(Collectors.toList());
		List<Integer> womanlist = IntStream.rangeClosed(21, woman).boxed().collect(Collectors.toList());

		Map<Integer, List<Integer>> menpref = new HashMap<Integer, List<Integer>>();
		Map<Integer, List<Integer>> womanpref = new HashMap<Integer, List<Integer>>();

		for (int i = 0; i < n; i++) {
			List<Integer> womantemp = new ArrayList<>();
			List<Integer> mentemp = new ArrayList<>();
			for (int j = 0; j < n; j++) {
				womantemp.add(womanlist.get(j));
				mentemp.add(menlist.get(j));
			}
			Collections.shuffle(womantemp);
			Collections.shuffle(mentemp);
			menpref.put(menlist.get(i), womantemp);
			womanpref.put(womanlist.get(i), mentemp);

		}
		return Arrays.asList(menlist, womanlist, menpref, womanpref);
	}

	//algorithm implementation
	public static Map<Integer, Integer> galeShapleyAlgo(int n, Map<Integer, List<Integer>> menpref,
			Map<Integer, List<Integer>> womanpref, List<Integer> menlist, List<Integer> womanlist) {

		// Initialization
		List<Integer> menUnmatched = new ArrayList<>();
		List<Integer> womanUnmatched = new ArrayList<>();
		Map<Integer, Integer> pair = new HashMap<Integer, Integer>();
		Map<Integer, Integer> menIndex = new HashMap<Integer, Integer>();
		for (int i = 0; i < n; i++) {
			menIndex.put(menlist.get(i), 0);
		}
		for (int i = 0; i < n; i++) {
			menUnmatched.add(menlist.get(i));
			womanUnmatched.add(womanlist.get(i));
		}

		// start time
		long startTime = System.nanoTime();

		// algorithm starts
		while (!menUnmatched.isEmpty()) {

			int man = menUnmatched.get(0);
			List<Integer> hisPref = menpref.get(man);
			int indexnum = menIndex.get(man);
			int woman = hisPref.get(indexnum);
			List<Integer> herPref = womanpref.get(woman);

			if (womanUnmatched.contains(woman)) {
				pair.put(man, woman);
				menUnmatched.remove(menUnmatched.indexOf(man));
				womanUnmatched.remove(womanUnmatched.indexOf(woman));
			} else if (pair.containsValue(woman) && (hisPref.contains(woman))) {
				// getting current partner
				int currentPartner = 0;
				for (Object partner : pair.keySet()) {
					if (pair.get(partner).equals(woman)) {
						currentPartner = (int) partner;
					}
				}
				if (herPref.indexOf(man) < herPref.indexOf(currentPartner)) {

					pair.remove(currentPartner, woman);
					menUnmatched.add(currentPartner);
					pair.put(man, woman);
					menUnmatched.remove(menUnmatched.indexOf(man));
				}
			}

			indexnum++;
			menIndex.put(man, indexnum);

		}
		// end time
		long endTime = System.nanoTime();
		long totalTime = (endTime - startTime) / 1000;
		System.out.println("Time took to run algorithm " + totalTime + " microseconds");
		return pair;

	}
	
	//test for stable match
	public static void testForStableMatch(Map<Integer, Integer> pair, Map<Integer, List<Integer>> menpref,
			Map<Integer, List<Integer>> womanpref) {

		for (Map.Entry<Integer, Integer> matched : pair.entrySet()) {
			Integer man = matched.getKey();
			Integer woman = matched.getValue();
			List<Integer> hisPref = menpref.get(man);
			for (int i = 0; i < hisPref.size(); i++) {
				int tempwoman = hisPref.get(i);
				if (tempwoman == woman) {
					break;
				} else {
					// getting current partner
					int tempWomanPartner = 0;
					for (Object partner : pair.keySet()) {
						if (pair.get(partner).equals(tempwoman)) {
							tempWomanPartner = (int) partner;
						}

					}
					List<Integer> herPref = womanpref.get(tempwoman);
					if (herPref.indexOf(man) < herPref.indexOf(tempWomanPartner)) {
						System.out.println(man + " and " + tempwoman
								+ " are unstable partner and prefer each other over their current partner");

					}
				}
			}
			System.out.println(man + " and " + woman + " are stable match");

		}
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int n = 10;
		//for (int i = 0; i < 5; i++) {
		// call preflist method
		// men list, woman list auto generated
		// men's preference list and woman's preference list also auto generated
		List<Object> generatedpref = preflist(n);
		List<Integer> menlist = (List<Integer>) generatedpref.get(0);
		List<Integer> womanlist = (List<Integer>) generatedpref.get(1);
		Map<Integer, List<Integer>> menpref = (Map<Integer, List<Integer>>) generatedpref.get(2);
		Map<Integer, List<Integer>> womanpref = (Map<Integer, List<Integer>>) generatedpref.get(3);
		Map<Integer, Integer> finalPair = new HashMap<Integer, Integer>();
		System.out.println("men's preference list " + menpref);
		System.out.println("woman's preference list " + womanpref);

		// calling algorithm - returns matched pair
		finalPair = galeShapleyAlgo(n, menpref, womanpref, menlist, womanlist);
		//}
		
		System.out.println("Matched pair: " + finalPair);
		
		// test for stable match
		testForStableMatch(finalPair, menpref, womanpref);
		

	}

}
