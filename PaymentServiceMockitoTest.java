package Mock;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.mockito.PowerMockito.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PaymentServiceMockitoTest.class)
public class PaymentServiceMockitoTest {
	@Test
	   public void testSendPayment() throws Exception {
		URL url = mock(URL.class);
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        whenNew(URL.class).withAnyArguments().thenReturn(url);
        when(url.openConnection()).thenReturn(mockConnection);
        when(mockConnection.getResponseCode()).thenReturn(505);
		
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
	   public void testSendPaymentVerify() throws Exception{
	    PaymentService service = new PaymentService();
	    service.setPaymentDAO(new StubPaymentDAO());

	    URL url = mock(URL.class);
	    HttpURLConnection mockConnection = mock(HttpURLConnection.class);

	    whenNew(URL.class).withAnyArguments().thenReturn(url);
	    when(url.openConnection()).thenReturn(mockConnection);
	    when(mockConnection.getResponseCode()).thenReturn(200);

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
	    verify(mockConnection, atLeast(2)).getResponseCode();
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
