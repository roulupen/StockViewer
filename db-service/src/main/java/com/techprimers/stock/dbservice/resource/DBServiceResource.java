package com.techprimers.stock.dbservice.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techprimers.stock.dbservice.model.Quote;
import com.techprimers.stock.dbservice.model.Quotes;
import com.techprimers.stock.dbservice.repository.QuotesRepository;

@RestController
@RequestMapping("/rest/db")
public class DBServiceResource {

	@Autowired
	private QuotesRepository quotesRepository;

	@GetMapping("/{username}")
	public List<String> getQuotes(@PathVariable("username") final String username) {
		return quotesRepository.findByUserName(username)
					.stream()
					.map(Quote::getQuote)
					.collect(Collectors.toList());
	}
	
	@PostMapping("/add")
	public List<String> addQuote(@RequestBody final Quotes quotes) {
		
		quotes.getQuotes()
				.stream()
				.map(quote -> new Quote(quotes.getUserName(), quote))
				.forEach(quote -> quotesRepository.save(quote));
		
		return getQuotesByUserName(quotes.getUserName());
	}

	@DeleteMapping("/delete/{username}")
	public boolean delete(@PathVariable("username") final String username) {
		quotesRepository.deleteAll(quotesRepository.findByUserName(username));
		return true;
	}
	
	@DeleteMapping("/delete/{username}/{quoteName}")
	public boolean deleteQuote(@PathVariable("username") final String username, @PathVariable("quoteName") final String quoteName ) {
		
		System.out.println("Inside Delete...");
		
		List<Quote> quotes = quotesRepository.findByUserName(username)
								.stream()
								.filter(quote -> quote.getQuote().equalsIgnoreCase(quoteName))
								.collect(Collectors.toList());
		quotesRepository.deleteAll(quotes);
		return true;
	}
	
	private List<String> getQuotesByUserName(final String userName) {
		return quotesRepository.findByUserName(userName)
				.stream()
				.map(Quote::getQuote)
				.collect(Collectors.toList());
	}
}
