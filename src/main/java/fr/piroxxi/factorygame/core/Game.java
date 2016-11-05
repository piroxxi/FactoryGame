package fr.piroxxi.factorygame.core;

import fr.piroxxi.factorygame.core.data.Department;
import fr.piroxxi.factorygame.core.data.Employee;
import fr.piroxxi.factorygame.log.Log;
import fr.piroxxi.factorygame.storage.Storage;
import org.slf4j.Logger;

public class Game {
    @Log
    private static Logger LOG;

    private final Storage storage;

    public Game(Storage storage) {
        this.storage = storage;
    }

    public void start() {
        LOG.debug("start()");

        Department d = new Department("chimie");
        this.storage.saveObject(d);
        Employee e = new Employee("Gerard", d);
        this.storage.saveObject(e);
    }
}
