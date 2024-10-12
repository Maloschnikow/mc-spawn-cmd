package io.maloschnikow.spawncmdplugin;

import java.util.Random;

import org.jetbrains.annotations.NotNull;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public class SpawnCommand implements BasicCommand {

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {

        Random rand = new Random();
        int n = rand.nextInt(1000);
        if (n > 0) {
            stack.getSender().sendRichMessage("<green>Du wirst gleich teleportiert.</green>");
        }
        else {
            stack.getSender().sendRichMessage("<red>Ne, heute nicht.</red>");
        }

    }

}
