package com.YoungMoney.controllers;

import com.YoungMoney.entities.Concert;
import com.YoungMoney.entities.Go;
import com.YoungMoney.entities.User;
import com.YoungMoney.services.ConcertRepository;
import com.YoungMoney.services.GoingRepository;
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

    @Autowired
    GoingRepository going;

    @PostConstruct
    public void init() throws PasswordStorage.CannotPerformOperationException {
        User defaultUser = new User("Steven", PasswordStorage.createHash("Young"));
        if (users.findFirstByName(defaultUser.name) == null) {
            users.save(defaultUser);
        }
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(Model model, String citySearch, String venueSearch, String bandSearch, HttpSession session) {
        String name = (String) session.getAttribute("username");
        User user = users.findFirstByName(name);

        Iterable<Concert> concertList;
        if (isValid(citySearch) && (!isValid(venueSearch)) &&
                isValid(bandSearch)) {
            concertList = concerts.findByCity(citySearch);
        }
        else if (isValid(venueSearch) && (!isValid(citySearch) &&
                !isValid(bandSearch))) {
            concertList = concerts.findByVenue(venueSearch);
        }
        else if (isValid(bandSearch) && (!isValid(venueSearch) &&
                !isValid(citySearch))) {
            concertList = concerts.findByName(bandSearch);
        }
        else if ((isValid(bandSearch) && isValid(citySearch)) &&
            (!isValid(venueSearch))) {
            concertList = concerts.findByNameAndCity(bandSearch, citySearch);
        }
        else if ((isValid(venueSearch) && isValid(bandSearch)) &&
            (!isValid(citySearch))) {
            concertList = concerts.findByVenueAndName(venueSearch, bandSearch);
        }
        else if ((isValid(venueSearch) && isValid(citySearch)) &&
                (!isValid(bandSearch))) {
            concertList = concerts.findFirstByCityAndVenue(citySearch, venueSearch);
        }
        else if (isValid(citySearch) && isValid(venueSearch) && isValid(bandSearch)) {
            concertList = concerts.findByCityAndVenueAndName(citySearch, venueSearch, bandSearch);
        }
        else {
            concertList = concerts.findAll();
        }
        for (Concert concert : concertList) {
            concert.isMe = concert.user.name.equals(name);
            concert.isGoing = going.findFirstByUserAndConcert(user, concert) != null;
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

    @RequestMapping(path = "/edit-concert", method = RequestMethod.GET)
    public String editPage(Model model, HttpSession session, int id) throws Exception {
        String name = (String) session.getAttribute("username");
        User user = users.findFirstByName(name);
        Concert concert = concerts.findOne(id);
        if (user == null) {
            throw new Exception("You can't do that!");
        }
        model.addAttribute("user", user);
        model.addAttribute("concert", concert);
        model.addAttribute("now", LocalDate.now());
        return "edit";
    }

    @RequestMapping(path = "/edit-concert", method = RequestMethod.POST)
    public String editConcert(String name, String venue, String city, String state, String date, int id, HttpSession session) throws Exception {
        if (!validateUser(session,id)) {
            throw new Exception("Not logged in!");
        }
        Concert concert = concerts.findOne(id);
        concert.setDate(LocalDate.parse(date));
        concert.setName(name);
        concert.setVenue(venue);
        concert.setCity(city);
        concert.setState(state);
        concerts.save(concert);
        return "redirect:/";
    }

    @RequestMapping(path = "/going", method = RequestMethod.POST)
    public String addGoing(HttpSession session, int id) {
        String name = (String) session.getAttribute("username");
        User user = users.findFirstByName(name);
        Concert concert = concerts.findOne(id);
        Go go = going.findFirstByConcertAndUser(concert, user);
        if (go != null) {
            going.delete(go);
        }
        else {
            go = new Go(user, concert);
            going.save(go);
        }
        return "redirect:/";
    }

    @RequestMapping(path = "/delete-concert", method = RequestMethod.POST)
    public String deleteConcert(HttpSession session, int id) throws Exception {
        if (!validateUser(session, id)) {
            throw new Exception("Not logged in!");
        }
        concerts.delete(id);
        return "redirect:/";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    public boolean validateUser(HttpSession session, int id) {
        String name = (String) session.getAttribute("username");
        User user = users.findFirstByName(name);
        Concert concert = concerts.findOne(id);
        return user != null && concert !=null && user.name.equals(concert.user.name);
    }

    public boolean isValid(String string) {
        return string != null && !string.isEmpty();
    }
}
