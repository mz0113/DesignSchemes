public class FallSkinFactory extends AbstractFactory {

    @Override
    public IButton createButton() {
        return new FallButton();
    }

    @Override
    public IText createText() {
        return new FallText();
    }
}
