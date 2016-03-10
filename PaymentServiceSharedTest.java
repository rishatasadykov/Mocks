package Mock;

import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;


public class PaymentServiceSharedTest {
  @Test
   public void testSendPayment() throws Exception {
    PaymentService service = new PaymentService();
    service.setPaymentDAO(new StubPaymentDAO());
    Account account1 = new Account();
    account1.setName("John");
    account1.setMail("John.Smith@gmail.com");
    account1.setBalance(100);
    Account account2 = new Account();
    account2.setName("Mike");
    account2.setMail("Mike.Tester@gmail.com");
    account2.setBalance(100);

    service.sendPayment(account1, account2, 100);

    //TODO: assert there was request to DAO
    //TODO: assert the mail was sent

    Assert.assertEquals(0, account1.getBalance());
    Assert.assertEquals(200, account2.getBalance());
  }

  @Test
  public void testSendNonExistingPayment() throws Exception {
    PaymentService service = new PaymentService();
    service.setPaymentDAO(new StubPaymentDAO());
    Account account1 = new Account();
    account1.setName("John");
    account1.setMail("John.Smith@gmail.com");
    account1.setBalance(100);
    Account account2 = new Account();
    account2.setName("Mike");
    account2.setMail("Mike.Tester@gmail.com");
    account2.setBalance(100);

    // account1 doesn't have such amount: 500
    service.sendPayment(account1, account2, 500);

    //TODO: assert mail was sent anyway

    Assert.assertEquals(100, account1.getBalance());
    Assert.assertEquals(100, account2.getBalance());
  }
  public static class StubPaymentDAO implements PaymentDAO{
	@Override
	public void transferPayment(Account from, Account to, int amount) throws SQLException, ClassNotFoundException {
		System.out.println("transfer performed");
	}

	@Override
	public void updateAccounts(Account from, Account to, int amount) {
		if (from.balance >= amount) {
			from.balance = from.balance - amount;
			to.balance = to.balance + amount;
		}
	}
  }
}
