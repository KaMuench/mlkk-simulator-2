package com.mlkk.simulator;

import com.mlkk.simulator.entities.Account;
import com.mlkk.simulator.entities.Address;
import com.mlkk.simulator.entities.Event;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("mlkk")
public class Controller {
    static int ID_COUNTER = 0;
    HashMap<Integer,Account> list = new HashMap<>();
    HashMap<Integer, Event> events = createEvents();


    @GetMapping("account/get_events")
    public void getPersonalisedEvents(@RequestParam("account_id") int id, @RequestParam("token") String token, HttpServletResponse resp) throws IOException {
        for(Map.Entry<Integer,Account> a : list.entrySet()) {
            if(a.getValue().getToken().equals(token) && a.getValue().getId() == id) {
                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().print(createJson(id));
                resp.getWriter().close();
                return;
            } else {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        }
        resp.setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @PostMapping("registration/new_account")
    public void createAccount(@RequestParam("name") String name, @RequestParam("surname") String surname, @RequestParam("email") String email, @RequestParam("password") String password, HttpServletResponse resp) {
        for(Map.Entry<Integer,Account> a : list.entrySet()) {
            if(a.getValue().getEmail().equals(email)) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }
        }
        list.put(ID_COUNTER, new Account(name, surname, email, password, ID_COUNTER));
        System.out.println(list.get(ID_COUNTER));
        ID_COUNTER++;
        resp.setStatus(HttpServletResponse.SC_OK);
    }
    @DeleteMapping("account/delete_account")
    public void deleteAccount(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletResponse resp) {
        int id = -1;
        for(Map.Entry<Integer,Account> a : list.entrySet()) {
            if(a.getValue().getEmail().equals(email) && a.getValue().getPsw().equals(password)) {
                id = a.getKey();
                break;
            }
        }
        if(id != -1) {
            list.remove(id);
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }

    @PostMapping("login")
    public void login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletResponse resp) throws IOException {
        Account ac;
        Random rand = new Random();
        for(Map.Entry<Integer,Account> e : list.entrySet()) {
            if(e.getValue().getEmail().equals(email) && e.getValue().getPsw().equals(password)) {
                ac = e.getValue();
                ac.setToken(String.valueOf(rand.nextInt(10000, 20000)) + e.getKey());
                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println(ac.toJson());
                resp.getWriter().close();
                System.out.println(ac.toJson());
                return;
            } else {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                System.out.println(email + "   " + password);
            }
        }
        resp.setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @PostMapping("account/attend_event")
    public void attend(@RequestParam("account_id") int accId, @RequestParam("event_id") int eventId, @RequestParam("token") String token, HttpServletResponse resp) throws IOException {
        for(Map.Entry<Integer,Account> a : list.entrySet()) {
            if(a.getValue().getToken().equals(token) && a.getValue().getId() == accId && events.containsKey(eventId)) {
                a.getValue().getMSignedUpForEvents().put(eventId, null);
                System.out.println(a.getValue().getMSignedUpForEvents());
                resp.setStatus(HttpServletResponse.SC_OK);
                return;
            } else {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        }
        resp.setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @PostMapping("account/change_password")
    public void changePassword(@RequestParam("email") String email, @RequestParam("new_password") String newPassword, @RequestParam("old_password") String oldPassword, HttpServletResponse resp) throws IOException {
        for(Map.Entry<Integer,Account> a : list.entrySet()) {
            if(a.getValue().getEmail().equals(email) && a.getValue().getPsw().equals(oldPassword)) {
                a.getValue().setPsw(newPassword);
                System.out.printf("%-25s Old: %-25s New: %-25s", email, oldPassword, newPassword);
                resp.setStatus(HttpServletResponse.SC_OK);
                return;
            } else {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        }
    }

    HashMap<Integer, Event> createEvents() {
        HashMap<Integer,Event> map = new HashMap<>();
        map.put(1, new Event(1,"Pizza essen", "2022-12-01T11:32:00", "Wollte euch zum Pizza essen einladen", null, 0, true, true));
        map.put(2, new Event(2,"Film gucken", "2022-12-17T18:00:00", "Heute werden wir Sing 1 angucken\\nBitte bringt reichlich Snacks mit :D", null, 0, false, false));
        map.put(3, new Event(3,"Spazieren", "2022-11-01T11:20:00","Zusammen im Zuffenhausener Stadtwald spazieren gehen", new Address("Markgröniger Straße", "50", "70435", "Stuttgart"), 0, true, true));
        return map;
    }

    String createJson(int accId) {
        StringBuilder builder = new StringBuilder();
        builder.append("[\n");
        for(Map.Entry<Integer, Event> event : events.entrySet()) {
            builder.append("\t{\n").append("\t\t\"event\": ").append(event.getValue().toJson())
                    .append(",\n").append("\t\t\"attending\": ").append(list.get(accId).getMSignedUpForEvents().containsKey(event.getKey())).append(",\n")
                    .append("\t\t\"favourites\": ").append(list.get(accId).getMFavorites().containsKey(event.getKey())).append("\n")
                    .append("\t},");
        }
        if(builder.length() > 0) builder.deleteCharAt(builder.length()-1);
        builder.append("]");
        return builder.toString();
    }
}
