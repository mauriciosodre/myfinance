package br.com.msodrej.myfinance.domain.usecase;

import br.com.msodrej.myfinance.domain.enums.PaymentMethod;
import br.com.msodrej.myfinance.domain.enums.TransactionType;
import br.com.msodrej.myfinance.domain.model.Transaction;
import br.com.msodrej.myfinance.domain.model.TransactionDetails;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionExcelParser {

    private final CategoryUseCase categoryUseCase;

    public List<Transaction> parse(MultipartFile file) throws IOException {
        List<Transaction> transactions = new ArrayList<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                Transaction transaction = new Transaction();
                TransactionDetails details = new TransactionDetails();

                try {
                    // Amount (Column 0)
                    transaction.setAmount(new BigDecimal(row.getCell(0).getNumericCellValue()));

                    // Description (Column 1)
                    transaction.setDescription(row.getCell(1).getStringCellValue());

                    // Date (Column 2)
                    String dateStr = row.getCell(2).getStringCellValue();
                    transaction.setDate(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                    // Type (Column 3)
                    String typeStr = row.getCell(3).getStringCellValue().toUpperCase();
                    transaction.setType(TransactionType.valueOf(typeStr));

                    // Category ID (Column 4, optional)
                    if (row.getCell(4) != null && !row.getCell(5).getStringCellValue().isEmpty()) {
                        var categoryName = row.getCell(5).getStringCellValue();
                        try {
                            var category = categoryUseCase.findByName(categoryName);
                            transaction.setCategory(category);
                        }catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }

                    // Payment Method (Column 5, optional for expenses)
                    if (row.getCell(5) != null && !row.getCell(6).getStringCellValue().isBlank()) {
                        String paymentMethodStr = row.getCell(6).getStringCellValue().toUpperCase();
                        details.setPaymentMethod(PaymentMethod.valueOf(paymentMethodStr));
                    }

                    // Income Source (Column 6, optional for income)
                    if (row.getCell(6) != null && !row.getCell(7).getStringCellValue().isBlank()) {
                        details.setIncomeSource(row.getCell(7).getStringCellValue());
                    }

                    transaction.setDetails(details);
                    transactions.add(transaction);

                } catch (Exception e) {
                    // If any parsing or validation fails, skip this row and let the controller handle errors
                    continue;
                }
            }
        }
        return transactions;
    }
}