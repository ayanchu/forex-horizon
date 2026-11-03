package com.example.forexhorizon.service;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
class CalculatorServiceTest {
 private final CalculatorService calculator=new CalculatorService();
 @Test void addition(){assertEquals(0,calculator.calculate(new BigDecimal("7"),new BigDecimal("3"),"add").compareTo(new BigDecimal("10")));}
 @Test void subtraction(){assertEquals(0,calculator.calculate(new BigDecimal("7"),new BigDecimal("3"),"subtract").compareTo(new BigDecimal("4")));}
 @Test void multiplication(){assertEquals(0,calculator.calculate(new BigDecimal("7"),new BigDecimal("3"),"multiply").compareTo(new BigDecimal("21")));}
 @Test void division(){assertEquals(0,calculator.calculate(new BigDecimal("7"),new BigDecimal("2"),"divide").compareTo(new BigDecimal("3.5")));}
 @Test void divisionByZero(){assertThrows(IllegalArgumentException.class,()->calculator.calculate(BigDecimal.ONE,BigDecimal.ZERO,"divide"));}
 @Test void unsupportedOperation(){assertThrows(IllegalArgumentException.class,()->calculator.calculate(BigDecimal.ONE,BigDecimal.ONE,"power"));}
 @Test void missingOperand(){assertThrows(IllegalArgumentException.class,()->calculator.calculate(null,BigDecimal.ONE,"add"));}
 @Test void negativeNumbers(){assertEquals(0,calculator.calculate(new BigDecimal("-2"),new BigDecimal("5"),"multiply").compareTo(new BigDecimal("-10")));}
}
