package com.example.forexhorizon.controller;
import com.example.forexhorizon.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Controller public class PageController {
 private final CalculatorService calculator;private final OandaService oanda;private final MnbService mnb;
 @Value("${app.authors}") private String authors;
 public PageController(CalculatorService calculator,OandaService oanda,MnbService mnb){this.calculator=calculator;this.oanda=oanda;this.mnb=mnb;}
 @ModelAttribute
 public void common(Model model){
  model.addAttribute("authors",authors);
  model.addAttribute("configured",oanda.configured());
  model.addAttribute("instruments",new String[]{"EUR_USD","GBP_USD","USD_JPY","EUR_GBP","AUD_USD","USD_CHF"});model.addAttribute("granularities",new String[]{"M5","M15","H1","H4","D","W"});
 }
 @GetMapping("/")public String home(){return "index";}
 @GetMapping("/test")public String test(){return "test";}
 @PostMapping("/test")public String calculate(@RequestParam BigDecimal a,@RequestParam BigDecimal b,@RequestParam String operation,Model m){try{m.addAttribute("result",calculator.calculate(a,b,operation));}catch(Exception e){m.addAttribute("error",e.getMessage());}m.addAttribute("a",a);m.addAttribute("b",b);m.addAttribute("operation",operation);return "test";}
 @GetMapping("/soap")public String soap(){return "soap";}
 @PostMapping("/soap")public String rates(@RequestParam String currency,@RequestParam LocalDate start,@RequestParam LocalDate end,Model m){try{var rates=mnb.history(currency,start,end);m.addAttribute("rates",rates);m.addAttribute("labels",rates.stream().map(MnbService.Rate::date).toList());m.addAttribute("values",rates.stream().map(r->Double.parseDouble(r.value())).toList());}catch(Exception e){m.addAttribute("error",e.getMessage());}m.addAttribute("currency",currency);m.addAttribute("start",start);m.addAttribute("end",end);return "soap";}
 @GetMapping("/forex/account")public String account(Model m){return run(m,"Forex Account",()->oanda.account());}
 @GetMapping("/forex/price")public String price(@RequestParam(defaultValue="EUR_USD") String instrument,Model m){m.addAttribute("instrument",instrument);return run(m,"Forex Actual Price",()->oanda.price(instrument));}
 @GetMapping("/forex/history")public String history(@RequestParam(defaultValue="EUR_USD") String instrument,@RequestParam(defaultValue="D") String granularity,Model m){m.addAttribute("instrument",instrument);m.addAttribute("granularity",granularity);return run(m,"Forex Historical Price",()->oanda.candles(instrument,granularity));}
 @GetMapping("/forex/open")public String openPage(Model m){m.addAttribute("title","Open Forex Position");m.addAttribute("page","open");return "forex";}
 @PostMapping("/forex/open")public String open(@RequestParam String instrument,@RequestParam int units,Model m){m.addAttribute("instrument",instrument);return run(m,"Open Forex Position",()->oanda.open(instrument,units));}
 @GetMapping("/forex/positions")public String positions(Model m){return run(m,"Open Forex Positions",()->oanda.positions());}
 @GetMapping("/forex/close")public String closePage(Model m){m.addAttribute("title","Close Forex Position");m.addAttribute("page","close");return "forex";}
 @PostMapping("/forex/close")public String close(@RequestParam long tradeId,Model m){return run(m,"Close Forex Position",()->oanda.close(tradeId));}
 private String run(Model m,String title,java.util.function.Supplier<Object> job){m.addAttribute("title",title);m.addAttribute("page",title.contains("Actual")?"price":title.contains("Historical")?"history":title.contains("Account")?"account":title.contains("Positions")?"positions":title.contains("Open")?"open":"close");try{m.addAttribute("data",job.get());}catch(Exception e){m.addAttribute("error",e.getMessage());}return "forex";}
}
