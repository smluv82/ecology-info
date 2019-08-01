package me.ecology.common.listener;

import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApplicationFailEventListener implements ApplicationListener<ApplicationFailedEvent> {
	@Override
	public void onApplicationEvent(ApplicationFailedEvent event) {
		log.info("applicationFailEvent : {}", event.getException());
	}
}
