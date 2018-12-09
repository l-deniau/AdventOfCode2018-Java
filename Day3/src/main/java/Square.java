import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Square {
    private int ID;
    private int x;
    private int y;
    private int width;
    private int height;

    public int getX2 () {
        return x + width;
    }

    public int getY2() {
        return y + height;
    }
}
