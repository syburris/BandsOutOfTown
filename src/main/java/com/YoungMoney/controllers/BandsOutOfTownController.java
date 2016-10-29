package com.YoungMoney.controllers;

import com.YoungMoney.entities.Concert;
import com.YoungMoney.entities.User;
import com.YoungMoney.services.ConcertRepository;
import com.YoungMoney.services.UserRepository;
import com.YoungMoney.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by stevenburris on 10/28/16.
 */
@Controller
public class BandsOutOfTownController {

    @Autowired
    UserRepository users;

    @Autowired
    ConcertRepository concerts;

    @PostConstruct
    public void init() throws PasswordStorage.CannotPerformOperationException {
        User defaultUser = new User("Steven", PasswordStorage.createHash("Young"));
        if (users.findFirstByName(defaultUser.name) == null) {
            users.save(defaultUser);
        }
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(Model model, String search, HttpSession session) {
        String name = (String) session.getAttribute("username");
        User user = users.findFirstByName(name);

        Iterable<Concert> concertList;
        if (search != null) {
            concertList = concerts.findFirstByCity(search);
        }
        else {
            concertList = concerts.findAll();
        }

        model.addAttribute("now", LocalDate.now());
        model.addAttribute("user", user);
        model.addAttribute("concerts", concertList);
        return "index";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, HttpSession session) throws Exception {
        User user = users.findFirstByName(username);
        if (user == null) {
            user = new User(username, PasswordStorage.createHash(password));
            users.save(user);
        }
        else if(!PasswordStorage.verifyPassword(password, user.password)) {
            throw new Exception("Wrong password!");
        }
        session.setAttribute("username",username);
        return "redirect:/";

    }

    @RequestMapping(path = "/create-concert", method = RequestMethod.POST)
    public String create(String name, String venue, String city, String state, String date, HttpSession session) throws Exception {
        String username = (String) session.getAttribute("username");
        User user = users.findFirstByName(username);
        if (user == null) {
            throw new Exception("Not logged in!");
        }
        Concert concert = new Concert(name, LocalDate.parse(date), venue, city, state, user);
        concerts.save(concert);
        return "redirect:/";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


}
