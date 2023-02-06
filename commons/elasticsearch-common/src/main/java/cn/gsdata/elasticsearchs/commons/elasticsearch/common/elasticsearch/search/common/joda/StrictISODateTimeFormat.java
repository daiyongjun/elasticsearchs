package cn.gsdata.elasticsearchs.commons.elasticsearch.common.elasticsearch.search.common.joda;

import org.joda.time.DateTimeFieldType;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

/**
 * 基础包joda-time 2.9.5 下补充类
 *
 * @author daiyongjun
 */
public class StrictISODateTimeFormat {

    public static DateTimeFormatter date() {
        return yearMonthDay();
    }

    public static DateTimeFormatter yearMonthDay() {
        return Constants.ymd;
    }

    public static DateTimeFormatter weekDate() {
        return Constants.wwd;
    }

    public static DateTimeFormatter hour() {
        return Constants.hde;
    }

    public static DateTimeFormatter basicWeekDate() {
        return Constants.bwd;
    }

    public static DateTimeFormatter basicWeekDateTime() {
        return Constants.bwdt;
    }

    public static DateTimeFormatter basicWeekDateTimeNoMillis() {
        return Constants.bwdtx;
    }

    public static DateTimeFormatter dateHour() {
        return Constants.dh;
    }

    public static DateTimeFormatter dateHourMinute() {
        return Constants.dhm;
    }

    public static DateTimeFormatter dateHourMinuteSecond() {
        return Constants.dhms;
    }

    public static DateTimeFormatter dateHourMinuteSecondFraction() {
        return Constants.dhmsf;
    }

    public static DateTimeFormatter dateHourMinuteSecondMillis() {
        return Constants.dhmsl;
    }

    public static DateTimeFormatter dateOptionalTimeParser() {
        return Constants.dotp;
    }

    public static DateTimeFormatter dateTime() {
        return Constants.dt;
    }

    public static DateTimeFormatter dateTimeNoMillis() {
        return Constants.dtx;
    }

    public static DateTimeFormatter hourMinute() {
        return Constants.hm;
    }

    public static DateTimeFormatter hourMinuteSecond() {
        return Constants.hms;
    }

    public static DateTimeFormatter hourMinuteSecondFraction() {
        return Constants.hmsf;
    }

    public static DateTimeFormatter hourMinuteSecondMillis() {
        return Constants.hmsl;
    }

    public static DateTimeFormatter ordinalDate() {
        return Constants.od;
    }


    public static DateTimeFormatter ordinalDateTime() {
        return Constants.odt;
    }

    public static DateTimeFormatter ordinalDateTimeNoMillis() {
        return Constants.odtx;
    }

    public static DateTimeFormatter time() {
        return Constants.t;
    }

    public static DateTimeFormatter timeNoMillis() {
        return Constants.tx;
    }

    public static DateTimeFormatter tTimeNoMillis() {
        return Constants.ttx;
    }

    public static DateTimeFormatter weekDateTime() {
        return Constants.wdt;
    }

    public static DateTimeFormatter weekDateTimeNoMillis() {
        return Constants.wdtx;
    }

    public static DateTimeFormatter tTime() {
        return Constants.tt;
    }

    public static DateTimeFormatter weekyear() {
        return Constants.we;
    }

    public static DateTimeFormatter weekyearWeek() {
        return Constants.ww;
    }

    public static DateTimeFormatter weekyearWeekDay() {
        return Constants.wwd;
    }

    public static DateTimeFormatter year() {
        return Constants.ye;
    }

    public static DateTimeFormatter yearMonth() {
        return Constants.ym;
    }



    static final class Constants {
        private static final DateTimeFormatter
                ye = yearElement(),  // year element (yyyy)
                mye = monthElement(), // monthOfYear element (-MM)
                dme = dayOfMonthElement(), // dayOfMonth element (-dd)
                we = weekyearElement(),  // weekyear element (xxxx)
                wwe = weekElement(), // weekOfWeekyear element (-ww)
                dwe = dayOfWeekElement(), // dayOfWeek element (-ee)
                dye = dayOfYearElement(), // dayOfYear element (-DDD)
                hde = hourElement(), // hourOfDay element (HH)
                mhe = minuteElement(), // minuteOfHour element (:mm)
                sme = secondElement(), // secondOfMinute element (:ss)
                fse = fractionElement(), // fractionOfSecond element (.SSSSSSSSS)
                ze = offsetElement(),  // zone offset element
                lte = literalTElement(), // literal 'T' element

        //y,   // year (same as year element)
        ym = yearMonth(),  // year month
                ymd = yearMonthDay(), // year month day

        //w,   // weekyear (same as weekyear element)
        ww = weekyearWeek(),  // weekyear week
                wwd = weekyearWeekDay(), // weekyear week day

        //h,    // hour (same as hour element)
        hm = hourMinute(),   // hour minute
                hms = hourMinuteSecond(),  // hour minute second
                hmsl = hourMinuteSecondMillis(), // hour minute second millis
                hmsf = hourMinuteSecondFraction(), // hour minute second fraction

        dh = dateHour(),    // date hour
                dhm = dateHourMinute(),   // date hour minute
                dhms = dateHourMinuteSecond(),  // date hour minute second
                dhmsl = dateHourMinuteSecondMillis(), // date hour minute second millis
                dhmsf = dateHourMinuteSecondFraction(), // date hour minute second fraction

        //d,  // date (same as ymd)
        t = time(),  // time
                tx = timeNoMillis(),  // time no millis
                tt = tTime(),  // Ttime
                ttx = tTimeNoMillis(),  // Ttime no millis
                dt = dateTime(), // date time
                dtx = dateTimeNoMillis(), // date time no millis

        //wd,  // week date (same as wwd)
        wdt = weekDateTime(), // week date time
                wdtx = weekDateTimeNoMillis(), // week date time no millis

        od = ordinalDate(),  // ordinal date (same as yd)
                odt = ordinalDateTime(), // ordinal date time
                odtx = ordinalDateTimeNoMillis(), // ordinal date time no millis

        bd = basicDate(),  // basic date
                bt = basicTime(),  // basic time
                btx = basicTimeNoMillis(),  // basic time no millis
                btt = basicTTime(), // basic Ttime
                bttx = basicTTimeNoMillis(), // basic Ttime no millis
                bdt = basicDateTime(), // basic date time
                bdtx = basicDateTimeNoMillis(), // basic date time no millis

        bod = basicOrdinalDate(),  // basic ordinal date
                bodt = basicOrdinalDateTime(), // basic ordinal date time
                bodtx = basicOrdinalDateTimeNoMillis(), // basic ordinal date time no millis

        bwd = basicWeekDate(),  // basic week date
                bwdt = basicWeekDateTime(), // basic week date time
                bwdtx = basicWeekDateTimeNoMillis(), // basic week date time no millis

        dpe = dateElementParser(), // date parser element
                tpe = timeElementParser(), // time parser element
                dp = dateParser(),  // date parser
                ldp = localDateParser(), // local date parser
                tp = timeParser(),  // time parser
                ltp = localTimeParser(), // local time parser
                dtp = dateTimeParser(), // date time parser
                dotp = dateOptionalTimeParser(), // date optional time parser
                ldotp = localDateOptionalTimeParser(); // local date optional time parser

        //-----------------------------------------------------------------------
        private static DateTimeFormatter dateParser() {
            if (dp == null) {
                DateTimeParser tOffset = new DateTimeFormatterBuilder()
                        .appendLiteral('T')
                        .append(offsetElement()).toParser();
                return new DateTimeFormatterBuilder()
                        .append(dateElementParser())
                        .appendOptional(tOffset)
                        .toFormatter();
            }
            return dp;
        }

        private static DateTimeFormatter localDateParser() {
            if (ldp == null) {
                return dateElementParser().withZoneUTC();
            }
            return ldp;
        }

        private static DateTimeFormatter dateElementParser() {
            if (dpe == null) {
                return new DateTimeFormatterBuilder()
                        .append(null, new DateTimeParser[]{
                                new DateTimeFormatterBuilder()
                                        .append(yearElement())
                                        .appendOptional
                                                (new DateTimeFormatterBuilder()
                                                        .append(monthElement())
                                                        .appendOptional(dayOfMonthElement().getParser())
                                                        .toParser())
                                        .toParser(),
                                new DateTimeFormatterBuilder()
                                        .append(weekyearElement())
                                        .append(weekElement())
                                        .appendOptional(dayOfWeekElement().getParser())
                                        .toParser(),
                                new DateTimeFormatterBuilder()
                                        .append(yearElement())
                                        .append(dayOfYearElement())
                                        .toParser()
                        })
                        .toFormatter();
            }
            return dpe;
        }

        private static DateTimeFormatter timeParser() {
            if (tp == null) {
                return new DateTimeFormatterBuilder()
                        .appendOptional(literalTElement().getParser())
                        .append(timeElementParser())
                        .appendOptional(offsetElement().getParser())
                        .toFormatter();
            }
            return tp;
        }

        private static DateTimeFormatter localTimeParser() {
            if (ltp == null) {
                return new DateTimeFormatterBuilder()
                        .appendOptional(literalTElement().getParser())
                        .append(timeElementParser())
                        .toFormatter().withZoneUTC();
            }
            return ltp;
        }

        private static DateTimeFormatter timeElementParser() {
            if (tpe == null) {
                // Decimal point can be either '.' or ','
                DateTimeParser decimalPoint = new DateTimeFormatterBuilder()
                        .append(null, new DateTimeParser[]{
                                new DateTimeFormatterBuilder()
                                        .appendLiteral('.')
                                        .toParser(),
                                new DateTimeFormatterBuilder()
                                        .appendLiteral(',')
                                        .toParser()
                        })
                        .toParser();

                return new DateTimeFormatterBuilder()
                        // time-element
                        .append(hourElement())
                        .append
                                (null, new DateTimeParser[]{
                                        new DateTimeFormatterBuilder()
                                                // minute-element
                                                .append(minuteElement())
                                                .append
                                                        (null, new DateTimeParser[]{
                                                                new DateTimeFormatterBuilder()
                                                                        // second-element
                                                                        .append(secondElement())
                                                                        // second fraction
                                                                        .appendOptional(new DateTimeFormatterBuilder()
                                                                                .append(decimalPoint)
                                                                                .appendFractionOfSecond(1, 9)
                                                                                .toParser())
                                                                        .toParser(),
                                                                // minute fraction
                                                                new DateTimeFormatterBuilder()
                                                                        .append(decimalPoint)
                                                                        .appendFractionOfMinute(1, 9)
                                                                        .toParser(),
                                                                null
                                                        })
                                                .toParser(),
                                        // hour fraction
                                        new DateTimeFormatterBuilder()
                                                .append(decimalPoint)
                                                .appendFractionOfHour(1, 9)
                                                .toParser(),
                                        null
                                })
                        .toFormatter();
            }
            return tpe;
        }

        private static DateTimeFormatter dateTimeParser() {
            if (dtp == null) {
                // This is different from the general time parser in that the 'T'
                // is required.
                DateTimeParser time = new DateTimeFormatterBuilder()
                        .appendLiteral('T')
                        .append(timeElementParser())
                        .appendOptional(offsetElement().getParser())
                        .toParser();
                return new DateTimeFormatterBuilder()
                        .append(null, new DateTimeParser[]{time, dateOptionalTimeParser().getParser()})
                        .toFormatter();
            }
            return dtp;
        }

        private static DateTimeFormatter dateOptionalTimeParser() {
            if (dotp == null) {
                DateTimeParser timeOrOffset = new DateTimeFormatterBuilder()
                        .appendLiteral('T')
                        .appendOptional(timeElementParser().getParser())
                        .appendOptional(offsetElement().getParser())
                        .toParser();
                return new DateTimeFormatterBuilder()
                        .append(dateElementParser())
                        .appendOptional(timeOrOffset)
                        .toFormatter();
            }
            return dotp;
        }

        private static DateTimeFormatter localDateOptionalTimeParser() {
            if (ldotp == null) {
                DateTimeParser time = new DateTimeFormatterBuilder()
                        .appendLiteral('T')
                        .append(timeElementParser())
                        .toParser();
                return new DateTimeFormatterBuilder()
                        .append(dateElementParser())
                        .appendOptional(time)
                        .toFormatter().withZoneUTC();
            }
            return ldotp;
        }

        //-----------------------------------------------------------------------
        private static DateTimeFormatter time() {
            if (t == null) {
                return new DateTimeFormatterBuilder()
                        .append(hourMinuteSecondFraction())
                        .append(offsetElement())
                        .toFormatter();
            }
            return t;
        }

        private static DateTimeFormatter timeNoMillis() {
            if (tx == null) {
                return new DateTimeFormatterBuilder()
                        .append(hourMinuteSecond())
                        .append(offsetElement())
                        .toFormatter();
            }
            return tx;
        }

        private static DateTimeFormatter tTime() {
            if (tt == null) {
                return new DateTimeFormatterBuilder()
                        .append(literalTElement())
                        .append(time())
                        .toFormatter();
            }
            return tt;
        }

        private static DateTimeFormatter tTimeNoMillis() {
            if (ttx == null) {
                return new DateTimeFormatterBuilder()
                        .append(literalTElement())
                        .append(timeNoMillis())
                        .toFormatter();
            }
            return ttx;
        }

        private static DateTimeFormatter dateTime() {
            if (dt == null) {
                return new DateTimeFormatterBuilder()
                        .append(date())
                        .append(tTime())
                        .toFormatter();
            }
            return dt;
        }

        private static DateTimeFormatter dateTimeNoMillis() {
            if (dtx == null) {
                return new DateTimeFormatterBuilder()
                        .append(date())
                        .append(tTimeNoMillis())
                        .toFormatter();
            }
            return dtx;
        }

        private static DateTimeFormatter ordinalDate() {
            if (od == null) {
                return new DateTimeFormatterBuilder()
                        .append(yearElement())
                        .append(dayOfYearElement())
                        .toFormatter();
            }
            return od;
        }

        private static DateTimeFormatter ordinalDateTime() {
            if (odt == null) {
                return new DateTimeFormatterBuilder()
                        .append(ordinalDate())
                        .append(tTime())
                        .toFormatter();
            }
            return odt;
        }

        private static DateTimeFormatter ordinalDateTimeNoMillis() {
            if (odtx == null) {
                return new DateTimeFormatterBuilder()
                        .append(ordinalDate())
                        .append(tTimeNoMillis())
                        .toFormatter();
            }
            return odtx;
        }

        private static DateTimeFormatter weekDateTime() {
            if (wdt == null) {
                return new DateTimeFormatterBuilder()
                        .append(weekDate())
                        .append(tTime())
                        .toFormatter();
            }
            return wdt;
        }

        private static DateTimeFormatter weekDateTimeNoMillis() {
            if (wdtx == null) {
                return new DateTimeFormatterBuilder()
                        .append(weekDate())
                        .append(tTimeNoMillis())
                        .toFormatter();
            }
            return wdtx;
        }

        //-----------------------------------------------------------------------
        private static DateTimeFormatter basicDate() {
            if (bd == null) {
                return new DateTimeFormatterBuilder()
                        .appendYear(4, 4)
                        .appendFixedDecimal(DateTimeFieldType.monthOfYear(), 2)
                        .appendFixedDecimal(DateTimeFieldType.dayOfMonth(), 2)
                        .toFormatter();
            }
            return bd;
        }

        private static DateTimeFormatter basicTime() {
            if (bt == null) {
                return new DateTimeFormatterBuilder()
                        .appendFixedDecimal(DateTimeFieldType.hourOfDay(), 2)
                        .appendFixedDecimal(DateTimeFieldType.minuteOfHour(), 2)
                        .appendFixedDecimal(DateTimeFieldType.secondOfMinute(), 2)
                        .appendLiteral('.')
                        .appendFractionOfSecond(3, 9)
                        .appendTimeZoneOffset("Z", false, 2, 2)
                        .toFormatter();
            }
            return bt;
        }

        private static DateTimeFormatter basicTimeNoMillis() {
            if (btx == null) {
                return new DateTimeFormatterBuilder()
                        .appendFixedDecimal(DateTimeFieldType.hourOfDay(), 2)
                        .appendFixedDecimal(DateTimeFieldType.minuteOfHour(), 2)
                        .appendFixedDecimal(DateTimeFieldType.secondOfMinute(), 2)
                        .appendTimeZoneOffset("Z", false, 2, 2)
                        .toFormatter();
            }
            return btx;
        }

        private static DateTimeFormatter basicTTime() {
            if (btt == null) {
                return new DateTimeFormatterBuilder()
                        .append(literalTElement())
                        .append(basicTime())
                        .toFormatter();
            }
            return btt;
        }

        private static DateTimeFormatter basicTTimeNoMillis() {
            if (bttx == null) {
                return new DateTimeFormatterBuilder()
                        .append(literalTElement())
                        .append(basicTimeNoMillis())
                        .toFormatter();
            }
            return bttx;
        }

        private static DateTimeFormatter basicDateTime() {
            if (bdt == null) {
                return new DateTimeFormatterBuilder()
                        .append(basicDate())
                        .append(basicTTime())
                        .toFormatter();
            }
            return bdt;
        }

        private static DateTimeFormatter basicDateTimeNoMillis() {
            if (bdtx == null) {
                return new DateTimeFormatterBuilder()
                        .append(basicDate())
                        .append(basicTTimeNoMillis())
                        .toFormatter();
            }
            return bdtx;
        }

        private static DateTimeFormatter basicOrdinalDate() {
            if (bod == null) {
                return new DateTimeFormatterBuilder()
                        .appendYear(4, 4)
                        .appendFixedDecimal(DateTimeFieldType.dayOfYear(), 3)
                        .toFormatter();
            }
            return bod;
        }

        private static DateTimeFormatter basicOrdinalDateTime() {
            if (bodt == null) {
                return new DateTimeFormatterBuilder()
                        .append(basicOrdinalDate())
                        .append(basicTTime())
                        .toFormatter();
            }
            return bodt;
        }

        private static DateTimeFormatter basicOrdinalDateTimeNoMillis() {
            if (bodtx == null) {
                return new DateTimeFormatterBuilder()
                        .append(basicOrdinalDate())
                        .append(basicTTimeNoMillis())
                        .toFormatter();
            }
            return bodtx;
        }

        private static DateTimeFormatter basicWeekDate() {
            if (bwd == null) {
                return new DateTimeFormatterBuilder()
                        .appendFixedSignedDecimal(DateTimeFieldType.weekyear(), 4) // ES change, was .appendWeekyear(4, 4)
                        .appendLiteral('W')
                        .appendFixedDecimal(DateTimeFieldType.weekOfWeekyear(), 2)
                        .appendFixedDecimal(DateTimeFieldType.dayOfWeek(), 1)
                        .toFormatter();
            }
            return bwd;
        }

        private static DateTimeFormatter basicWeekDateTime() {
            if (bwdt == null) {
                return new DateTimeFormatterBuilder()
                        .append(basicWeekDate())
                        .append(basicTTime())
                        .toFormatter();
            }
            return bwdt;
        }

        private static DateTimeFormatter basicWeekDateTimeNoMillis() {
            if (bwdtx == null) {
                return new DateTimeFormatterBuilder()
                        .append(basicWeekDate())
                        .append(basicTTimeNoMillis())
                        .toFormatter();
            }
            return bwdtx;
        }

        //-----------------------------------------------------------------------
        private static DateTimeFormatter yearMonth() {
            if (ym == null) {
                return new DateTimeFormatterBuilder()
                        .append(yearElement())
                        .append(monthElement())
                        .toFormatter();
            }
            return ym;
        }

        private static DateTimeFormatter yearMonthDay() {
            if (ymd == null) {
                return new DateTimeFormatterBuilder()
                        .append(yearElement())
                        .append(monthElement())
                        .append(dayOfMonthElement())
                        .toFormatter();
            }
            return ymd;
        }

        private static DateTimeFormatter weekyearWeek() {
            if (ww == null) {
                return new DateTimeFormatterBuilder()
                        .append(weekyearElement())
                        .append(weekElement())
                        .toFormatter();
            }
            return ww;
        }

        private static DateTimeFormatter weekyearWeekDay() {
            if (wwd == null) {
                return new DateTimeFormatterBuilder()
                        .append(weekyearElement())
                        .append(weekElement())
                        .append(dayOfWeekElement())
                        .toFormatter();
            }
            return wwd;
        }

        private static DateTimeFormatter hourMinute() {
            if (hm == null) {
                return new DateTimeFormatterBuilder()
                        .append(hourElement())
                        .append(minuteElement())
                        .toFormatter();
            }
            return hm;
        }

        private static DateTimeFormatter hourMinuteSecond() {
            if (hms == null) {
                return new DateTimeFormatterBuilder()
                        .append(hourElement())
                        .append(minuteElement())
                        .append(secondElement())
                        .toFormatter();
            }
            return hms;
        }

        private static DateTimeFormatter hourMinuteSecondMillis() {
            if (hmsl == null) {
                return new DateTimeFormatterBuilder()
                        .append(hourElement())
                        .append(minuteElement())
                        .append(secondElement())
                        .appendLiteral('.')
                        .appendFractionOfSecond(3, 3)
                        .toFormatter();
            }
            return hmsl;
        }

        private static DateTimeFormatter hourMinuteSecondFraction() {
            if (hmsf == null) {
                return new DateTimeFormatterBuilder()
                        .append(hourElement())
                        .append(minuteElement())
                        .append(secondElement())
                        .append(fractionElement())
                        .toFormatter();
            }
            return hmsf;
        }

        private static DateTimeFormatter dateHour() {
            if (dh == null) {
                return new DateTimeFormatterBuilder()
                        .append(date())
                        .append(literalTElement())
                        .append(hour())
                        .toFormatter();
            }
            return dh;
        }

        private static DateTimeFormatter dateHourMinute() {
            if (dhm == null) {
                return new DateTimeFormatterBuilder()
                        .append(date())
                        .append(literalTElement())
                        .append(hourMinute())
                        .toFormatter();
            }
            return dhm;
        }

        private static DateTimeFormatter dateHourMinuteSecond() {
            if (dhms == null) {
                return new DateTimeFormatterBuilder()
                        .append(date())
                        .append(literalTElement())
                        .append(hourMinuteSecond())
                        .toFormatter();
            }
            return dhms;
        }

        private static DateTimeFormatter dateHourMinuteSecondMillis() {
            if (dhmsl == null) {
                return new DateTimeFormatterBuilder()
                        .append(date())
                        .append(literalTElement())
                        .append(hourMinuteSecondMillis())
                        .toFormatter();
            }
            return dhmsl;
        }

        private static DateTimeFormatter dateHourMinuteSecondFraction() {
            if (dhmsf == null) {
                return new DateTimeFormatterBuilder()
                        .append(date())
                        .append(literalTElement())
                        .append(hourMinuteSecondFraction())
                        .toFormatter();
            }
            return dhmsf;
        }

        //-----------------------------------------------------------------------
        private static DateTimeFormatter yearElement() {
            if (ye == null) {
                return new DateTimeFormatterBuilder()
                        .appendFixedSignedDecimal(DateTimeFieldType.year(), 4) // ES change, was .appendYear(4, 9)
                        .toFormatter();
            }
            return ye;
        }

        private static DateTimeFormatter monthElement() {
            if (mye == null) {
                return new DateTimeFormatterBuilder()
                        .appendLiteral('-')
                        .appendFixedSignedDecimal(DateTimeFieldType.monthOfYear(), 2) // ES change, was .appendMonthOfYear(2)
                        .toFormatter();
            }
            return mye;
        }

        private static DateTimeFormatter dayOfMonthElement() {
            if (dme == null) {
                return new DateTimeFormatterBuilder()
                        .appendLiteral('-')
                        .appendFixedSignedDecimal(DateTimeFieldType.dayOfMonth(), 2) // ES change, was .appendDayOfMonth(2)
                        .toFormatter();
            }
            return dme;
        }

        private static DateTimeFormatter weekyearElement() {
            if (we == null) {
                return new DateTimeFormatterBuilder()
                        .appendFixedSignedDecimal(DateTimeFieldType.weekyear(), 4) // ES change, was .appendWeekyear(4, 9)
                        .toFormatter();
            }
            return we;
        }

        private static DateTimeFormatter weekElement() {
            if (wwe == null) {
                return new DateTimeFormatterBuilder()
                        .appendLiteral("-W")
                        .appendFixedSignedDecimal(DateTimeFieldType.weekOfWeekyear(), 2) // ES change, was .appendWeekOfWeekyear(2)
                        .toFormatter();
            }
            return wwe;
        }

        private static DateTimeFormatter dayOfWeekElement() {
            if (dwe == null) {
                return new DateTimeFormatterBuilder()
                        .appendLiteral('-')
                        .appendDayOfWeek(1)
                        .toFormatter();
            }
            return dwe;
        }

        private static DateTimeFormatter dayOfYearElement() {
            if (dye == null) {
                return new DateTimeFormatterBuilder()
                        .appendLiteral('-')
                        .appendFixedSignedDecimal(DateTimeFieldType.dayOfYear(), 3) // ES change, was .appendDayOfYear(3)
                        .toFormatter();
            }
            return dye;
        }

        private static DateTimeFormatter literalTElement() {
            if (lte == null) {
                return new DateTimeFormatterBuilder()
                        .appendLiteral('T')
                        .toFormatter();
            }
            return lte;
        }

        private static DateTimeFormatter hourElement() {
            if (hde == null) {
                return new DateTimeFormatterBuilder()
                        .appendFixedSignedDecimal(DateTimeFieldType.hourOfDay(), 2) // ES change, was .appendHourOfDay(2)
                        .toFormatter();
            }
            return hde;
        }

        private static DateTimeFormatter minuteElement() {
            if (mhe == null) {
                return new DateTimeFormatterBuilder()
                        .appendLiteral(':')
                        .appendFixedSignedDecimal(DateTimeFieldType.minuteOfHour(), 2) // ES change, was .appendMinuteOfHour(2)
                        .toFormatter();
            }
            return mhe;
        }

        private static DateTimeFormatter secondElement() {
            if (sme == null) {
                return new DateTimeFormatterBuilder()
                        .appendLiteral(':')
                        .appendFixedSignedDecimal(DateTimeFieldType.secondOfMinute(), 2) // ES change, was .appendSecondOfMinute(2)
                        .toFormatter();
            }
            return sme;
        }

        private static DateTimeFormatter fractionElement() {
            if (fse == null) {
                return new DateTimeFormatterBuilder()
                        .appendLiteral('.')
                        // Support parsing up to nanosecond precision even though
                        // those extra digits will be dropped.
                        .appendFractionOfSecond(3, 9)
                        .toFormatter();
            }
            return fse;
        }

        private static DateTimeFormatter offsetElement() {
            if (ze == null) {
                return new DateTimeFormatterBuilder()
                        .appendTimeZoneOffset("Z", true, 2, 4)
                        .toFormatter();
            }
            return ze;
        }

    }

}
