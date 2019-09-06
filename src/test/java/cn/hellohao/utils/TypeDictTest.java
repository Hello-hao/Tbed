package cn.hellohao.utils;

import cn.hellohao.utils.TypeDict;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;

public class TypeDictTest {

  @Rule public final ExpectedException thrown = ExpectedException.none();

  @Rule public final Timeout globalTimeout = new Timeout(10000);

  // Test written by Diffblue Cover.
  @Test
  public void checkTypeInputNotNullOutputNotNull() {

    // Act and Assert result
    Assert.assertEquals("0000", TypeDict.checkType(""));
  }

  // Test written by Diffblue Cover.
  @Test
  public void checkTypeInputNotNullOutputNotNull2() {

    // Act and Assert result
    Assert.assertEquals("0000", TypeDict.checkType("\uc666\u5f34"));
  }

  // Test written by Diffblue Cover.
  @Test
  public void checkTypeInputNotNullOutputNotNull3() {

    // Act and Assert result
    Assert.assertEquals("0000", TypeDict.checkType("\u6310\ucfb0\u3553\u11d8\u0594"));
  }

  // Test written by Diffblue Cover.
  @Test
  public void checkTypeInputNotNullOutputNotNull4() {

    // Act and Assert result
    Assert.assertEquals("0000", TypeDict.checkType("\uc9b5\u0690\u949d\u0d0c"));
  }

  // Test written by Diffblue Cover.
  @Test
  public void checkTypeInputNotNullOutputNotNull5() {

    // Act and Assert result
    Assert.assertEquals("bmp", TypeDict.checkType("424D"));
  }

  // Test written by Diffblue Cover.
  @Test
  public void checkTypeInputNotNullOutputNotNull6() {

    // Act and Assert result
    Assert.assertEquals("0000", TypeDict.checkType("\ud925\u0387\u4580\u6feb"));
  }

  // Test written by Diffblue Cover.
  @Test
  public void checkTypeInputNotNullOutputNotNull7() {

    // Act and Assert result
    Assert.assertEquals("0000", TypeDict.checkType("\u7241\u6920\u6490\ufffe\u0001"));
  }

  // Test written by Diffblue Cover.
  @Test
  public void checkTypeInputNotNullOutputNotNull8() {

    // Act and Assert result
    Assert.assertEquals("jif", TypeDict.checkType("474946"));
  }

  // Test written by Diffblue Cover.
  @Test
  public void checkTypeInputNotNullOutputNotNull9() {

    // Act and Assert result
    Assert.assertEquals("gif", TypeDict.checkType("47494638"));
  }

  // Test written by Diffblue Cover.
  @Test
  public void checkTypeInputNotNullOutputNotNull10() {

    // Act and Assert result
    Assert.assertEquals("png", TypeDict.checkType("89504E"));
  }

  // Test written by Diffblue Cover.
  @Test
  public void checkTypeInputNotNullOutputNotNull11() {

    // Act and Assert result
    Assert.assertEquals("jpg", TypeDict.checkType("FFD8FF"));
  }
}
