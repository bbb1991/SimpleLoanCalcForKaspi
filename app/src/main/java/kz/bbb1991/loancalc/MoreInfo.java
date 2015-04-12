package kz.bbb1991.loancalc;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

public class MoreInfo extends Activity {

    /**
     * Класс для описания подробной описания платежей по месяцам.
     * То есть: месяц, остаток долга, начисленный процент за месяц и ежемесячный платеж
     */

    private TableLayout terms, balance, accuredInterest, monthlyPayment;
    private int balanceOfDebt = Values.amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_info);



        terms = (TableLayout) findViewById(R.id.moreInfoMonthLayout);
        monthlyPayment = (TableLayout) findViewById(R.id.moreInfoPaymentLayout);
        balance = (TableLayout) findViewById(R.id.moreInfoBalanceLayout);
        accuredInterest = (TableLayout) findViewById(R.id.moreInfoInterestLayout);

        AdView adView = (AdView) findViewById(R.id.adViewMoreInfo);
        adView.loadAd(MainActivity.adRequest);


        fillInfo(Values.term);
    }

    private void fillInfo(int TERM) {
        for (int i = 1; i <= TERM; i++) {
            creatingTextInfo(terms, String.valueOf(i));

            creatingTextInfo(balance, String.valueOf(balanceOfDebt));

            int interest = Math.round(balanceOfDebt * ((Values.rate / 100) / 12) + (Values.amount * (Values.service / 100)));
            creatingTextInfo(accuredInterest, String.valueOf(interest));

            balanceOfDebt -= Values.monthlyPayment - interest;

            creatingTextInfo(monthlyPayment, String.valueOf(Values.monthlyPayment));

        }

        showTotalInfo(terms, "Итого:");
        showTotalInfo(balance, String.valueOf(Values.amount));
        showTotalInfo(accuredInterest, String.valueOf(Values.monthlyPayment * Values.term - Values.amount));
        showTotalInfo(monthlyPayment, String.valueOf(Values.monthlyPayment * Values.term));

    }

    private void creatingTextInfo(TableLayout tableLayout, String data) {
        TextView tv = new TextView(this);
        tv.setText(data);
        tv.setTextSize(15);
        tableLayout.addView(tv);
    }

    private void showTotalInfo(TableLayout layout, String message) {
        TextView textViewTotal = new TextView(this);
        textViewTotal.setText(message);
        textViewTotal.setTextSize(15);
        textViewTotal.setTypeface(null, Typeface.BOLD);
        layout.addView(textViewTotal);
    }


}
