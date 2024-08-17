package com.coresaken.mcserverlist.data.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionPoints{
    Long serverId;
    int promotionPoints;
}