import java.util.Scanner;

public class SkinDemo {
    static AbstractFactory fc = null;
    //第一种抽象工厂模式，它核心在于每一个具体工厂负责一个横向不同类别的产品族的生成，例如春天风格工厂负责春天按钮，春天输入框
    //第二种抽象工厂模式，它核心在于每一个具体工厂只负责纵向的同类别的产品等级的生成，例如按钮工厂负责春天，秋天，夏天的按钮，而输
    //    入框工厂负责春天，冬天，秋天输入框，因此调用时候，也需要同时2个工厂分别初始化不同种类的控件

    //本demo是第一种抽象工厂,分为SpringFactory,FallFactory...
    public static void main(String args[]){
        getFactory();
        IButton button = fc.createButton();
        IText text = fc.createText();
        button.display();
        text.display();
    }
    public static void getFactory(){
            System.out.println("请输入初始化皮肤工厂名,1:SpringSkinFactory,2:SummerSkinFactory,3:FallSkinFactory");
            Scanner scanner = new Scanner(System.in);
            String skin = scanner.nextLine();
        try {
             Class c = Class.forName(skin);
             fc = (AbstractFactory) c.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
