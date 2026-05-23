package com.wash.laundry_app.notifications;

import com.wash.laundry_app.users.Role;
import com.wash.laundry_app.users.User;
import com.wash.laundry_app.users.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final PushNotificationService pushNotificationService;

    @Async
    @Transactional
    public Notification createNotification(User recipient, String title, String message, String type, String referenceId) {
        try {
            Notification notification = Notification.builder()
                    .recipient(recipient)
                    .title(title)
                    .message(message)
                    .type(type)
                    .referenceId(referenceId)
                    .build();
            Notification saved = notificationRepository.save(notification);
            pushWebSocketNotification(saved);
            pushNotificationService.sendPushNotification(recipient, title, message,
                    Map.of("type", type, "referenceId", referenceId != null ? referenceId : ""));
            return saved;
        } catch (Exception e) {
            log.error("Failed to create notification for user {}: {}", recipient.getEmail(), e.getMessage());
            return null;
        }
    }

    private void pushWebSocketNotification(Notification notification) {
        try {
            messagingTemplate.convertAndSendToUser(
                    notification.getRecipient().getId().toString(),
                    "/queue/notifications",
                    notification
            );
        } catch (Exception e) {
            log.warn("WebSocket push failed for notification {}: {}", notification.getId(), e.getMessage());
        }
    }

    @Async
    @Transactional
    public void notifyRole(Role role, String title, String message, String type, String referenceId) {
        try {
            List<User> users = userRepository.findByRole(role);
            for (User user : users) {
                createNotification(user, title, message, type, referenceId);
            }
        } catch (Exception e) {
            log.error("Failed to notify role {}: {}", role, e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByRecipientIdAndReadFalse(userId);
    }

    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            if (n.getRecipient().getId().equals(userId)) {
                n.setRead(true);
                notificationRepository.save(n);
            }
        });
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
    }
}
