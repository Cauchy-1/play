// * * * * * * * * * * * * * * * * * * * * * * * *
// * REDROCK-TEAM HOMEWORK 2 (2021 6)          *
// * Level 3 - Poker & Shuffle                   *
// * Level 4 - Texas Poker                       *
// * Author:  He Yujia   Chen Xinxu              *
// * Time:    2021 6                             *
// * * * * * * * * * * * * * * * * * * * * * * * *
package cn.com.caoyue.game.poker;

import java.util.Random;  //提供对随机数的支持

/**
 * Description:
 * <br>单副扑克牌类，提供定义、获取牌、洗牌方法
 * <br>This is homework in RedRockTeam.
 * Some Algorithm Reference: http://att.newsmth.net/att.php?p.1032.47005.1743.pdf
 *
 * @author He Yujia   Chen Xinxu
 */

public class Poker {
    private Card[] poker;

    /**
     * 生成一副牌，如果需要大小王，则生成 54 张牌，如果不需要，则生成 52 张牌。按照 spades-2, clubs-2, hearts-2, diamonds-2, ..., spades-10, ..., spades-K, ..., spades-A, ..., BlackJoker, RedJoker 排列
     * @param isNeedJoker 是否需要大小王， true 需要， false 不需要
     */
    public Poker(boolean isNeedJoker) {
        int index = 0;
        poker = new Card[isNeedJoker ? 54 : 52];
        for (Card.Point point : Card.Point.values()) {
            for (Card.Suit suit : Card.Suit.values()) {
                poker[index] = new Card(suit.name(), point.name());
                index++;
            }
        }
        if (isNeedJoker) {
            poker[52] = new Card("Black");
            poker[53] = new Card("Red");
        }
    }

    /**
     * 获得这副牌
     * @return 存储每张卡片信息的数组
     */
    public Card[] getPoker() {
        return poker;
    }

    /**
     * 获得这副牌中的一些卡片
     * @param startIndex 起始索引值
     * @param numberOfCards 要获得卡片的张数
     * @return 存储这些卡片信息的数组
     */
    public Card[] getCards(int startIndex, int numberOfCards) {
        Card[] temp = new Card[numberOfCards];
        System.arraycopy(poker, startIndex, temp, 0, numberOfCards);
        return temp;
    }

    /**
     * 获得这副牌开头的一些卡片，相当于 getCards(0,numberOfCards)
     * @param numberOfCards 要获得卡片的张数
     * @return 存储这些卡片信息的数组
     */
    public Card[] getCards(int numberOfCards) {
        return getCards(0, numberOfCards);
    }

    //单次交叉洗牌 (不可单独使用，否则结果无随机性)
    private void crossShuffle() {
        Card[] pokerA = new Card[poker.length / 2];
        System.arraycopy(poker, 0, pokerA, 0, pokerA.length);
        Card[] pokerB = new Card[poker.length / 2];
        System.arraycopy(poker, pokerA.length, pokerB, 0, pokerB.length);
        for (int i = 0; i < pokerA.length + pokerB.length; i++) {
            poker[i] = ((i % 2 == 0) ? pokerA : pokerB)[i / 2];
        }
    }

    /**
     * 三次交叉洗牌<br>
     * 第一种洗牌方式，中间分开， 2 摞牌交叉洗。洗 3 次
     * @deprecated 此洗牌方式缺乏随机性
     */
    public void threeTimesCrossShuffle() {
        crossShuffle();
        crossShuffle();
        crossShuffle();
    }

    //交换两张牌
    private void exchange(int indexOfCard1, int indexOfCard2) {
        Card temp;
        temp = poker[indexOfCard1];
        poker[indexOfCard1] = poker[indexOfCard2];
        poker[indexOfCard2] = temp;
    }

    /**
     * 交换法洗牌<br>
     * 最后一张牌与他前面 53 张牌中随机一张交换位置。<br>倒数第二张牌与他前面 52 张牌中随机一张交换位置。<br>倒数第三张牌与他前面 51 张牌中随机一张交换位置。<br>…………<br>第 2 张与第一张交换
     */
    public void exchangeShuffle() {
        for (int i = poker.length - 1; i > 0; i--) {
            exchange(i, new Random().nextInt(i));
        }
    }

    //Reference: http://att.newsmth.net/att.php?p.1032.47005.1743.pdf
    //数组下标从1开始，from是圈的头部，mod是要取模的数 mod 应该为 2 * n + 1，时间复杂度O(圈长）
    private void cycleLeader(int from, int mod) {
        Card last = poker[from], t;
        int i;
        for (i = from * 2 % mod; i != from; i = i * 2 % mod) {
            t = poker[i];
            poker[i] = last;
            last = t;
        }
        poker[from] = last;
    }

    //翻转字符串时间复杂度O(to - from)
    private void reverse(int from, int to) {
        Card t;
        for (; from < to; ++from, --to) {
            t = poker[from];
            poker[from] = poker[to];
            poker[to] = t;
        }
    }

    //循环右移num位 时间复杂度O(n)
    private void rightRotate(int num, int n) {
        reverse(1, n - num);
        reverse(n - num + 1, n);
        reverse(1, n);
    }

    //完美洗牌算法 (不可单独使用，否则结果无随机性) 时间O(n)，空间O(1)
    private void inShuffle() {
        int n = poker.length / 2 - 1, k, m;
        for (k = 1; !(Math.pow(3, k) <= 2 * n && 2 * n < Math.pow(3, k + 1)); k++) ;
        m = (int) ((Math.pow(3, k) - 1) / 2);
        rightRotate(m, n);
        for (int i = 0; i < k; i++) {
            cycleLeader((int) Math.pow(3, i), 2 * n + 1);
        }
        reverse(2 * m + 1, 2 * n);
        exchange(poker.length - 1, new Random().nextInt(poker.length));
        exchangeShuffle();
    }

    //奇后置，偶前置 (不可单独使用，否则结果无随机性)
    private void gatherOddEven() {
        Card[] tempPoker = new Card[poker.length];
        for (int i = 0; i < poker.length / 2; i++) {
            tempPoker[i] = poker[2 * i];
        }
        for (int i = 1; i <= poker.length / 2; i++) {
            tempPoker[poker.length / 2 + i - 1] = poker[2 * i - 1];
        }
        poker = tempPoker;
    }

    /**
     * 随机组合方式洗牌<br>
     * 将几种方式随机组合来洗牌，这些方式包含：交叉洗牌、交换法洗牌、inShuffle、奇后偶前交换，洗 5 次，每次的随机选择一种方法
     */
    public void randomMethodShuffle() {
        for (int i = 1; i <= 5; i++) {
            switch (new Random().nextInt(4)) {
                case 0:
                    crossShuffle();
                    break;
                case 1:
                    exchangeShuffle();
                    break;
                case 2:
                    inShuffle();
                    break;
                case 3:
                    gatherOddEven();
                    break;
            }
        }
    }
}
