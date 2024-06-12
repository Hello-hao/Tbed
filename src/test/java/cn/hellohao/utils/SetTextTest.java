package cn.hellohao.utils;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;

public class SetTextTest {

  @Rule public final ExpectedException thrown = ExpectedException.none();

  // Test written by Diffblue Cover.
  @Test
  public void getSubStringInputNotNullNotNullNotNullOutputNotNull() {

    // Act and Assert result
    Assert.assertEquals("", SetText.getSubString("foo", "foo", "foo"));
  }

  // Test written by Diffblue Cover.
  @Test
  public void getSubStringInputNotNullNotNullNotNullOutputNotNull2() {

    // Act and Assert result
    Assert.assertEquals(
        "\uffef\uffef\uffef\uffef\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee",
        SetText.getSubString(
            "\uffef\uffef\uffef\uffef\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee",
            "\u0000", ""));
  }

  // Test written by Diffblue Cover.
  @Test
  public void getSubStringInputNotNullNullNotNullOutputNotNull() {

    // Act and Assert result
    Assert.assertEquals(
        "\ucfec\ucec9\ucec9\u46c0\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee",
        SetText.getSubString(
            "\ucfec\ucec9\ucec9\u46c0\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee",
            null,
            "\b\b\b\b\b\b\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\u8018\u8018\u8018\u8018\u8018\u8018\u8018\u8018\u8018\u8018\u8018\u8018\u8018\u8018\u8018"));
  }

  // Test written by Diffblue Cover.
  @Test
  public void getSubStringInputNotNullNullNotNullOutputNotNull2() {

    // Act and Assert result
    Assert.assertEquals(
        "\ucfec\ucec9\ucec9\u46c0\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee",
        SetText.getSubString(
            "\ucfec\ucec9\ucec9\u46c0\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee\uffee",
            null, ""));
  }

  // Test written by Diffblue Cover.
  @Test
  public void getSubStringInputNotNullNullNotNullOutputNotNull3() {

    // Act and Assert result
    Assert.assertEquals("!!!!!!!!!!!!!!!!", SetText.getSubString("!!!!!!!!!!!!!!!! ", null, " "));
  }
}
