package org.apdoer.channel.server.utils;

import org.apdoer.channel.client.dto.MsgerRequestDto;
import org.apdoer.channel.server.model.dto.SupplierRatio;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.List;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/4/23 15:32
 */
public class ChannelUtils {


    public static int getIndexByWeight(int[] weightArr) {
        int[][] randArr = new int[weightArr.length][2];
        int totalRank = 0;
        for (int i = 0; i < weightArr.length; i++) {
            if (weightArr[i] <= 0) {
                continue;
            }
            totalRank += weightArr[i];
            randArr[i][0] = i;
            randArr[i][1] = totalRank;
        }
        int hitRank = new SecureRandom().nextInt(totalRank) + 1;
        for (int i = 0; i < randArr.length; i++) {
            if (hitRank <= randArr[i][1]) {
                return randArr[i][0];
            }
        }
        return randArr[0][0];
    }

    public static SupplierRatio choose(List<SupplierRatio> supplierRatio) {
        if (null == supplierRatio) {
            return null;
        }
        int[] weightArr = new int[supplierRatio.size()];
        for (int i = 0; i < supplierRatio.size(); i++) {
            if (supplierRatio.get(i).getWeight() > 0) {
                weightArr[i] = supplierRatio.get(i).getWeight();
            }
        }
        if (weightArr.length == 0) {
            return null;
        }
        int chosenIndex = getIndexByWeight(weightArr);
        return supplierRatio.get(chosenIndex);
    }

    public static String getSupplier(MsgerRequestDto request, List<SupplierRatio> supplierRatio) {
        String supplierStr = "";
        if (StringUtils.isEmpty(supplierStr)) {
            SupplierRatio supplier = choose(supplierRatio);
            supplierStr = supplier.getSupplier();
        }
        return supplierStr;
    }
}
