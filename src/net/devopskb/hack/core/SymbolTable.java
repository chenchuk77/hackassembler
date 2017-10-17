package net.devopskb.hack.core;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by chenchuk on 10/14/17.
 */
public class SymbolTable {
    Map<String, Integer> symbolsMap;

    public SymbolTable() {
        // generate predefined symbols
        symbolsMap = new TreeMap<>();
        addPredefinedSymbols();
    }
    private void addPredefinedSymbols(){
        symbolsMap.put("SP",0);
        symbolsMap.put("LCL",1);
        symbolsMap.put("ARG",2);
        symbolsMap.put("THIS",3);
        symbolsMap.put("THAT",4);
        symbolsMap.put("SCREEN",16384);
        symbolsMap.put("KBD",24576);
        for (int i=0; i<=15; i++){
            symbolsMap.put("R" + i ,i);
        }
    }

    public void addEntry(String symbol, Integer address){
        symbolsMap.put(symbol,address);
    }

    public Boolean contains(String symbol){
        return symbolsMap.containsKey(symbol);
    }
    public Integer getAddress(String symbol){
        return (Integer) symbolsMap.get(symbol);
    }

    @Override
    public String toString() {
        return "SymbolTable{" +
                "symbolsMap=" + symbolsMap +
                '}';
    }

    public void show (){
        symbolsMap.forEach((symbol, address)-> System.out.println(symbol +": "+ address));
    }

}
