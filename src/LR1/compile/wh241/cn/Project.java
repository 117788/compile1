package LR1.compile.wh241.cn;

public class Project {
    //项目的第一个参数
    private Integer num;
    //项目的第二个参数
    private Integer place;
    //构造函数，初始化参数
    Project(Integer num, Integer place){
        this.num = num;
        this.place = place;
    }
    public Integer getNum() {
        return num;
    }

    public Integer getPlace() {
        return place;
    }
}
