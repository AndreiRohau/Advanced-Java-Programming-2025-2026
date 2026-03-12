package uz.itpu.workingWithTransactionsUsingJdbc;

public class MainTransactional {
    private static final boolean isIsolationApplied = true;

    public static void main(String[] args) {
        // Solution: READ COMMITTED, REPEATABLE READ, and SERIALIZABLE isolation levels
        dirtyReads(isIsolationApplied);
        // Solution: REPEATABLE READ and SERIALIZABLE isolation levels
        nonRepeatableReads(isIsolationApplied);
        // Solution: SERIALIZABLE isolation level
        phantomReads(isIsolationApplied);
    }
}
