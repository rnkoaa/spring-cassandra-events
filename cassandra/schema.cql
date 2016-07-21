CREATE TABLE IF NOT EXISTS request_events (
    request_id UUID,
    request_time timestamp,
    response_code INT,
    resource_id INT,
    resource_type TEXT,
    request_server TEXT,
    PRIMARY KEY(request_id, event_time)
)WITH CLUSTERING ORDER BY (event_time DESC);

https://github.com/brianmhess/cassandra-loader

 INSERT INTO request_events (request_id, request_time, resource_id, resource_type, response_code, request_server)
 values(dfefe5ac-e59e-41c9-8219-a19c0380a997, '2016-07-18T12:33:25.240', 914381, 'person', 404, '52.40.93.212');

 INSERT INTO request_events (request_id, request_time, resource_id, resource_type, response_code, request_server)
 values(c2ccd59f-bf9d-41bc-a55f-90a355b7e74c, '2016-07-09T05:53:20.915', 407789, 'movie', 404, '52.39.9.159');

 INSERT INTO request_events (request_id, request_time, resource_id, resource_type, response_code, request_server)
 values(53e68450-f94d-44f8-81fa-6be5e71a4b9d, '2016-07-17T09:45:22.744', 336172, 'person', 404, '52.37.102.108');

  drop table if exists request_events;