package chabi.upmc.pendu;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout container;
    private Button btn_send;
    private TextView lettres_tapees;
    private ImageView image;
    private EditText letter;
    private String word;
    private int found;
    private int error;
    private List<Character> listOfLetters = new ArrayList<>();
    private boolean win;
    private List<String> wordList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//modelise le fichier xml

        container = (LinearLayout) findViewById(R.id.word_container);
        btn_send = (Button) findViewById(R.id.btn_send);
        letter = (EditText) findViewById(R.id.et_letter);
        image = (ImageView) findViewById(R.id.iv_pendu);
        lettres_tapees = (TextView) findViewById(R.id.tv_lettres_tapees);

        initGame();
        btn_send.setOnClickListener(this);
    }

    public void initGame() {
        word = generateWord();
        win = false;
        error = 0;
        found = 0;
        lettres_tapees.setText("");
        image.setBackgroundResource(R.drawable.first);
        container.removeAllViews();
        listOfLetters=new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            TextView oneLetter = (TextView) getLayoutInflater().inflate(R.layout.textview, null);
            container.addView(oneLetter);
        }
    }

    @Override
    public void onClick(View v) {
        //    Toast.makeText(getApplicationContext(), "YO", Toast.LENGTH_SHORT).show();
        String lettreFromInput = letter.getText().toString().toUpperCase();
        letter.setText("");
        if (lettreFromInput.length() > 0) {
            if (!letterAlreadyUsed(lettreFromInput.charAt(0), listOfLetters)) {
                listOfLetters.add(lettreFromInput.charAt(0));
                checkIfLetterIsInWord(lettreFromInput, word);
            }
            if (found == word.length()) {
                win = true;
                createDialog(win);
            }
            if (!word.contains(lettreFromInput)) {
                error++;
            }
            setImage(error);
            if (error == 6) {
                win = false;
                createDialog(win);
            }
            showLetters(listOfLetters);
        }
    }

    public boolean letterAlreadyUsed(char a, List<Character> listOfLetters) {
        for (int i = 0; i < listOfLetters.size(); i++) {
            if (listOfLetters.get(i) == a) {
                Toast.makeText(getApplicationContext(), "Vous avez deja rentré cette lettre", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    public void checkIfLetterIsInWord(String letter, String word) {
        for (int i = 0; i < word.length(); i++) {
            if (letter.equals(String.valueOf(word.charAt(i)))) {
                TextView tv = (TextView) container.getChildAt(i);
                tv.setText(String.valueOf(word.charAt(i)));
                found++;
            }
        }
    }

    public void showLetters(List<Character> listOfLetters) {
        String chaine = "";
        for (int i = 0; i < listOfLetters.size(); i++) {
            chaine += listOfLetters.get(i) + "\n";
        }
        if (!chaine.equals("")) {
            lettres_tapees.setText(chaine);
        }
    }

    public void setImage(int error) {
        switch (error) {
            case 1:
                image.setBackgroundResource(R.drawable.second);
                break;
            case 2:
                image.setBackgroundResource(R.drawable.third);
                break;
            case 3:
                image.setBackgroundResource(R.drawable.fourth);
                break;
            case 4:
                image.setBackgroundResource(R.drawable.fifth);
                break;
            case 5:
                image.setBackgroundResource(R.drawable.sixth);
                break;
            case 6:
                image.setBackgroundResource(R.drawable.seventh);
                break;
        }
    }

    public void createDialog(boolean win) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (!win) {
            builder.setTitle("Vous avez perdu !");
            builder.setMessage("Le mot à trouver était :  " + word);
        }
        builder.setPositiveButton(getResources().getString(R.string.replay), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initGame();
            }
        });
        builder.create().show();


    }
    public List<String> getList()
    {
        try{

            BufferedReader buffer= new BufferedReader(new InputStreamReader(getAssets().open("pendu_liste.txt")) );
            String line;
            while ((line=buffer.readLine()) !=null)
            {
                wordList.add(line);
            }
            buffer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return wordList;
    }
    public String generateWord()
    {
        wordList=getList();
        int random=(int) (Math.floor(Math.random() * wordList.size()));
        String word = wordList.get(random).trim();
        return word;
    }
}