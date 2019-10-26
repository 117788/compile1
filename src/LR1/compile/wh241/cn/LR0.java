package LR1.compile.wh241.cn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class LR0 {
    /**
     * 定义数组结构
     */
    public ArrayList<String> LR1List = new ArrayList<>();
    //项目
    public HashMap<Integer, Integer> project = new HashMap<>();
    //项目集
    public TreeSet<String> projectC = new TreeSet<>();
    //项目集族
    public ArrayList<TreeSet<String>> projectCC= new ArrayList<>();
    //终结符集合
    public TreeSet<Character> VnSet= new TreeSet<>();
    //非终结符集合
    public TreeSet<Character> VtSet =  new TreeSet<>();
    //开始符号
    public Character startSymbol = 'S';
    /**
     * 临时测试主程序
     */
    public static void main(String[] args) {
        LR0 lr0 = new LR0();
        lr0.initLR1List();
        lr0.getVnVt();
        System.out.println("---非终结符集合---");
        for (Character Vn : lr0.VnSet){
            System.out.println(Vn);
        }
        System.out.println("---终结符集合---");
        for (Character Vt : lr0.VtSet){
            System.out.println(Vt);
        }
    }
    /**
     * 初始化LR1文法
     */
    public void initLR1List(){
        LR1List.add("S->E");
        LR1List.add("E->aA");
        LR1List.add("E->bB");
        LR1List.add("A->cA");
        LR1List.add("A->d");
        LR1List.add("B->cB");
        LR1List.add("B->d");
    }
    /**
     * 求终结符和非终结符
     */
    public void getVnVt(){
        for (String LL1Str : LR1List){
            String[] split = LL1Str.split("->");
            //先求终结符，在产生式左边
            char vnChar = split[0].charAt(0);
            VnSet.add(vnChar);
        }
        for (String LL1Str : LR1List){
            String[] split = LL1Str.split("->");
            //然后求终结符，在产生式右边
            String vtStr = split[1];
            for (int i = 0; i < vtStr.length(); i++) {
                char vtItem = vtStr.charAt(i);
                if (!VnSet.contains(vtItem)){
                    VtSet.add(vtItem);
                }
            }
        }
    }

}
