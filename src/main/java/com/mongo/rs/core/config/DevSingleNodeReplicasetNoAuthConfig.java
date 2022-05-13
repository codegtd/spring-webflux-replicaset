package com.mongo.rs.core.config;


import com.mongo.rs.core.YamlConverter;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@PropertySource(value = "classpath:application.yml", factory = YamlConverter.class)
@ConfigurationProperties(prefix = "db.mongodb.replicaset")
@Setter
@Getter
@Profile("dev-single-node-rs")
@Import({DbTransactionManagerConfig.class})
@Slf4j
@Configuration
@EnableReactiveMongoRepositories(basePackages = {"com.mongo.rs.modules.user"})
public class DevSingleNodeReplicasetNoAuthConfig extends AbstractReactiveMongoConfiguration {

  private String rootUri;
  private String db;
  private String rsName;
  private String authDb;
  private String username;
  private String password;

  @Override
  public MongoClient reactiveMongoClient() {
/*╔═══════════════════════════════════════════════════╗
  ║ DEV-SINGLENODE-REPLICASET-NOAUTH DEVELOPMENT URL  ║
  ╠═══════════════════════════════════════════════════╣
  ║ mongodb://myservice-mongodb:27017/                ║
  ║ ?connect=direct                                   ║
  ║ &replicaSet=singleNodeReplSet                     ║
  ║ &readPreference=primary                           ║
  ╚═══════════════════════════════════════════════════╝*/
    final String connection =
         rootUri +
              "/?connect=direct" +
              "&replicaSet=" + rsName +
              "&readPreference=primary";

    System.out.println("DevSingleNodeReplicasetNoAuth ---> " + connection);

    return MongoClients.create(connection);
  }


  @Override
  protected String getDatabaseName() {

    return db;
  }
}