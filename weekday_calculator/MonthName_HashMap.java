/*********************************************************************************
 *                            Name : Saranya Balasubramaniyan
 *							  
 * month_name function is implemented. This returns the Month name for the corresponding 
 * integer for month number
 *********************************************************************************/

import java.util.HashMap;
public class MonthName_HashMap
{
	private static HashMap<Integer, String> monthMap = new HashMap<>();
	/**
	 * The method returns the name of the month corresponding to the 
	 * integer passed as an input to the method
	 * @param month
	 * @return name of the month
	 */
	public static String month_name( int month )
	{
		String result = "error";
		if(monthMap.containsKey(month))
		{
			result = monthMap.get(month);
		}
		return result;
	}

	/**
	 * The method updates the map with the month number and the corresponding month name
	 */
	private static void loadMonthMap()
	{
		monthMap.put(1, "January");
		monthMap.put(2, "February");
		monthMap.put(3, "March");
		monthMap.put(4, "April");
		monthMap.put(5, "May");
		monthMap.put(6, "June");
		monthMap.put(7, "July");
		monthMap.put(8, "August");
		monthMap.put(9, "September");
		monthMap.put(10, "October");
		monthMap.put(11, "November");
		monthMap.put(12, "December");
	}

	public static void main( String[] args )
	{
		loadMonthMap();
		System.out.println( "Month 1: " + month_name(1) );
		System.out.println( "Month 2: " + month_name(2) );
		System.out.println( "Month 3: " + month_name(3) );
		System.out.println( "Month 4: " + month_name(4) );
		System.out.println( "Month 5: " + month_name(5) );
		System.out.println( "Month 6: " + month_name(6) );
		System.out.println( "Month 7: " + month_name(7) );
		System.out.println( "Month 8: " + month_name(8) );
		System.out.println( "Month 9: " + month_name(9) );
		System.out.println( "Month 10: " + month_name(10) );
		System.out.println( "Month 11: " + month_name(11) );
		System.out.println( "Month 12: " + month_name(12) );
		System.out.println( "Month 43: " + month_name(43) );
	}
}