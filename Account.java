package Mock;

public class Account {

  protected String name;
  protected int balance;
  protected String mail;

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public int getBalance() {
    return balance;
  }

  public void setBalance(final int balance) {
    this.balance = balance;
  }

  public String getMail() {
    return mail;
  }

  public void setMail(final String mail) {
    this.mail = mail;
  }
}