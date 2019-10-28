package LR1.compile.wh241.cn;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Stack;

public class Main {
    private static JFrame jf = new JFrame("LL(1)分析法");// 创建窗口
    private static JTable table2;//ACTION表
    private static JTable table3;//GOTO表
    private static JTable table1;//LR(1)分析过程
    private static JTable table;//LR(1)分析过程
    private static DefaultTableModel dtm2;//ACTION表
    private static DefaultTableModel dtm3;//GOTO表
    private static DefaultTableModel dtm1;//LR(1)分析过程
    private static String[] columnNames2 = {"非终结符", "First集"};//ACTION表
    private static String[] columnNames3 = {"非终结符", "Follow集"};//GOTO表
    private static String[] columnNames1 = {"步骤", "状态栈", "符号栈", "输入串"};//LR(1)分析过程
    private static Object[][] rowData1 = {};//LR(1)分析过程
    private static Object[][] rowData2 = {};//ACTION表
    private static Object[][] rowData3 = {};//GOTO表
    public static void main(String[] args) {
        // 创建文本框，指定可见列数为8列
        JTextField textField = new JTextField(8);
        textField.setFont(new Font(null, Font.PLAIN, 20));
        // 创建一个 5 行 10 列的文本区域
        JTextArea textArea = new JTextArea(10,10);
        // 设置自动换行
        textArea.setLineWrap(true);
        //设置字体大小
        textArea.setFont(new Font(null, Font.PLAIN, 25));
        // 创建一个顶层容器（窗口）
        //JFrame jf = new JFrame("LR(1)分析法");          // 创建窗口
        jf.setSize(900, 900);                       // 设置窗口大小
        jf.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 当点击窗口的关闭按钮时退出程序（没有这一句，程序不会退出）
        // 创建中间容器（面板容器）
        //JPanel panel = new JPanel(new FlowLayout());                // 创建面板容器，使用流式布局管理器
        // 创建内容面板容器
        JPanel panel = new JPanel();
        // 创建分组布局，并关联容器
        GroupLayout layout = new GroupLayout(panel);
        // 设置容器的布局
        panel.setLayout(layout);
        // 自动创建组件之间的间隙
        layout.setAutoCreateGaps(true);
        // 自动创建容器与触到容器边框的组件之间的间隙
        layout.setAutoCreateContainerGaps(true);
        //创建文字
        JLabel label02 = new JLabel("<html>完整演示<br/></html>");
        label02.setFont(new Font(null, Font.PLAIN, 25));  // 设置字体，null 表示使用默认字体
        label02.setToolTipText("完整演示");
        label02.setForeground(new Color(84, 232, 57));
        JLabel label03 = new JLabel("<html>请输入LR(1)文法<br/></html>");
        label03.setFont(new Font(null, Font.PLAIN, 25));  // 设置字体，null 表示使用默认字体
        JLabel label04 = new JLabel("<html>请输入符号串<br/></html>");
        label04.setFont(new Font(null, Font.PLAIN, 25));  // 设置字体，null 表示使用默认字体
        JLabel label06 = new JLabel("<html>LR(1)分析过程<br/></html>");
        label06.setFont(new Font(null, Font.PLAIN, 20));  // 设置字体，null 表示使用默认字体
        JLabel label07 = new JLabel("<html>ACTION表<br/></html>");
        label07.setFont(new Font(null, Font.PLAIN, 16));  // 设置字体，null 表示使用默认字体
        JLabel label08 = new JLabel("<html>GOTO表<br/></html>");
        label08.setFont(new Font(null, Font.PLAIN, 16));  // 设置字体，null 表示使用默认字体
        JButton btn5 = new JButton("全部展示");
        btn5.setBackground(new Color(255, 128,192));
        btn5.setFocusPainted(false);//去除文字周围边框
        /**
         * ACTION表
         */
        // 创建一个表格，指定 表头 和 所有行数据
        dtm2 = new DefaultTableModel(rowData2, columnNames2);
        table2 = new JTable(dtm2);
        // 设置表格内容颜色
        table2.setForeground(Color.BLACK);                   // 字体颜色
        table2.setFont(new Font(null, Font.PLAIN, 14));      // 字体样式
        table2.setSelectionForeground(Color.DARK_GRAY);      // 选中后字体颜色
        table2.setSelectionBackground(Color.LIGHT_GRAY);     // 选中后字体背景
        table2.setGridColor(Color.GRAY);                     // 网格颜色
        // 设置表头
        table2.getTableHeader().setFont(new Font(null, Font.BOLD, 14));  // 设置表头名称字体样式
        table2.getTableHeader().setForeground(Color.RED);                // 设置表头名称字体颜色
        table2.getTableHeader().setResizingAllowed(false);               // 设置不允许手动改变列宽
        table2.getTableHeader().setReorderingAllowed(false);             // 设置不允许拖动重新排序各列
        // 设置行高
        table2.setRowHeight(30);
        // 第一列列宽设置为40
        table2.getColumnModel().getColumn(0).setPreferredWidth(40);
        // 设置滚动面板视口大小（超过该大小的行数据，需要拖动滚动条才能看到）
        table2.setPreferredScrollableViewportSize(new Dimension(400, 150));
        // 把 表格 放到 滚动面板 中（表头将自动添加到滚动面板顶部）
        JScrollPane scrollPane2 = new JScrollPane(table2);
        /**
         * GOTO表
         */
        // 创建一个表格，指定 表头 和 所有行数据
        dtm3 = new DefaultTableModel(rowData3, columnNames3);
        table3 = new JTable(dtm3);
        // 设置表格内容颜色
        table3.setForeground(Color.BLACK);                   // 字体颜色
        table3.setFont(new Font(null, Font.PLAIN, 14));      // 字体样式
        table3.setSelectionForeground(Color.DARK_GRAY);      // 选中后字体颜色
        table3.setSelectionBackground(Color.LIGHT_GRAY);     // 选中后字体背景
        table3.setGridColor(Color.GRAY);                     // 网格颜色
        // 设置表头
        table3.getTableHeader().setFont(new Font(null, Font.BOLD, 14));  // 设置表头名称字体样式
        table3.getTableHeader().setForeground(Color.RED);                // 设置表头名称字体颜色
        table3.getTableHeader().setResizingAllowed(false);               // 设置不允许手动改变列宽
        table3.getTableHeader().setReorderingAllowed(false);             // 设置不允许拖动重新排序各列
        // 设置行高
        table3.setRowHeight(30);
        // 第一列列宽设置为40
        table3.getColumnModel().getColumn(0).setPreferredWidth(40);
        // 设置滚动面板视口大小（超过该大小的行数据，需要拖动滚动条才能看到）
        table3.setPreferredScrollableViewportSize(new Dimension(400, 150));
        // 把 表格 放到 滚动面板 中（表头将自动添加到滚动面板顶部）
        JScrollPane scrollPane3 = new JScrollPane(table3);


        /**
         * LR(1)分析过程
         */
        // 创建一个表格，指定 表头 和 所有行数据
        dtm1 = new DefaultTableModel(rowData1, columnNames1);
        table1 = new JTable(dtm1);
        // 设置表格内容颜色
        table1.setForeground(Color.BLACK);                   // 字体颜色
        table1.setFont(new Font(null, Font.PLAIN, 14));      // 字体样式
        table1.setSelectionForeground(Color.DARK_GRAY);      // 选中后字体颜色
        table1.setSelectionBackground(Color.LIGHT_GRAY);     // 选中后字体背景
        table1.setGridColor(Color.GRAY);                     // 网格颜色
        // 设置表头
        table1.getTableHeader().setFont(new Font(null, Font.BOLD, 14));  // 设置表头名称字体样式
        table1.getTableHeader().setForeground(Color.RED);                // 设置表头名称字体颜色
        table1.getTableHeader().setResizingAllowed(false);               // 设置不允许手动改变列宽
        table1.getTableHeader().setReorderingAllowed(false);             // 设置不允许拖动重新排序各列
        // 设置行高
        table1.setRowHeight(30);
        // 第一列列宽设置为40
        table1.getColumnModel().getColumn(0).setPreferredWidth(40);
        // 设置滚动面板视口大小（超过该大小的行数据，需要拖动滚动条才能看到）
        table1.setPreferredScrollableViewportSize(new Dimension(400, 300));
        // 把 表格 放到 滚动面板 中（表头将自动添加到滚动面板顶部）
        JScrollPane scrollPane1 = new JScrollPane(table1);
        // 把 面板容器 作为窗口的内容面板 设置到 窗口
        JScrollPane jScrollPane = new JScrollPane(panel);
        jf.setContentPane(jScrollPane);
        // 添加按钮的点击事件监听器

        btn5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String fieldText = textField.getText();
                String textAreaText = textArea.getText();
                goAnalyze(fieldText, textAreaText);
                //LR(1)分析过程
                dtm1.setDataVector(rowData1, columnNames1);
                //ACTION表
                dtm2.setDataVector(rowData2, columnNames2);
                //GOTO表
                dtm3.setDataVector(rowData3, columnNames3);

            }
        });
        /*
         * 水平组（仅确定 X 轴方向的坐标/排列方式）
         *
         * 水平串行: 水平排列（左右排列）
         * 水平并行: 垂直排列（上下排列）
         */
        // 水平串行（左右）label07,scrollPane2,label08,scrollPane3
        GroupLayout.SequentialGroup hSeqGroup2 = layout.createSequentialGroup().addComponent(label07).addComponent(table2)
                .addComponent(label08).addComponent(table3);
        //水平并行（上下）scrollPane1、label06
        GroupLayout.ParallelGroup hParalGroup05 = layout.createParallelGroup().addComponent(label06).addComponent(scrollPane1);
        //水平并行（上下）table、label05
        GroupLayout.ParallelGroup hParalGroup04 = layout.createParallelGroup();
        //水平并行（上下）textField、label04
        GroupLayout.ParallelGroup hParalGroup03 = layout.createParallelGroup().addComponent(label04).addComponent(textField);
        //水平并行（上下）textarea、label03
        GroupLayout.ParallelGroup hParalGroup02 = layout.createParallelGroup().addComponent(label03).addComponent(textArea);

        // 水平并行（上下） label01 和 hSeqGroup
        GroupLayout.ParallelGroup hParalGroup01 = layout.createParallelGroup().addGroup(hParalGroup02).addGroup(hParalGroup03).addComponent(label02).
                addComponent(btn5).addGroup(hSeqGroup2).addGroup(hParalGroup04).addGroup(hParalGroup05);
        layout.setHorizontalGroup(hParalGroup01);  // 指定布局的 水平组（水平坐标）
        /*
         * 垂直组（仅确定 Y 轴方向的坐标/排列方式）
         *
         * 垂直串行: 垂直排列（上下排列）
         * 垂直并行: 水平排列（左右排列）
         */
        // 垂直并行（左右）label07,scrollPane2,label08,scrollPane3
        GroupLayout.ParallelGroup vParalGroup02 = layout.createParallelGroup().addComponent(label07).addComponent(table2)
                .addComponent(label08).addComponent(table3);
        // 垂直串行（上下）
        GroupLayout.SequentialGroup vSeqGroup4 = layout.createSequentialGroup().addComponent(label06).addComponent(scrollPane1);
        // 垂直串行（上下）
        GroupLayout.SequentialGroup vSeqGroup3 = layout.createSequentialGroup();
        // 垂直串行（上下）
        GroupLayout.SequentialGroup vSeqGroup2 = layout.createSequentialGroup().addComponent(label04).addComponent(textField);
        // 垂直串行（上下）
        GroupLayout.SequentialGroup vSeqGroup1 = layout.createSequentialGroup().addComponent(label03).addComponent(textArea);
        // 垂直串行（上下）vParalGroup01, label01
        GroupLayout.SequentialGroup vSeqGroup = layout.createSequentialGroup().addGroup(vSeqGroup1).addGroup(vSeqGroup2).addComponent(label02).addComponent(btn5)
                .addGroup(vParalGroup02).addGroup(vSeqGroup3).addGroup(vSeqGroup4);
        layout.setVerticalGroup(vSeqGroup);    // 指定布局的 垂直组（垂直坐标）
        // 显示窗口，前面创建的信息都在内存中，通过 jf.setVisible(true) 把内存中的窗口显示在屏幕上。
        jf.pack();
        jf.setVisible(true);
    }
    public static void goAnalyze(String inStr, String LR){
        LR1 lr1 = new LR1();
        lr1.initLR1List(LR);
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
        //String inStr = "aabab#";
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
        //LR(1)分析过程表
        ArrayList<ArrayList<String>> anlyzeList = new ArrayList<>();
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
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(count + "");
            arrayList.add(statusT);
            arrayList.add(symbolT);
            arrayList.add(inProcessStr);
            anlyzeList.add(arrayList);
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
        /**
         * ACTION表数据
         */
        rowData2 = lr1.ACTION;
        columnNames2 = lr1.ACTION[0];
        /**
         * GOTO表数据
         */
        rowData3 = lr1.GOTO;
        columnNames3 = lr1.GOTO[0];
        /**
         * LR(分析过程数据)
         */
        Object[][] objects2 = new Object[anlyzeList.size()][];
        for (int j = 0; j < anlyzeList.size(); j++) {
            objects2[j] = anlyzeList.get(j).toArray();
            //System.out.println(anlyzeList.get(j));
        }
        rowData1 = objects2;

    }
    public static void printError(){
        System.out.println("Error!!");
    }
}
