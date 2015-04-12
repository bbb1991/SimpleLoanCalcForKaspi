package kz.bbb1991.loancalc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

public class OtherTerms extends Activity {

    private TableLayout terms, monthlyPayment, overpayment, total;

    private float service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_terms);

        AdView adView = (AdView) findViewById(R.id.adViewOtherTerms);
        adView.loadAd(MainActivity.adRequest);

        terms = (TableLayout) findViewById(R.id.otherInfoTermsLayout);
        monthlyPayment = (TableLayout) findViewById(R.id.otherInfoMonthlyPayment);
        overpayment = (TableLayout) findViewById(R.id.otherInfoOverpayment);
        total = (TableLayout) findViewById(R.id.otherInfoTotal);


        if (Values.amount <= Values.MAX_SUM_FOR_KN15) {
            service = Values.SERVICE_FOR_KN15;
            fillOtherInfo(Values.MIN_TERM_FOR_KN15, Values.MAX_TERM_FOR_KN15);

        } else {
            service = Values.SERVICE_FOR_KN_6_24;
            fillOtherInfo(Values.MIN_TERM_FOR_KN, Values.MAX_TERM_FOR_KN);

        }
    }

    private void fillOtherInfo(int i, int maxTerm) {
        for (; i <= maxTerm; i += Values.STEP_FOR_TERM) {


            if (i == 27) {
                service = Values.SERVICE_FOR_KN_27_48;
            } else if (i == 51) {

                service = Values.SERVICE_FOR_KN_51_60;
            }

            // Ежемесячные платежи по разным срокам
            int payment = Calculate.calcMonthlyFee(i, Values.amount, Values.rate, service);

            creatingTextInfo(terms, String.valueOf(i));
            creatingTextInfo(monthlyPayment, String.valueOf(payment));
            creatingTextInfo(overpayment, String.valueOf(payment * i - Values.amount));
            creatingTextInfo(total, String.valueOf(payment * i));

        }
    }

    private void creatingTextInfo(TableLayout tableLayout, String data) {
        TextView tv = new TextView(this);
        tv.setText(data);
        tv.setTextSize(15);
        tableLayout.addView(tv);
    }
}
