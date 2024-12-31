import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Kutu {
        int x;
        int y;

        Kutu(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int tahtaGenisligi;
    int tahtaYuksekligi;
    int kareBoyutu = 25;

    //yılan
    Kutu yilanBasi;
    ArrayList<Kutu> yilanVucudu;

    //yemek
    Kutu yemek;
    Random random;

    //oyun mantığı
    int hizX;
    int hizY;
    Timer oyunDongusu;

    boolean oyunBitti = false;

    SnakeGame(int tahtaGenisligi, int tahtaYuksekligi) {
        this.tahtaGenisligi = tahtaGenisligi;
        this.tahtaYuksekligi = tahtaYuksekligi;
        setPreferredSize(new Dimension(this.tahtaGenisligi, this.tahtaYuksekligi));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        yilanBasi = new Kutu(5, 5);
        yilanVucudu = new ArrayList<Kutu>();

        yemek = new Kutu(10, 10);
        random = new Random();
        yemekYerleştir();

        hizX = 1;
        hizY = 0;

        //oyun zamanlayıcı
        oyunDongusu = new Timer(100, this); //zamanlayıcı başlama süresi, her kare arasındaki milisaniye
        oyunDongusu.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ciz(g);
    }

    public void ciz(Graphics g) {
        //Izgara çizgileri
        for(int i = 0; i < tahtaGenisligi/kareBoyutu; i++) {
            //(x1, y1, x2, y2)
            g.drawLine(i*kareBoyutu, 0, i*kareBoyutu, tahtaYuksekligi);
            g.drawLine(0, i*kareBoyutu, tahtaGenisligi, i*kareBoyutu);
        }

        //Yemek
        g.setColor(Color.red);
        // g.fillRect(yemek.x*kareBoyutu, yemek.y*kareBoyutu, kareBoyutu, kareBoyutu);
        g.fill3DRect(yemek.x*kareBoyutu, yemek.y*kareBoyutu, kareBoyutu, kareBoyutu, true);

        //Yılan Başı
        g.setColor(Color.green);
        // g.fillRect(yilanBasi.x, yilanBasi.y, kareBoyutu, kareBoyutu);
        // g.fillRect(yilanBasi.x*kareBoyutu, yilanBasi.y*kareBoyutu, kareBoyutu, kareBoyutu);
        g.fill3DRect(yilanBasi.x*kareBoyutu, yilanBasi.y*kareBoyutu, kareBoyutu, kareBoyutu, true);

        //Yılan Vücudu
        for (int i = 0; i < yilanVucudu.size(); i++) {
            Kutu yilanParcasi = yilanVucudu.get(i);
            // g.fillRect(yilanParcasi.x*kareBoyutu, yilanParcasi.y*kareBoyutu, kareBoyutu, kareBoyutu);
            g.fill3DRect(yilanParcasi.x*kareBoyutu, yilanParcasi.y*kareBoyutu, kareBoyutu, kareBoyutu, true);
        }

        //Puan
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (oyunBitti) {
            g.setColor(Color.red);
            g.drawString("Oyun Bitti: " + String.valueOf(yilanVucudu.size()), kareBoyutu - 16, kareBoyutu);
        }
        else {
            g.drawString("Puan: " + String.valueOf(yilanVucudu.size()), kareBoyutu - 16, kareBoyutu);
        }
    }

    public void yemekYerleştir(){
        yemek.x = random.nextInt(tahtaGenisligi/kareBoyutu);
        yemek.y = random.nextInt(tahtaYuksekligi/kareBoyutu);
    }

    public void hareketEt() {
        //yemek ye
        if (carpisma(yilanBasi, yemek)) {
            yilanVucudu.add(new Kutu(yemek.x, yemek.y));
            yemekYerleştir();
        }

        //yılan vücudunu hareket ettir
        for (int i = yilanVucudu.size()-1; i >= 0; i--) {
            Kutu yilanParcasi = yilanVucudu.get(i);
            if (i == 0) { //baştan bir önceki parça
                yilanParcasi.x = yilanBasi.x;
                yilanParcasi.y = yilanBasi.y;
            }
            else {
                Kutu oncekiYilanParcasi = yilanVucudu.get(i-1);
                yilanParcasi.x = oncekiYilanParcasi.x;
                yilanParcasi.y = oncekiYilanParcasi.y;
            }
        }
        //yılan başını hareket ettir
        yilanBasi.x += hizX;
        yilanBasi.y += hizY;

        //oyun bitiş koşulları
        for (int i = 0; i < yilanVucudu.size(); i++) {
            Kutu yilanParcasi = yilanVucudu.get(i);

            //yılan başına çarpma
            if (carpisma(yilanBasi, yilanParcasi)) {
                oyunBitti = true;
            }
        }

        if (yilanBasi.x*kareBoyutu < 0 || yilanBasi.x*kareBoyutu > tahtaGenisligi || //sol sınırı geçme veya sağ sınırı geçme
                yilanBasi.y*kareBoyutu < 0 || yilanBasi.y*kareBoyutu > tahtaYuksekligi ) { //üst sınırı geçme veya alt sınırı geçme
            oyunBitti = true;
        }
    }

    public boolean carpisma(Kutu kutu1, Kutu kutu2) {
        return kutu1.x == kutu2.x && kutu1.y == kutu2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) { //her x milisaniyede oyunDongusu zamanlayıcısı tarafından çağrılır
        hareketEt();
        repaint();
        if (oyunBitti) {
            oyunDongusu.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // System.out.println("KeyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP && hizY != 1) {
            hizX = 0;
            hizY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && hizY != -1) {
            hizX = 0;
            hizY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && hizX != 1) {
            hizX = -1;
            hizY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && hizX != -1) {
            hizX = 1;
            hizY = 0;
        }
    }

    //gereksiz
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
