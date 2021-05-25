// * * * * * * * * * * * * * * * * * * * * * * * *
// * * * * * * * * * * * * * * * * * * * * * * * *
// * REDROCK-TEAM HOMEWORK 2 (2021 6)          *
// * Level 3 - Poker & Shuffle                   *
// * Level 4 - Texas Poker                       *
// * Author:  He Yujia   Chen Xinxu              *
// * Time:    2021 6                             *
// * * * * * * * * * * * * * * * * * * * * * * * *

package cn.com.caoyue.game.poker;

/**
 * Description:
 * <br>单张扑克牌类，提供定义、获取、比较方法
 * <br>This is homework in RedRockTeam.
 *
 * @author He Yujia   Chen Xinxu
 */

public class Card implements Comparable {
	
    enum Suit {
    	
        spades("♠"), clubs("♦"), hearts("♥"), diamonds("♣");
        private final String suit;
        private Object o;

        Suit(String suit) {
            this.suit = suit;
        }

        public String getSuit() {
            return this.suit;
        }

        @Override
        public String toString() {
            return this.suit;
        }
    }

    enum Point {
        two("2"), three("3"), four("4"), five("5"), six("6"), seven("7"), eight("8"), nine("9"), ten("10"), J("J"), Q("Q"), K("K"), A("A");
        private final String point;

        Point(String point) {
            this.point = point;
        }

        public String getPoint() {
            return this.point;
        }

        @Override
        public String toString() {
            return this.point;
        }
    }

    enum Joker {
        Red("RedJoker"), Black("BlackJoker");
        private String name;

        Joker(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private final Suit suit;
    private final Point point;
    private final Joker joker;
    private final boolean isJoker;

    /**
     * 定义一张非大小王扑克
     * @param suit 这张扑克的花色: spades(♠), clubs(♦), hearts(♥), diamonds(♣)
     * @param point 这张扑克的点数: two(2), three(3), four(4), five(5), six(6), seven(7), eight(8), nine(9), ten(10), J(J), Q(Q), K(K), A(A)
     */
    public Card(String suit, String point) {
        this.suit = Suit.valueOf(suit);
        this.point = Point.valueOf(point);
        this.isJoker = false;
        this.joker = Joker.Black;
    }

    /**
     * 定义一张大小王扑克
     * @param joker 大小王: Red(大王) Black(小王)
     */
    public Card(String joker) {
        this.joker = Joker.valueOf(joker);
        this.isJoker = true;
        this.suit = Suit.spades;
        this.point = Point.A;
    }

    /**
     * 获得这张牌的花色
     * @return 如果是大小王，返回 "RedJoker"(大王), "BlackJoker"(小王)，否则，返回花色符号 "♠", "♥", "♦", "♣"
     */
    public String getSuit() {
        return isJoker ? joker.toString() : suit.toString();
    }

    /**
     * 获得这张牌的点数
     * @return 如果是大小王，返回 "RedJoker"(大王), "BlackJoker"(小王)，否则，返回点数字符 "2" - "10", "J", "Q", "K", "A"
     */
    public String getPoint() {
        return isJoker ? joker.toString() : point.toString();
    }

    /**
     * 获得这张牌的点数数字
     * @return 如果是数字点数(2 to 10)。返回点数对应数值，如果不是，J=11, Q=12, K=13, A=14, BlackJoker=15, RedJack=16
     */
    public int getPointNum() {
        return isJoker ? (joker == Joker.Black ? 15 : 16) : point.ordinal() + 2;
    }

    /**
     * 获得这张牌的花色枚举对象
     * @return 如果是大小王，返回 null ，如果不是，返回这张牌的花色枚举对象
     */
    public Suit getSuitObj() {
        return isJoker ? null : suit;
    }

    /**
     * 转换成字符串，以便输出
     * @return 如果是大小王，返回 "RedJoker" 或者 "BlackJoker" ，否则，返回 花色标志 + 点数 如 "♥8"
     */
    @Override
    public String toString() {
        return isJoker ? joker.toString() : (suit.toString() + point.toString());
    }

    /**
     * 把这张牌与另一张牌进行比较
     * @param anotherCard 另一张牌，必须也是相同类型
     * @return 1大于，0等于，-1小于
     */
    @Override
    public int compareTo(Object anotherCard) {
        if (this == anotherCard || getPointNum() == ((Card) anotherCard).getPointNum()) {
            return 0;
        }
        if (getPointNum() < ((Card) anotherCard).getPointNum()) {
            return -1;
        }
        if (getPointNum() > ((Card) anotherCard).getPointNum()) {
            return 1;
        }
        return 0;
    }
}
