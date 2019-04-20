package com.st.orchestrator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.st.orchestrator.models.BookCopies;
import com.st.orchestrator.dto.BkAuthPubDTO;
import com.st.orchestrator.dto.BkCopiesDTO;
import com.st.orchestrator.dto.BkLoansBkAuthDTO;
import com.st.orchestrator.models.Author;
import com.st.orchestrator.models.BookLoans;
import com.st.orchestrator.models.Borrower;
import com.st.orchestrator.models.LibraryBranch;
import com.st.orchestrator.models.Publisher;

@RestController
@RequestMapping("/orch")
public class Orchestrator {

	@Autowired
	RestTemplate restTemplate;
	
	
	/******************************************
	*Admin Publishers Orch Methods 
	*******************************************/

	@SuppressWarnings("unchecked")
	@GetMapping("/publishers")
	public List<Publisher> getAllPublishers() {
		return restTemplate.getForObject("http://admin-service/admin/publishers", List.class);
	}
	
	@GetMapping("/publisher/{id}")
	public Publisher getPublisher(@PathVariable int id) {
		return restTemplate.getForObject("http://admin-service/admin/publisher/"+id, Publisher.class);
	}
	
	@PostMapping("/publisher")
	public ResponseEntity<Publisher> addPublisher(@RequestBody Publisher pub) {
		return restTemplate.postForEntity("http://admin-service/admin/publisher", pub, Publisher.class);
	}
	
	@PutMapping("/publisher/{id}")
	public void updatePublisher(@PathVariable int id, @RequestBody Publisher pub) {
		restTemplate.put("http://admin-service/admin/publisher/"+id, pub);
	}
	
	@DeleteMapping("/publisher/{id}")
	public void deletePublisher(@PathVariable int id) {
		restTemplate.delete("http://admin-service/admin/publisher/"+id);
	}
	
	/******************************************
	*Admin Books Orch Methods
	*******************************************/
	
	@SuppressWarnings("unchecked")
	@GetMapping("/books")
	public List<BkAuthPubDTO> getAllBooks() {
		return restTemplate.getForObject("http://admin-service/admin/books", List.class);
	}
	
	@GetMapping("/book/{id}")
	public BkAuthPubDTO getBook(@PathVariable int id) {
		return restTemplate.getForObject("http://admin-service/admin/book/"+id, BkAuthPubDTO.class);
	}
	
	@PostMapping("/book")
	public ResponseEntity<BkAuthPubDTO> addBook(@RequestBody BkAuthPubDTO bookBody) {
		return restTemplate.postForEntity("http://admin-service/admin/book", bookBody, BkAuthPubDTO.class);
	}
	
	@PutMapping("/book/{id}")
	public void updateBook(@PathVariable int id, @RequestBody BkAuthPubDTO bookBody) {
		restTemplate.put("http://admin-service/admin/book/"+id, bookBody);
	}
	
	@DeleteMapping("/book/{id}")
	public void deleteBook(@PathVariable int id) {
		restTemplate.delete("http://admin-service/admin/book/"+id);
	}
	
	/******************************************
	*Admin Book Loans Orch Methods
	*******************************************/
	
	@SuppressWarnings("unchecked")
	@GetMapping("/bookLoans")
	public List<BookLoans> getAllBookLoans() {
		return restTemplate.getForObject("http://admin-service/admin/bookLoans", List.class);
	}
	
	//note: when testing, every field in BookLoans must have some value (not null)
	@PutMapping("/bookLoans/bookId/{bookId}/branchId/{branchId}/cardNo/{cardNo}")
	public void updateBookLoanDueDate(@PathVariable int bookId, @PathVariable int branchId, @PathVariable int cardNo, @RequestBody BookLoans newDueDate) {
		System.out.println(newDueDate.getDueDate());
		restTemplate.put("http://admin-service/admin/bookLoans/bookId/"+bookId+"/branchId/"+branchId+"/cardNo/"+cardNo, newDueDate);
	}
	
	/******************************************
	*Admin Authors Orch Methods
	*******************************************/
	
	String authorUrl = "http://admin-service/admin/author/";
	
	@SuppressWarnings("unchecked")
	@GetMapping("/authors")
    public List<Author> getAllAuthors() {
    	return restTemplate.getForObject("http://admin-service/admin/authors/", List.class);
    }
    
    @GetMapping("/author/{authorId}")
    public Author getAuthor(@PathVariable("authorId") int authorId){
    	return restTemplate.getForEntity(authorUrl+authorId, Author.class).getBody();
    }
    
    @PostMapping("/author")
    public Author postAuthor(@RequestBody Author author) {
    	HttpEntity<Author> request = new HttpEntity<>(author);
    	Author out = restTemplate.postForObject(authorUrl, request, Author.class);
    	return out;
    }
    
    @PutMapping("/author/{authorId}")
    public void putAuthor(@PathVariable("authorId") int authorId, @RequestBody Author author) {
    	HttpEntity<Author> request = new HttpEntity<Author>(author);
    	restTemplate.put(authorUrl+authorId, request);
    }
    
    @DeleteMapping("/author/{authorId}")
    public void deleteAuthor(@PathVariable("authorId") int authorId) {
    	restTemplate.delete(authorUrl+authorId);
    }
    
    /******************************************
	*Admin Borrower Orch Methods
	*******************************************/
    
    String borrowerUrl = "http://admin-service/admin/borrower/";
    
    @SuppressWarnings("unchecked")
	@GetMapping("/borrowers")
    public List<Borrower> getAllBorrowers() {
    	return restTemplate.getForObject("http://admin-service/admin/borrowers/", List.class);
    }
    
    @GetMapping("/borrower/{borrowerId}")
    public Borrower getBorrower(@PathVariable("borrowerId") int borrowerId){
    	return restTemplate.getForEntity(borrowerUrl+borrowerId, Borrower.class).getBody();
    }
    
    @PostMapping("/borrower")
    public Borrower postBorrower(@RequestBody Borrower borrower) {
    	HttpEntity<Borrower> request = new HttpEntity<>(borrower);
    	Borrower out = restTemplate.postForObject(borrowerUrl, request, Borrower.class);
    	return out;
    }
    
    @PutMapping("/borrower/{borrowerId}")
    public void putBorrower(@PathVariable("borrowerId") int borrowerId, @RequestBody Borrower borrower) {
    	HttpEntity<Borrower> request = new HttpEntity<Borrower>(borrower);
    	restTemplate.put(borrowerUrl+borrowerId, request);
    }
    
    @DeleteMapping("/borrower/{borrowerId}")
    public void deleteBorrower(@PathVariable("borrowerId") int borrowerId) {
    	restTemplate.delete(borrowerUrl+borrowerId);
    }
    
    /******************************************
	*Admin Library Branch Orch Methods
	*******************************************/
    
    String libraryBranchUrl = "http://admin-service/admin/libraryBranch/";
    
    @SuppressWarnings("unchecked")
	@GetMapping("/libraryBranches")
    public List<LibraryBranch> getAllLibraryBranches() {
    	return restTemplate.getForObject("http://admin-service/admin/libraryBranches/", List.class);
    }
    
    @GetMapping("/libraryBranch/{libraryBranchId}")
    public LibraryBranch getLibraryBranch(@PathVariable("libraryBranchId") int libraryBranchId){	
    	return restTemplate.getForEntity(libraryBranchUrl+libraryBranchId, LibraryBranch.class).getBody();
    }
    
    @PostMapping("/libraryBranch")
    public LibraryBranch postLibraryBranch(@RequestBody LibraryBranch libraryBranch) {
    	HttpEntity<LibraryBranch> request = new HttpEntity<>(libraryBranch);
    	LibraryBranch out = restTemplate.postForObject(libraryBranchUrl, request, LibraryBranch.class);
    	return out;
    }
    
    @PutMapping("/libraryBranch/{libraryBranchId}")
    public void putLibraryBranch(@PathVariable("libraryBranchId") int libraryBranchId, @RequestBody LibraryBranch libraryBranch) {
    	HttpEntity<LibraryBranch> request = new HttpEntity<LibraryBranch>(libraryBranch);
    	restTemplate.put(libraryBranchUrl+libraryBranchId, request);
    }
    
    @DeleteMapping("/libraryBranch/{libraryBranchId}")
    public void deleteLibraryBranch(@PathVariable("libraryBranchId") int libraryBranchId) {
    	restTemplate.delete(libraryBranchUrl+libraryBranchId);
    }
    
    /******************************************
   	*Borrower Orch Methods
   	*******************************************/
    
    //returns all books borrowed by that borrower
    @SuppressWarnings("unchecked")
    @GetMapping("/borrowers/{cardNo}")
    public List<BkLoansBkAuthDTO> getBorroweredBooks(@PathVariable int cardNo){
        return restTemplate.getForObject("http://borrower-service/borrower/cardNo/"+cardNo, List.class);
    }
    
    @SuppressWarnings("unchecked")
    @GetMapping("/borrowers/{cardNo}/libraries")
    public List<BkLoansBkAuthDTO> getAllBranches(@PathVariable int cardNo){
        return restTemplate.getForObject("http://borrower-service/borrower/cardNo/"+cardNo+"/libraries", List.class);
    }
    
    @SuppressWarnings("unchecked")
    @GetMapping("/borrowers/{cardNo}/libraries/{branchId}/books")
    public List<BkLoansBkAuthDTO> getAllLoans(@PathVariable int cardNo, @PathVariable int branchId){
        return restTemplate.getForObject("http://borrower-service/borrower/cardNo/"+cardNo+"/libraries/"+branchId+"/books", List.class);
    }
    
    @PostMapping("/borrowers/{cardNo}/checkout")
    public void borrowBook(@PathVariable int cardNo, @RequestBody BookLoans loan){
        BookLoans temp = new BookLoans();
        temp.setCardNo(cardNo);
        temp.setBookId(loan.getBookId());
        temp.setBranchId(loan.getBranchId());
        restTemplate.postForEntity("http://borrower-service/borrower/cardNo/"+cardNo+"/checkout", temp, BookLoans.class);
    }
    
    @DeleteMapping("/borrowers/{cardNo}/libraries/{branchId}/books/{bookId}")
    public void returnBook(@PathVariable int cardNo, @PathVariable int branchId, @PathVariable int bookId){
        restTemplate.delete("http://borrower-service/borrower/cardNo/"+cardNo+"/libraries/"+branchId+"/books/"+bookId);
    }
    
    /******************************************
   	*Librarian Orch Methods
   	*******************************************/
    
    @SuppressWarnings("unchecked")
	@GetMapping("/libraries")
	public List<LibraryBranch> getAllBranches() {
		return restTemplate.getForObject("http://librarian-service/librarian/libraries", List.class);
	}
    
    @GetMapping("/libraries/{branchId}")
	public LibraryBranch getLibBranch(@PathVariable int branchId) {
		return restTemplate.getForObject("http://librarian-service/librarian/libraries/"+branchId, LibraryBranch.class);
	}
    
    @PutMapping("/libraries/{branchId}")
	public void updateLibBranch(@PathVariable int branchId, @RequestBody LibraryBranch libBranch) {
		restTemplate.put("http://librarian-service/librarian/libraries/"+branchId, libBranch);
	}
    
    @SuppressWarnings("unchecked")
   	@GetMapping("/libraries/{id}/book_copies")
   	public List<BkCopiesDTO> getBookCopies(@PathVariable int id) {
   		return restTemplate.getForObject("http://librarian-service/librarian/libraries/"+id+"/book_copies", List.class);
   	}
    
    @PutMapping("/libraries/{branchId}/book_copies/{bookId}")
	public void updateBookCopies(@PathVariable int branchId, @PathVariable int bookId, @RequestBody BookCopies bkCopy) {
		restTemplate.put("http://librarian-service/librarian/libraries/"+branchId+"/book_copies/"+bookId, bkCopy);
	}
}
