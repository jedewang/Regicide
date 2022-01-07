package com.game.system;


import com.game.Player;
import com.game.boss.Boss;
import com.game.card.Card;
import com.game.card.Graveyard;
import com.game.card.Tavern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * @Author：WangWei
 * @Version：1.0
 * @Date：2022/1/5-23:34
 * @Since:jdk1.8
 * @Description:游戏系统，控制游戏流程
 */
public class GameMaster {
    private ArrayList<Boss> castle;   //boss牌堆(城堡）
    private Boss currentBoss; //当前boss
    private Tavern tavern = new Tavern();   //玩家牌堆（酒馆）
    private Player player = new Player(8);   //玩家
    public static Card joker =new Card("", 2);  //小丑牌
    private Graveyard graveyard = new Graveyard();   //弃牌堆（墓地）
    private Caster caster = new Caster();      //施法器（法术缓存区）
    public static final String[] COLOR = {"♥", "♦", "♣", "♠"};
    Scanner scanner = new Scanner(System.in);

    /**
     * 初始化游戏，包括洗boss堆，酒馆堆，初始化弃牌堆，给玩家发牌
     */
    public void init() {

        //初始化Boss堆
        //按J、Q、K的顺序
        initCastle();

        //初始化酒馆堆
        tavern.init();
        //初始化弃牌堆
        graveyard.clear();

        //初始化缓存池
        caster.setPlayer(player);
        caster.setCurrentBoss(currentBoss);

        //初始化玩家
        //给玩家发牌
        player.setTavern(tavern);
        player.setCaster(caster);
        player.draw(player.getHandLimit());

    }

    public void initCastle() {
        ArrayList<Boss> bossJ = new ArrayList<>();
        ArrayList<Boss> bossQ = new ArrayList<>();
        ArrayList<Boss> bossK = new ArrayList<>();
        castle = new ArrayList<>(); //初始化城堡
        for (int i = 0; i < 4; i++) {
            bossJ.add(new Boss(10, 20, COLOR[i]));

        }
        Collections.shuffle(bossJ);
        castle.addAll(bossJ);
        for (int i = 0; i < 4; i++) {
            bossQ.add(new Boss(15, 30, COLOR[i]));
        }
        Collections.shuffle(bossQ);
        castle.addAll(bossQ);
        for (int i = 0; i < 4; i++) {
            bossK.add(new Boss(20, 40, COLOR[i]));
        }
        Collections.shuffle(bossK);
        castle.addAll(bossK);
    }



    /**
     * 开始游戏
     */
    public void startGame() {
        System.out.println("游戏开始");
        init(); //初始化游戏
        //城堡不空，游戏不结束
        while (!castle.isEmpty()){
            //当前boss初始化
            currentBoss = castle.remove(0);
            currentBoss.setTavern(tavern);
            currentBoss.setGraveyard(graveyard);
            System.out.println("新挑战者登场！");
            //只要玩家和boss都存活，就继续战斗流程
            while (player.isLive() && currentBoss.isLive()){
                System.out.println("当前boss生命为:"+ currentBoss.getHealth()+"，攻击力为："+(currentBoss.getATK())+"，免疫能力："+currentBoss.getImmune());
                System.out.println("请选择要出的牌（输入牌前的数字），按0使用Joker，当前拥有Joker"+joker+"张");
                player.showHand();
                player.attack(currentBoss);
            }
            if(!player.isLive()){
                System.out.println("游戏结束");
                return;
            }
        }
        System.out.println("你击败了所有的Boss");
        System.out.println("你赢了！！！");
        switch (joker.getPoint()){
            case 2:
                System.out.println("你获得了金牌成就！！！");
                break;
            case 1:
                System.out.println("你获得了银牌成就！");
                break;
            case 0:
                System.out.println("你获得了铜牌成就");
                break;
        }
        System.out.println("是否再来一局？(y/n)");
        if(!"y".equals(scanner.next())){
            return;
        }
        startGame();
    }
}
