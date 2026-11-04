package com.example.forexhorizon.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
@Service public class OandaService {
 private final RestClient client; private final ObjectMapper mapper; private final String account; private final String token;
 public OandaService(@Value("${app.oanda.base-url}") String base,@Value("${app.oanda.token}") String token,@Value("${app.oanda.account}") String account,ObjectMapper mapper){
  this.client=RestClient.builder().baseUrl(base).build(); this.token=token;this.account=account;this.mapper=mapper;
 }
 public boolean configured(){return !account.isBlank()&&!token.isBlank();}
 private void check(){if(!configured())throw new IllegalStateException("Set OANDA_API_TOKEN and OANDA_ACCOUNT_ID to enable live Forex actions.");}
 private JsonNode call(HttpMethod method,String path,Object payload){check(); try{
  RestClient.RequestBodySpec req=client.method(method).uri(path).header("Authorization","Bearer "+token).accept(MediaType.APPLICATION_JSON);
  String response=(payload==null?req: req.contentType(MediaType.APPLICATION_JSON).body(payload)).retrieve().body(String.class);
  return mapper.readTree(response==null?"{}":response);
 }catch(Exception e){throw new IllegalStateException("OANDA request failed: "+e.getMessage(),e);}}
 private String enc(String value){return URLEncoder.encode(value,StandardCharsets.UTF_8);}
 public JsonNode account(){return call(HttpMethod.GET,"/v3/accounts/"+enc(account)+"/summary",null).path("account");}
 public JsonNode price(String instrument){return call(HttpMethod.GET,"/v3/accounts/"+enc(account)+"/pricing?instruments="+enc(instrument),null).path("prices");}
 public JsonNode candles(String instrument,String granularity){return call(HttpMethod.GET,"/v3/instruments/"+enc(instrument)+"/candles?count=10&price=M&granularity="+enc(granularity),null).path("candles");}
 public JsonNode open(String instrument,int units){if(units==0)throw new IllegalArgumentException("Quantity must not be 0");return call(HttpMethod.POST,"/v3/accounts/"+enc(account)+"/orders",java.util.Map.of("order",java.util.Map.of("units",Integer.toString(units),"instrument",instrument,"timeInForce","FOK","type","MARKET","positionFill","DEFAULT")));}
 public JsonNode positions(){return call(HttpMethod.GET,"/v3/accounts/"+enc(account)+"/openTrades",null).path("trades");}
 public JsonNode close(long tradeId){if(tradeId<=0)throw new IllegalArgumentException("Trade ID must be positive");return call(HttpMethod.PUT,"/v3/accounts/"+enc(account)+"/trades/"+tradeId+"/close",java.util.Map.of("units","ALL"));}
}
