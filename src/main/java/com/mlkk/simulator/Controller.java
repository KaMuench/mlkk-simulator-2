package com.mlkk.simulator;

import com.mlkk.simulator.entities.Account;
import com.mlkk.simulator.entities.Address;
import com.mlkk.simulator.entities.Event;
import org.springframework.web.bind.annotation.*;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("mlkk")
public class Controller {
    static int ID_COUNTER = 0;
    HashMap<Integer,Account> list = createAccounts();
    HashMap<Integer, Event> events = createEvents();


    @GetMapping("account/get_events")
    public void getPersonalisedEvents(@RequestParam("account_id") int id, @RequestParam("token") String token, HttpServletResponse resp) throws IOException {
        for(Map.Entry<Integer,Account> a : list.entrySet()) {
            if(a.getValue().getToken().equals(token) && a.getValue().getId() == id) {
                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_OK);
                String json = createJson(id);
                System.out.println(json);
                resp.getWriter().print(json);
                resp.getWriter().close();
                return;
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
            System.out.println("Account deleted: \n" + list.get(id).toJson() );
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
                ac.setToken(ac.getId() != 0 ? String.valueOf(rand.nextInt(10000, 20000)) + e.getKey() : "100000");
                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println(ac.toJson());
                resp.getWriter().close();
                System.out.println(ac.toJson());
                return;
            }
        }
        System.out.println(email + "   " + password);
        resp.setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @PostMapping("account/attend_event")
    public void attend(@RequestParam("account_id") int accId, @RequestParam("event_id") int eventId, @RequestParam("token") String token, HttpServletResponse resp) throws IOException {
        for(Map.Entry<Integer,Account> a : list.entrySet()) {
            if(a.getValue().getToken().equals(token) && a.getValue().getId() == accId && events.containsKey(eventId)) {
                if(a.getValue().getMSignedUpForEvents().containsKey(eventId)) {
                    a.getValue().getMSignedUpForEvents().remove(eventId);
                    System.out.println("Signed out from event: " + eventId + " " + a.getValue().getMSignedUpForEvents());
                } else {
                    a.getValue().getMSignedUpForEvents().put(eventId, null);
                    System.out.println("Signed up for event: " + eventId + " " + a.getValue().getMSignedUpForEvents());
                }
                resp.setStatus(HttpServletResponse.SC_OK);
                return;
            }
        }
        resp.setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @PostMapping("account/favour")
    public void favour(@RequestParam("account_id") int accId, @RequestParam("event_id") int eventId, @RequestParam("token") String token, HttpServletResponse resp) throws IOException {
        for(Map.Entry<Integer,Account> a : list.entrySet()) {
            if(a.getValue().getToken().equals(token) && a.getValue().getId() == accId && events.containsKey(eventId)) {
                if(a.getValue().getMFavorites().containsKey(eventId)) {
                    a.getValue().getMFavorites().remove(eventId);
                    System.out.println("Favourite List removed: " + eventId + " " + a.getValue().getMSignedUpForEvents());
                } else {
                    a.getValue().getMFavorites().put(eventId, null);
                    System.out.println("Favourite List added: " + eventId + " " + a.getValue().getMSignedUpForEvents());
                }
                resp.setStatus(HttpServletResponse.SC_OK);
                return;
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
            }
        }
        resp.setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @GetMapping("event/download_image")
    public void downloadImage(@RequestParam("event_id") int eventId, @RequestParam("token") String token, HttpServletResponse resp) {
        if(events.containsKey(eventId) && list.values().stream().anyMatch(a -> a.getToken().equals(token))) {
            try(FileInputStream fis = new FileInputStream(eventId + ".png");
                BufferedInputStream bus = new BufferedInputStream(fis)) {
                byte[] image = bus.readAllBytes();
                resp.getOutputStream().write(image);
                resp.setContentType("image/png");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getOutputStream().close();
                System.out.println("Image: " + eventId + " was downloaded");
            } catch (IOException e) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        } else {
            System.out.println("Image: " + eventId + " download conflict");
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }

    HashMap<Integer, Event> createEvents() {
        HashMap<Integer,Event> map = new HashMap<>();
        map.put(1, new Event(1,"Pizza essen", "2022-12-01T11:32:00", "Wollte euch zum Pizza essen einladen", null, 2, "muench.kaleb@gmail.com",true, true));
        map.put(2, new Event(2,"Film gucken", "2022-12-17T18:00:00", "Heute werden wir Sing 1 angucken\\nBitte bringt reichlich Snacks mit :D", null, 0, "muench.kaleb@gmail.com",false, false));
        map.put(3, new Event(3,"Weinachtsfeier", "2022-12-22T16:20:00","Hey Leute wir wollen gerne am letzten Arbeitstag dieses Jahr eine Weinachtsfeier starten!! Dazu seid ihr alle herzlich eingeladen. Bringt doch gerne etwas zu naschen mit. Wir stellen die Getränke. \n\nMeldet euch am besten bei Prita falls ihr noch fragen habt. \nBis dahin :)"
                , new Address("Unsere Straße", "502c", "79989", "Weit weit weg"), 10, "muench.kaleb@gmail.com", true, true));
        map.put(4, new Event(4,"Zusammen skaten", "2022-01-01T11:00:00","Hey Leute, wollen skaten gehen. Am Nordplatz",null, 5, "muench.kaleb@gmail.com", true, false));
        map.put(5, new Event(5,"Dance Battle", "2022-12-03T20:15:00","Lets dance. Seid dabei fühlt euch wieder neu!!", new Address("Tanzstraße", "5", "12345", "Dance City"), 123, "muench.kaleb@gmail.com", true, true));
        map.put(6, new Event(6,"Gemütlich Frühstücken", "2022-12-15T08:20:00","Wir wollen zusammen frühstücken. Ich hatte an Pancakes gedacht. Fall wer Lust hat, darf er gerne kommen. Bringt doch gerne auch etwas mit. Am besten viel Kaffee",
                new Address("Schmale Gasse", "3", "80105", "Glückshausen"), 2, "muench.kaleb@gmail.com", false, false));
        map.put(7, new Event(7,"Spazieren", "2022-11-01T11:20:00","Zusammen im Zuffenhausener Stadtwald spazieren gehen", new Address("Markgröniger Straße", "50", "70435", "Stuttgart"), 0, "muench.kaleb@gmail.com", true, true));
        map.put(8, new Event(8,"Fußball Spielen", "2022-07-03T20:00:00","Sport frei!!!.", new Address("Weisenweg", "12", "52335", "Schöndorf"), 0, "muench.kaleb@gmail.com", true, true));
        map.put(9, new Event(9,"Gebetsabend", "2022-08-21T20:30:00","Lasst uns Gemeinschaft haben!.", new Address("Schiller-Straße", "13", "70435", "Stuttgart"), 0, "muench.kaleb@gmail.com", true, true));
        return map;
    }
    HashMap<Integer, Account> createAccounts() {
        HashMap<Integer,Account> map = new HashMap<>();
        Random rand = new Random();
        map.put(0, new Account("Kaleb", "Münch", "muench.kaleb@gmail.com", "Hallo123", 0));
        map.get(0).setToken("100000");
        return map;
    }

    String createJson(int accId) {
        StringBuilder builder = new StringBuilder();
        builder.append("[\n");
        for(Map.Entry<Integer, Event> event : events.entrySet()) {
            builder.append("\t{\n").append("\t\t\"event\": ").append(event.getValue().toJson()).append(",\n")
                    .append("\t\t\"attending\": ").append(list.get(accId).getMSignedUpForEvents().containsKey(event.getKey())).append(",\n")
                    .append("\t\t\"favourites\": ").append(list.get(accId).getMFavorites().containsKey(event.getKey())).append("\n")
                    .append("\t},\n");
        }
        if(builder.length() > 0) builder.deleteCharAt(builder.length()-2);
        builder.append("]");
        return builder.toString();
    }
}
