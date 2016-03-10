package Mock;

import java.sql.SQLException;

public interface PaymentDAO {

  void transferPayment(final Account from, final Account to,
                       final int amount) throws SQLException,
    ClassNotFoundException;

  void updateAccounts(Account from, Account to, int amount) throws Exception;


}