package org.learning.LibrarySimpleCRUD.services;

import org.hibernate.Hibernate;
import org.learning.LibrarySimpleCRUD.models.Book;
import org.learning.LibrarySimpleCRUD.models.Person;
import org.learning.LibrarySimpleCRUD.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findById(int personId) {
        return peopleRepository.findById(personId).orElse(null);
    }

    @Transactional
    public void save(Person newPerson) {
        peopleRepository.save(newPerson);
    }

    @Transactional
    public void update(int personId, Person newPerson) {
        newPerson.setPersonId(personId);
        peopleRepository.save(newPerson);
    }

    @Transactional
    public void delete(int personId) {
        peopleRepository.deleteById(personId);
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    public List<Book> getBooksByPersonId(int personId) {
        Optional<Person> person = peopleRepository.findById(personId);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());

            person.get().getBooks().forEach(book -> {
                long diffInMillis = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                if (diffInMillis > 864000000) {
                    book.setExpired(true);
                }
            });

            return person.get().getBooks();
        }

        return Collections.emptyList();
    }
}
