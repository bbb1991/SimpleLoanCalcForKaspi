package kz.bbb1991.loancalc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;


public class ShortInfo extends Activity {




    private TextView infoTerm;
    private TextView infoAmount;
    private TextView infoPayment;
    private TextView infoOverPayment;
    private TextView infoTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.short_info);

        AdView adView = (AdView) findViewById(R.id.adViewShortInfo);
        adView.loadAd(MainActivity.adRequest);

        showInfo();

    }

    // метод для инициализаций значений: "Процентная ставка" и "Банковское обслуживание"


    private void showInfo() {
        infoTerm = (TextView) findViewById(R.id.shortInfoTerm);
        infoTerm.setText(Values.term + " мес.");

        infoAmount = (TextView) findViewById(R.id.shortInfoAmount);
        infoAmount.setText(Values.amount + " тенге.");

        infoPayment = (TextView) findViewById(R.id.shortInfoPayment);
        // Нужно приводить в String, иначе будет выброшено исключение
        // android.content.res.Resources$NotFoundException: String resource ID #0x2770
        infoPayment.setText(String.valueOf(Values.monthlyPayment) + " тенге.");

        infoOverPayment = (TextView) findViewById(R.id.shortInfoOverpayment);
        infoOverPayment.setText(String.valueOf(
                Values.monthlyPayment * Values.term - Values.amount) + " тенге.");

        infoTotal = (TextView) findViewById(R.id.shortInfoTotal);
        infoTotal.setText(String.valueOf(Values.monthlyPayment * Values.term) + " тенге.");
    }
}
