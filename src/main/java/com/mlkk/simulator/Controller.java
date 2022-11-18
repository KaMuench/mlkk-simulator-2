package com.mlkk.simulator;

import com.mlkk.simulator.entities.Account;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

@RestController
@RequestMapping("mlkk")
public class Controller {
    static int ID_COUNTER = 0;
    HashMap<Integer,Account> list = new HashMap<>();

    @GetMapping("account/get_events")
    public void getPersonalisedEvents(@RequestParam("account_id") int id, HttpServletResponse resp) throws IOException {
        Scanner sc = new Scanner(new File("src/main/resources/event.json"));
        StringBuilder builder = new StringBuilder();
        while(sc.hasNext()) {
            builder.append(sc.next());
        }
        System.out.println(builder);
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().print(builder);
        resp.getWriter().close();
    }

    @PostMapping("registration/new_account")
    public void createAccount(@RequestParam("name") String name, @RequestParam("surname") String surname, @RequestParam("email") String email, @RequestParam("password") String password, HttpServletResponse resp) {
        list.put(ID_COUNTER, new Account(name, surname, email, password));
        System.out.println(list.get(ID_COUNTER));
        ID_COUNTER++;
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
