package fr.piroxxi.factorygame;

import fr.piroxxi.factorygame.log.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.slf4j.Logger;

@Controller
public class MainAppController {
    @Log
    private static Logger LOG;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        LOG.info("home()");
        model.addAttribute("name", "World");
        return "index.html";
    }

}