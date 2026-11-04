package com.example.forexhorizon.controller;
import com.example.forexhorizon.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(PageController.class) class PageControllerTest {
 @Autowired MockMvc mvc;
 @MockitoBean CalculatorService calculator;
 @MockitoBean OandaService oanda;
 @MockitoBean MnbService mnb;
 @Test void homePage() throws Exception {mvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));}
 @Test void testPage() throws Exception {mvc.perform(get("/test")).andExpect(status().isOk()).andExpect(view().name("test"));}
 @Test void calculatorSuccess() throws Exception {when(calculator.calculate(new BigDecimal("2"),new BigDecimal("3"),"add")).thenReturn(new BigDecimal("5"));mvc.perform(post("/test").param("a","2").param("b","3").param("operation","add")).andExpect(status().isOk()).andExpect(model().attribute("result",new BigDecimal("5")));}
 @Test void calculatorError() throws Exception {when(calculator.calculate(any(),any(),eq("divide"))).thenThrow(new IllegalArgumentException("Cannot divide by zero"));mvc.perform(post("/test").param("a","2").param("b","0").param("operation","divide")).andExpect(status().isOk()).andExpect(model().attributeExists("error"));}
 @Test void soapPage() throws Exception {mvc.perform(get("/soap")).andExpect(status().isOk()).andExpect(view().name("soap"));}
 @Test void closePage() throws Exception {mvc.perform(get("/forex/close")).andExpect(status().isOk()).andExpect(view().name("forex"));}
}
