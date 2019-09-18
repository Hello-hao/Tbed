package cn.hellohao.utils;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

public class ImgUrlUtilTest {

  @Rule public final ExpectedException thrown = ExpectedException.none();

  // Test written by Diffblue Cover.
  @Test
  public void bytesToHexStringInput0OutputNull() {

    // Arrange
    final byte[] src = {};

    // Act and Assert result
    Assert.assertNull(ImgUrlUtil.bytesToHexString(src));
  }

  // Test written by Diffblue Cover.
  @Test
  public void bytesToHexStringInput1OutputNotNull() {

    // Arrange
    final byte[] src = {(byte)9};

    // Act and Assert result
    Assert.assertEquals("09", ImgUrlUtil.bytesToHexString(src));
  }

  // Test written by Diffblue Cover.
  @Test
  public void bytesToHexStringInput2OutputNotNull() {

    // Arrange
    final byte[] src = {(byte)-1, (byte)12};

    // Act and Assert result
    Assert.assertEquals("ff0c", ImgUrlUtil.bytesToHexString(src));
  }

  // Test written by Diffblue Cover.
  @Test
  public void bytesToHexStringInputNullOutputNull() {

    // Act and Assert result
    Assert.assertNull(ImgUrlUtil.bytesToHexString(null));
  }

  // Test written by Diffblue Cover.
  @Test
  public void getFileLengthInputNotNullOutputZero() throws IOException {

    // Act and Assert result
    Assert.assertEquals(0L, ImgUrlUtil.getFileLength("foo"));
  }

  // Test written by Diffblue Cover.
  @Test
  public void getFileLengthInputNotNullOutputZero2() throws IOException {

    // Act and Assert result
    Assert.assertEquals(0L, ImgUrlUtil.getFileLength(""));
  }
}
