package com.example.forexhorizon.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.*;
@Service public class MnbService {
 private final RestClient client;
 public record Rate(String date,String currency,String value) {}
 public MnbService(@Value("${app.mnb.url}") String url){client=RestClient.builder().baseUrl(url).build();}
 public List<Rate> history(String currency,LocalDate from,LocalDate to){
  if(from.isAfter(to))throw new IllegalArgumentException("Start date must be on or before end date");
  if(!Set.of("EUR","USD","GBP","CHF","JPY","CZK","PLN","RON").contains(currency))throw new IllegalArgumentException("Unsupported currency");
  String inner="<GetExchangeRates xmlns=\"http://www.mnb.hu/webservices/\"><startDate>"+from+"</startDate><endDate>"+to+"</endDate><currencyNames>"+currency+"</currencyNames></GetExchangeRates>";
  String envelope="<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body>"+inner+"</soap:Body></soap:Envelope>";
  try{
   String xml=client.post().header("SOAPAction","\"http://www.mnb.hu/webservices/GetExchangeRates\"").contentType(MediaType.parseMediaType("text/xml;charset=utf-8")).body(envelope).retrieve().body(String.class);
   DocumentBuilderFactory f=secureFactory();Document doc=f.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
   NodeList results=doc.getElementsByTagNameNS("*","GetExchangeRatesResult");if(results.getLength()==0)throw new IllegalStateException("MNB returned no SOAP result");
   String nested=results.item(0).getTextContent();Document ratesDoc=f.newDocumentBuilder().parse(new InputSource(new StringReader(nested)));
   NodeList days=ratesDoc.getElementsByTagName("Day");List<Rate> rates=new ArrayList<>();
   for(int i=0;i<days.getLength();i++){Element day=(Element)days.item(i);NodeList items=day.getElementsByTagName("Rate");for(int j=0;j<items.getLength();j++){Element r=(Element)items.item(j);if(currency.equals(r.getAttribute("curr"))){String value=r.getTextContent().trim().replace(',','.');String unit=r.getAttribute("unit");if(!unit.isBlank()&&!unit.equals("1")){value=String.valueOf(Double.parseDouble(value)/Double.parseDouble(unit));}rates.add(new Rate(day.getAttribute("date"),currency,value));}}}
   return rates;
  }catch(Exception e){throw new IllegalStateException("Unable to retrieve MNB SOAP rates: "+e.getMessage(),e);}
 }
 private DocumentBuilderFactory secureFactory() throws Exception {DocumentBuilderFactory f=DocumentBuilderFactory.newInstance();f.setNamespaceAware(true);f.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);f.setFeature("http://xml.org/sax/features/external-general-entities",false);f.setFeature("http://xml.org/sax/features/external-parameter-entities",false);f.setXIncludeAware(false);f.setExpandEntityReferences(false);return f;}
}
