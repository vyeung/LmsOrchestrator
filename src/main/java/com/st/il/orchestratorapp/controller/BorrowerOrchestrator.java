package com.st.il.orchestratorapp.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.st.il.orchestratorapp.dto.BkLoansBkAuthDTO;
import com.st.il.orchestratorapp.dto.BkLoansBranchDTO;
import com.st.il.orchestratorapp.exception.BadRequestException;
import com.st.il.orchestratorapp.exception.NotFoundException;
import com.st.il.orchestratorapp.models.BookLoans;




/**
 * Orchestrator Controller for Borrower Services.
 * 
 * @author Vien Yeung
 * @author Tonny Huang
 * @author Al-amine AHMED MOUSSA
 */


@RestController
@RequestMapping("/orchs")
@SuppressWarnings("unchecked")
public class BorrowerOrchestrator {
	
	@Autowired
	RestTemplate restTemplate;
	

    
    //returns all books borrowed by that borrower
    @GetMapping("/borrowers/{cardNo}")
    public ResponseEntity<List<BkLoansBkAuthDTO>> getBorroweredBooks(@PathVariable int cardNo) throws NotFoundException {
    	List<BkLoansBkAuthDTO> list = null;
		try {
			list = restTemplate.getForObject("http://borrower-service/borrower/cardNo/"+cardNo, List.class);
			return new ResponseEntity<>(list, HttpStatus.OK);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
		    throw new NotFoundException(map.get("message"));
		}
    }
    
    @GetMapping("/borrowers/{cardNo}/libraries")
    public ResponseEntity<List<BkLoansBranchDTO>> getAllBranches(@PathVariable int cardNo) throws NotFoundException {
    	List<BkLoansBranchDTO> list = null;
		try {
			list = restTemplate.getForObject("http://borrower-service/borrower/cardNo/"+cardNo+"/libraries", List.class);
			return new ResponseEntity<>(list, HttpStatus.OK);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
		    throw new NotFoundException(map.get("message"));
		}
    }
    
    @GetMapping("/borrowers/{cardNo}/libraries/{branchId}/books")
    public ResponseEntity<List<BkLoansBkAuthDTO>> getAllLoans(@PathVariable int cardNo, @PathVariable int branchId) throws NotFoundException {
    	List<BkLoansBkAuthDTO> list = null;
		try {
			list = restTemplate.getForObject("http://borrower-service/borrower/cardNo/"+cardNo+"/libraries/"+branchId+"/books", List.class);
			return new ResponseEntity<>(list, HttpStatus.OK);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
		    throw new NotFoundException(map.get("message"));
		}
    }
    
    @PostMapping("/borrowers/{cardNo}/checkout")
    public ResponseEntity<BookLoans> borrowBook(@PathVariable int cardNo, @RequestBody BookLoans loan) 
    throws NotFoundException, BadRequestException {
    	try {
    		BookLoans temp = new BookLoans();
            temp.setCardNo(cardNo);
            temp.setBookId(loan.getBookId());
            temp.setBranchId(loan.getBranchId());
            restTemplate.postForEntity("http://borrower-service/borrower/cardNo/"+cardNo+"/checkout", temp, BookLoans.class);
            return new ResponseEntity<>(HttpStatus.CREATED);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
		    
			if(map.get("message").contains("Login failed.")) {
				throw new NotFoundException(map.get("message"));
			}
			else if(map.get("message").contains("Check out failed. The library")) {
				throw new NotFoundException(map.get("message"));
			}
			else if(map.get("message").contains("Check out failed. The book")) {
				throw new NotFoundException(map.get("message"));
			}
			//case: "Check out failed. That library does not have any copies available."
			else {
				throw new BadRequestException(map.get("message"));
			}
		}	
    }
    
    @DeleteMapping("/borrowers/{cardNo}/libraries/{branchId}/books/{bookId}")
    public ResponseEntity<BookLoans> returnBook(@PathVariable int cardNo, @PathVariable int branchId, @PathVariable int bookId) 
    throws NotFoundException, BadRequestException {
    	try {
            restTemplate.delete("http://borrower-service/borrower/cardNo/"+cardNo+"/libraries/"+branchId+"/books/"+bookId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
			
			if(map.get("message").contains("Login failed.")) {
				throw new NotFoundException(map.get("message"));
			}
			else if(map.get("message").contains("Return failed. The library")) {
				throw new NotFoundException(map.get("message"));
			}
			else if(map.get("message").contains("Return failed. The book")) {
				throw new NotFoundException(map.get("message"));
			}
			//case: "Return failed. You do not have that book checked out!"
			else {
				throw new BadRequestException(map.get("message"));
			}
		}    	
    }

}
