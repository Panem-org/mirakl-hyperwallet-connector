package com.paypal.notifications.services.sender;

import com.hyperwallet.clientsdk.model.HyperwalletWebhookNotification;
import com.paypal.infrastructure.events.HMCEvent;
import com.paypal.infrastructure.events.KycBusinessStakeholderEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KycBusinessStakeHolderSenderTest {

	private static final String EXPECTED_TYPE = "expectedType";

	private static final String UNEXPECTED_TYPE = "unexpectedType";

	@Spy
	@InjectMocks
	private KycBusinessStakeHolderSender testObj;

	@Mock
	private ApplicationEventPublisher applicationEventPublisherMock;

	@Mock
	private KycBusinessStakeholderEvent kycBusinessStakeholderEventMock;

	@Mock
	private HyperwalletWebhookNotification hyperwalletWebhookNotificationMock;

	@Test
	void execute_shouldPublishEvent_andReturnNull() {
		doReturn(kycBusinessStakeholderEventMock).when(testObj).getEvent(hyperwalletWebhookNotificationMock);

		final Void result = testObj.execute(hyperwalletWebhookNotificationMock);

		verify(applicationEventPublisherMock).publishEvent(kycBusinessStakeholderEventMock);
		assertThat(result).isNull();
	}

	@Test
	void getEvent_shouldReturnKycBusinessStakeholderEventWithNotification() {
		final HMCEvent event = testObj.getEvent(hyperwalletWebhookNotificationMock);

		assertThat(event).isInstanceOf(KycBusinessStakeholderEvent.class);
		assertThat(event.getNotification()).isEqualTo(hyperwalletWebhookNotificationMock);
	}

	@Test
	void isApplicable_whenNotificationTypeIsNull_shouldReturnFalse() {
		final boolean result = testObj.isApplicable(hyperwalletWebhookNotificationMock);

		assertThat(result).isFalse();
	}

	@Test
	void isApplicable_whenNotificationStartsWithExpectedType_shouldReturnTrue() {
		when(hyperwalletWebhookNotificationMock.getType()).thenReturn(EXPECTED_TYPE);
		doReturn(EXPECTED_TYPE).when(testObj).getNotificationType();

		final boolean result = testObj.isApplicable(hyperwalletWebhookNotificationMock);

		assertThat(result).isTrue();
	}

	@Test
	void isApplicable_whenNotificationDoesNotStartWithExpectedType_shouldReturnTrue() {
		when(hyperwalletWebhookNotificationMock.getType()).thenReturn(UNEXPECTED_TYPE);
		doReturn(EXPECTED_TYPE).when(testObj).getNotificationType();

		final boolean result = testObj.isApplicable(hyperwalletWebhookNotificationMock);

		assertThat(result).isFalse();
	}

}
