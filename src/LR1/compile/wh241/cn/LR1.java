package LR1.compile.wh241.cn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

public class LR1 {
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
    private Character startSymbol = 'E';
    //GO函数状态集合
    private ArrayList<GoObject> GoList = new ArrayList<>();
    //ACTION字表
    public String[][] ACTION;
    //GOTO字表
    public String[][] GOTO;
    //First集
    private HashMap<Character, TreeSet<Character>> firstCollection  = new HashMap<>();
    //Follow集
    private HashMap<Character, TreeSet<Character>> followCollection  = new HashMap<>();
    /**
     * 临时测试主程序
     */
    public static void main(String[] args) {
        LR1 lr1 = new LR1();
        lr1.initLR1List();
        lr1.getVnVt();
        lr1.FirstCollection();
        lr1.FollowCollection();
        lr1.CalProjectCC();
        /*
        TreeSet<Character> characters = new TreeSet<>();
        characters.add('#');
        Project project = new Project(0, 0, characters);
        ArrayList<Project> projectC = new ArrayList<>();
        projectC.add(project);
        lr1.CLOSURE(projectC);
        ArrayList<Project> b = lr1.GO(projectC, 'a');

         */
        lr1.initTable();
        lr1.CalTable();
        System.out.println("---ACTION表---");
        for (int i = 0; i < lr1.ACTION.length; i++) {
            for (int j = 0; j < lr1.ACTION[i].length; j++) {
                System.out.printf("%-10s",lr1.ACTION[i][j]);
            }
            System.out.println("");
        }
        System.out.println("---GOTO表---");
        for (int i = 0; i < lr1.GOTO.length; i++) {
            for (int j = 0; j < lr1.GOTO[i].length; j++) {
                System.out.printf("%-10s",lr1.GOTO[i][j]);
            }
            System.out.println("");
        }
        System.out.println("");
    }
    /**
     * 初始化LR1文法
     */
    public void initLR1List(){

        LR1List.add("E->S");
        LR1List.add("S->BB");
        LR1List.add("B->aB");
        LR1List.add("B->b");
        /*
        LR1List.add("E->E+T");
        LR1List.add("E->T");
        LR1List.add("T->T*F");
        LR1List.add("T->F");
        LR1List.add("F->(E)");
        LR1List.add("F->i");

         */
    }
    /**
     * 求终结符和非终结符
     */
    public void getVnVt(){
        for (String LR1Str : LR1List){
            String[] split = LR1Str.split("->");
            //先求终结符，在产生式左边
            char vnChar = split[0].charAt(0);
            VnSet.add(vnChar);
            allSet.add(vnChar);
        }
        for (String LR1Str : LR1List){
            String[] split = LR1Str.split("->");
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
     * 2) A->α.Bγ,a属于CLOSURE(I)，对于B->Ω，要把B->Ω,b∈ First(γa)加入到CLOSURE(I)
     * @param projectCItem 项目集
     */
    public void CLOSURE(ArrayList<Project> projectCItem){
        for (int i = 0; i < projectCItem.size(); i++) {
            Project projectItem = projectCItem.get(i);
            //检查项目集中的每个项目是否有A->α.Bγ,a
            Integer num = projectItem.getNum();
            Integer place = projectItem.getPlace();
            TreeSet<Character> findStr = projectItem.getFindStr();
            //产生式
            String LR1Str = LR1List.get(num);
            String[] split = LR1Str.split("->");
            //产生式右部
            String rightStr = split[1];
            if (place < rightStr.length()){
                //判断圆点后面第一个字符
                char afterCirclePoint = rightStr.charAt(place);
                if (VnSet.contains(afterCirclePoint)){
                    //求First(γa)
                    TreeSet<Character> FirstSPart = new TreeSet<>();
                    String newStr = "";
                    for (int j = place + 1; j < rightStr.length(); j++) {
                        newStr += rightStr.charAt(i);
                    }
                    if (!newStr.equals("")){
                        TreeSet<Character> FirstSPart1 = CalFirstS(newStr);
                        if (FirstSPart1.contains('ε')){
                            FirstSPart.addAll(findStr);
                        }else {
                            FirstSPart.addAll(FirstSPart1);
                        }
                    }else{
                        FirstSPart.addAll(findStr);
                    }
                    for (int j = 0; j < LR1List.size(); j++) {
                        String[] split1 = LR1List.get(j).split("->");
                        if (split1[0].charAt(0) == afterCirclePoint){
                            Project project = new Project(j, 0,FirstSPart);
                            projectCItem.add(project);
                        }
                    }
                }
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
            TreeSet<Character> findStr = projectItem.getFindStr();
            //产生式
            String LR1Str = LR1List.get(num);
            String[] split = LR1Str.split("->");
            //产生式右部
            String rightStr = split[1];
            if (place < rightStr.length()){
                //判断圆点后面第一个字符
                Character afterCirclePoint = rightStr.charAt(place);
                if (afterCirclePoint == X){
                    Project newProject = new Project(num, place + 1, findStr);
                    J.add(newProject);
                    CLOSURE(J);
                }
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
                                //int count1 = 0;
                                Object[] objectI = projects.get(i).getFindStr().toArray();
                                Object[] objectJ = goResult.get(j).getFindStr().toArray();
                                /*
                                if (objectI.length == objectJ.length){
                                    for (int m = 0; m < objectI.length; m++) {
                                        for (int n = 0; n < objectJ.length; n++) {
                                            if (objectI[m].equals(objectJ[n])){
                                                count1++;
                                                break;
                                            }
                                        }
                                    }
                                    if (count1 == objectI.length){
                                        count++;
                                        break;
                                    }
                                }
                                 */
                                if (Arrays.equals(objectI, objectJ)){
                                    count++;
                                    break;
                                }
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
     * LR(1)项目集规范族
     */
    public void CalProjectCC(){
        TreeSet<Character> characters = new TreeSet<>();
        characters.add('#');
        Project project = new Project(0, 0, characters);
        ArrayList<Project> projects = new ArrayList<>();
        projects.add(project);
        CLOSURE(projects);
        projectCC.add(projects);
        allSet.remove('E');
        for (int i = 0; i < projectCC.size(); i++) {
            for (Character charItem : allSet){
                ArrayList<Project> goResult = GO(projectCC.get(i), charItem);
                if (!goResult.isEmpty()){
                    Integer integer = IsExist(goResult);
                    if (integer != -1){
                        GoObject goObject = new GoObject(i, charItem, integer);
                        GoList.add(goObject);
                    }
                    if (!goResult.isEmpty() && IsExist(goResult) == -1){
                        projectCC.add(goResult);
                        GoObject goObject = new GoObject(i, charItem, projectCC.size() - 1);
                        GoList.add(goObject);
                    }
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
                TreeSet<Character> findStr = projectItem.getFindStr();
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
                                    if (findStr.contains(ACTION[0][m].charAt(0))){
                                        ACTION[i][m] = "r" + num;
                                    }
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
    /**
     * 查找ACTION表，并返回内容
     */
    public String searchACTION(Integer status, Character a){
        String actionStr = "";
        for (int i = 0; i < ACTION.length; i++) {
            for (int j = 0; j < ACTION[i].length; j++) {
                if (ACTION[i][0].equals(status.toString()) && ACTION[0][j].equals(a.toString())){
                    actionStr = ACTION[i][j];
                }
            }
        }
        return actionStr;
    }
    public String searchGOTO(Integer status, Character A){
        String gotoStr = "";
        for (int i = 0; i < GOTO.length; i++) {
            for (int j = 0; j < GOTO[i].length; j++) {
                if (GOTO[i][0].equals(status.toString()) && GOTO[0][j].equals(A.toString()) ){
                    gotoStr = GOTO[i][j];
                }
            }
        }
        return gotoStr;
    }
    /**
     *求First集
     */
    public void FirstCollection(){
        for (String LR1Str: LR1List) {
            String[] split = LR1Str.split("->");
            //产生式左边为非终结符
            char Vn = split[0].charAt(0);
            //计算Vn的First集
            if (!firstCollection.containsKey(Vn)){
                CalFirstCollection(Vn);
            }
        }
    }
    /**
     *计算某非终结符T的First集
     *T->X1X2X3...
     * 1) X1是终结符，则将X1添加到First(T)中
     * 2) X1是非终结符
     *  2.1 First(X1)不含ε，则将First(X1)添加到First(T)中
     *  2.2 First(X1)含ε，则将First(X1)\{ε}添加到First(T)中，并继续判断X2是否未终结符...
     *  2.3 First(X1)，First(X2)...均含ε，则将First(X1)\{ε}添加到First(T)中
     */
    public void CalFirstCollection(Character T){
        for (String LR1Str: LR1List) {
            String[] split = LR1Str.split("->");
            //产生式左边为非终结符
            char Vn = split[0].charAt(0);
            //产生式右边为待求项
            String vtStr = split[1];
            if (Vn == T){
                //1) X1是终结符，则将X1添加到First(T)中
                if (VtSet.contains(vtStr.charAt(0))){
                    //First集
                    //首先判断firstCollection中是否含有key Vn，有则添加value，没有则创建一个
                    if (firstCollection.containsKey(Vn)){
                        TreeSet<Character> characterTreeSet = firstCollection.get(Vn);
                        characterTreeSet.add(vtStr.charAt(0));
                    }else{
                        TreeSet<Character> firstTreeSet = new TreeSet<>();
                        firstTreeSet.add(vtStr.charAt(0));
                        firstCollection.put(Vn, firstTreeSet);
                    }
                }else{//2) X1是非终结符
                    for (int i = 0; i < vtStr.length(); i++) {
                        char Xn = vtStr.charAt(i);
                        //2.3 First(X1)，First(X2)...均含ε，则将First(X1)\{ε}添加到First(T)中
                        if (i == vtStr.length() - 1 && firstCollection.containsKey(Xn)){
                            if (firstCollection.get(Xn).contains('ε')){
                                TreeSet<Character> characterTreeSet = firstCollection.get(Vn);
                                characterTreeSet.add('ε');
                            }
                        }
                        if (!firstCollection.containsKey(Xn)){
                            CalFirstCollection(Xn);
                        }
                        // 2.1 First(X1)不含ε，则将First(X1)添加到First(T)中
                        if (!firstCollection.get(Xn).contains('ε')){
                            if (firstCollection.containsKey(Vn)){
                                TreeSet<Character> characterTreeSet = firstCollection.get(Vn);
                                characterTreeSet.addAll(firstCollection.get(Xn));
                            }else{
                                TreeSet<Character> characters = new TreeSet<>();
                                characters.addAll(firstCollection.get(Xn));
                                firstCollection.put(Vn, characters);
                            }
                            break;
                        }else{
                            //2.2 First(X1)含ε，则将First(X1)\{ε}添加到First(T)中，并继续判断X2是否未终结符...
                            if (firstCollection.containsKey(Vn)){
                                TreeSet<Character> characterTreeSet = firstCollection.get(Vn);
                                TreeSet<Character> characters = firstCollection.get(Xn);
                                if (characters.contains('ε')){
                                    characters.remove('ε');
                                    characterTreeSet.addAll(characters);
                                    characters.add('ε');
                                }else{
                                    characterTreeSet.addAll(characters);
                                }

                            }else{
                                TreeSet<Character> characters1 = new TreeSet<>();
                                TreeSet<Character> characters= firstCollection.get(Xn);
                                if (characters.contains('ε')){
                                    characters.remove('ε');
                                    characters1.addAll(characters);
                                    firstCollection.put(Vn, characters1);
                                    characters.add('ε');
                                }else{
                                    characters1.addAll(characters);
                                    firstCollection.put(Vn, characters1);
                                }

                            }
                        }
                    }
                }
            }
        }
    }
    /**
     *求Follow集
     */
    public void FollowCollection(){
        for (String LR1Str : LR1List){
            String[] split = LR1Str.split("->");
            //产生式左边为非终结符
            char Vn = split[0].charAt(0);
            if (!followCollection.containsKey(Vn)){
                CalFollowCollection(Vn);
            }
        }
    }
    /**
     * 计算Follow集
     * 1)若B为开始符号S，则将#添加到Follow(B)中
     * 2)A->αBγ，首先将First(γ)\{ε}添加到Follow(B)中
     * 2.1 如果ε属于First(γ)，则将Follow(A)添加到Follow(B)中
     * 3)A->αB，则将Follow(A)添加到Follow(B)中
     */
    public void CalFollowCollection(Character B){
        //1)若B为开始符号S，则将#添加到Follow(B)中
        if (B == startSymbol){
            TreeSet<Character> characters = new TreeSet<>();
            characters.add('#');
            followCollection.put(B, characters);
        }
        for (String LR1Str : LR1List){
            String[] split = LR1Str.split("->");
            //产生式左边为非终结符
            char Vn = split[0].charAt(0);
            //产生式右边为待求项
            String vtStr = split[1];
            for (int i = 0; i < vtStr.length(); i++) {
                char chari = vtStr.charAt(i);
                if (chari == B){
                    if(i < vtStr.length() - 1){
                        //2)A->αBγ，首先将First(γ)\{ε}添加到Follow(B)中
                        //γ为终结符,First(γ) = {γ}
                        if (VtSet.contains(vtStr.charAt(i + 1))){
                            if (followCollection.containsKey(chari)){
                                TreeSet<Character> followTreeSet = followCollection.get(chari);
                                followTreeSet.add(vtStr.charAt(i + 1));
                            }else{
                                TreeSet<Character> characters = new TreeSet<>();
                                characters.add(vtStr.charAt(i + 1));
                                followCollection.put(chari, characters);
                            }
                        }else{
                            //γ为非终结符
                            if (followCollection.containsKey(chari)){
                                TreeSet<Character> followTreeSet = followCollection.get(chari);
                                TreeSet<Character> firstTreeSet = firstCollection.get(vtStr.charAt(i + 1));
                                if (firstTreeSet.contains('ε')){
                                    firstTreeSet.remove('ε');
                                    followTreeSet.addAll(firstTreeSet);
                                    firstTreeSet.add('ε');
                                }else{
                                    followTreeSet.addAll(firstTreeSet);
                                }

                            }else{
                                TreeSet<Character> characters = new TreeSet<>();
                                TreeSet<Character> firstTreeSet = firstCollection.get(vtStr.charAt(i + 1));
                                if (firstTreeSet.contains('ε')){
                                    firstTreeSet.remove('ε');
                                    characters.addAll(firstTreeSet);
                                    firstTreeSet.add('ε');
                                }else{
                                    characters.addAll(firstTreeSet);
                                }
                                followCollection.put(chari, characters);
                            }
                            if (isFirstNull(vtStr, i + 1)){
                                //2.1 如果ε属于First(γ)，则将Follow(A)添加到Follow(B)中
                                if ((firstCollection.get(vtStr.charAt(i + 1)).contains('ε'))){
                                    if (Vn == B){
                                        break;
                                    }
                                    if (!followCollection.containsKey(Vn)){
                                        CalFollowCollection(Vn);
                                    }
                                    if (followCollection.containsKey(chari)){
                                        TreeSet<Character> followTreeSet = followCollection.get(chari);
                                        followTreeSet.addAll(followCollection.get(Vn));
                                    }else{
                                        followCollection.put(chari, followCollection.get(Vn));
                                    }
                                }
                            }

                        }
                    }
                    //判断是否chari后面是否有元素
                    //3)A->αB，则将Follow(A)添加到Follow(B)中
                    if (i == vtStr.length() - 1){//chari后面没有元素
                        if (Vn == B){
                            break;
                        }
                        if (!followCollection.containsKey(Vn)){
                            CalFollowCollection(Vn);
                        }
                        if (followCollection.containsKey(chari)){
                            TreeSet<Character> followTreeSet = followCollection.get(chari);
                            followTreeSet.addAll(followCollection.get(Vn));
                        }else{
                            followCollection.put(chari, followCollection.get(Vn));
                        }
                    }
                }
            }
        }
    }
    /**
     * 判断ε是否属于First(γ)
     */
    public boolean isFirstNull(String vtStr, int i){
        for (int j = i; j < vtStr.length(); j++) {
            //如果包含非终结符，false
            if (VtSet.contains(vtStr.charAt(i))){
                return false;
            }else{//vtStr.charAt(i)为非终结符
                if (!firstCollection.get(vtStr.charAt(i)).contains('ε')){
                    return false;
                }
            }
            if (j == vtStr.length() - 1){
                return true;
            }
        }
        return true;
    }
    /**
     *计算某字符串α:X1X2X3...的First集
     *T->X1X2X3...
     * 1) X1是终结符，则将X1添加到First(α)中
     * 2) X1是非终结符
     *  2.1 First(X1)不含ε，则将First(X1)添加到First(α)中
     *  2.2 First(X1)含ε，则将First(X1)\{ε}添加到First(α)中，并继续判断X2是否未终结符...
     *  2.3 First(X1)，First(X2)...均含ε，则将First(X1)\{ε}添加到First(α)中
     */
    public TreeSet<Character> CalFirstS(String LR1Str){
        //FirstS集
        TreeSet<Character> firstSTree = new TreeSet<>();
        //产生式右边为待求项
        String vtStr = LR1Str;
        //1) X1是终结符，则将X1添加到First(α)中
        if (VtSet.contains(vtStr.charAt(0))){
            firstSTree.add(vtStr.charAt(0));
        }else{//2) X1是非终结符
            for (int i = 0; i < vtStr.length(); i++) {
                char Xn = vtStr.charAt(i);
                //2.3 First(X1)，First(X2)...均含ε，则将First(X1)\{ε}添加到First(α)中
                if (i == vtStr.length() - 1 ){
                    if (firstCollection.get(Xn).contains('ε')){
                        firstSTree.add('ε');
                    }
                }
                // 2.1 First(X1)不含ε，则将First(X1)添加到First(α)中
                if (!firstCollection.get(Xn).contains('ε')){
                    firstSTree.addAll(firstCollection.get(Xn));
                    break;
                }else{
                    //2.2 First(X1)含ε，则将First(X1)\{ε}添加到First(α)中，并继续判断X2是否未终结符...
                    TreeSet<Character> characters = firstCollection.get(Xn);
                    if (characters.contains('ε')){
                        characters.remove('ε');
                        firstSTree.addAll(characters);
                        characters.add('ε');
                    }else{
                        firstSTree.addAll(characters);
                    }
                }
            }
        }
        return firstSTree;
    }

}
