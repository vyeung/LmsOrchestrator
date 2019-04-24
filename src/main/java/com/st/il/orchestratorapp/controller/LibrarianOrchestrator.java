package com.st.il.orchestratorapp.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.st.il.orchestratorapp.dto.BkCopiesDTO;
import com.st.il.orchestratorapp.exception.NotFoundException;
import com.st.il.orchestratorapp.models.BookCopies;
import com.st.il.orchestratorapp.models.LibraryBranch;


/**
 * Orchestrator Controller for Librarian Services.
 * 
 * @author Vien Yeung
 * @author Tonny Huang
 * @author Al-amine AHMED MOUSSA
 */

@RestController
@RequestMapping("/orchs")
@SuppressWarnings("unchecked")
public class LibrarianOrchestrator {
	
	
	@Autowired
	RestTemplate restTemplate;
	
	

    
	@GetMapping("/libraries")
	public List<LibraryBranch> getAllBranches() {
		return restTemplate.getForObject("http://librarian-service/librarian/libraries", List.class);
	}
    
    @GetMapping("/libraries/{branchId}")
	public ResponseEntity<LibraryBranch> getLibBranch(@PathVariable int branchId) throws NotFoundException {
    	LibraryBranch libBranch = null;
		try {
			libBranch = restTemplate.getForObject("http://librarian-service/librarian/libraries/"+branchId, LibraryBranch.class);
			return new ResponseEntity<>(libBranch, HttpStatus.OK);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
		    throw new NotFoundException(map.get("message"));
		}
	}
    
    @PutMapping("/libraries/{branchId}")
	public ResponseEntity<LibraryBranch> updateLibBranch(@PathVariable int branchId, @RequestBody LibraryBranch libBranch) throws NotFoundException {
    	try {
			restTemplate.put("http://librarian-service/librarian/libraries/"+branchId, libBranch);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
			throw new NotFoundException(map.get("message"));
		}
	}
    
   	@GetMapping("/libraries/{id}/book_copies")
   	public ResponseEntity<List<BkCopiesDTO>> getBookCopies(@PathVariable int id) throws NotFoundException {
   		List<BkCopiesDTO> list = null;
		try {
			list = restTemplate.getForObject("http://librarian-service/librarian/libraries/"+id+"/book_copies", List.class);
			return new ResponseEntity<>(list, HttpStatus.OK);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
		    throw new NotFoundException(map.get("message"));
		}
   	}
    
    @PutMapping("/libraries/{branchId}/book_copies/{bookId}")
	public ResponseEntity<BookCopies> updateBookCopies(@PathVariable int branchId, @PathVariable int bookId, @RequestBody BookCopies bkCopy) 
	throws NotFoundException {
     	try {
			restTemplate.put("http://librarian-service/librarian/libraries/"+branchId+"/book_copies/"+bookId, bkCopy);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
			throw new NotFoundException(map.get("message"));
		}
	}

}
