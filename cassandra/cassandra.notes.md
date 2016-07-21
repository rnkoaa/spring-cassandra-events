create table videos (
    ...
    ....
    primary key((year), name)
) with clustering order by (name desc);

UDT:

CREATE TYPE address (
    street text,
    city text,
    zip_code int,
    phones set<text>
);

create type full_name (
    first_name text,
    last_name text
)

Using UDT
----
create table users (
    id uuid,
    name frozen <full_name>
    direct_reports set<frozen <full_name>>,
    addresses map<text, frozen<address>>,
    primary key((id))
)

imports csv  to table

COPY table1 (column1, column2, column3) FROM 'tabledata.csv';

-- If there is a header in the file
COPY table1 (column1, column2, column3) FROM 'tabledata.csv'
WITH HEADER=true;

select statements like in sql
select *,
select count(*)
select * ...limit 10

Source: Execute a file containing cql statements
eg. SOURCE './myscripts.cql';

        (acted in)
videos <character> actor


Domains:
Videos
Users
Comments
Ratings
Actors

Modeling Entities
1-1: Use either of the participating entity keys
1-m: Use entity key sitting on the many side of the relationship
m-n: combine both entity keys

video types:

video
    trailer
    full
        movie
        tv
            season
            episode

https://docs.datastax.com/en/developer/java-driver/3.0/supplemental/manual/paging/?local=true&nav=toc

Async/Reactive
https://docs.datastax.com/en/developer/java-driver/3.0/supplemental/manual/async/?local=true&nav=toc
        
-Dspring.profiles.active=docker_machine


spring data cassandra

liquibase.enabled: false;

spring-data-cassandra
cassandra-driver-core
cassandra-driver-mapping


Configuration
EnableCassandraRepositories
CassandraConfig extends AbstractCassandraConfiguration {
	
	Bean
	CassandraClusterFactoryBean
		cluster.setContactPoints("ip")
		cluster.setPort(9042)
		
	Bean
	CassandraMappingContext cassandraMappingContext
		return new BasicCan....
	
	Bean
	String getKeySpace()
		return "cwt"
}


Table
class Member

primarykeycolumn(name, type: PrimaryKeyType.Partioned, ordinal=0)
username
...

column
email


MemberRepository extends CassandraRepository<Member> {
	
	
	@Query("select * from members where username = ?)"
	Iterable<Member> findByUsername(string username)
}

session = cluster.connect("cwt");
CQLOperations cops = new CQLTemplate(session)

Insert example = QueryBuilder.InsertInto("table)
					.value("key", "value");
cops.execute(example)

Statement stmt = cops.getSession().prepare("insert into table (columns) values (????) ...).bind("values");
cops.execute(stmt)

ResultSet rset = cops.query("select * from tbl")
while(!rset.isExhausted(){
row = rset.getOne()
string = row.getString
...
....
}

===
cql template

CassandraOperations ops = new CassandraTemplate(session);
ops.insert(new Person())

Select s = QueryBuilder.select().from("tbl")
.where(QueryBuilder.eq("id", "val"));

p = ops.selectOne(s, Person.class)

D3.js
bl.ocks.org/andredumas