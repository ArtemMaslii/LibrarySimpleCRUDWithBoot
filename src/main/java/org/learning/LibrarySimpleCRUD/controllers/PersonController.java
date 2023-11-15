package org.learning.LibrarySimpleCRUD.controllers;


import jakarta.validation.Valid;
import org.learning.LibrarySimpleCRUD.models.Person;
import org.learning.LibrarySimpleCRUD.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PersonController {
    private final PeopleService peopleService;

    @Autowired
    public PersonController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("personList", this.peopleService.findAll());
        return "people/index";
    }

    @GetMapping({"/{personId}"})
    public String show(@PathVariable("personId") int personId, Model model) {
        model.addAttribute("person", peopleService.findById(personId));
        model.addAttribute("books", peopleService.getBooksByPersonId(personId));
        return "/people/show";
    }

    @GetMapping({"/new"})
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult result) {

        if (result.hasErrors()) {
            return "/people/new";
        } else {
            this.peopleService.save(person);
            return "redirect:/people";
        }
    }

    @GetMapping({"/{personId}/edit"})
    public String edit(@PathVariable("personId") int personId, Model model) {
        model.addAttribute("person", this.peopleService.findById(personId));
        return "people/edit";
    }

    @PostMapping({"/{personId}/update"})
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult result, @PathVariable("personId") int personId) {
        if (result.hasErrors()) {
            return "people/edit";
        } else {
            this.peopleService.update(personId, person);
            return "redirect:/people";
        }
    }

    @PostMapping({"/{personId}/delete"})
    public String delete(@PathVariable("personId") int personId) {
        this.peopleService.delete(personId);
        return "redirect:/people";
    }
}
