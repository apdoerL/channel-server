package org.apdoer.channel.server.controller.task;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.channel.server.service.OverageAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/task/channel/alert")
public class OverageAlertController {

    @Autowired
    private OverageAlertService overageAlertService;


    /**
     * 定时任务监控平台余额情况,xxljob
     */
    @GetMapping("/alertCheck")
    public void alertCheck() {
        log.info("alert task starting...");
        overageAlertService.alertCheck();
    }

}
