// * * * * * * * * * * * * * * * * * * * * * * * *
// * REDROCK-TEAM HOMEWORK 2 (2021 6)          *
// * Level 3 - Poker & Shuffle                   *
// * Level 4 - Texas Poker                       *
// * Author:  He Yujia   Chen Xinxu              *
// * Time:    2021 6                             *
// * * * * * * * * * * * * * * * * * * * * * * * *

package cn.com.caoyue.game.poker;

import java.util.*;

/**
 * Description:
 * <br>德州扑克类，提供定义、获取、比较方法
 * <br>This is homework in RedRockTeam.
 * <br>Using some algorithm by <a href="https://github.com/Jude95/Texas.git">Jude95</a>
 *
 * @author He Yujia   Chen Xinxu
 */

public class TexasPoker implements Comparable {
    private Card[] cards;
    private int value;

    /**
     * 随机牌堆<br>
     * 随机获得 7 张牌来组合成一个牌堆
     */
    public TexasPoker() {
        Poker poker = new Poker(false);
        poker.exchangeShuffle();
        cards = poker.getCards(7);
        Arrays.sort(cards);
        value = getResult();
    }

    /**
     * 自定义牌堆<br>
     * 自定义 7 张牌来组成一个牌堆
     * @param cards 需要自定义的 7 张牌组成的数组，必须是 Card 类型且 cards.length == 7
     */
    public TexasPoker(Card[] cards) {
        this.cards = cards;
        Arrays.sort(this.cards);
        value = getResult();
    }

    /**
     * 获得牌堆中的 7 张牌
     * @return 7 张牌组成的数组
     */
    public Card[] getCards() {
        return cards;
    }

    /**
     * 将这个德州扑克牌堆与另一个牌堆比较，返回比较结果
     * @param anotherTexasPoker 需要比较的另一个牌堆
     * @return 1大于，0等于，-1小于
     */
    @Override
    public int compareTo(Object anotherTexasPoker) {
        if (value == ((TexasPoker) anotherTexasPoker).value) {
            return 0;
        }
        if (value < ((TexasPoker) anotherTexasPoker).value) {
            return -1;
        }
        if (value > ((TexasPoker) anotherTexasPoker).value) {
            return 1;
        }
        return 0;
    }

    //朱大算法
    //是不是同花顺
    private boolean isStraightFlush(Card[] poker) {
        return isSameColor(poker) && isStraight(poker);
    }

    // 是不是四条
    private boolean isFourKind() {
        boolean flag = false;
        if (map.size() == 2) {
            for (int i : map.keySet()) {
                if (map.get(i) == 4) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    // 是不是葫芦
    private boolean isFullHouse() {
        boolean flag = false;
        if (map.size() == 2) {
            for (int i : map.keySet()) {
                if (map.get(i) == 3) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    //是不是同花
    private boolean isSameColor(Card[] poker) {
        Card.Suit color = poker[0].getSuitObj();
        boolean flag = true;
        for (int i = 0; i < poker.length; i++) {
            if (!color.equals(poker[i].getSuitObj())) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    // 是不是顺子
    private boolean isStraight(Card[] poker) {
        boolean flag = true;
        Arrays.sort(poker);
        int temp = poker[0].getPointNum();
        for (int i = 1; i < poker.length; i++) {
            if (poker[i].getPointNum() - temp == 1) {
                temp = poker[i].getPointNum();
            } else {
                flag = false;
                break;
            }
        }
        return flag;
    }

    // 是不是三条
    private boolean isThreeKind() {
        boolean flag = false;
        if (map.size() == 3) {
            for (int i : map.keySet()) {
                if (map.get(i) == 3) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    // 是不是两对
    private boolean isTwoPair() {
        boolean flag = false;
        if (map.size() == 3) {
            for (int i : map.keySet()) {
                if (map.get(i) == 2) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    // 是不是一对
    private boolean isOnePair() {
        boolean flag = false;
        if (map.size() == 4) {
            for (int i : map.keySet()) {
                if (map.get(i) == 2) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    private Card[] printCombination(Card[] array, byte[] bits) {
        List<Card> list = new ArrayList<Card>();
        for (int i = 0; i < bits.length; i++) {
            if (bits[i] == (byte) 1) {
                list.add(array[i]);
            }
        }
        Card[] res = new Card[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    private Card[][] combine(Card[] poker, int n, int length) {
        int t = 0;
        Card[][] res = new Card[length][n];
        boolean find = false;
        // 初始化移位法需要的数组
        byte[] bits = new byte[poker.length];
        for (int i = 0; i < bits.length; i++) {
            bits[i] = i < n ? (byte) 1 : (byte) 0;
        }

        do {
            // 找到10，换成01
            res[t] = printCombination(poker, bits);
            find = false;

            for (int i = 0; i < poker.length - 1; i++) {
                if (bits[i] == 1 && bits[i + 1] == 0) {
                    find = true;
                    bits[i] = 0;
                    bits[i + 1] = 1;

                    if (bits[0] == 0) // 如果第一位为0，则将第i位置之前的1移到最左边，如为1则第i位置之前的1就在最左边，无需移动
                    {
                        for (int k = 0, j = 0; k < i; k++) // O(n)复杂度使1在前0在后
                        {
                            if (bits[k] == 1) {
                                byte temp = bits[k];
                                bits[k] = bits[j];
                                bits[j] = temp;
                                j++;
                            }
                        }
                    }

                    break;
                }
            }
            t++;
        } while (find);

        return res;
    }

    private int getResult() {
        Card[][] res;
        Card[] poker = cards;
        int temp = 0;
        int s = 0;
        res = combine(poker, 5, 21);
        for (int j = 0; j < res.length; j++) {
            s = getCode(res[j]);
            if (temp < s) {
                temp = s;
            }
        }
        return temp;
    }

    private final int s = 579194;
    private final int a = 10 * s;
    private final int b = 9 * s;
    private final int c = 8 * s;
    private final int d = 7 * s;
    private final int e = 6 * s;
    private final int f = 5 * s;
    private final int g = 4 * s;
    private final int h = 3 * s;
    private final int l = 2 * s;

    private HashMap<Integer, Integer> map;

    private int getCode(Card[] poker) {
        initMap(poker);
        Arrays.sort(poker);
        int sum = 0;
        for (int i = 0; i < poker.length; i++) {
            sum += poker[i].getPointNum() * Math.pow(14, i);
        }
        if (isStraightFlush(poker)) {
            sum += a;
        } else if (isFourKind()) {
            sum += b;
        } else if (isFullHouse()) {
            sum += c;
        } else if (isSameColor(poker)) {
            sum += d;
        } else if (isStraight(poker)) {
            sum += e;
        } else if (isThreeKind()) {
            sum += f;
        } else if (isTwoPair()) {
            sum += g;
        } else if (isOnePair()) {
            sum += h;
        } else {
            sum += l;
        }
        resetMap();
        return sum;
    }

    private void resetMap() {
        map.clear();
    }

    private void initMap(Card[] poker) {
        map = new HashMap<Integer, Integer>();
        for (int i = 0; i < poker.length; i++) {
            if (map.containsKey(poker[i].getPointNum())) {
                map.put(poker[i].getPointNum(), (map.get(poker[i].getPointNum()) + 1));
            } else {
                map.put(poker[i].getPointNum(), 1);
            }
        }
    }
}
