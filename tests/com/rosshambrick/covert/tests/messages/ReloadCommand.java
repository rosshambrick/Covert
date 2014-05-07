package com.rosshambrick.covert.tests.messages;

import com.rosshambrick.covert.Command;

public class ReloadCommand extends Command {
    @Override
    protected void execute() {
        reload(new AllCitiesQuery());
    }

}
