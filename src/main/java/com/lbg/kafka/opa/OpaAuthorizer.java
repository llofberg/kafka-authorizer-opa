package com.lbg.kafka.opa;

import kafka.security.auth.Acl;
import kafka.security.auth.Authorizer;
import kafka.security.auth.Operation;
import kafka.security.auth.Resource;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.security.auth.KafkaPrincipal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static kafka.network.RequestChannel.Session;

@Slf4j
public class OpaAuthorizer implements Authorizer {
  private final Map<String, Object> configs = new HashMap<>();

  private boolean allow(String data) throws IOException {
    // TODO: Caching
    String spec = "http://opa:8181/v1/data/kafka/authz/allow";
    HttpURLConnection conn = (HttpURLConnection) new URL(spec).openConnection();

    conn.setDoOutput(true);
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/json");

    OutputStream os = conn.getOutputStream();
    os.write(data.getBytes());
    os.flush();

    if (log.isTraceEnabled()) {
      log.trace("Response code: {}, Request data:{}", conn.getResponseCode(), data);
    }

    @Cleanup BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

    return "{\"result\":true}".equals(br.readLine());
  }

  public boolean authorize(Session session, Operation operation, Resource resource) {
    String query = "{\"input\":{" +
      "\"Principal\":\"" + session.principal().toString() + "\"," +
      "\"Operation\":\"" + operation.toString() + "\"," +
      "\"Resource\":\"" + resource.toString() + "\"," +
      "\"ClientAddress\":\"" + session.clientAddress().toString() + "\"}}";
    try {
      return allow(query);
    } catch (IOException e) {
      log.error("Failed to query OPA: {}", query, e);
      return false;
    }
  }

  public void configure(Map<String, ?> configs) {
    this.configs.putAll(configs);
  }

  public void addAcls(scala.collection.immutable.Set<Acl> acls, Resource resource) {
  }

  public boolean removeAcls(scala.collection.immutable.Set<Acl> acls, Resource resource) {
    return false;
  }

  public boolean removeAcls(Resource resource) {
    return false;
  }

  public scala.collection.immutable.Set<Acl> getAcls(Resource resource) {
    return null;
  }

  public scala.collection.immutable.Map<Resource, scala.collection.immutable.Set<Acl>> getAcls(KafkaPrincipal principal) {
    return null;
  }

  public scala.collection.immutable.Map<Resource, scala.collection.immutable.Set<Acl>> getAcls() {
    return null;
  }

  public void close() {
  }

}
