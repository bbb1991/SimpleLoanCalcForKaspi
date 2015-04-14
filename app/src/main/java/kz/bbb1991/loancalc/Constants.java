package kz.bbb1991.loancalc;

public interface Constants {

    // шаги округления, например нельзя оформить сумму 100 002 или там 666 666
    public static int       STEP_FOR_TERM           = 3;
    public static int       STEP_FOR_AMOUNT         = 10_000;

    // условие кредитование КН15 (сумма до 300 000 включительно)
    public static final int     MIN_TERM_FOR_KN15   = 3;
    public static final int     MAX_TERM_FOR_KN15   = 24;
    public static final int     MIN_SUM_FOR_KN15    = 20_000;
    public static final int     MAX_SUM_FOR_KN15    = 300_000;
    public static final float   RATE_FOR_KN15       = 26.99F;
    public static final float   RATE_FOR_LK_KN15    = 23.99F;
    public static final float   SERVICE_FOR_KN15    = 2.19F;

    // условие кредитования КН (сумма больше 300 000)
    public static final int     MIN_TERM_FOR_KN     = 6;
    public static final int     MAX_TERM_FOR_KN     = 60;
    public static final int     MAX_SUM_FOR_KN      = 1_000_000;
    public static final float   RATE_FOR_KN         = 20.99F;
    public static final float   RATE_FOR_LK_KN      = 17.99F;
    public static final float   SERVICE_FOR_KN_6_24 = 1.32F;
    public static final float   SERVICE_FOR_KN_27_48 = 1.42F;
    public static final float   SERVICE_FOR_KN_51_60 = 1.58F;

}
