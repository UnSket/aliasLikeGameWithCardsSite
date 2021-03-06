package deck.image.processing;

import java.util.Objects;

class RGBAColor {

    static final int MAX_RGBA_BYTE = 255;
    static final long BACKGROUND_RADII = 400;

    private int b;
    private int r;
    private int g;
    private int a;

    RGBAColor(Integer rgbaBytes) {

        b = (rgbaBytes >> 24) & 0xff;
        g = (rgbaBytes >> 16) & 0xff;
        r = (rgbaBytes >> 8) & 0xff;
        a = rgbaBytes & 0xff;
    }

    int convertTo4ByteFormat() {
        return (b << 24) | (g << 16) | (r << 8) | a;
    }

    public void setColor(RGBAColor color) {
        this.a = color.getA();
        this.b = color.getB();
        this.g = color.getG();
        this.r = color.getR();
    }

    public long calculateCartesianDistance(RGBAColor other) {
        return (other.a - this.a) * (other.a - this.a) +
                (other.b - this.b) * (other.b - this.b) +
                (other.g - this.g) * (other.g - this.g)*10000 +
                (other.r - this.r) * (other.r - this.r);
    }

    int getB() {
        return b;
    }

    void setB(int b) {
        this.b = b;
    }

    int getR() {
        return r;
    }

    void setR(int r) {
        this.r = r;
    }

    int getG() {
        return g;
    }

    void setG(int g) {
        this.g = g;
    }

    int getA() {
        return a;
    }

    void setA(int a) {
        this.a = a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RGBAColor rgbaColor = (RGBAColor) o;
        return b/3 == rgbaColor.b/3 &&
                r/3 == rgbaColor.r/3 &&
                g/3 == rgbaColor.g/3 &&
                a/3 == rgbaColor.a/3;
    }

    @Override
    public int hashCode() {
        return Objects.hash(b/3, r/3, g/3, a/3);
    }
}
