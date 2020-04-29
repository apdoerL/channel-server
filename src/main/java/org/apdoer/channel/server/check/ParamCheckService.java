package org.apdoer.channel.server.check;

import org.springframework.stereotype.Service;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/4/26 19:40
 */
@Service
public class ParamCheckService {

    /**
     * 校验接收用户是否配入黑名单,
     * 1.可以从数据实时加载,
     * 2.也可以通过定时任务定时刷入内存,
     *      2.1 也可以通过配置文件在bean实例化的时候加载进内存(配置文件,数据库)
     *      2.2
     * @param contact
     * @return
     */
    public boolean checkBlackList(String contact) {

        return true;
    }
}
