package gmailgdogra;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExtractOfficers {

    public static Set<Officer> from(List<TransactionRecord> transactionRecords) {
        return transactionRecords.stream()
                .map(transactionRecord -> new Officer(transactionRecord.getFirstName(),
                        transactionRecord.getLastName()))
                .collect(Collectors.toSet());
    }
}
