package org.learning.LibrarySimpleCRUD.services;

import org.learning.LibrarySimpleCRUD.models.Book;
import org.learning.LibrarySimpleCRUD.models.Person;
import org.learning.LibrarySimpleCRUD.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(Sort.by("yearOfPublish"));
        }
        return booksRepository.findAll();
    }

    public Book findById(int bookId) {
        return booksRepository.findById(bookId).orElse(null);
    }

    @Transactional
    public void save(Book newBook) {
        booksRepository.save(newBook);
    }

    @Transactional
    public void update(int bookId, Book newBook) {
        Book book = booksRepository.findById(bookId).get();

        newBook.setBookId(bookId);
        newBook.setOwner(book.getOwner());

        booksRepository.save(newBook);
    }

    @Transactional
    public void delete(int bookId) {
        booksRepository.deleteById(bookId);
    }

    @Transactional
    public void releaseBook(int bookId) {
        booksRepository.findById(bookId).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setTakenAt(null);
                    }
        );
    }

    @Transactional
    public void assignBook(int bookId, Person owner) {
        booksRepository.findById(bookId).ifPresent(
                book -> {
                    book.setOwner(owner);
                    book.setTakenAt(new Date());
                }
        );
    }

    public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
        return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("yearOfPublish"))).getContent();
    }

    public List<Book> search(String title) {
        return booksRepository.findByTitleStartingWith(title);
    }

    public Person getBookOwner(int bookId) {
        return booksRepository.findById(bookId).map(Book::getOwner).orElse(null);
    }
}
