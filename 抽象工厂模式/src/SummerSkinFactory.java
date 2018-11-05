public class SummerSkinFactory extends AbstractFactory {
    @Override
    public IButton createButton() {
        return new SummerButton();
    }

    @Override
    public IText createText() {
        return new SummerText();
    }
}
