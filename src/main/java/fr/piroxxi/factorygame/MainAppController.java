package fr.piroxxi.factorygame;

import fr.piroxxi.factorygame.core.Game;
import fr.piroxxi.factorygame.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.slf4j.Logger;

@Controller
public class MainAppController {
    @Log
    private static Logger LOG;

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        int r = (int) (Math.random() * 900 + 100);
        LOG.debug("home(" + r + ")");
        model.addAttribute("name", "World " + r);

        Game game = applicationContext.getBean(Game.class);
        game.start();

        return "index.html";
    }

}
