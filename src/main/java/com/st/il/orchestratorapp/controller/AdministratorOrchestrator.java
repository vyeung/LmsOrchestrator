package com.st.il.orchestratorapp.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.st.il.orchestratorapp.dto.BkAuthPubDTO;
import com.st.il.orchestratorapp.dto.BookLoansDTO;
import com.st.il.orchestratorapp.exception.AlreadyExistsException;
import com.st.il.orchestratorapp.exception.BadRequestException;
import com.st.il.orchestratorapp.exception.NotFoundException;
import com.st.il.orchestratorapp.models.Author;
import com.st.il.orchestratorapp.models.Book;
import com.st.il.orchestratorapp.models.BookLoans;
import com.st.il.orchestratorapp.models.Borrower;
import com.st.il.orchestratorapp.models.LibraryBranch;
import com.st.il.orchestratorapp.models.Publisher;




/**
 * Orchestrator Controller for Administrator Services.
 * 
 * @author Vien Yeung
 * @author Tonny Huang
 * @author Al-amine AHMED MOUSSA
 */

@CrossOrigin
@RestController
@RequestMapping("/orchs")
@SuppressWarnings("unchecked")
public class AdministratorOrchestrator {

	@Autowired
	RestTemplate restTemplate;
	
	
	/******************************************
	*Admin Publishers Orch Methods 
	*******************************************/

	@GetMapping("/publishers")
	public List<Publisher> getAllPublishers() {	
		return restTemplate.getForObject("http://admin-service/admin/publishers", List.class);
	}
	
	@GetMapping("/publisher/{id}")
	public ResponseEntity<Publisher> getPublisher(@PathVariable int id) throws NotFoundException {
		Publisher publisher = null;
		try {
			publisher = restTemplate.getForObject("http://admin-service/admin/publisher/"+id, Publisher.class);
			return new ResponseEntity<>(publisher, HttpStatus.OK);
		}
		catch(HttpClientErrorException ex) {
			//convert json string to a usable hashmap and then throw exception based on message field
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
		    throw new NotFoundException(map.get("message"));
		}
	}
	
	//note: don't need id in request body
	@PostMapping("/publisher")
	public ResponseEntity<Publisher> addPublisher(@RequestBody Publisher pub) throws AlreadyExistsException  {
		try {
			return restTemplate.postForEntity("http://admin-service/admin/publisher", pub, Publisher.class);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
		    throw new AlreadyExistsException(map.get("message"));
		}
	}
	
	//note: don't need id in request body
	@PutMapping("/publisher/{id}")
	public ResponseEntity<Publisher> updatePublisher(@PathVariable int id, @RequestBody Publisher pub) throws NotFoundException {
		try {
			restTemplate.put("http://admin-service/admin/publisher/"+id, pub);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
			throw new NotFoundException(map.get("message"));
		}
	}
	
	@DeleteMapping("/publisher/{id}")
	public ResponseEntity<Publisher> deletePublisher(@PathVariable int id) throws NotFoundException {
		try {
			restTemplate.delete("http://admin-service/admin/publisher/"+id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
			throw new NotFoundException(map.get("message"));
		}
	}
	
	/******************************************
	*Admin Books Orch Methods
	*******************************************/
	
	@GetMapping("/books")
	public List<BkAuthPubDTO> getAllBooks() {
		return restTemplate.getForObject("http://admin-service/admin/books", List.class);
	}
	
	@GetMapping("/book/{id}")
	public ResponseEntity<BkAuthPubDTO> getBook(@PathVariable int id) throws NotFoundException {
		BkAuthPubDTO obj = null;
		try {
			obj = restTemplate.getForObject("http://admin-service/admin/book/"+id, BkAuthPubDTO.class);
			return new ResponseEntity<>(obj, HttpStatus.OK);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
		    throw new NotFoundException(map.get("message"));
		}		
	}
	
	@PostMapping("/book")
	public ResponseEntity<BkAuthPubDTO> addBook(@RequestBody BkAuthPubDTO bookBody) 
	throws AlreadyExistsException, BadRequestException {
		try {
			return restTemplate.postForEntity("http://admin-service/admin/book", bookBody, BkAuthPubDTO.class);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
		    
			if(map.get("message").contains("by")) {
				throw new AlreadyExistsException(map.get("message"));
			}
			else if(map.get("message").contains("Add author")) {
				throw new BadRequestException(map.get("message"));
			}
			//case: Add publisher ____ to the database first
			else {
				throw new BadRequestException(map.get("message"));
			}
		}		
	}
	
	@PutMapping("/book/{id}")
	public ResponseEntity<BkAuthPubDTO> updateBook(@PathVariable int id, @RequestBody BkAuthPubDTO bookBody) 
	throws NotFoundException, BadRequestException {
		try {
			restTemplate.put("http://admin-service/admin/book/"+id, bookBody);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
			
			if(map.get("message").contains("Update failed.")) {
				throw new NotFoundException(map.get("message"));
			}
			else if(map.get("message").contains("Add author")) {
				throw new BadRequestException(map.get("message"));
			}
			//case: Add publisher ____ to the database first
			else {
				throw new BadRequestException(map.get("message"));
			}
		}
	}
	
	@DeleteMapping("/book/{id}")
	public ResponseEntity<Book> deleteBook(@PathVariable int id) throws NotFoundException {
		try {
			restTemplate.delete("http://admin-service/admin/book/"+id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
			throw new NotFoundException(map.get("message"));
		}
	}
	
	/******************************************
	*Admin Book Loans Orch Methods
	*******************************************/
	
	@GetMapping("/bookLoansAsDTO")
	public List<BookLoansDTO> getAllBookLoansAsDTO() {
		return restTemplate.getForObject("http://admin-service/admin/bookLoansAsDTO", List.class);
	}
	
	@GetMapping("/bookLoans")
	public List<BookLoans> getAllBookLoans() {
		return restTemplate.getForObject("http://admin-service/admin/bookLoans", List.class);
	}
	
	//note: when testing, every field in BookLoans must have some value (not null)
	@PutMapping("/bookLoans/bookId/{bookId}/branchId/{branchId}/cardNo/{cardNo}")
	public ResponseEntity<BookLoans> updateBookLoanDueDate(
			@PathVariable int bookId, 
			@PathVariable int branchId, 
			@PathVariable int cardNo, 
			@RequestBody BookLoans newDueDate) throws NotFoundException {
		
		try {
			restTemplate.put("http://admin-service/admin/bookLoans/bookId/"+bookId+"/branchId/"+branchId+"/cardNo/"+cardNo, newDueDate);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
			throw new NotFoundException(map.get("message"));
		}
	}
	
	/******************************************
	*Admin Authors Orch Methods
	*******************************************/
	
	String authorUrl = "http://admin-service/admin/author/";
	
	@GetMapping("/authors")
    public List<Author> getAllAuthors() {
    	return restTemplate.getForObject("http://admin-service/admin/authors/", List.class);
    }
    
    @GetMapping("/author/{authorId}")
    public ResponseEntity<Author> getAuthor(@PathVariable int authorId) throws NotFoundException {
    	Author author = null;
		try {
			author = restTemplate.getForEntity(authorUrl+authorId, Author.class).getBody();
			return new ResponseEntity<>(author, HttpStatus.OK);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
		    throw new NotFoundException(map.get("message"));
		}
    }
    
    @PostMapping("/author")
    public ResponseEntity<Author> addAuthor(@RequestBody Author author) throws AlreadyExistsException {
    	try {
			restTemplate.postForEntity(authorUrl, author, Author.class);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
		    throw new AlreadyExistsException(map.get("message"));
		}
    }
    
    @PutMapping("/author/{authorId}")
    public ResponseEntity<Author> updateAuthor(@PathVariable int authorId, @RequestBody Author author) throws NotFoundException {
    	try {
			restTemplate.put(authorUrl+authorId, author);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
			throw new NotFoundException(map.get("message"));
		}	
    }
    
    @DeleteMapping("/author/{authorId}")
    public ResponseEntity<Author> deleteAuthor(@PathVariable int authorId) throws NotFoundException {
    	try {
			restTemplate.delete(authorUrl+authorId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
			throw new NotFoundException(map.get("message"));
		}
    }
    
    /******************************************
	*Admin Borrower Orch Methods
	*******************************************/
    
    String borrowerUrl = "http://admin-service/admin/borrower/";
    
	@GetMapping("/borrowers")
    public List<Borrower> getAllBorrowers() {
    	return restTemplate.getForObject("http://admin-service/admin/borrowers/", List.class);
    }
    
    @GetMapping("/borrower/{borrowerId}")
    public ResponseEntity<Borrower> getBorrower(@PathVariable int borrowerId) throws NotFoundException {
    	Borrower borrower = null;
		try {
			borrower = restTemplate.getForEntity(borrowerUrl+borrowerId, Borrower.class).getBody();
			return new ResponseEntity<>(borrower, HttpStatus.OK);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
		    throw new NotFoundException(map.get("message"));
		}
    }
    
    @PostMapping("/borrower")
    public ResponseEntity<Borrower> addBorrower(@RequestBody Borrower borrower) throws AlreadyExistsException {
    	try {
			restTemplate.postForEntity(borrowerUrl, borrower, Borrower.class);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
		    throw new AlreadyExistsException(map.get("message"));
		}
    }
    
    @PutMapping("/borrower/{borrowerId}")
    public ResponseEntity<Borrower> updateBorrower(@PathVariable int borrowerId, @RequestBody Borrower borrower) throws NotFoundException {
    	try {
			restTemplate.put(borrowerUrl+borrowerId, borrower);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
			throw new NotFoundException(map.get("message"));
		}	
    }
    
    @DeleteMapping("/borrower/{borrowerId}")
    public ResponseEntity<Borrower> deleteBorrower(@PathVariable int borrowerId) throws NotFoundException {
    	try {
			restTemplate.delete(borrowerUrl+borrowerId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
			throw new NotFoundException(map.get("message"));
		}
    }
    
    /******************************************
	*Admin Library Branch Orch Methods
	*******************************************/
    
    String libraryBranchUrl = "http://admin-service/admin/libraryBranch/";
    
	@GetMapping("/libraryBranches")
    public List<LibraryBranch> getAllLibraryBranches() {
    	return restTemplate.getForObject("http://admin-service/admin/libraryBranches/", List.class);
    }
    
    @GetMapping("/libraryBranch/{libraryBranchId}")
    public ResponseEntity<LibraryBranch> getLibraryBranch(@PathVariable int libraryBranchId) throws NotFoundException {	
    	LibraryBranch libBranch = null;
		try {
			libBranch = restTemplate.getForEntity(libraryBranchUrl+libraryBranchId, LibraryBranch.class).getBody();
			return new ResponseEntity<>(libBranch, HttpStatus.OK);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
		    throw new NotFoundException(map.get("message"));
		}
	}
    
    @PostMapping("/libraryBranch")
    public ResponseEntity<LibraryBranch> addLibraryBranch(@RequestBody LibraryBranch libraryBranch) throws AlreadyExistsException {
    	try {
			restTemplate.postForEntity(libraryBranchUrl, libraryBranch, LibraryBranch.class);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
		    throw new AlreadyExistsException(map.get("message"));
		}	
    }
    
    @PutMapping("/libraryBranch/{libraryBranchId}")
    public ResponseEntity<LibraryBranch> updateLibraryBranch(@PathVariable int libraryBranchId, @RequestBody LibraryBranch libraryBranch) throws NotFoundException {
    	try {
			restTemplate.put(libraryBranchUrl+libraryBranchId, libraryBranch);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
			throw new NotFoundException(map.get("message"));
		}
    }
    
    @DeleteMapping("/libraryBranch/{libraryBranchId}")
    public ResponseEntity<LibraryBranch> deleteLibraryBranch(@PathVariable int libraryBranchId) throws NotFoundException {
    	try {
			restTemplate.delete(libraryBranchUrl+libraryBranchId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch(HttpClientErrorException ex) {
			HashMap<String,String> map = new Gson().fromJson(ex.getResponseBodyAsString(), HashMap.class);
			throw new NotFoundException(map.get("message"));
		}
    }
}