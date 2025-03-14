package event;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import data.DataManager;
import main.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class RandomBoxEvent implements Listener {
	private DataManager data = Main.data;
	private Main plugin = Main.main;
	HashMap<String, String> map = new HashMap<>();
	HashMap<String, Integer> task_map = new HashMap<>();
	BukkitScheduler scheduler;
	int task_id;

	@EventHandler
	public void onClickRandomBoxEvent(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
				&& !task_map.containsKey(p.getName().toString())) {
			if (task_map.containsKey(p.getName().toString())) {
				e.setCancelled(true);
				return;
			}
			ItemStack item = p.getInventory().getItemInMainHand();
			Set<String> keys = data.getFile().getConfigurationSection("randomBox").getKeys(true);
			Iterator<String> iter = keys.iterator();
			while (iter.hasNext()) {
				String name = iter.next();
				if ((data.getFile().getItemStack("randomBox." + name) != null) && (data.getFile()
						.getItemStack("randomBox." + name).getItemMeta().equals(item.getItemMeta()))) {
					item.setAmount(item.getAmount() - 1);
					ItemStack tmp_item = data.getFile().getItemStack("randomBox." + name);
					p.getInventory().setItemInMainHand(item);
					scheduler = Bukkit.getScheduler();
					map.put(p.getName().toString(), "true");
					task_id = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
						int cnt = 0;

						@Override
						public void run() {
							try {
							if (cnt == 0)
								task_map.put(p.getName().toString(), task_id);
							if (cnt > 10 && task_map.containsKey(p.getName().toString())) { // 카운트가 다 세졌을 때
								scheduler.cancelTask(task_map.get(p.getName().toString()));
								task_map.remove(p.getName());
								String boxName = name.replaceAll(".item", " ");
								p.chat("/rewardrandombox" + " " + p.getName() + " " + boxName);
							} else if (!map.containsKey(p.getName().toString()) && cnt < 10) { // 중간에 취소됐을 때
								scheduler.cancelTask(task_map.get(p.getName().toString()));
								p.sendTitle(" ", " ", 1, 20, 1);
								p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
										TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "랜덤박스 오픈 중 움직이셔서 취소되었습니다."));
								task_map.remove(p.getName());
								if (p.getInventory().getItemInMainHand() == null
										|| p.getInventory().getItemInMainHand().getType() == Material.AIR) {
									tmp_item.setAmount(1);
									p.getInventory().setItemInMainHand(tmp_item);
								} else {
									item.setAmount(item.getAmount() + 1);
									p.getInventory().setItemInMainHand(item);
								}
							}
							}catch(Exception e) {
								e.printStackTrace();
								scheduler.cancelAllTasks();
							}
							switch (cnt) {
							case 0:
								p.sendTitle(ChatColor.GRAY + "" + ChatColor.BOLD + "| | | | | | | | | |", "랜덤박스 오픈 중!",
										1, 20, 1);
								break;
							case 1:
								p.sendTitle(
										ChatColor.DARK_RED + "" + ChatColor.BOLD + "|" + ChatColor.GRAY + ""
												+ ChatColor.BOLD + " | | | | | | | | |",
										ChatColor.WHITE + "랜덤박스 오픈 중!", 1, 4, 1);
								break;
							case 2:
								p.sendTitle(
										ChatColor.RED + "" + ChatColor.BOLD + "| |" + ChatColor.GRAY + ""
												+ ChatColor.BOLD + " | | | | | | | |",
										ChatColor.WHITE + "랜덤박스 오픈 중!", 1, 4, 1);
								break;
							case 3:
								p.sendTitle(
										ChatColor.RED + "" + ChatColor.BOLD + "| | |" + ChatColor.GRAY + ""
												+ ChatColor.BOLD + " | | | | | | |",
										ChatColor.WHITE + "랜덤박스 오픈 중!", 1, 4, 1);
								break;
							case 4:
								p.sendTitle(
										ChatColor.GOLD + "" + ChatColor.BOLD + "| | | |" + ChatColor.GRAY + ""
												+ ChatColor.BOLD + " | | | | | |",
										ChatColor.WHITE + "랜덤박스 오픈 중!", 1, 4, 1);
								break;
							case 5:
								p.sendTitle(
										ChatColor.GOLD + "" + ChatColor.BOLD + "| | | | |" + ChatColor.GRAY + ""
												+ ChatColor.BOLD + " | | | | |",
										ChatColor.WHITE + "랜덤박스 오픈 중!", 1, 4, 1);
								break;
							case 6:
								p.sendTitle(ChatColor.YELLOW + "" + ChatColor.BOLD + "| | | | | |" + ChatColor.GRAY + ""
										+ ChatColor.BOLD + " | | | |", ChatColor.WHITE + "랜덤박스 오픈 중!", 1, 4, 1);
								break;
							case 7:
								p.sendTitle(ChatColor.YELLOW + "" + ChatColor.BOLD + "| | | | | | |" + ChatColor.GRAY
										+ "" + ChatColor.BOLD + " | | |", ChatColor.WHITE + "랜덤박스 오픈 중!", 1, 4, 1);
								break;
							case 8:
								p.sendTitle(
										ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "| | | | | | | |" + ChatColor.GRAY
												+ "" + ChatColor.BOLD + " | |",
										ChatColor.WHITE + "랜덤박스 오픈 중!", 1, 4, 1);
								break;
							case 9:
								p.sendTitle(
										ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "| | | | | | | | |"
												+ ChatColor.GRAY + "" + ChatColor.BOLD + " |",
										ChatColor.WHITE + "랜덤박스 오픈 중!", 1, 4, 1);
								break;
							case 10:
								p.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "| | | | | | | | | |",
										ChatColor.WHITE + "랜덤박스 오픈 중!", 1, 4, 1);
								break;
							}
							cnt++;
							p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0.2f * cnt);
						}
					}, 0L, 5L);
					break;
				}
			}
		}
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (map.containsKey(p.getName().toString()))
			map.remove(p.getName().toString());
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (map.containsKey(p.getName().toString())) {
			map.remove(p.getName().toString());
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemHeldEvent(PlayerItemHeldEvent e) {
		Player p = e.getPlayer();
		if (map.containsKey(p.getName().toString()) || task_map.containsKey(p.getName().toString())) {
			map.remove(p.getName().toString());
			e.setCancelled(true);
		}
	}
}
