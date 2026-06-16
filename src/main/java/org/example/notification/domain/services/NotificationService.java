package org.example.notification.domain.services;


import org.example.notification.domain.repositories.NotificationRepository;
import org.example.shared.domain.EmailSenderRepository;
import org.example.shared.events.AuctionClosedEvent;
import org.example.shared.events.BidPlacedEvent;


public class NotificationService {
    private final NotificationRepository notificationUserRepository;
    private final EmailSenderRepository emailSenderRepository;

    public NotificationService(
            NotificationRepository notificationUserRepository,
            EmailSenderRepository emailSenderRepository
    ) {
        this.notificationUserRepository = notificationUserRepository;
        this.emailSenderRepository = emailSenderRepository;
    }

    private String getEmailByUserId(
            Long userId
    ) {
        // at this point email exists as user placed the bid. unauthorized user can't place the bid
        return notificationUserRepository
                .findEmailByUserId(userId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No email was found for user with userId: " + userId
                        )
                );
    }

    public void notifyAuctionClosed(
            AuctionClosedEvent auctionClosedEvent
    ) {
        // No bids were places
        if (auctionClosedEvent.winnerId() == null) {
            return;
        }

        emailSenderRepository.send(
                getEmailByUserId(auctionClosedEvent.winnerId()),
                "Auction was finished",
                buildAuctionClosedEmail(
                        "Congratulations! You have won the auction.",
                        auctionClosedEvent.auctionId(),
                        auctionClosedEvent.maxBidAmount()
                )
        );
    }

    public void notifyBidderLeading(
            BidPlacedEvent bidPlacedEvent
    ) {
        emailSenderRepository.send(
                getEmailByUserId(bidPlacedEvent.bidderId()),
                "Bid placed",
                buildAuctionClosedEmail(
                        "Congratulations! You have just placed a bid.",
                        bidPlacedEvent.auctionId(),
                        bidPlacedEvent.amount()
                )
        );
    }

    private String buildAuctionClosedEmail(
            String message,
            Long auctionId,
            Double maxBidAmount
    ) {
        return """
                <html>
                  <body style="font-family: sans-serif;">
                    <p>%s</p>
                    <table style="border-collapse: collapse; margin: 16px 0;">
                      <tr>
                        <td style="padding: 6px 12px; font-weight: bold;">Auction ID</td>
                        <td style="padding: 6px 12px;">%d</td>
                      </tr>
                      <tr style="background: #f5f5f5;">
                        <td style="padding: 6px 12px; font-weight: bold;">Winning Bid</td>
                        <td style="padding: 6px 12px;">$%.2f</td>
                      </tr>
                    </table>
                  </body>
                </html>
                """.formatted(message, auctionId, maxBidAmount);
    }
}
