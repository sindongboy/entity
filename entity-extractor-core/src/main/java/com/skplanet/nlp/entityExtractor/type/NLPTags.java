package com.skplanet.nlp.entityExtractor.type;

/**
 *
 */
public final class NLPTags {

    // 체언 품사 정리

    /**
     * 명사류 식별자입니다.
     */
    public static final String NN = "nn";

    /**
     * 일반명사 식별자입니다.
     */
    public static final String NNG = "nng";

    /**
     * 고유명사 식별자입니다.
     */
    public static final String NNP = "nnp";

    /**
     * 의존명사 식별자입니다.
     */
    public static final String NNB = "nnb";

    /**
     * 인명명사 식별자입니다.
     */
    public static final String NNK = "nnk";

    /**
     * 복합명사 식별자입니다.
     */
    public static final String NCP = "ncp";

    /**
     * 대명사 식별자입니다.
     */
    public static final String NP = "np";

    /**
     * 수사 식별자입니다.
     */
    public static final String NR = "nr";

    /**
     * 영문 식별자입니다.
     */
    public static final String ENG = "eng";

    // 용언 품사 정리

    /**
     * 동사 식별자입니다.
     */
    public static final String VV = "vv";

    /**
     * 형용사 식별자입니다.
     */
    public static final String VA = "va";

    /**
     * 보조용언 식별자입니다.
     */
    public static final String VX = "vx";

    /**
     * 긍정지정사 식별자입니다.
     */
    public static final String VCP = "vcp";

    /**
     * 긍정보조용언 식별자입니다.
     */
    public static final String VXP = "vxp";

    /**
     * 부정보조용언 식별자입니다.
     */
    public static final String VXN = "vxn";

    /**
     * 연결형보조용언 식별자입니다.
     */
    public static final String VXC = "vxc";

    // 수식언 품사 정리

    /**
     * 관형사 식별자입니다.
     */
    public static final String MM = "mm";

    /**
     * 부사 식별자입니다.
     */
    public static final String MA = "ma";

    /**
     *
     */
    public static final String MAR = "mar";

    /**
     * 일반부사 식별자입니다.
     */
    public static final String MAG = "mag";

    /**
     * 접속부사 식별자입니다.
     */
    public static final String MAJ = "maj";

    /**
     * 정도부사 극성 상향형 식별자입니다.
     */
    public static final String MAplus = "ma+";

    /**
     * 정도부사 극성 하향형 식별자입니다.
     */
    public static final String MAminus = "ma-";

    // 관계언 품사 정리

    /**
     * 격조사 식별자입니다.
     */
    public static final String JK = "jk";

    /**
     * 주격조사 식별자입니다.
     */
    public static final String JKS = "jks";

    /**
     * 보격조사 식별자입니다.
     */
    public static final String JKC = "jkc";

    /**
     * 관형격조사 식별자입니다.
     */
    public static final String JKG = "jkg";

    /**
     * 목적격조사 식별자입니다.
     */
    public static final String JKO = "jko";

    /**
     * 부사격조사 식별자입니다.
     */
    public static final String JKB = "jkb";

    /**
     * 호격조사 식별자입니다.
     */
    public static final String JKV = "jkv";

    /**
     * 인용격조사 식별자입니다.
     */
    public static final String JKQ = "jkq";

    /**
     * 보조사 식별자입니다.
     */
    public static final String JX = "jx";

    /**
     * 접속조사 식별자입니다.
     */
    public static final String JC = "jc";

    // 의존형태 품사 정리

    /**
     * 선어말어미 식별자입니다.
     */
    public static final String EP = "ep";

    /**
     * 종결어미 식별자입니다.
     */
    public static final String EF = "ef";

    /**
     * 연결어미 식별자입니다.
     */
    public static final String EC = "ec";

    /**
     * 의존연결어미 식별자 입니다.
     */
    public static final String ECB = "ecb";

    /**
     * 보격연결어미 식별자입니다.
     */
    public static final String ECC = "ecc";

    /**
     * 관형형연결어미 식별자입니다.
     */
    public static final String ECM = "ecm";

    /**
     * 명사형전성어미 식별자입니다.
     */
    public static final String ETN = "etn";

    /**
     * 관형형전성어미 식별자입니다.
     */
    public static final String ETM = "etm";

    /**
     * 체언접두사 식별자입니다.
     */
    public static final String XPN = "xpn";

    /**
     * 명사파생접미사 식별자입니다.
     */
    public static final String XSN = "xsn";

    /**
     * 동사파생접미사 식별자입니다.
     */
    public static final String XSV = "xsv";

    /**
     * 형용사파생접미사 식별자입니다.
     */
    public static final String XSA = "xsa";

    /**
     * 부사파생접미사 식별자입니다.
     */
    public static final String XSB = "xsb";

    /**
     * 어근 식별자입니다.
     */
    public static final String XR = "xr";

    // 기호 품사 정리

    /**
     * 마침표,물음표,느낌표 식별자입니다.
     */
    public static final String SF = "sf";

    /**
     * 쉼표,가운데접,콜론,빗금 식별자입니다.
     */
    public static final String SP = "sp";

    /**
     * 따옴표,괄호표,줄표 식별자입니다.
     */
    public static final String SS = "ss";

    /**
     * 줄임표 식별자입니다.
     */
    public static final String SE = "se";

    /**
     * 붙임표(물결,숨김,빠짐) 식별자입니다.
     */
    public static final String SO = "so";

    /**
     * 외국어 식별자입니다.
     */
    public static final String SL = "sl";

    /**
     * 한자 식별자입니다.
     */
    public static final String SH = "sh";

    /**
     * 기타기호 식별자입니다.
     */
    public static final String SW = "sw";

    /**
     * 숙자 식별자입니다.
     */
    public static final String SN = "sn";

    /**
     * 알수없는 품사 식별자입니다.
     */
    public static final String UNK = "unk";

    /**
     *  정도부사 '너무' 와 극성 '0' 이 만났을 경우 극성을 낮추기 위한 용도의 식별자입니다.
     */
    public static final String StringMAminus = "너무";

    /**
     * "지않"과 "지는않" 부정의 차이를 추가하기 위해서 임시로 "지는않" 부정의 경우 phrase tag 에 'x' 를 append 시키는 식별자입니다.
     */
    public static final String StringChunkNegationKeyword = "지는않";

    /**
     * "지는않" 부정과 마찮가지로 "진않" 부정의 경우 phrase tag 에 'x' 를 append 시키는 식별자입니다.
     */
    public static final String StringChunkNegationKeywordJINAN = "진않";

    /**
     *
     */
    public static final String StringEfKeyword = "다";

    /**
     *
     */
    public static final String StringEui	   = "의";

    /**
     *
     */
    public static final String StringMit	   = "및";

    /**
     *
     */
    public static final String StringANi	   = "아니";

    /**
     *
     */
    public static final String StringNeun	   = "는";

    /**
     *
     */
    public static final String StringEun	   = "은";

    /**
     *
     */
    public static final String StringN	   	   = "ㄴ";

    /**
     *
     */
    public static final String StringKae	   = "게";

    /**
     *
     */
    public static final String StringSeo	   = "서";

    /**
     *
     */
    public static final String StringEop	   = "없";

    /**
     *
     */
    public static final String HanAn	   	   = "안";

    /**
     *
     */
    public static final String HanMot	   	   = "못";

    /**
     *
     */
    public static final String HanKoreanLangType = "한국어";

    /**
     *
     */
    public static final String HanGoon = "군";
    public static String SLASH = "/";

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        if ("jk".equals(NLPTags.JK))
            System.out.println("YES");
        else
            System.out.println("NO");

    }

}
