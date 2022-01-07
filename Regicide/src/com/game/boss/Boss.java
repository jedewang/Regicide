package com.game.boss;

import com.game.Player;
import com.game.card.Card;
import com.game.card.CardHeap;
import com.game.card.Graveyard;
import com.game.card.Tavern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @Author： WangWei
 * @Version： 1.0
 * @Date： 2022/1/6-0:12
 * @Since: jdk1.8
 * @Description: Boss
 * 属性：攻击 生命 攻击削减 当前身上牌堆 免疫的能力
 * 方法：攻击 - 对玩家造成攻击力的伤害
 * 受攻击 - 受到玩家的伤害
 * 死亡 - 生命值小于0时进入墓地，弃掉当前身上所有牌
 * 劝服 - 生命值等于0时进入酒馆
 * 能力免疫 - 消除掉攻击牌中的对应花色
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Boss {
    private int ATK; //攻击力
    private int health; //生命
    private int impairment = 0; //攻击削减
    private CardHeap keptCard = new CardHeap();    //当前身上牌堆
    private String immune;  //免疫属性
    private boolean isLive = true;  //  是否存活
    private HashSet<String> colorBuffer = new HashSet<>();    //花色缓存池
    private int pointSum = 0;   //  点数总和
    private Tavern tavern;  //和酒馆交互
    private Graveyard graveyard;    //和弃牌堆交互

    public void attack(Player player) {
        int damage = Math.max((ATK - impairment),0);
        player.discard(damage);
    }
    public int getATK(){
        return Math.max((ATK - impairment),0);
    }

    public void getAttacked(Player player, ArrayList<Card> buffer) {
        pointSum = 0;   //重置点数和
        for (Card card : buffer) {
            colorBuffer.add(card.getColor());
            pointSum += card.getPoint();
        }
        colorBuffer.remove(immune); //去掉免疫的能力
        //剩下的能力依次取出直到空，激活对应效果
        while (!colorBuffer.isEmpty()) {
            if (colorBuffer.contains("♥")) {
                tavern.heal(pointSum, graveyard);
                colorBuffer.remove("♥");
            }
            if (colorBuffer.contains("♠")) {
                this.impairment += pointSum;
                colorBuffer.remove("♠");
            }
            if (colorBuffer.contains("♦")) {
                player.draw(pointSum);
                colorBuffer.remove("♦");
            }
            if (colorBuffer.contains("♣")) {
                pointSum *= 2;
                colorBuffer.remove("♣");
            }
        }
        System.out.println("你造成了"+pointSum+"点伤害");
        this.health -= pointSum;
        keptCard.addAll(buffer);    //  受到的攻击的所有牌存在身上
        buffer.clear(); //清空缓存池
        if (this.health < 0) {
            dead(graveyard);
        } else if (this.health == 0) {
            persuaded(tavern, graveyard);
        }else {
            attack(player);
        }
    }

    /**
     * 死亡
     *
     * @param graveyard 目标弃牌堆
     */
    public void dead(Graveyard graveyard) {
        System.out.println("你击败了Boss！");
        graveyard.addAll(keptCard);
        isLive = false;
    }

    /**
     * 被劝服,自身加入酒馆最上方，保留牌进弃牌堆
     *
     * @param tavern    目标酒馆
     * @param graveyard 目标弃牌堆
     */
    public void persuaded(Tavern tavern, Graveyard graveyard) {
        System.out.println("你劝服了Boss!");
        tavern.add(0, new Card(immune, ATK));
        graveyard.addAll(keptCard);
        isLive = false;
    }

    //初始化构造方法
    public Boss(int ATK, int health, String immune) {
        this.ATK = ATK;
        this.health = health;
        this.immune = immune;
    }


}
