package com.ip.iproject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    BluetoothDevice ev3device = null;
    BluetoothSocket socket = null;

    ConstraintLayout allLayout;
    ConstraintLayout connectLayout;

    EditText BrickName;// поле ввода для имени кирпича

    ImageView pigFon;
    ImageView cowFon;
    ImageView vegetablesFon;

    ImageView teaFon;
    ImageView coffeeFon;
    ImageView kiselFon;

    boolean pigTaste = false;
    boolean cowTaste = false;
    boolean vegetablesTaste = false;
    byte drinkTaste = 0;

    final String SAVED_TEXT = "saved_text";
    SharedPreferences sPref;


    byte[] b1 = {0,-2,-4,-6,-8,-10,-12,-14,-16,-18,-20,-22,-24,-26,-28,-30,-32,-34,-36,-38,-40,-42,
            -44,-46,-48,-50,-52,-54,-56,-58,-60,-62,-64,-66,-68,-70,-72,-74,-76,-78,-80,-82,
            -84,-86,-88,-90,-92,-94,-96,-98,-100,-102,-104,-106,-108,-110,-112,-114,-116,-118,
            -120,-122,-124,-126,-128,124,120,116,112,108,104,100,96,92,88,84,80,76,72,68,64,60,
            56,52,48,44,40,36,32,28,24,20,16,12,8,4,0,-8,-16,-24,-32,-40,-48,-56,-64,-72,-80,
            -88,-96,-104,-112,-120,-128,112,96,80,64,48,32,16,0,-32,-64,-96,-128,64,0,-128,0,
            -128,0,64,-128,-96,-64,-32,0,16,32,48,64,80,96,112,-128,-120,-112,-104,-96,-88,-80,
            -72,-64,-56,-48,-40,-32,-24,-16,-8,0,4,8,12,16,20,24,28,32,36,40,44,48,52,56,60,64,
            68,72,76,80,84,88,92,96,100,104,108,112,116,120,124,-128,-126,-124,-122,-120,-118,
            -116,-114,-112,-110,-108,-106,-104,-102,-100,-98,-96,-94,-92,-90,-88,-86,-84,-82,
            -80,-78,-76,-74,-72,-70,-68,-66,-64,-62,-60,-58,-56,-54,-52,-50,-48,-46,-44,-42,
            -40,-38,-36,-34,-32,-30,-28,-26,-24,-22,-20,-18,-16,-14,-12,-10,-8,-6,-4,-2};// нужно для отправки byte

    byte[] b2 = {-61,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,
            -62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,
            -62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,
            -62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,
            -62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-62,-63,-63,-63,
            -63,-63,-63,-63,-63,-63,-63,-63,-63,-63,-63,-63,-63,-63,-63,-63,-63,-63,-63,-63,
            -63,-64,-64,-64,-64,-64,-64,-65,0,63,64,64,64,64,64,64,65,65,65,65,65,65,65,65,
            65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,66,66,66,66,66,66,66,66,66,66,
            66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,
            66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,
            66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,66,// и это тоже))))))))
            66,66,66,66,66,66,66,66};

    @Override
    protected void onCreate(Bundle savedInstanceState) {//загрузка приложения
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//системное

        allLayout = findViewById(R.id.allLayout);
        connectLayout = findViewById(R.id.connectLayout);

        BrickName = findViewById(R.id.BrickName);//поле ввода имени кирпича

        pigFon = findViewById(R.id.pigFon);//фоны под картинками
        cowFon = findViewById(R.id.cowFon);
        vegetablesFon = findViewById(R.id.vegetablesFon);

        teaFon = findViewById(R.id.teaFon);
        coffeeFon = findViewById(R.id.coffeeFon);
        kiselFon = findViewById(R.id.kiselFon);

        loadText();//загрузить имя кирпича с последнего запуска приложения
    }

    public void ConnectEV3(View view) throws IOException, InterruptedException {
        saveText();//сохраняю имя кирпича
        for (BluetoothDevice device : BluetoothAdapter.getDefaultAdapter().getBondedDevices())//перебираю подключёные устройства
        {
            if (device.getName().compareTo(BrickName.getText().toString()) == 0) {
                ev3device = device;
                break;
            }
        }
        if (ev3device != null)//если нашёл кирпич
        {
            socket = ev3device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socket.connect();// то подключаю
            Toast.makeText(getApplicationContext(),  "Я подключил " + BrickName.getText().toString() + " :D", Toast.LENGTH_SHORT).show();
            connectLayout.setVisibility(View.INVISIBLE);
            allLayout.setVisibility(View.VISIBLE);
        }
        else
            Toast.makeText(getApplicationContext(), "Я не нашёл " + BrickName.getText().toString() + " :(", Toast.LENGTH_SHORT).show();
    }
    public void StartCooking(View view) throws IOException {
        if ((socket != null) && ((cowTaste) || (pigTaste) || (vegetablesTaste) || (drinkTaste != 0))) {
            send("s1", pigTaste);
            send("s2", cowTaste);
            send("s3", vegetablesTaste);

            send("d", drinkTaste);

            send("r", true);
        }
        else if (!((cowTaste) || (pigTaste) || (vegetablesTaste) || (drinkTaste != 0)))
            Toast.makeText(getApplicationContext(), "Вы ничего не выбрали", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Вы не подключили устройство", Toast.LENGTH_SHORT).show();
    }


    private void send(String n, String s) throws IOException {
        byte[] cmd = new byte[11 + n.length() + s.length()];
        cmd[0] = (byte)(cmd.length - 2 & 255);
        cmd[1] = (byte)(cmd.length - 2 >> 8);
        cmd[2] = 0;
        cmd[3] = 0;
        cmd[4] = -127;
        cmd[5] = -98;
        cmd[6] = (byte)(n.length() + 1);
        int pos = 7;

        int i;
        for(i = 0; i < n.length() && i < 255; ++i) {
            cmd[pos] = (byte)n.charAt(i);
            ++pos;
        }

        cmd[pos] = 0;
        ++pos;
        cmd[pos] = (byte)(s.length() + 1 & 255);
        ++pos;
        cmd[pos] = (byte)(s.length() + 1 >> 8);
        ++pos;

        for(i = 0; i < s.length(); ++i) {
            cmd[pos] = (byte)s.charAt(i);
            ++pos;
        }

        cmd[pos] = 0;
        socket.getOutputStream().write(cmd);
    }
    private void send(String n, byte b ) throws IOException {
        byte[] cmd = new byte[14+n.length()];

        cmd[0] = (byte)( (cmd.length - 2) & 0xFF );
        cmd[1] = (byte)( (cmd.length - 2) >> 8 );
        cmd[2] = (byte)0x00;
        cmd[3] = (byte)0x00;
        cmd[4] = (byte)0x81;
        cmd[5] = (byte)0x9E;
        cmd[6] = (byte)(n.length()+1);
        int pos = 7;
        for(int i = 0; i < n.length() && i < 255; i++ )
        {
            cmd[pos] = (byte)n.charAt(i);
            pos++;
        }
        cmd[pos] = (byte)0x00;
        pos++;
        cmd[pos] = (byte)0x04;
        pos++;
        cmd[pos] = (byte)0x00;
        pos++;
        cmd[pos] = (byte)0x00;
        pos++;
        cmd[pos] = (byte)0x00;
        pos++;

        if (b < -128 || b > 127)
        {
            cmd[pos] = 0;
            pos++;
            cmd[pos] = 0;
        }
        else
        {

            cmd[pos] = b1[b+128];
            pos++;
            cmd[pos] = b2[b+128];
        }

        socket.getOutputStream().write(cmd);

    }
    private void send(String n, boolean l ) throws IOException {
        byte[] cmd = new byte[11+n.length()];

        cmd[0] = (byte)( (cmd.length - 2) & 0xFF );
        cmd[1] = (byte)( (cmd.length - 2) >> 8 );
        cmd[2] = (byte)0x00;
        cmd[3] = (byte)0x00;
        cmd[4] = (byte)0x81;
        cmd[5] = (byte)0x9E;
        cmd[6] = (byte)(n.length()+1);
        int pos = 7;
        for(int i = 0; i < n.length() && i < 255; i++ )
        {
            cmd[pos] = (byte)n.charAt(i);
            pos++;
        }
        cmd[pos] = (byte)0x00;
        pos++;
        cmd[pos] = (byte)0x01;
        pos++;
        cmd[pos] = (byte)0x00;
        pos++;
        if (l)
            cmd[pos] = (byte)0x01;
        else
            cmd[pos] = (byte)0x00;

        socket.getOutputStream().write(cmd);

    }

    public void pigClick(View view) {
        if (pigFon.getVisibility() == View.VISIBLE) {
            pigTaste = false;
            pigFon.setVisibility(View.INVISIBLE);
        }
        else {
            pigTaste = true;
            pigFon.setVisibility(View.VISIBLE);
        }
    }
    public void cowClick(View view) {
        if (cowFon.getVisibility() == View.VISIBLE) {
            cowTaste = false;
            cowFon.setVisibility(View.INVISIBLE);
        }
        else {
            cowTaste = true;
            cowFon.setVisibility(View.VISIBLE);
        }
    }
    public void vegetablesClick(View view) {
        if (vegetablesFon.getVisibility() == View.VISIBLE) {
            vegetablesTaste = false;
            vegetablesFon.setVisibility(View.INVISIBLE);
        }
        else {
            vegetablesTaste = true;
            vegetablesFon.setVisibility(View.VISIBLE);
        }
    }


    public void teaClick(View view) {
        if (teaFon.getVisibility() == View.VISIBLE) {
            drinkTaste = 0;
            teaFon.setVisibility(View.INVISIBLE);
            coffeeFon.setVisibility(View.INVISIBLE);
            kiselFon.setVisibility(View.INVISIBLE);
        }
        else {
            drinkTaste = 1;
            teaFon.setVisibility(View.VISIBLE);
            coffeeFon.setVisibility(View.INVISIBLE);
            kiselFon.setVisibility(View.INVISIBLE);
        }
    }
    public void coffeeClick(View view) {
        if (coffeeFon.getVisibility() == View.VISIBLE) {
            drinkTaste = 0;
            teaFon.setVisibility(View.INVISIBLE);
            coffeeFon.setVisibility(View.INVISIBLE);
            kiselFon.setVisibility(View.INVISIBLE);
        }
        else {
            drinkTaste = 2;
            coffeeFon.setVisibility(View.VISIBLE);
            teaFon.setVisibility(View.INVISIBLE);
            kiselFon.setVisibility(View.INVISIBLE);
        }
    }
    public void kiselClick(View view) {
        if (kiselFon.getVisibility() == View.VISIBLE) {
            drinkTaste = 0;
            teaFon.setVisibility(View.INVISIBLE);
            coffeeFon.setVisibility(View.INVISIBLE);
            kiselFon.setVisibility(View.INVISIBLE);
        }
        else {
            drinkTaste = 3;
            kiselFon.setVisibility(View.VISIBLE);
            teaFon.setVisibility(View.INVISIBLE);
            coffeeFon.setVisibility(View.INVISIBLE);
        }
    }

    void saveText() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT, BrickName.getText().toString());
        ed.commit();
    }

    void loadText() {
        sPref = getPreferences(MODE_PRIVATE);
        String savedText = sPref.getString(SAVED_TEXT, "");
        BrickName.setText(savedText);
    }

}
