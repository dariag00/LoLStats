package com.example.klost.lolstats.models.items;

import java.util.ArrayList;
import java.util.List;

public class ItemList {

    List<Item> itemList;

    public ItemList(){
        itemList = new ArrayList<>();
    }

    public void addItem(Item item){
        itemList.add(item);
    }

    public Item getItem(Item item){
        for(Item it : itemList){
            if(item.equals(it)){
                return it;
            }
        }
        return null;
    }

    public Item getItemById(int id){
        for(Item it : itemList){
            if(it.getId() == id){
                return it;
            }
        }
        return null;
    }
}
