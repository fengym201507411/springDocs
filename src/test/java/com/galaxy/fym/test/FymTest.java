package com.galaxy.fym.test;

import com.galaxy.fym.model.FymComparator;
import com.galaxy.fym.model.FymCompareble;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by fengyiming on 2016/7/25.
 */
public class FymTest {

    private final static Integer SIZE = 10;

    private List<FymCompareble> comparebleList = new ArrayList<FymCompareble>(SIZE);

    private List<FymComparator> comparatorList = new ArrayList<FymComparator>(SIZE);

    private Random random = new Random();

    @Before
    public void init() {
        for (int i = 0; i < SIZE; i++) {
            int randomIndex = random.nextInt(SIZE);
            FymCompareble fymCompareble = new FymCompareble(SIZE - randomIndex, "index" + i);
            FymComparator fymComparator = new FymComparator(randomIndex, "index" + i);
            comparebleList.add(fymCompareble);
            comparatorList.add(fymComparator);
        }

        for (FymCompareble dto : comparebleList) {
            System.out.println("comparebleList :" + dto.getKey() + "--" + dto.getValue());
        }
        System.out.println("---------------------------------------------------------");
        for (FymComparator dto : comparatorList) {
            System.out.println("comparatorList :" + dto.getKey() + "--" + dto.getValue());
        }
        System.out.println("--------------------------sort before-------------------------------");
    }

    @Test
    public void printSort() {
        Collections.sort(comparebleList);
        Collections.sort(comparatorList, FymComparator.comparable);
    }

    @After
    public void print() {
        System.out.println("--------------------------sort after-------------------------------");
        for (FymCompareble dto : comparebleList) {
            System.out.println("comparebleList :" + dto.getKey() + "--" + dto.getValue());
        }
        System.out.println("---------------------------------------------------------------------------");
        for (FymComparator dto : comparatorList) {
            System.out.println("comparatorList :" + dto.getKey() + "--" + dto.getValue());
        }
    }
}
