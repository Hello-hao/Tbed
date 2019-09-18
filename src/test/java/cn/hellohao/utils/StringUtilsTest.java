package cn.hellohao.utils;

import cn.hellohao.pojo.Keys;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class StringUtilsTest {

  @Rule public final ExpectedException thrown = ExpectedException.none();

  @Test
  public void concatDomainAndPathInputNotNullNotNullOutputNotNull() {

    // Arrange
    final String domain = "'";
    final String path = "foo";

    // Act
    final String actual = StringUtils.concatDomainAndPath(domain, path);

    // Assert result
    Assert.assertEquals("'/foo", actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void concatDomainAndPathInputNotNullNotNullOutputNotNull2() {

    // Arrange
    final String domain =
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000//";
    final String path = "\u0000?";

    // Act
    final String actual = StringUtils.concatDomainAndPath(domain, path);

    // Assert result
    Assert.assertEquals(
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000/\u0000?",
        actual);
  }

  // Test written by Diffblue Cover.

  @Test
  public void concatDomainAndPathInputNotNullNotNullOutputStringIndexOutOfBoundsException() {

    // Arrange
    final String domain = "/";
    final String path = "/";

    // Act
    thrown.expect(StringIndexOutOfBoundsException.class);
    StringUtils.concatDomainAndPath(domain, path);

    // The method is not expected to return due to exception thrown
  }

  // Test written by Diffblue Cover.

  @Test
  public void concatDomainAndPathInputNotNullNotNullOutputStringIndexOutOfBoundsException2() {

    // Arrange
    final String domain = "/";
    final String path = "foo";

    // Act
    thrown.expect(StringIndexOutOfBoundsException.class);
    StringUtils.concatDomainAndPath(domain, path);

    // The method is not expected to return due to exception thrown
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse() {

    // Arrange
    final Keys k = new Keys();

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(2, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse2() {

    // Arrange
    final Keys k = new Keys();

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(3, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse3() {

    // Arrange
    final Keys k = new Keys();

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(6, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse4() {

    // Arrange
    final Keys k = new Keys();

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(15, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse5() {

    // Arrange
    final Keys k = new Keys();

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(4, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse6() {

    // Arrange
    final Keys k = new Keys();

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(7, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse7() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint("");
    k.setStorageType(null);
    k.setRequestAddress("");
    k.setAccessSecret(null);
    k.setBucketname(null);
    k.setId(0);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(7, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse8() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint(null);
    k.setStorageType(null);
    k.setRequestAddress("");
    k.setAccessSecret("");
    k.setBucketname(null);
    k.setId(0);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(1, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse9() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint("");
    k.setStorageType(null);
    k.setRequestAddress("");
    k.setAccessSecret("");
    k.setBucketname(null);
    k.setId(0);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(1, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse10() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint("");
    k.setStorageType(null);
    k.setRequestAddress(null);
    k.setAccessSecret("");
    k.setBucketname("");
    k.setId(0);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(1, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse11() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint("");
    k.setStorageType(null);
    k.setRequestAddress(null);
    k.setAccessSecret(null);
    k.setBucketname("");
    k.setId(0);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(3, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse12() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint("");
    k.setStorageType(null);
    k.setRequestAddress(null);
    k.setAccessSecret("");
    k.setBucketname(null);
    k.setId(0);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(3, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse13() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint("");
    k.setStorageType(null);
    k.setRequestAddress(null);
    k.setAccessSecret("");
    k.setBucketname("");
    k.setId(0);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(3, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse14() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint("");
    k.setStorageType(null);
    k.setRequestAddress(null);
    k.setAccessSecret("");
    k.setBucketname("");
    k.setId(0);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(7, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse15() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint("");
    k.setStorageType(null);
    k.setRequestAddress("");
    k.setAccessSecret("");
    k.setBucketname("");
    k.setId(0);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(7, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse16() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint(
        "\ue82e\ue82e\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7\uecc7");
    k.setStorageType(null);
    k.setRequestAddress(
        "\ufffe\ufffd\ufffd\uffdc\ufffe\uffff\ufffc\u0001\u0003\ufeff\ufffe\ufbff\ufffe\u0003\ubfff\uffff");
    k.setAccessSecret(null);
    k.setBucketname("");
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(6, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse17() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint(
        "\ue82e\ue82e\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7");
    k.setStorageType(null);
    k.setRequestAddress(
        "\ufffe\uffff\ufffd\ufffc\ufffe\uffff\ufffc\u0001\u0003\ufeff\ufffe\uffff\ufffe\u0003\ubfff\uffff");
    k.setAccessSecret("\u4ff9\u4ff9\u4ff9\u4ff9");
    k.setBucketname(null);
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(6, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse18() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint(
        "\ue82e\ue82e\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7\ue4c7");
    k.setStorageType(null);
    k.setRequestAddress(null);
    k.setAccessSecret("\u4ff9\u4ff9\u4ff9\u4ff9");
    k.setBucketname("");
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(6, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse19() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint("");
    k.setStorageType(null);
    k.setRequestAddress(
        "\ufffe\uf593\u713d\u33a0\ub112\uffff\u3530\u0001\u0003\u710b\uf52e\ud1bb\ub5a6\u0003\ub597\ub7f3");
    k.setAccessSecret("\u4ff9\u4ff9\u4ff9\u4ff9");
    k.setBucketname("");
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(6, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse20() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint("");
    k.setStorageType(null);
    k.setRequestAddress(
        "\ufffe\u7593\u611d\u3380\ub110\uffff\u2110\u0001\u0003\u610b\uf526\ud133\u2522\u0003\ub515\ub7d1");
    k.setAccessSecret(null);
    k.setBucketname("");
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(4, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse21() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint("\ue82e");
    k.setStorageType(null);
    k.setRequestAddress(
        "\ufffe\u6513\u6009\u2380\ub100\uffff\u2000\u0001\u0003\u6103\uf126\uc123\u2522\u0003\ua514\u3281");
    k.setAccessSecret("");
    k.setBucketname("");
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(6, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse22() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint(
        "\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e");
    k.setStorageType(null);
    k.setRequestAddress(
        "\ufffe\u609b\uc109\ua600\u3480\uffff\u0108\u0001\u0003\uc483\u5166\u8043\u2522\u0003\u0096\u9709");
    k.setAccessSecret("\u4ffc");
    k.setBucketname("");
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(6, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse23() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint(
        "\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e");
    k.setStorageType(null);
    k.setRequestAddress(
        "\ufffe\u4093\u0101\u8480\u3080\uffff\u0000\u0001\u0003\u8003\u1146A\u01a2\u0003\u0084\ub581");
    k.setAccessSecret(null);
    k.setBucketname("");
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(2, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse24() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint(
        "\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e");
    k.setStorageType(null);
    k.setRequestAddress(
        "\ufffe\u4093\u0001\u8080\u3080\uffff\u0000\u0001\u0003\u9003\u1000A\u00a2\u0003\u0084\ub080");
    k.setAccessSecret("\u4ffc");
    k.setBucketname(null);
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(2, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse25() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint(
        "\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e");
    k.setStorageType(null);
    k.setRequestAddress(
        "\ufffe\u6093\u0081\u4000\u3080\uffff\u0000\u0001\u0003\u1003\u0000\u2041\u0002\u0003\u0080\u1000");
    k.setAccessSecret("\u4ffc");
    k.setBucketname(null);
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(4, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse26() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint(
        "\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e\ue82e");
    k.setStorageType(null);
    k.setRequestAddress(null);
    k.setAccessSecret("\u4ffc");
    k.setBucketname("");
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(4, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse27() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint("");
    k.setStorageType(null);
    k.setRequestAddress(
        "\ufffe\u0881\u5011\u4c50\u9e94\uffff\u1d94\u0001\u0003\u1e0b\u2454\u0410\ud8a6\u0003\u0e14\u0804");
    k.setAccessSecret("\u4ffc");
    k.setBucketname("");
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(4, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse28() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint("");
    k.setStorageType(null);
    k.setRequestAddress(
        "\ufffe\u0881\u5811\u4800\u9c94\uffff\u1d94\u0001\u0003\u1c0b\u2454\u0000\ud806\u0003\u0e14\u0804");
    k.setAccessSecret("\u4ffc");
    k.setBucketname("");
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(2, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse29() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint("\u582e");
    k.setStorageType(null);
    k.setRequestAddress(
        "\ufffe\u0001\u0811\u4000\u9c94\uffff\u1d94\u0001\u0003\u0c0b\u2050\u0000\u8002\u0003\u0610\u0800");
    k.setAccessSecret("");
    k.setBucketname("");
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(4, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse30() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint("");
    k.setStorageType(null);
    k.setRequestAddress(
        "\ufffe\u0081\u0011\u4000\u1994\uffff\u1194\u0001\u0003\u0c0b\u0094\u0404\u8002\u0003\u0504\u0c04");
    k.setAccessSecret(
        "\u4ffc\u4ffc\u4ffc\u4ffc\u4ffc\u4ffc\u4ffc\u4ffc\u4ffc\u4ffc\u4ffc\u4ffc\u4ffc\u4ffc\u4ffc\u4ffc");
    k.setBucketname("");
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(3, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse31() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey(null);
    k.setEndpoint("");
    k.setStorageType(null);
    k.setRequestAddress("                                ");
    k.setAccessSecret("((((((((((((((((((((((((((((((((");
    k.setBucketname("                                ");
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(1, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse32() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey(null);
    k.setEndpoint("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    k.setStorageType(null);
    k.setRequestAddress("                                ");
    k.setAccessSecret("");
    k.setBucketname("                                ");
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(1, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse33() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey(null);
    k.setEndpoint("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    k.setStorageType(null);
    k.setRequestAddress("                                ");
    k.setAccessSecret("");
    k.setBucketname("                                ");
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(2, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputFalse34() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey(null);
    k.setEndpoint("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    k.setStorageType(null);
    k.setRequestAddress("                                ");
    k.setAccessSecret("");
    k.setBucketname("                                ");
    k.setId(null);

    // Act and Assert result
    Assert.assertFalse(StringUtils.doNull(3, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputTrue() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey("");
    k.setEndpoint(
        "\ue82e\ue82e\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7\u1ff7");
    k.setStorageType(null);
    k.setRequestAddress(
        "\ufffe\ufffd\ufffd\uffdc\ufffe\uffff\ufffc\u0001\u0003\ufffb\ufffe\ufffd\ufffe\u0003\uffff\uffff");
    k.setAccessSecret("\u57f9\u57f9\u57f9\u57f9");
    k.setBucketname("");
    k.setId(null);

    // Act and Assert result
    Assert.assertTrue(StringUtils.doNull(7, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputTrue2() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey(null);
    k.setEndpoint("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    k.setStorageType(null);
    k.setRequestAddress("                                ");
    k.setAccessSecret("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    k.setBucketname("00000000000000000000000000000000");
    k.setId(null);

    // Act and Assert result
    Assert.assertTrue(StringUtils.doNull(1, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputTrue3() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey(null);
    k.setEndpoint("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    k.setStorageType(null);
    k.setRequestAddress("                                ");
    k.setAccessSecret("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    k.setBucketname("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    k.setId(null);

    // Act and Assert result
    Assert.assertTrue(StringUtils.doNull(3, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void doNullInputPositiveNotNullOutputTrue4() {

    // Arrange
    final Keys k = new Keys();
    k.setAccessKey(null);
    k.setEndpoint("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    k.setStorageType(null);
    k.setRequestAddress("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    k.setAccessSecret("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    k.setBucketname(" ");
    k.setId(null);

    // Act and Assert result
    Assert.assertTrue(StringUtils.doNull(2, k));
  }

  // Test written by Diffblue Cover.
  @Test
  public void removeFirstSeparatorInputNotNullOutputNotNull() {

    // Act and Assert result
    Assert.assertEquals("foo", StringUtils.removeFirstSeparator("foo"));
  }

  // Test written by Diffblue Cover.
  @Test
  public void removeFirstSeparatorInputNotNullOutputNotNull2() {

    // Act and Assert result
    Assert.assertEquals("", StringUtils.removeFirstSeparator("/"));
  }

  // Test written by Diffblue Cover.
  @Test
  public void removeFirstSeparatorInputNotNullOutputNotNull3() {

    // Act and Assert result
    Assert.assertEquals("", StringUtils.removeFirstSeparator(""));
  }

  // Test written by Diffblue Cover.
  @Test
  public void removeLastSeparatorInputNotNullOutputNotNull() {

    // Act and Assert result
    Assert.assertEquals("", StringUtils.removeLastSeparator("/"));
  }

  // Test written by Diffblue Cover.
  @Test
  public void removeLastSeparatorInputNotNullOutputNotNull2() {

    // Act and Assert result
    Assert.assertEquals("", StringUtils.removeLastSeparator(""));
  }
}
