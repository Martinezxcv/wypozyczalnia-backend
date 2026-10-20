package projekt.wypozyczalnia.services;

import java.time.LocalDate;
import java.util.Map;

public interface ReportService {

    byte[] generateMonthlyReportAsPDF(LocalDate startOfMonth, LocalDate endOfMonth);
}
