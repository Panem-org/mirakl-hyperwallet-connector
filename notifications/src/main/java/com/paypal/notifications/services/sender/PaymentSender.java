package com.paypal.notifications.services.sender;

import com.hyperwallet.clientsdk.model.HyperwalletWebhookNotification;
import com.paypal.infrastructure.events.HMCEvent;
import com.paypal.infrastructure.events.PaymentEvent;
import com.paypal.infrastructure.strategy.Strategy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Strategy class for sending payment notifications
 */
@Slf4j
@Getter
@Service
public class PaymentSender extends AbstractHMCEventSender implements Strategy<HyperwalletWebhookNotification, Void> {

	@Value("${notifications.payments.routingKey}")
	private String notificationType;

	@Override
	public HMCEvent getEvent(final HyperwalletWebhookNotification notification) {
		return new PaymentEvent(this, notification);
	}

	@Override
	public boolean isApplicable(final HyperwalletWebhookNotification source) {
		final String type = source.getType();
		return type != null && type.startsWith(getNotificationType());
	}

}
