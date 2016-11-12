package fr.piroxxi.factorygame;

import fr.piroxxi.factorygame.core.model.Game;
import fr.piroxxi.factorygame.log.Log;
import fr.piroxxi.factorygame.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.slf4j.Logger;

import java.util.List;

@Controller
public class MainAppController {
    @Log
    private static Logger LOG;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Storage storage;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        int r = (int) (Math.random() * 900 + 100);
        LOG.info("home(" + r + ")");
        model.addAttribute("name", "World " + r);

        List<Object> games = storage.listAllObjects(Game.class);
        for (Object o : games) {
            System.out.println(o.toString());
        }

        Game game = new Game(6);
        game.generateMap();

        return "index.html";
    }

}
