package kz.bbb1991.loancalc;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class ResultTest extends TabActivity implements Constants {

    Intent shortInfoIntern, moreInfoIntent, otherTermsIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.result_layout);

        shortInfoIntern = new Intent(this, ShortInfo.class);
        moreInfoIntent = new Intent(this, MoreInfo.class);
        otherTermsIntent = new Intent(this, OtherTerms.class);

        initializeRateAndService();
        Values.monthlyPayment = Calculate.calcMonthlyFee(Values.term, Values.amount, Values.rate, Values.service);



        TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec;

        // Tab для краткой инфы
        spec = tabs.newTabSpec("tab1");
        spec.setContent(shortInfoIntern);
        spec.setIndicator("Краткая информация");
        tabs.addTab(spec);

        // Tab для подробного графика платежей
        spec = tabs.newTabSpec("tab2");
        spec.setContent(moreInfoIntent);
        spec.setIndicator("Детальная информация");
        tabs.addTab(spec);

        // Tab для вывода других сроков
        spec = tabs.newTabSpec("tab3");

        spec.setContent(otherTermsIntent);

        spec.setIndicator("Другие сроки");
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

    }

    private void initializeRateAndService() {

        // если введенная сумма меньше или равно 300 000
        if (Values.amount <= MAX_SUM_FOR_KN15) {

            // если является лучшим клиентом, считаем по пониженным ставкам, иначе обычной ставке КН15
            if (Values.isUserLk) {
                Values.rate = RATE_FOR_LK_KN15;
            } else {
                Values.rate = RATE_FOR_KN15;

            }
            Values.service = SERVICE_FOR_KN15;

            // если сумма кредита больше или равно 310 000
        } else {
            if (Values.isUserLk) {  // если лучший клиент банка
                Values.rate = RATE_FOR_LK_KN;
            } else {
                Values.rate = RATE_FOR_KN;
            }

            // банковское обслуживание согласно срокам
            if (Values.term < 27) {
                Values.service = SERVICE_FOR_KN_6_24;
            } else if (Values.term < 51) {
                Values.service = SERVICE_FOR_KN_27_48;
            } else {
                Values.service = SERVICE_FOR_KN_51_60;
            }
        }
    }
}
