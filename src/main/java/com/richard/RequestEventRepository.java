package com.richard;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created on 7/21/2016.
 */
@Component
@Slf4j
public class RequestEventRepository {
    private final Session session;
    //private final static Logger LOGGER = LoggerFactory.getLogger(AuctionDao.class);

   /*
    private PreparedStatement createAuction;
    private PreparedStatement getAuction;
    private PreparedStatement storeBid;
    private PreparedStatement getAllAuctionSparse;
    private PreparedStatement getAuctionBids;*/

    private PreparedStatement createEvent;
    //private PreparedStatement createBatchEvent;
    private PreparedStatement getEventById;
    private PreparedStatement getEvents;

    @Autowired
    public RequestEventRepository(Session session) {
        this.session = session;
    }

    public boolean insert(RequestEvent requestEvent) {
        // BoundStatement bound = createAuction.bind(auction.getName(), auction.getOwner(), auction.getEnds().toEpochMilli());
        //session.execute(bound);

        return true;
    }

    public List<RequestEvent> getAllRequestEventsSparse() {
        /*BoundStatement bound = getAllAuctionSparse.bind();
        return session.execute(bound).all().stream().map(row ->
                new Auction(row.getString("name"), row.getString("owner"), Instant.ofEpochMilli(row.getLong("ends"))))
                .collect(Collectors.toList());*/
        return new ArrayList<>(0);
    }

    public Optional<RequestEvent> get(String requestId) {
       /* BoundStatement auctionBoundStatement = getAuction.bind(auctionName);
        Row auction = session.execute(auctionBoundStatement).one();

        LOGGER.debug("Getting auction information for auction {} rows {}", auctionName, auction);

        BoundStatement bidsBound = getAuctionBids.bind(auctionName);
        List<BidVo> bids = session.execute(bidsBound).all().stream().map(row ->
                new BidVo(row.getString("bid_user"),
                        row.getLong("bid_amount"),
                        UUIDs.unixTimestamp(row.getUUID("bid_time"))))
                .collect(Collectors.toList());

        return Optional.of(new Auction(auction.getString("name"),
                Instant.ofEpochMilli(auction.getLong("ends")),
                bids,
                auction.getString("owner")));*/
        return Optional.empty();
    }


    @PostConstruct
    public void prepareStatements() {
        createEvent = session.prepare("INSERT INTO request_events " +
                "(request_id, request_time, resource_id, resource_type, response_code, request_server) " +
                "values (?, ?, ?, ?, ?, ?)");
        getEventById = session.prepare("SELECT * FROM request_events WHERE request_id = ?");
    }
}
