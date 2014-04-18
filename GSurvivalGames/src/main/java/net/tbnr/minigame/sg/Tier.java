/*
 * Copyright (c) 2014.
 * CogzMC LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

package net.tbnr.minigame.sg;

import lombok.*;
import net.tbnr.gearz.Gearz;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class Tier {
    private List<RawItem> chances;
    @NonNull
    private Integer minimum;
    @NonNull
    private Integer maximum;
    @NonNull
    private String name;

    {
        this.chances = new ArrayList<>();
        if (this.minimum == null) this.minimum = 0;
        if (this.maximum == null) this.maximum = 0;
    }

    //Commented for tutorial purposes
    public List<ItemStack> getItems() {
        //So, we get the minimum, and add some random number to increase it somewhere near the maximum
        int numberOfItems = this.minimum + Gearz.getRandom().nextInt(this.maximum - this.minimum);
        //Create a new list of item stacks (bukkit item representation) to populate from our raw data
        List<ItemStack> items = new ArrayList<>();
        for (int x = 0; x < numberOfItems; x++) {
            //Create a null itemstack that we will put the item into that we want to add to the list above.
            ItemStack stack = null;
            //Null raw item as well to put stuff into
            RawItem rawItem = null;
            //While that stack is null...
            while (stack == null) {
                //Generate a new number between 0 - 100
                Integer chance_key = Gearz.getRandom().nextInt(100);
                //Get a random item from the list of raw items (represents a json item that's been parsed)
                rawItem = chances.get(Gearz.getRandom().nextInt(chances.size()));
                //Check if the chance of having this item is greater or equal to the random number from above.
                if (rawItem.getChance() >= chance_key) {
                    //If so, we make our stack no longer null, and put this value into it. This breaks our while loop
                    stack = new ItemStack(rawItem.getMaterial(), 1, rawItem.getData());
                }
                //Otherwise, continue on and try again :D
            }
            //If the enchants of the raw item we just found are not null, we should add them
            if (rawItem.getRawEnchants() != null) {
                //Lets iterate through all of them (this is foreach in java)
                for (RawEnchant rawEnchant : rawItem.getRawEnchants()) {
                    //Add the enchantment for that item.
                    stack.addUnsafeEnchantment(rawEnchant.getEnchantmentType(), rawEnchant.getLevel());
                }
            }
            //Lets do the item name now? Check if it's null
            if (rawItem.getName() != null) {
                //It's not! Let's go ahead and add this name
                ItemMeta itemMeta = stack.getItemMeta();
                itemMeta.setDisplayName(rawItem.getName());
                stack.setItemMeta(itemMeta);
            }
            //Now, have we added this item to our items array list? Nope!
            boolean added = false;
            //Now, let's check all the items we've previously added
            for (ItemStack i : items) {
                //If they're the same item (material wise, enchantment wise, etc), and the amount of items in this stack is less than max size)...
                if (i.getType().equals(stack.getType()) && i.getAmount() + stack.getAmount() <= i.getMaxStackSize() && i.getEnchantments().equals(stack.getEnchantments())) {
                    //Update that item stack just to have more, rather than adding another
                    i.setAmount(i.getAmount() + stack.getAmount());
                    //And now we've added it
                    added = true;
                    //Break out of this for loop
                    break;
                }
            }
            //If we still haven't added this item, we'll do it here
            if (!added) {
                //Add!
                items.add(stack);
            }
            //Rince and repeat
        }
        //All done, let's return our list
        return items;
    }

    public void addItem(RawItem data) {
        this.chances.add(data);
    }

    public static Tier fromJSONResource(String jsonFileName) {
        GSurvivalGames instance = GSurvivalGames.getInstance();
        JSONObject jsonResource = instance.getJSONResource(jsonFileName);
        if (jsonResource == null) return null;
        int minimum;
        int maximum;
        String name;
        JSONArray data;
        try {
            minimum = jsonResource.getInt("minimum");
            maximum = jsonResource.getInt("maximum");
            name = jsonResource.getString("name");
            data = jsonResource.getJSONArray("data");
        } catch (JSONException ex) {
            return null;
        }
        List<RawItem> items = new ArrayList<>();
        for (int x = 0; x < data.length(); x++) {
            String material;
            int data1 = 0;
            int chance;
            JSONObject jsonObject;
            String item_name = null;
            try {
                jsonObject = data.getJSONObject(x);
                material = jsonObject.getString("material");
                chance = jsonObject.getInt("chance");
            } catch (JSONException ex) {
                continue;
            }
            try {
                data1 = jsonObject.getInt("data");
                item_name = jsonObject.getString("title");
            } catch (JSONException ignored) {
            }
            Material material1 = Material.getMaterial(material);
            if (material1 == null) continue;
            RawItem rawItem = new RawItem(material1, (short) data1, chance);
            if (jsonObject.has("enchants")) {
                boolean goOnNow = true;
                JSONArray enchants = null;
                try {
                    enchants = jsonObject.getJSONArray("enchants");
                } catch (JSONException e) {
                    goOnNow = false;
                }
                if (goOnNow) {
                    List<RawEnchant> rawEnchants = new ArrayList<>();
                    for (int y = 0; y < enchants.length(); y++) {
                        JSONObject enchant;
                        String enchant_name;
                        Integer level;
                        try {
                            enchant = enchants.getJSONObject(y);
                            enchant_name = enchant.getString("name");
                            level = enchant.getInt("level");
                        } catch (JSONException e) {
                            continue;
                        }
                        Enchantment byName = Enchantment.getByName(enchant_name);
                        if (byName == null) continue;
                        rawEnchants.add(new RawEnchant(byName, level));
                    }
                    rawItem.setRawEnchants(rawEnchants);
                }
            }
            if (item_name != null) {
                rawItem.setName(ChatColor.translateAlternateColorCodes('&', item_name));
            }
            items.add(rawItem);
        }
        Tier tier = new Tier(minimum, maximum, name);
        for (RawItem jsonData : items) {
            tier.addItem(jsonData);
        }
        return tier;
    }

    @Data
    @RequiredArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class RawItem {
        @NonNull
        private Material material;
        @NonNull
        private short data;
        @NonNull
        private Integer chance;
        private List<RawEnchant> rawEnchants;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    private static class RawEnchant {
        @NonNull
        private Enchantment enchantmentType;
        @NonNull
        private Integer level;
    }
}
