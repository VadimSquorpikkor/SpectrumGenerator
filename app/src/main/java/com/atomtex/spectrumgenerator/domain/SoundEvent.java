package com.atomtex.spectrumgenerator.domain;


/**
 * The enum contains all sound events for application.
 * <p>
 * The sounds SoundEvent are used to voice some events while application work.
 * Each enum contains link to associated sound file and must be init before using.
 *
 * @author stanislav.kleinikov@gmail.com
 */
public enum SoundEvent {

    //The numbers
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    TWENTY,
    THIRTY,
    FORTY,
    FIFTY,
    SIXTY,
    SEVENTY,
    EIGHTY,
    NINETY,
    ONE_HUNDRED,

    //The modes
    SCANNING_MODE,
    SPECTROMETRIC_MODE,
    RESTART_SCANNING,
    IDENTIFICATION_MODE,
    IDENTIFICATION_EXTENDED,
    IDENTIFICATION_COMPLETED,
    BG_MEASUREMENT,
    OUTAGE,

    //The nuclides
    AM_241,
    B_10,
    B_11,
    BA_133,
    BA_140,
    BE_8,
    CD_109,
    CD_115m,
    CE_141,
    CE_144,
    CO_57,
    CO_60,
    CR_51,
    CS_134,
    CS_137,
    EU_152,
    F_18,
    F_9,
    GA_67,
    I_123,
    I_125,
    I_131,
    IN_111,
    IR_192,
    K_40,
    LA_140,
    LI_7,
    MN_54,
    MO_99,
    NA_22,
    NB_95,
    ND_147,
    NP_237,
    O_17,
    PR_144,
    PU_238,
    PU_239,
    PU_RG,
    PU_WG,
    RA_226,
    RH_106,
    RU_103,
    SE_75,
    SR_89,
    SR_90,
    TC_99m,
    TH,
    TH_228,
    TH_232,
    TL_201,
    U_232,
    U_233,
    U_235,
    U_238,
    XE_131m,
    XE_133,
    XE_133m,
    XE_135,
    ZR_95,

    //Nuclide categories
    NATURAL,
    MEDICAL,
    INDUSTRIAL,
    NUCLEAR,
    PROMPT_GAMMA,


    BG_ADAPTIVE_MODE,
    BG_CONSTANT_MODE,
    BG_MEASURED,
    LOW_BATTERY_PDA_LEVEL,
    LOW_BATTERY_ADAPTER_LEVEL,
    GAMMA_DETECTION_UNIT_CONNECTED,
    NO_GAMMA_CONNECTION,
    NEUTRON_DETECTION_UNIT_CONNECTED,
    NO_NEUTRON_CONNECTION,
    HIGH_DOSE_RATE_CHANNEL_CONNECTED,
    NO_HIGH_DOSE_RATE_CHANNEL_CONNECTION,
    GAMMA_OVERLOAD,
    NEUTRON_OVERLOAD,
    RADIATION_ALARM,
    NO_CALIBRATIONS,
    STABILIZATION_COMPLETED,
    STABILIZATION_FAILED,
    OVERLOAD,
    LOW_COUNTS,
    DOSE_GREAT_THAN,
    DOSE_LESS_THAN,
    MICROSIVERT_PER_HOUR,
    GAMMA_RADIATION_DETECTED,
    NEUTRON_THREAD_EXCEEDED,
    NOTHING_IDENTIFIED,
    IDENTIFIED,
    GAMMA_DISABLED,
    NO_GPS_SIGNAL,
    GPS_SIGNAL_RECEIVED,
    JUST_SOUND,
    ATTENTION_NO_GPS,
    UNKNOWN;

    /**
     * The id of associated sound resource
     */
    private int mResId;

    /**
     * The string name of the event
     */
    private String mName;

    /**
     * The time of last voicing of the event.
     * <p>
     * It is necessary to prevent sound from very frequent repeating
     */
    private long mLastTimeUsed;

    public int getResId() {
        return mResId;
    }

    public void setResId(int resId) {
        mResId = resId;
    }

    public long getLastTimeUsed() {
        return mLastTimeUsed;
    }

    public void setLastTimeUsed(long lastTimeUsed) {
        mLastTimeUsed = lastTimeUsed;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setData(int resId, String name) {
        mResId = resId;
        mName = name;
    }
}

