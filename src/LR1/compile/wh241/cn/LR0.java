package LR1.compile.wh241.cn;

import java.util.ArrayList;
import java.util.TreeSet;

public class LR0 {
    /**
     * 定义数组结构
     */
    public ArrayList<String> LR1List = new ArrayList<>();
    //项目
    //new Project project;
    //项目集
    // ArrayList<Project> projectC;
    //项目集族
    private ArrayList<ArrayList<Project>> projectCC= new ArrayList<>();
    //终结符集合
    private TreeSet<Character> VnSet= new TreeSet<>();
    //非终结符集合
    private TreeSet<Character> VtSet =  new TreeSet<>();
    //所有符号集合
    private TreeSet<Character> allSet = new TreeSet<>();
    //开始符号
    public Character startSymbol = 'S';
    //GO函数状态集合
    public ArrayList<GoObject> GoList = new ArrayList<>();
    //ACTION字表
    private String[][] ACTION;
    //GOTO字表
    private String[][] GOTO;

    /**
     * 临时测试主程序
     */
    public static void main(String[] args) {
        LR0 lr0 = new LR0();
        lr0.initLR1List();
        lr0.getVnVt();
        /*
        System.out.println("---非终结符集合---");
        for (Character Vn : lr0.VnSet){
            System.out.println(Vn);
        }
        System.out.println("---终结符集合---");
        for (Character Vt : lr0.VtSet){
            System.out.println(Vt);
        }
         */
        /*
        lr0.CLOSURE(projects);
        System.out.println("---CLOSURE---");
        for (Project projectItem : projects){
            Integer num = projectItem.getNum();
            Integer place = projectItem.getPlace();
            String LR1Str = lr0.LR1List.get(num);
            System.out.println(LR1Str +" : "+ num + "---" + place);
        }
        ArrayList<Project> go = lr0.GO(projects, 'E');
        System.out.println("---GO---");
        for (Project projectItem : go){
            Integer num = projectItem.getNum();
            Integer place = projectItem.getPlace();
            String LR1Str = lr0.LR1List.get(num);
            System.out.println(LR1Str +" : "+ num + "---" + place);
        }
         */
        lr0.CalProjectCC();
        lr0.initTable();
        lr0.CalTable();
        /*
        for (ArrayList<Project> projects : lr0.projectCC){
            for (Project projectItem : projects){
                Integer num = projectItem.getNum();
                Integer place = projectItem.getPlace();
                String LR1Str = lr0.LR1List.get(num);
                System.out.println(LR1Str +" : "+ num + "---" + place);
            }
        }

         */
        System.out.println("---ACTION表---");
        for (int i = 0; i < lr0.ACTION.length; i++) {
            for (int j = 0; j < lr0.ACTION[i].length; j++) {
                System.out.printf("%-10s",lr0.ACTION[i][j]);
            }
            System.out.println("");
        }
        System.out.println("---GOTO表---");
        for (int i = 0; i < lr0.GOTO.length; i++) {
            for (int j = 0; j < lr0.GOTO[i].length; j++) {
                System.out.printf("%-10s",lr0.GOTO[i][j]);
            }
            System.out.println("");
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
            allSet.add(vnChar);
        }
        for (String LL1Str : LR1List){
            String[] split = LL1Str.split("->");
            //然后求终结符，在产生式右边
            String vtStr = split[1];
            for (int i = 0; i < vtStr.length(); i++) {
                char vtItem = vtStr.charAt(i);
                if (!VnSet.contains(vtItem)){
                    VtSet.add(vtItem);
                    allSet.add(vtItem);
                }
            }
        }
    }
    /**
     * 闭包函数
     * 1) 把I中的项目加到CLOSURE(I)
     * 2) A->α.Bγ属于CLOSURE(I)，对于B->Ω，要把B->Ω加入到CLOSURE(I)
     * @param projectCItem 项目集
     */
    public void CLOSURE(ArrayList<Project> projectCItem){
        for (int i = 0; i < projectCItem.size(); i++) {
            Project projectItem = projectCItem.get(i);
            //检查项目集中的每个项目是否有A->α.Bγ
            Integer num = projectItem.getNum();
            Integer place = projectItem.getPlace();
            //产生式
            String LR1Str = LR1List.get(num);
            //System.out.println(num + "---" + place);
            //System.out.println(LR1Str);
            String[] split = LR1Str.split("->");
            //产生式右部
            String rightStr = split[1];
            if (place < rightStr.length()){
                //判断圆点后面第一个字符
                Character afterCirclePoint = rightStr.charAt(place);
                if (VnSet.contains(afterCirclePoint)){
                    //System.out.println("非终结符");
                    for (int j = 0; j < LR1List.size(); j++) {
                        String[] split1 = LR1List.get(j).split("->");
                        if (split1[0].charAt(0) == afterCirclePoint){
                            Project project = new Project(j, 0);
                            projectCItem.add(project);
                        }
                    }
                }
            }else{
                //System.out.println("可归约");
            }
        }
    }
    /**
     * 状态转换函数
     * @param I 项目集
     * @param X 文法符号
     * @return J   项目集
     * 检查项目集I中的每一个项目，看圆点后面的字符是不是等于X，如果是则加入到项目集J中
     * 然后ClOSURE(J)
     */
    public ArrayList<Project> GO(ArrayList<Project> I, Character X){
        ArrayList<Project> J = new ArrayList<Project>();
        for (int i = 0; i < I.size(); i++) {
            Project projectItem = I.get(i);
            Integer num = projectItem.getNum();
            Integer place = projectItem.getPlace();
            //产生式
            String LR1Str = LR1List.get(num);
            String[] split = LR1Str.split("->");
            //产生式右部
            String rightStr = split[1];
            if (place < rightStr.length()){
                //判断圆点后面第一个字符
                Character afterCirclePoint = rightStr.charAt(place);
                if (afterCirclePoint == X){
                    Project newProject = new Project(num, place + 1);
                    J.add(newProject);
                    CLOSURE(J);
                }
            }else{
                /*
                Project newProject = new Project(num, place);
                ArrayList<Project> newList = new ArrayList<>();
                newList.add(newProject);
                projectCC.add(newList);

                 */
            }
        }
        return J;
    }
    /**
     * 判断项目集族中是否已有此项目集
     * 通过循环比较内容得出
     */
    public Integer IsExist(ArrayList<Project> goResult){
        int count = 0;
        for (int k = 0; k < projectCC.size(); k++) {
            ArrayList<Project> projects = projectCC.get(k);
            count = 0;
            if (projects.size() == goResult.size()){
                for (int i = 0; i < projects.size(); i++) {
                    for (int j = 0; j < goResult.size(); j++) {
                        if (projects.get(i).getNum().equals(goResult.get(j).getNum())){
                            if (projects.get(i).getPlace().equals(goResult.get(j).getPlace())){
                                count++;
                                break;
                            }
                        }
                    }
                }
                if (count == goResult.size()){
                    return k;
                }
            }
        }
        return -1;
    }
    /**
     * LR(0)项目集规范族
     */
    public void CalProjectCC(){
        Project project = new Project(0, 0);
        ArrayList<Project> projects = new ArrayList<>();
        projects.add(project);
        CLOSURE(projects);
        projectCC.add(projects);
        for (int i = 0; i < projectCC.size(); i++) {
            for (Character charItem : allSet){
                ArrayList<Project> goResult = GO(projectCC.get(i), charItem);
                Integer integer = IsExist(goResult);
                if (integer != -1){
                    GoObject goObject = new GoObject(i, charItem, integer);
                    GoList.add(goObject);
                }
                if (!goResult.isEmpty() && IsExist(goResult) == -1){
                    projectCC.add(goResult);
                    GoObject goObject = new GoObject(i, charItem, projectCC.size() - 1);
                    GoList.add(goObject);
                    /*
                    for (Project projectItem : goResult){
                        Integer num = projectItem.getNum();
                        Integer place = projectItem.getPlace();
                        String LR1Str = LR1List.get(num);
                        System.out.println(LR1Str +" : "+ num + "---" + place);
                    }
                     */
                }
            }
        }
    }
    /**
     *初始化ACTION子表和GOTO子表
     */
    public void initTable(){
        //初始化ACTION子表
        TreeSet<Character> characters = new TreeSet<>(VtSet);
        characters.remove('ε');
        characters.add('#');
        ACTION = new String[projectCC.size() + 1][characters.size() + 1];
        for (int i = 0; i < ACTION.length; i++) {
            for (int j = 0; j < ACTION[i].length; j++) {
                if (j == 0 && i >= 1){
                    ACTION[i][j] = (i - 1) + "";
                }else{
                    ACTION[i][j] = "";
                }
            }
        }
        int m = 1;
        for (Character charItem : characters){
            ACTION[0][m] = charItem.toString();
            m++;
        }
        //初始化GOTO子表
        TreeSet<Character> characters1 = new TreeSet<>(VnSet);
        characters1.remove(startSymbol);
        GOTO = new String[projectCC.size() + 1][characters1.size() + 1];
        for (int i = 0; i < GOTO.length; i++) {
            for (int j = 0; j < GOTO[i].length; j++) {
                if (j == 0 && i >= 1){
                    GOTO[i][j] = (i - 1) + "";
                }else{
                    GOTO[i][j] = "";
                }
            }
        }
        m = 1;
        for (Character Vn : characters1){
            GOTO[0][m] = Vn.toString();
            m++;
        }
    }
    /**
     * 计算ACTION子表和GOTO子表
     */
    public void CalTable(){
        //1.计算GOTO子表
        for (GoObject goObject : GoList){
            Integer k = goObject.getK();
            Character a = goObject.getA();
            Integer j = goObject.getJ();
            if (VnSet.contains(a)){
                for (int i = 0; i < GOTO.length; i++) {
                    for (int m = 0; m < GOTO[i].length; m++) {
                        if (GOTO[i][0].equals(k.toString()) && GOTO[0][m].equals(a.toString())){
                            GOTO[i][m] = j.toString();
                        }
                    }
                }
            }
        }
        //2.计算ACTION表
        //遍历项目集族，取出项目集
        for (int p = 0; p < projectCC.size(); p++) {
            ArrayList<Project> projects = projectCC.get(p);
            //遍历项目集，取出项目
            for (Project projectItem : projects){
                Integer num = projectItem.getNum();
                Integer place = projectItem.getPlace();
                //产生式
                String LR1Str = LR1List.get(num);
                String[] split = LR1Str.split("->");
                //产生式右部
                String rightStr = split[1];
                //规则(1)
                if (place < rightStr.length()){
                    //判断圆点后面第一个字符
                    Character afterCirclePoint = rightStr.charAt(place);
                    if (VtSet.contains(afterCirclePoint)){
                        for (GoObject goObject : GoList){
                            Integer k = goObject.getK();
                            Character a = goObject.getA();
                            Integer j = goObject.getJ();
                            if (p == k && afterCirclePoint == a){
                                for (int i = 0; i < ACTION.length; i++) {
                                    for (int m = 0; m < ACTION[m].length; m++) {
                                        if (ACTION[i][0].equals(k.toString()) && ACTION[0][m].equals(a.toString())){
                                            ACTION[i][m] = "s" + j.toString();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else{
                    //规则(2)
                    for (int i = 0; i < ACTION.length; i++) {
                        for (int m = 0; m < ACTION[m].length; m++) {
                            if (ACTION[i][0].equals(p + "") && m >= 1 ){
                                if (num != 0){
                                    ACTION[i][m] = "r" + num;
                                }else{
                                    //规则(3)
                                    if (ACTION[0][m].equals("#")){
                                        ACTION[i][m] = "acc";
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
