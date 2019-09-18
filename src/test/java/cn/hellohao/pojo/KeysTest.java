package cn.hellohao.pojo;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class KeysTest {

  @Rule public final ExpectedException thrown = ExpectedException.none();

  // Test written by Diffblue Cover.
  @Test
  public void constructorInputPositiveNotNullNotNullNotNullNotNullNotNullPositiveOutputNotNull() {

    // Act, creating object to test constructor
    final Keys actual = new Keys(2, "/", "foo", "/", "/", "/", 2);

    // Assert result
    Assert.assertNotNull(actual);
    Assert.assertEquals("/", actual.getAccessKey());
    Assert.assertEquals("/", actual.getEndpoint());
    Assert.assertEquals(new Integer(2), actual.getStorageType());
    Assert.assertEquals("/", actual.getRequestAddress());
    Assert.assertEquals("foo", actual.getAccessSecret());
    Assert.assertEquals("/", actual.getBucketname());
    Assert.assertEquals(new Integer(2), actual.getId());
  }

  // Test written by Diffblue Cover.
  @Test
  public void constructorOutputNotNull() {

    // Act, creating object to test constructor
    final Keys actual = new Keys();

    // Assert result
    Assert.assertNotNull(actual);
    Assert.assertNull(actual.getAccessKey());
    Assert.assertNull(actual.getEndpoint());
    Assert.assertNull(actual.getStorageType());
    Assert.assertNull(actual.getRequestAddress());
    Assert.assertNull(actual.getAccessSecret());
    Assert.assertNull(actual.getBucketname());
    Assert.assertNull(actual.getId());
  }

  // Test written by Diffblue Cover.
  @Test
  public void getAccessKeyOutputNull() {

    // Arrange
    final Keys keys = new Keys();

    // Act and Assert result
    Assert.assertNull(keys.getAccessKey());
  }

  // Test written by Diffblue Cover.
  @Test
  public void getAccessSecretOutputNull() {

    // Arrange
    final Keys keys = new Keys();

    // Act and Assert result
    Assert.assertNull(keys.getAccessSecret());
  }

  // Test written by Diffblue Cover.
  @Test
  public void getBucketnameOutputNull() {

    // Arrange
    final Keys keys = new Keys();

    // Act and Assert result
    Assert.assertNull(keys.getBucketname());
  }

  // Test written by Diffblue Cover.
  @Test
  public void getEndpointOutputNull() {

    // Arrange
    final Keys keys = new Keys();

    // Act and Assert result
    Assert.assertNull(keys.getEndpoint());
  }

  // Test written by Diffblue Cover.
  @Test
  public void getIdOutputNull() {

    // Arrange
    final Keys keys = new Keys();

    // Act and Assert result
    Assert.assertNull(keys.getId());
  }

  // Test written by Diffblue Cover.
  @Test
  public void getRequestAddressOutputNull() {

    // Arrange
    final Keys keys = new Keys();

    // Act and Assert result
    Assert.assertNull(keys.getRequestAddress());
  }

  // Test written by Diffblue Cover.
  @Test
  public void getStorageTypeOutputNull() {

    // Arrange
    final Keys keys = new Keys();

    // Act and Assert result
    Assert.assertNull(keys.getStorageType());
  }

  // Test written by Diffblue Cover.
  @Test
  public void setAccessKeyInputNotNullOutputVoid() {

    // Arrange
    final Keys keys = new Keys();

    // Act
    keys.setAccessKey("/");

    // Assert side effects
    Assert.assertEquals("/", keys.getAccessKey());
  }

  // Test written by Diffblue Cover.
  @Test
  public void setAccessSecretInputNotNullOutputVoid() {

    // Arrange
    final Keys keys = new Keys();

    // Act
    keys.setAccessSecret("/");

    // Assert side effects
    Assert.assertEquals("/", keys.getAccessSecret());
  }

  // Test written by Diffblue Cover.
  @Test
  public void setBucketnameInputNotNullOutputVoid() {

    // Arrange
    final Keys keys = new Keys();

    // Act
    keys.setBucketname("/");

    // Assert side effects
    Assert.assertEquals("/", keys.getBucketname());
  }

  // Test written by Diffblue Cover.
  @Test
  public void setEndpointInputNotNullOutputVoid() {

    // Arrange
    final Keys keys = new Keys();

    // Act
    keys.setEndpoint("/");

    // Assert side effects
    Assert.assertEquals("/", keys.getEndpoint());
  }

  // Test written by Diffblue Cover.
  @Test
  public void setIdInputPositiveOutputVoid() {

    // Arrange
    final Keys keys = new Keys();

    // Act
    keys.setId(2);

    // Assert side effects
    Assert.assertEquals(new Integer(2), keys.getId());
  }

  // Test written by Diffblue Cover.
  @Test
  public void setRequestAddressInputNotNullOutputVoid() {

    // Arrange
    final Keys keys = new Keys();

    // Act
    keys.setRequestAddress("/");

    // Assert side effects
    Assert.assertEquals("/", keys.getRequestAddress());
  }

  // Test written by Diffblue Cover.
  @Test
  public void setStorageTypeInputPositiveOutputVoid() {

    // Arrange
    final Keys keys = new Keys();

    // Act
    keys.setStorageType(2);

    // Assert side effects
    Assert.assertEquals(new Integer(2), keys.getStorageType());
  }
}
