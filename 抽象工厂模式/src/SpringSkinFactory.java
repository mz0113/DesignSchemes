public class SpringSkinFactory extends AbstractFactory {
    @Override
    public IButton createButton() {
        return new SpringButton();
    }

    @Override
    public IText createText() {
        return new SpringText();
    }
}
