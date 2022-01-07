package com.game;


import com.game.boss.Boss;
import com.game.card.Card;

/**
 * @Author：WangWei
 * @Version：1.0
 * @Date：2022/1/5-23:34
 * @Since:jdk1.8
 * @Description:游戏系统，控制游戏流程
 */
public class GameMaster {
    private Boss[] castle;   //boss牌堆(城堡）
    private Card[] tavern;   //玩家牌堆（酒馆）
    private Player player;   //玩家
    private int joker;  //小丑牌
    private Card[] graveyard;   //弃牌堆（墓地）

    /**
     * 初始化游戏，包括洗boss堆，酒馆堆，初始化弃牌堆，给玩家发牌
     */
    public void startGame() {
        System.out.println("游戏开始");
        castle.ini
    }
}
