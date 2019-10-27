package LR1.compile.wh241.cn;

public class GoObject {
    //GO(Ik, a) = Ij;
    //第一个参数，k
    private Integer k;
    //第一个参数，a
    private Character a;
    //第一个参数，j
    private Integer j;
    GoObject(Integer k, Character a, Integer j){
        this.k = k;
        this.a = a;
        this.j = j;
    }

    public Integer getK() {
        return k;
    }

    public Character getA() {
        return a;
    }

    public Integer getJ() {
        return j;
    }
}
