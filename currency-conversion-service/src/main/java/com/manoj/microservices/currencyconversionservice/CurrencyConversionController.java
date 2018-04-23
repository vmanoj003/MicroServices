package com.manoj.microservices.currencyconversionservice;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RestController
public class CurrencyConversionController {
	
	@Autowired
	private CurrencyExchangeServiceProxy feignProxy;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	
	
	//  Invoking other MicroService Using RestTemplate
	
	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {
		
		Map<String,String> uriVariables = new HashMap<>();
		
		uriVariables.put("from",from);
		uriVariables.put("to", to);
		
		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class,uriVariables);
	    CurrencyConversionBean response = responseEntity.getBody();
		
		return new CurrencyConversionBean(response.getId(),from,to,response.getConversionMultiple(),quantity,quantity.multiply(response.getConversionMultiple()),response.getPort());
		
	}
	
	
	//  Invoking other MicroService Using Feign Proxy Interface
	
	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable("from") String from, @PathVariable("to") String to,
			@PathVariable("quantity") BigDecimal quantity) {
		
		 
	    CurrencyConversionBean response = feignProxy.retrieveExchangeValue(from, to);
	    
	    logger.info("Response Totoal Amount -> {}", response.getTotalCalculatedAmount());
		
		return new CurrencyConversionBean(response.getId(),from,to,response.getConversionMultiple() ,quantity,quantity.multiply(response.getConversionMultiple()),response.getPort());
		
	}


	
}
