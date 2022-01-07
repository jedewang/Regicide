package com.game;

import com.game.boss.Boss;
import com.game.card.Card;
import com.game.card.CardHeap;
import com.game.card.Tavern;
import com.game.system.Caster;
import com.game.system.GameMaster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Scanner;

/**
 * @Author：WangWei
 * @Version：1.0
 * @Date： 2022/1/5-23:33
 * @Since:jdk1.8
 * @Description: 玩家 属性：手牌
 * 方法：抽牌
 * 弃牌
 * 攻击（出牌）
 * 死亡
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private CardHeap handCard = new CardHeap();  //手牌
    private Tavern tavern;  //与酒馆交互
    private Caster caster;  //与施法器交互
    private int handLimit;  //  手牌上限,默认为8
    private boolean isLive = true;  //是否存活
    Scanner scanner = new Scanner(System.in);

    //构造函数，只指定手牌上限
    public Player(int handLimit) {
        this.handLimit = handLimit;
    }

    public void init() {
        handCard = new CardHeap();
    }

    /**
     * 攻击
     */
    public void attack(Boss currentBoss) {
        Card choice = selectCard();
        //准备未完成时循环添加
        while (!caster.add(choice)) {
            choice = selectCard();
        }
        caster.castCard(this, currentBoss);
    }


    /**
     * 展示手牌
     */
    public void showHand() {
        for (int i = 0; i < handCard.size(); i++) {
            System.out.print("[" + (i + 1) + "." + handCard.get(i).getColor() + handCard.get(i).getPoint() + "] ");
        }
        System.out.println();
    }

    /**
     * 抽牌
     *
     * @param num 抽牌数量
     */
    public void draw(int num) {
        for (int i = 0; i < num; i++) {
            while (handCard.size() < 8) {
                handCard.add(tavern.pop());
            }
        }
    }

    /**
     * 丢手牌（承受伤害）
     *
     * @param damage 所受伤害
     */
    public void discard(int damage) {
        //如果手牌不足以抵挡伤害，且Joker为0，或选择不使用Joker，则死亡
        while (isDead(damage) && isLive) {
            if (GameMaster.joker.getPoint() > 0) {
                System.out.println("你即将受到致命伤害，是否使用1张Joker补充手牌？(y/n)");
                if ("n".equals(scanner.next())) {
                    System.out.println("你受到了致命伤害，你死了。");
                    this.dead();
                } else {
                    useJoker();
                }
            } else {
                System.out.println("你受到了致命伤害，你死了。");
                this.dead();
            }
        }
        while (damage > 0) {
            System.out.println("你还需要丢弃合计" + damage + "点以上的牌");
            System.out.println("请选择你要丢弃的牌");
            showHand();
            int discardCard = scanner.nextInt();
            damage -= handCard.remove(discardCard - 1).getPoint();
        }
    }

    /**
     * 死亡
     */
    public void dead() {
        this.isLive = false;
    }

    /**
     * 判断是否死亡
     *
     * @return 结果
     */
    public boolean isDead(int damage) {
        int sum = 0;    //计算手牌点数和
        System.out.println("你受到了" + damage + "点伤害");
        for (com.game.card.Card card : handCard) {
            sum += card.getPoint();
        }
        return sum <= damage;
    }

    public void useJoker() {

    }

    /**
     * 选择要出的牌
     *
     * @return 选择的牌
     */
    public Card selectCard() {
        int choice = scanner.nextInt();
        if (choice > handCard.size() || choice < 0) {
            System.out.println("输入有误，没有该手牌");
            return null;
        }
        if (choice == 0) {
            return GameMaster.joker; //TODO 将joker做成card返回，让caster去释放
        } else {
            return handCard.get(choice - 1);
        }
    }
}
