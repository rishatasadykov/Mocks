package Mock;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import Mock.PaymentServiceSharedTest.StubPaymentDAO;

public class ClassToTestWithMocks3 {

  /**
   * COVER WITH TESTS THIS METHOD
   *
   * @param from
   * @param to
   * @param amount
   * @throws Exception
   */
  public void sendPayment(final Account from, final Account to, final int
          amount) throws
          Exception {
    this.getPaymentDAO().transferPayment(from, to, amount);
    this.getPaymentDAO().updateAccounts(from, to, amount);
    String body = "<html><body>Payment to " + to.getName() + " " +
            "succeeded. <br/>Amount: " + String.valueOf(amount)
            + "</body></html>";
    sendMail(from.getMail(), to.getMail(), "Payment succeeded", body);
  }

  /**================ INTERNAL CLASSES AND METHODS ====================*/

  private PaymentDAO paymentDAO = new PaymentDAOImpl();



  public PaymentDAO getPaymentDAO() {
	return paymentDAO;
  }

  public void setPaymentDAO(PaymentDAO paymentDAO) {
	this.paymentDAO = paymentDAO;
  }

  public static class PaymentDAOImpl implements PaymentDAO {

    public void updateAccounts(final Account from, final Account to, final int
            amount) throws Exception {

      synchronized (this) {
        from.balance -= amount;
        to.balance += amount;
      }

    }

    public void transferPayment(final Account from, final Account to,
                                final int amount) throws SQLException,
            ClassNotFoundException {
      try {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        String url = "jdbc:derby:db111111;create=true";
        Connection con = DriverManager.getConnection(url);
        Statement stmt = con.createStatement();
        stmt.executeUpdate("UPDATE accounts SET balance = :balance WHERE id =" +
                        " :id");
        stmt.executeUpdate("SOME OTHER QUERIES UPDATING DB");
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }
  }

  public void sendMail(final String from, final String to, final String
          subject, final String body) throws Exception {

    validateSite();

    System.out.println("===== Emulate mail sending =====");
    System.out.println("From: " + from);
    System.out.println("To: " + to);
    System.out.println("Subject: " + subject);
    System.out.println("---------------------------");
    System.out.println("Body: \n\r" + body);
    System.out.println("================================");
    System.out.println();

  }

  private void validateSite() throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL
            ("http://google.com").openConnection();
    connection.setRequestMethod("HEAD");
    int responseCode = connection.getResponseCode();
    if (responseCode != 200) {
      throw new IllegalStateException("Couldn't ping site");
    }
  }

  public static void main(final String[] args) throws Exception {
    Account account1 = new Account();
    account1.setName("John");
    account1.setMail("John.Smith@gmail.com");
    account1.setBalance(100);
    Account account2 = new Account();
    account2.setName("Mike");
    account2.setMail("Mike.Tester@gmail.com");
    account2.setBalance(100);

    ClassToTestWithMocks3 paymentService = new ClassToTestWithMocks3();
    paymentService.setPaymentDAO(new PaymentDAOImpl());
//    paymentService.sendPayment(account1, account2, 10);

//    paymentService.sendPayment(account2, account1, 3000);
    System.out.println(account2.getBalance());
  }
}
