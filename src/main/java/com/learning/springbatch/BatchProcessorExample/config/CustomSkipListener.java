package com.learning.springbatch.BatchProcessorExample.config;

import com.learning.springbatch.BatchProcessorExample.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomSkipListener implements SkipListener<Employee,Employee> {

    @Override
    public void onSkipInRead(Throwable t) {
        log.info("onSkipInRead");
    }

    @Override
    public void onSkipInProcess(Employee item, Throwable t) {
        log.info("onWriteError"+item);
    }

    @Override
    public void onSkipInWrite(Employee item, Throwable t) {
        log.info("onSkipInWrite");
    }
}
