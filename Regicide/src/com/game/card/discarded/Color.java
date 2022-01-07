package com.game.card.discarded;

/**
 * @Author：WangWei
 * @Version：1.0
 * @Date：2022/1/6-10:52
 * @Since:jdk1.8
 * @Description:
 */
public enum Color {
    HEART("♥", 1),
    SPADE("♠", 2),
    CLUB("♣", 3),
    DIAMOND("♦", 4);
    private String picture;
    private int id;

    Color(String picture, int id) {
        this.picture = picture;
    }
}
