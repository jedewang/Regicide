package com.game.test;

import com.game.Player;
import com.game.card.Tavern;

/**
 * @Author：WangWei
 * @Version：1.0
 * @Date： 2022/1/6-16:36
 * @Since:jdk1.8
 * @Description:
 */
public class PlayerTest {
    public static void main(String[] args) {
        Player player1 = new Player(8);
        Tavern tavern1 = new Tavern();
        tavern1.init();

        player1.init();
        player1.setTavern(tavern1);
        player1.draw(8);
        player1.showHand();
        player1.discard(10);
    }

}
