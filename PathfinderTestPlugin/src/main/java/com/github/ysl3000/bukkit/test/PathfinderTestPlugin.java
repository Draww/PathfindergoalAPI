package com.github.ysl3000.bukkit.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ysl3000.bukkit.pathfinding.PathfinderGoalAPI;
import com.github.ysl3000.bukkit.pathfinding.entity.Insentient;
import com.github.ysl3000.bukkit.pathfinding.goals.PathfinderGoalGimmiCookie;
import com.github.ysl3000.bukkit.pathfinding.goals.PathfinderGoalMoveToLocation;
import com.github.ysl3000.bukkit.pathfinding.goals.TalkToStrangers;
import com.github.ysl3000.bukkit.pathfinding.goals.notworking.PathfinderGoalFollowEntity;
import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderGoal;
import com.github.ysl3000.bukkit.pathfinding.pathfinding.PathfinderManager;

/**
 * Created by ysl3000
 */
public class PathfinderTestPlugin extends JavaPlugin implements Listener {

  private PathfinderManager pathfinderManager;

  private Map<String, ICommand> commandMap = new HashMap<>();

  @Override
  public void onEnable() {

    this.pathfinderManager = PathfinderGoalAPI.getAPI();

    commandMap.put("chat", new Chat(pathfinderManager));
    commandMap.put("cookie", new DeliverCookie(pathfinderManager));
    commandMap.put("move", new MoveToLocation(pathfinderManager));
    commandMap.put("follow", new FollowOwner(pathfinderManager));
    commandMap.put("print", new PrintGoal(pathfinderManager));

    Bukkit.getServer().getPluginManager().registerEvents(this, this);
  }


  @EventHandler
  public void onChat(AsyncPlayerChatEvent event) {

    Player player = event.getPlayer();

    List<String> args = Arrays.asList(event.getMessage().split(" "));

    if (args.size() > 0) {

      String command = args.get(0);

      ICommand iCommand = commandMap.get(command);

      List<String> param = new ArrayList<>();

      if (args.size() > 1) {
        param = args.subList(1, args.size());
      }

      List<String> finalParam = param;
      Bukkit.getScheduler().runTask(this, () -> {

        if (iCommand != null) {
          iCommand.execute(player, finalParam);
        }

      });
    }
  }

  private class Chat implements ICommand {


    private PathfinderManager pathfinderManager;

    private Chat(PathfinderManager pathfinderManager) {
      this.pathfinderManager = pathfinderManager;
    }


    @Override
    public boolean execute(Player player, List<String> args) {

      Creature creature = player.getWorld().spawn(player.getLocation(), Zombie.class);
      Insentient pathfinderGoalEntity = this.pathfinderManager.getPathfindeGoalEntity(creature);

      if (pathfinderGoalEntity != null) {
        pathfinderGoalEntity.clearPathfinderGoals();
        pathfinderGoalEntity.clearTargetPathfinderGoals();
        pathfinderGoalEntity.addPathfinderGoal(0,
            new TalkToStrangers(pathfinderGoalEntity, args, TimeUnit.SECONDS.toMillis(20)));
      }
      return true;
    }
  }

  private class DeliverCookie implements ICommand {

    private PathfinderManager pathfinderManager;

    private DeliverCookie(PathfinderManager pathfinderManager) {
      this.pathfinderManager = pathfinderManager;
    }

    @Override
    public boolean execute(Player player, List<String> args) {

      ThreadLocalRandom rand = ThreadLocalRandom.current();

      Location spawn = player.getLocation().add(rand.nextDouble(10),0,rand.nextDouble(10));


      Creature creature = player.getWorld()
          .spawn(spawn, Zombie.class);
      Insentient pathfinderGoalEntity = this.pathfinderManager.getPathfindeGoalEntity(creature);

      if (pathfinderGoalEntity != null) {
        pathfinderGoalEntity.clearPathfinderGoals();
        pathfinderGoalEntity.clearTargetPathfinderGoals();
        pathfinderGoalEntity
            .addPathfinderGoal(0, new PathfinderGoalGimmiCookie(pathfinderGoalEntity));
      }

      return true;
    }
  }

  private class MoveToLocation implements ICommand {

    private PathfinderManager pathfinderManager;

    private MoveToLocation(PathfinderManager pathfinderManager) {
      this.pathfinderManager = pathfinderManager;
    }

    @Override
    public boolean execute(Player player, List<String> args) {

      Zombie creature = player.getWorld().spawn(player.getLocation(), Zombie.class);
      Insentient pathfinderGoalEntity = this.pathfinderManager.getPathfindeGoalEntity(creature);
      if (pathfinderGoalEntity != null) {
        pathfinderGoalEntity.clearPathfinderGoals();
        pathfinderGoalEntity.clearTargetPathfinderGoals();
        pathfinderGoalEntity.addPathfinderGoal(0,
            new PathfinderGoalMoveToLocation(
                pathfinderGoalEntity, new Location(player.getWorld(), 235, 70, 246),
                2, 0)
        );
      }

      return true;
    }
  }

  private class FollowOwner implements ICommand {

    private PathfinderManager pathfinderManager;

    private FollowOwner(PathfinderManager pathfinderManager) {
      this.pathfinderManager = pathfinderManager;
    }

    @Override
    public boolean execute(Player player, List<String> args) {

      Zombie creature = player.getWorld().spawn(player.getLocation(), Zombie.class);
      Insentient pathfinderGoalEntity = this.pathfinderManager.getPathfindeGoalEntity(creature);
      if (pathfinderGoalEntity != null) {
        pathfinderGoalEntity.clearPathfinderGoals();
        pathfinderGoalEntity.clearTargetPathfinderGoals();
        pathfinderGoalEntity.addPathfinderGoal(0,
           new PathfinderGoalFollowEntity(pathfinderGoalEntity,player,5.0,0.2)
        );
      }

      return true;
    }
  }


  private class PrintGoal implements ICommand {

    private final PathfinderManager pathfinderManager;

    public PrintGoal(PathfinderManager pathfinderManager) {
      this.pathfinderManager = pathfinderManager;
    }

    @Override
    public boolean execute(Player p, List<String> args) {
      Creature creature = p.getWorld().spawn(p.getLocation(), Zombie.class);

      Insentient pathfinderGoalEntity = this.pathfinderManager.getPathfindeGoalEntity(creature);
      if (pathfinderGoalEntity != null) {
        pathfinderGoalEntity.clearPathfinderGoals();
        pathfinderGoalEntity.clearTargetPathfinderGoals();
        pathfinderGoalEntity.addPathfinderGoal(0,
            new PathfinderGoalPrint()
        );
      }

      return true;
    }

    private class PathfinderGoalPrint implements PathfinderGoal {

      private boolean shE = true;
      private boolean shT = true;

      @Override
      public boolean shouldExecute() {
        System.out.println("called should execute");
        return shE = !shE;
      }

      @Override
      public boolean shouldTerminate() {
        System.out.println("Called should terminate");
        return shT = !shT;
      }

      @Override
      public void init() {
        System.out.println("Called Init");
      }

      @Override
      public void execute() {
        System.out.println("Called execute");
      }

      @Override
      public void reset() {
        System.out.println("Called reset");
      }
    }
  }
}
