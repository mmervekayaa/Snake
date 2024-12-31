import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int tahtaGenisligi = 600;
        int tahtaYuksekligi = tahtaGenisligi;

        JFrame pencere = new JFrame("Yılan");
        pencere.setVisible(true);
        pencere.setSize(tahtaGenisligi, tahtaYuksekligi);
        pencere.setLocationRelativeTo(null);
        pencere.setResizable(false);
        pencere.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGame yilanOyunu = new SnakeGame(tahtaGenisligi, tahtaYuksekligi);
        pencere.add(yilanOyunu);
        pencere.pack();
        yilanOyunu.requestFocus();
    }
}

