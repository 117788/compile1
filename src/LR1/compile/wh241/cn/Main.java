package LR1.compile.wh241.cn;

import java.util.ArrayList;
import java.util.Stack;

public class Main {
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
        /**
         * LR(1)主控程序
         */
        //状态栈
        Stack<Integer> statusStack = new Stack<>();
        //符号栈
        Stack<Character> symbolStack = new Stack<>();
        //输入串
        //String inStr = "abab#";
        String inStr = "aabab#";
        //指示输入串
        int i = 0;
        //状态标志
        boolean flag = true;
        //步骤序号
        int count = 0;
        //状态栈
        String statusT = "";
        //符号栈
        String symbolT = "";
        //输入串
        String inProcessStr = "";
        //初始化栈
        statusStack.push( 0 );
        symbolStack.push('#');
        System.out.printf("%-13s%-13s%-13s%-13s","步骤","状态栈","符号栈","输入串");
        System.out.println("");
        while (flag){
            //求状态栈字符串
            statusT = "";
            for (Integer status : statusStack){
                statusT += status;
            }
            //求符号栈字符串
            symbolT = "";
            for (Character symbol : symbolStack){
                symbolT += symbol;
            }
            //求输入串
            inProcessStr = "";
            for (int j = i; j < inStr.length(); j++) {
                inProcessStr += inStr.charAt(j);
            }
            System.out.printf("%-15s%-15s%-15s%-15s",count,statusT,symbolT,inProcessStr);
            System.out.println("");
            count++;
            Integer status = statusStack.peek();
            char a = inStr.charAt(i);
            String searchA = lr1.searchACTION(status, a);
            if (!searchA.equals("")){
                //去查找结果的第一个字符，来判断接下来的动作
                char firstStr = searchA.charAt(0);
                //取第一个字符后面的字符
                String remainStr = "";
                for (int j = 1; j < searchA.length(); j++) {
                    remainStr += searchA.charAt(j);
                }
                int remainInt;
                if (!remainStr.equals("cc")){
                    remainInt = Integer.parseInt(remainStr);
                }else{
                    remainInt = -100;
                }
                switch (firstStr){
                    case 's':
                    {
                        //执行移进动作
                        statusStack.push(remainInt);
                        symbolStack.push(a);
                        i++;
                        break;
                    }
                    case 'r': {
                        //执行归约动作
                        ArrayList<String> lr1List = lr1.LR1List;
                        //产生式
                        String LR1Str= lr1List.get(remainInt);
                        String[] split = LR1Str.split("->");
                        //产生式左部
                        String VnStr = split[0];
                        //得到产生式右部符号串
                        String rightStr = split[1];
                        //栈顶按照右部符号串的长度出栈
                        for (int j = 0; j < rightStr.length(); j++) {
                            statusStack.pop();
                            symbolStack.pop();
                        }
                        String searchG = lr1.searchGOTO(statusStack.peek(), VnStr.charAt(0));
                        //压栈
                        statusStack.push(Integer.parseInt(searchG));
                        symbolStack.push(VnStr.charAt(0));
                        break;
                    }
                    case 'a':{
                        flag = false;
                        System.out.println("匹配成功");
                        break;
                    }
                }
            }else {
                printError();
                flag = false;
            }
        }
        System.out.println("");
    }
    public static void printError(){
        System.out.println("Error!!");
    }
}
