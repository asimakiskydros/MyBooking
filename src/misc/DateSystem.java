package misc;

import javax.swing.table.DefaultTableModel;
import java.io.Serializable;
import java.time.YearMonth;
import java.util.HashMap;

/**
 * DateSystem
 * When making a booking, the customer needs to specify two important dates: check-in and check-out
 * These two need to be comparable for programming and managing purposes
 * A calendar helps the user decide, which is updated through here
 */
public class DateSystem implements Serializable {
    //Days array where each index represents the month rank and its value the month's number of days
    int[] days = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    final private HashMap<String, Months> months;
    private int yearCode;
    private int monthCode;

    public DateSystem(){
        months = new HashMap<>();
        months.put("January", Months.JANUARY);
        months.put("February", Months.FEBRUARY);
        months.put("March", Months.MARCH);
        months.put("April", Months.APRIL);
        months.put("May", Months.MAY);
        months.put("June", Months.JUNE);
        months.put("July", Months.JULY);
        months.put("August", Months.AUGUST);
        months.put("September", Months.SEPTEMBER);
        months.put("October", Months.OCTOBER);
        months.put("November", Months.NOVEMBER);
        months.put("December", Months.DECEMBER);
    }
    public static final long serialVersionUID = 72L;
    /**
     * Calendar model getter
     * Calculates the correct format for the month and year given,
     * then feeds that info to a tableModel
     * @param year the year of the month the calendar is going to project
     * @param month the month the calendar is going to project
     * @return a DefaultTableModel with the correct dates for the month,year
     */
    public DefaultTableModel getCalendarModel(int year, String month){
        //create Days of the Week column template
        String[] daysOfWeek = {
                "Sun",
                "Mon",
                "Tue",
                "Wed",
                "Thu",
                "Fri",
                "Sat"
        };
        this.yearCode = year;
        this.monthCode = months.get(month).getCode();

        //check for leap year
        boolean isLeapYear = false;
        if(this.yearCode % 4 == 0) {
            isLeapYear = true;
            if (this.yearCode % 100 == 0) {
                isLeapYear = this.yearCode % 400 == 0;
            }
        }
        if(isLeapYear) days[2] = 29;

        //create empty "dates" template, as unused slots need to be filled but should also show nothing
        String[][] dates = {
                {"", "", "", "", "", "", ""},
                {"", "", "", "", "", "", ""},
                {"", "", "", "", "", "", ""},
                {"", "", "", "", "", "", ""},
                {"", "", "", "", "", "", ""},
                {"", "", "", "", "", "", ""}
        };

        int day = 1;
        int j = 0;
        //start giving dates from the index that corresponds to the first day of the week of the month in question
        int i = YearMonth.of(this.yearCode, this.monthCode).atDay(1).getDayOfWeek().getValue()%7;

        while(day <= this.days[this.monthCode]){
            //Build in Java dayOfTheWeek enums use Monday-->1, ..., Sunday-->7 logic
            //Therefore our "i" is value%7
            //Do it manually, as when it hits 7, the calendar needs to change line, too
            if(i == 7){
                i = 0;
                j++;
            }
            dates[j][i++] = ((Integer)day++).toString();
        }

        //revert temporary change
        days[2] = 28;

        return new DefaultTableModel(dates, daysOfWeek);
    }
    /**
     * Date codifier to integer
     * Reservation check-in and check-out dates need to be comparable
     * @param date the date of the month the customer wishes to check-in/out
     * @return the full date codified into an integer (YYYYMMDD)
     */
    public int getDateCode(int date){
        //return the full date info into a number format, so it's comparable
        //ie the 30th of November 2021 --> 30/11/2021 --> 20211130
        //year first, then month, then date, as a change in year is drastically more impactful
        //(comparably speaking) than a change in month or date
        return date + this.monthCode*100 + this.yearCode*10000;
    }
    /**
     * Enhanced date codifier
     * Same as above, but the month and year members are also overridden
     * @param date the date of the month
     * @param month the month of the check-in/out
     * @param year the year of the check-in/out
     * @return the full date codified into an integer (YYYYMMDD)
     */
    public int getDateCode(int date, int month, int year){
        this.monthCode = month;
        this.yearCode = year;
        return getDateCode(date);
    }
}
