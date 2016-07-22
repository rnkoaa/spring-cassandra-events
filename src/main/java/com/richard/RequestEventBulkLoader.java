package com.richard;

import com.datastax.driver.core.*;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created on 7/22/2016.
 */
@Component
public class RequestEventBulkLoader {

    private final Session session;
    private PreparedStatement createBatchEvent;
    private static final int THREADS = 4;

    @Autowired
    public RequestEventBulkLoader(Session session) {
        this.session = session;
    }

    @PostConstruct
    public void insertQuery() {
        createBatchEvent = session.prepare("INSERT INTO request_events " +
                "(request_id, request_time, resource_id, resource_type, response_code, request_server) " +
                "values (?, ?, ?, ?, ?, ?)");
    }

    public static class IngestCallback implements FutureCallback<ResultSet> {

        @Override
        public void onSuccess(ResultSet result) {
            //placeholder: put any logging or on success logic here.
            System.out.println("Successfully Saved result: " + result.wasApplied());
        }

        @Override
        public void onFailure(Throwable t) {
            //go ahead and wrap in a runtime exception for this case, but you can do logging or start counting errors.
            throw new RuntimeException(t);
        }
    }

   /* public static Observable<ResultSet> queryAllAsObservable(Session session, String query, Object... partitionKeys) {
        List<ResultSetFuture> futures; // = sesssion //sendQueries(session, query, partitionKeys);
        Scheduler scheduler = Schedulers.io();
        List<Observable<ResultSet>> observables = Lists.transform(futures, (ResultSetFuture future) -> Observable.from(future, scheduler));
        return Observable.merge(observables);
    }*/

    /*Observable<ResultSet> results = ResultSets.queryAllAsObservable(session,
            "SELECT * FROM users WHERE id = ?",
            UUID.fromString("e6af74a8-4711-4609-a94f-2cbfab9695e5"), UUID.fromString("281336f4-2a52-4535-847c-11a4d3682ec1") //...
    );

results.subscribe(new Observer<ResultSet>() {
        @Override public void onNext(ResultSet resultSet) {
        ... // process the result set
        }

        @Override public void onError(Throwable throwable) {
        ... // process the error
        }

        @Override public void onCompleted() {
            // no more results
        }
    });*/

    public void bulkLoadData(List<RequestEvent> requestEvents) {
        ExecutorService executor = MoreExecutors.getExitingExecutorService(
                (ThreadPoolExecutor) Executors.newFixedThreadPool(THREADS));

        BoundStatement boundStatement = createBatchEvent.bind(new RequestEventIterator(requestEvents).next());
        ResultSetFuture future = session.executeAsync(boundStatement);
        // Observable<ResultSet> result = ResultSets
        Futures.addCallback(future, new IngestCallback(), executor);
    }

    public static class RequestEventIterator implements Iterator<Object[]> {

        private final List<RequestEvent> requestEvents;
        int cursor = 0;

        public RequestEventIterator(List<RequestEvent> requestEvents) {
            this.requestEvents = requestEvents;
        }

        @Override
        public boolean hasNext() {
            return cursor < requestEvents.size();
        }

        @Override
        public Object[] next() {
            if (this.hasNext()) {
                int current = cursor;
                cursor++;
                RequestEvent requestEvent = requestEvents.get(current);
                return new Object[]{requestEvent.getId(), requestEvent.getEventTime(),
                        requestEvent.getResourceId(), requestEvent.getResourceType(),
                        requestEvent.getResponseCode(), requestEvent.getServerIp()};
            }
            throw new NoSuchElementException();
        }
    }


}
