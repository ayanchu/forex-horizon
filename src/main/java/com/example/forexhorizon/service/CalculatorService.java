package com.example.forexhorizon.service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;
@Service public class CalculatorService {
 public BigDecimal calculate(BigDecimal a, BigDecimal b, String operation) {
  if(a==null || b==null) throw new IllegalArgumentException("Both numbers are required");
  return switch(operation) {
   case "add" -> a.add(b); case "subtract" -> a.subtract(b); case "multiply" -> a.multiply(b);
   case "divide" -> { if(b.compareTo(BigDecimal.ZERO)==0) throw new IllegalArgumentException("Cannot divide by zero"); yield a.divide(b,8,RoundingMode.HALF_UP).stripTrailingZeros(); }
   default -> throw new IllegalArgumentException("Unsupported operation");
  };
 }
}
