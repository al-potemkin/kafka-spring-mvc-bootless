## The project was made to test the basic functionality of Kafka along with Avro schema based on Java 7
#### To start the project you need to have:
- Set in the project or install on your computer Java 7
- [Kafka](https://kafka.apache.org/downloads)
- [Zookeeper](https://zookeeper.apache.org/releases.html)
- [Schema Registry (Confluent Platform)](https://www.confluent.io/installation/)
- Optional: [Schema Registry UI](https://github.com/lensesio/schema-registry-ui)

1. Start the services one by one using standard settings using the following commands:
```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
```
```bash
bin/kafka-server-start.sh config/server.properties
```
```bash
bin/schema-registry-start etc/schema-registry/schema-registry.properties
```
2. Generate classes based on schemas located in `/resources/avro` folder using next command in terminal from project root
```bash
mvn generate-sources
```
3. Run `com.kaf.bootless.ConsumerMain` and `com.kaf.bootless.ProducerMain`


### Useful requests for working with Schema Registry:
- Get subjects
```curl
curl --location 'http://localhost:8081/subjects'
```
- Get schemas
```curl
curl --location 'http://localhost:8081/schemas'
```
- Register new Schema
```curl
curl --location 'http://localhost:8081/subjects/kafka_topic-value/versions' \
--header 'Content-Type: application/vnd.schemaregistry.v1+json' \
--data '{"schema": "{\"type\": \"record\",\"namespace\": \"com.kaf.bootless\",\"name\": \"User\",\"fields\": [{\"name\": \"id\",\"type\": \"int\"},{\"name\": \"name\",\"type\":\"string\"}]}"}'
```
- Delete schema
```curl
curl --location --request DELETE 'http://localhost:8081/subjects/kafka_topic-value'
```