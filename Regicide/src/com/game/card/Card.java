package com.game.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * @Author：WangWei
 * @Version：1.0
 * @Date： 2022/1/5-23:41
 * @Since:jdk1.8
 * @Description: 卡牌，
 * 属性：点数 花色
 * 方法：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private String color;    //花色
    private int point;  //点数

}
