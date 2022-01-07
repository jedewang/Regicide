package com.game.card;

import com.game.system.GameMaster;

import java.util.Collections;

/**
 * @Author：WangWei
 * @Version：1.0
 * @Date：2022/1/6-14:23
 * @Since:jdk1.8
 * @Description:
 */
public class Tavern extends CardHeap {
    public Card pop() {
        return this.remove(this.size() - 1);
    }

    public void init() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                this.add(new Card(GameMaster.COLOR[i], j + 1));
            }
        }
        Collections.shuffle(this);
//        System.out.println(this.toString());
    }

    /**
     * 酒馆补牌
     *
     * @param graveyard 目标弃牌堆
     */
    public void heal(int healPoint, Graveyard graveyard) {
        for (int i = 0; i < healPoint; i++) {
            while (!graveyard.isEmpty()) {
                this.add(graveyard.remove((int) (Math.random() * graveyard.size())));
            }
        }
    }
}
