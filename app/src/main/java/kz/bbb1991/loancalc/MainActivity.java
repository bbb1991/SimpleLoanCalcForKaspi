package kz.bbb1991.loancalc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends ActionBarActivity {

    private EditText amount;           // поле для ввода суммы кредитования
    private EditText term;          // поле для ввода суммы кредита
    private CheckBox checkBoxLk;    // Чекбокс для расчета по пониженным ставкам, если клиент имеет статус "Лучший клиент"
    public static AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adRequest = new AdRequest.Builder().build();
        AdView adView = (AdView) findViewById(R.id.adViewMain);
        adView.loadAd(adRequest);

        // инициализация переменных
        amount = (EditText) findViewById(R.id.editTextSum);
        term = (EditText) findViewById(R.id.editTextTerm);
        checkBoxLk = (CheckBox) findViewById(R.id.checkBoxLk);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        // меню "О приложений"
        if (id == R.id.action_about) {

            /*final SpannableString s = new SpannableString(getText(R.string.about_text));
            Linkify.addLinks(s, Linkify.ALL);

            final AlertDialog d = new AlertDialog.Builder(this)
                    .setPositiveButton(android.R.string.ok, null)
                    .setMessage(s)
                    .create();

            d.show();

            ((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
            */

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("О приложений")
                    .setMessage(R.string.about_text)
                    .setPositiveButton("OK", null)
                    .show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // метод, отвечающий за нажатие кнопки
    public void onClick(View view) {

        try {
            // попытка считать с поля ввода данные.
            // если поле пустое или введенное значение больше int выбросится исключение NumberFormatException
            // (интересно, кому вздумается брать кредит на 2 миллиарда?!)
            Values.amount = Integer.parseInt(String.valueOf(amount.getText()));

            // ну хоть убейте, не могу сделать проект без пасхалок...
            if (Values.amount == 666 || Values.amount == 616 ) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Иоанн Богослов, Отк. 13:18, 15:2")
                        .setMessage(
                                "Здесь мудрость. " +
                                "Кто имеет ум, " +
                                "тот сочти число зверя, " +
                                "ибо число это человеческое; " +
                                "число его шестьсот шестьдесят шесть. "
                        ).setPositiveButton("OK", null)
                        .show();
                return;

             // если введенная сумма меньше порога кредитования
            } else if (Values.amount < Values.MIN_SUM_FOR_KN15) {
                amount.setText(String.valueOf(Values.MIN_SUM_FOR_KN15));
                throw new InvalidInput("Неверная сумма. Минимальная сумма кредитования: "
                        + String.valueOf(Values.MIN_SUM_FOR_KN15) + " тенге."
                );
            // если введенная сумма превышает порога кредитования
            } else if (Values.amount > Values.MAX_SUM_FOR_KN) {
                amount.setText(String.valueOf(Values.MAX_SUM_FOR_KN));
                throw new InvalidInput("Неверная сумма. Максимальная сумма кредитования: "
                        + String.valueOf(Values.MAX_SUM_FOR_KN) + " тенге."
                );

            // если все проверки прошли, по требованию банка, шаг суммы кредитования должна быть 10 000 тнг
            } else if (Values.amount % Values.STEP_FOR_AMOUNT != 0) {
                Toast.makeText(
                        this,
                        "Введенная сумма будет округлена до ближайшего значения в соответствий условиям банка",
                        Toast.LENGTH_SHORT)
                        .show();

                // округляем вниз
                if (Values.amount % Values.STEP_FOR_AMOUNT < Values.STEP_FOR_AMOUNT/2) {
                    Values.amount -= Values.amount % Values.STEP_FOR_AMOUNT;
                } else { // округляем вверх
                    Values.amount +=Values.STEP_FOR_AMOUNT - Values.amount % Values.STEP_FOR_AMOUNT;
                }
            }

            // попытка считать срок кредитования.
            // при пустом значений или больше int (глупость какая - срок кредитования 2,147,483,647 мес == 178 956 970 год)
            // будет exception, но, зная наших пользователей - обязательно нужно подстраховться
            Values.term = Integer.parseInt(String.valueOf(term.getText()));

            // так как есть 2 типа кредитования и сроки там разные проверяем
            if (Values.amount <= Values.MAX_SUM_FOR_KN15) { // если меньше или равно максимальной суммы экспресс кредита

                if (Values.term < Values.MIN_TERM_FOR_KN15) { // если введенный срок меньше чем 3 мес
                    term.setText(String.valueOf(Values.MIN_TERM_FOR_KN15));
                    throw new InvalidInput("Неверный срок. Минимальный срок кредитования: "
                            + Values.MIN_TERM_FOR_KN15 + " мес."
                    );
                } else if (Values.term > Values.MAX_TERM_FOR_KN15) { // если введенный срок больше 24 мес
                    term.setText(String.valueOf(Values.MAX_TERM_FOR_KN15));
                    throw new InvalidInput("Неверный срок. Минимальный срок кредитования: "
                            + Values.MAX_TERM_FOR_KN15 + " мес."
                    );
                }
            } else {
                if (Values.term < Values.MIN_TERM_FOR_KN) {
                    term.setText(String.valueOf(Values.MIN_TERM_FOR_KN));
                    throw new InvalidInput("Неверный срок. Минимальный срок кредитования: "
                            + Values.MIN_TERM_FOR_KN + " мес."
                    );
                } else if (Values.term > Values.MAX_TERM_FOR_KN) {
                    term.setText(String.valueOf(Values.MAX_TERM_FOR_KN));
                    throw new InvalidInput("Неверный срок. Минимальный срок кредитования: "
                            + Values.MAX_TERM_FOR_KN + " мес."
                    );
                }
            }

            if (Values.term % Values.STEP_FOR_TERM != 0) {
                Toast.makeText(
                        this,
                        "Введенный срок будет округлена до ближайшего значения в соответствий условиям банка",
                        Toast.LENGTH_LONG)
                        .show();

                if (Values.term % Values.STEP_FOR_AMOUNT < 5) {
                    Values.term -= Values.term % Values.STEP_FOR_TERM;
                } else {
                    Values.term +=Values.STEP_FOR_TERM - Values.term % Values.STEP_FOR_TERM;
                }
            }

            Values.isUserLk = checkBoxLk.isChecked();


            Intent intent = new Intent(this, ResultTest.class);
            startActivity(intent);

        } catch (NumberFormatException e) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Ошибка!")
                    .setMessage("Некорректный ввод данных!")
                    .setPositiveButton("OK", null)
                    .show();

            e.printStackTrace();

        } catch (InvalidInput invalidInput) {
            invalidInput.printStackTrace();
        }
    }


    class InvalidInput extends Exception {
        public InvalidInput(String message) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Ошибка!")
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .show();
        }

    }

}
