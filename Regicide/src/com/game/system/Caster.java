package com.game.system;

import com.game.Player;
import com.game.boss.Boss;
import com.game.card.Card;
import com.game.card.CardHeap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @Author：WangWei
 * @Version：1.0
 * @Date：2022/1/6-9:12
 * @Since:jdk1.8
 * @Description: 卡片（法术）缓冲池，用于释放卡片（法术）
 * 属性 - 缓存池
 * 方法：add - 向池中添加卡片（法术）
 * sum - 计算当前缓冲池中卡片总值
 * canCombo - 判断当前缓冲池中的卡片能否再添加combo卡
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Caster {
    private ArrayList<Card> buffer = new ArrayList<>();    //缓存池
    private Player player;  //与玩家交互
    private CardHeap playersHandCard;    // 玩家手牌
    private Boss currentBoss; //  与当前boss交互
    Scanner scanner = new Scanner(System.in);

    /**
     * @param card 添加的卡片
     * @return 是否添加完毕
     */
    public boolean add(Card card) {
        //选择的牌不存在
        if (card == null) {
            System.out.println("选择的手牌不存在，请重新选择！");
            return false;
        }
        //没有花色说明拿到的是小丑牌
        if ("".equals(card.getColor())) {
            if (card.getPoint() > 0) {
                player.getHandCard().clear();
                player.draw(8);
                card.setPoint((card.getPoint() - 1));
                System.out.println("您使用了1张Joker,当前手牌：");
                player.showHand();
            } else {
                System.out.println("Joker数量不足，抽牌失败");
            }
        }
        //如果buffer不为空，且已选的牌不是动物伙伴，且和传入的牌点数不同，那么不合法，不存储直接返回
        if (!buffer.isEmpty() && (buffer.get(0).getPoint() != 1 && buffer.get(0).getPoint() != card.getPoint())) {
            System.out.println("选择的牌不合法，请重新选择！");
            return false;
        }
        //传入的牌合法，可以存储
        buffer.add(card);
        playersHandCard = player.getHandCard();
        playersHandCard.remove(card);
        player.setHandCard(playersHandCard);
        if (card.getPoint() == 1) {
            System.out.println("你打出了1张动物伙伴，请选择它要协助的牌");
            player.showHand();
            return false;
        } else if (canCombo()) {
            System.out.println("要组成combo吗？（y/n)");
            if ("y".equals(scanner.next())) {
                System.out.println("请选择你要组成combo的牌：");
                return false;
            }
            return true;    //玩家选择不组成combo，返回true(准备完毕)
        }
        return true;    //不能组成combo，返回true(准备完毕)
    }

    //将当前缓存池中的所有牌向boss释放
    public boolean castCard(Player player, Boss currentBoss) {
        if (buffer == null || buffer.size() == 0) {
            System.out.println("错误，未选择要打出的牌！");
            return false;
        }
        currentBoss.getAttacked(player, buffer);
        return true;
    }

    public int sum() {
        int sum = 0;
        if (buffer == null || buffer.size() == 0) {
            return 0;
        }
        for (Card card : buffer) {
            sum += card.getPoint();
        }
        return sum;
    }

    public boolean canCombo() {
        //如果buffer存了4个，那必然是4个2，不能继续填了
        if (buffer.size() == 4) {
            return false;
        }
        /*
        如果buffer存了3个，那必然是3个3或者3个2，
        如果是3个3,那就不能继续combo了，
        如果不是，那必然是3个2，那么遍历玩家手牌，
        如果存在2，那么可以combo，如果不存在，那么不能
         */
        if (buffer.size() == 3) {
            if (buffer.get(0).getPoint() == 3) {
                return false;
            }
            for (Card card : player.getHandCard()) {
                if (card.getPoint() == 2) {
                    return true;
                }
            }
            return false;
        }
        /*
        如果buffer存了2个，那么可能是2、3、4、5，或者动物伙伴，
        如果不等，那就是动物伙伴，不能combo
        如果相等，看第一张卡是不是4或5，如果是，不能combo
        如果不是，那么判断是不是3，如果是，遍历玩家手牌，找到3则可以combo，否则不能
        如果不是3，那么遍历判断2
         */
        if (buffer.size() == 2) {
            if (buffer.get(0).getPoint() != buffer.get(1).getPoint()) {
                return false;
            }
            if (buffer.get(0).getPoint() == 4 || buffer.get(0).getPoint() == 5) {
                return false;
            }
            if (buffer.get(0).getPoint() == 3) {
                for (Card card : player.getHandCard()) {
                    if (card.getPoint() == 3) {
                        return true;
                    }
                }
                return false;
            }
            if (buffer.get(0).getPoint() == 2) {
                for (Card card : player.getHandCard()) {
                    if (card.getPoint() == 2) {
                        return true;
                    }
                }
                return false;
            }
        }
        /*
        如果buffer有一张牌，且大于5，那么不能combo
        否则，遍历玩家手牌，如果找到了一张一样点数的，那么可以combo

         */
        if (buffer.size() == 1) {
            if (buffer.get(0).getPoint() > 5) {
                return false;
            }
            for (Card card : player.getHandCard()) {
                if (card.getPoint() == buffer.get(0).getPoint()) {
                    return true;
                }
            }
            return false;

        }
        return true;
    }
}
