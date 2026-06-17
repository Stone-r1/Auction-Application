package org.example.shared.data;

public class RabbitConstants {
    public static final String AUCTION_EXCHANGE = "auction.exchange";
    public static final String NOTIFICATION_QUEUE = "notification.queue";

    public static final String AUCTION_CLOSED_ROUTING_KEY = "auction.closed";
    public static final String AUCTION_STARTED_ROUTING_KEY = "auction.started";
    public static final String BID_PLACED_ROUTING_KEY = "auction.bid";

    public static final String DEAD_LETTER_EXCHANGE = "auction.dlx";
    public static final String DEAD_LETTER_QUEUE = "notification.dlq";

    public static final String AUCTION_ROUTING_PATTERN = "auction.*";
    public static final String DLQ_ROUTING_PATTERN = "#";
}
