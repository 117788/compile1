package LR1.compile.wh241.cn;

import java.util.TreeSet;

public class Project {
    //项目的第一个参数，项目编号
    private Integer num;
    //项目的第二个参数，圆点位置
    private Integer place;
    //项目的第三个参数，搜索符
    private TreeSet<Character> findStr;
    //构造函数，初始化参数
    Project(Integer num, Integer place, TreeSet<Character> findStr){
        this.num = num;
        this.place = place;
        this.findStr = findStr;
    }
    public Integer getNum() {
        return num;
    }

    public Integer getPlace() {
        return place;
    }

    public TreeSet<Character> getFindStr() {
        return findStr;
    }
}
