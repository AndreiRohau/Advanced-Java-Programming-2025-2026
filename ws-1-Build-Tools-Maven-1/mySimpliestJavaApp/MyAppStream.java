import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

public class MyAppStream {
    	
    	private static final Logger log = Logger.getLogger(MyAppStream.class.getName());

	public static void main(String[] args) {
		log.info("Enter a date to count how much days left to it (ex. format: 2021-11-21)");
		
		Scanner scanner = new Scanner(System.in);
		String inputValue = scanner.next();
		log.info("inputValue=[" + inputValue + "].");

		LocalDate currentDate = LocalDateTime.now().toLocalDate();
		LocalDate inputDate = LocalDate.parse(inputValue);
		log.info("currentDate=[" + currentDate + "]. inputDate=[" + inputDate+ "].");

		long daysLeft = ChronoUnit.DAYS.between(currentDate, inputDate);
		log.info("Difference in days is =[" + daysLeft + "] days.");
	}
}