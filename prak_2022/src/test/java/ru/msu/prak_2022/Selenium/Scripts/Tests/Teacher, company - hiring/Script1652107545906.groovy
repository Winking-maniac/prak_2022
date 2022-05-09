import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory as CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as MobileBuiltInKeywords
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testcase.TestCaseFactory as TestCaseFactory
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testdata.TestDataFactory as TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository as ObjectRepository
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WSBuiltInKeywords
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUiBuiltInKeywords
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.testobject.SelectorMethod

import com.thoughtworks.selenium.Selenium
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.WebDriver
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium
import static org.junit.Assert.*
import java.util.regex.Pattern
import static org.apache.commons.lang3.StringUtils.join
import org.testng.asserts.SoftAssert
import com.kms.katalon.core.testdata.CSVData
import org.openqa.selenium.Keys as Keys

SoftAssert softAssertion = new SoftAssert();
WebUI.openBrowser('https://www.google.com/')
def driver = DriverFactory.getWebDriver()
String baseUrl = "https://www.google.com/"
selenium = new WebDriverBackedSelenium(driver, baseUrl)
selenium.open("http://localhost:8080/")
selenium.click("link=>9B8")
selenium.type("name=username", "test_teacher")
selenium.type("name=password", "test_pass")
selenium.click("xpath=//button[@type='submit']")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test_teacher'])[1]/following::button[1]")
softAssertion.assertEquals("0 40==K9 <><5=B ?@83;0H5=89 =5B", selenium.getText("xpath=(.//*[normalize-space(text()) and normalize-space(.)='59AB28O'])[1]/following::td[1]"))
softAssertion.assertEquals("><?0=88 =5 =0945=K", selenium.getText("xpath=(.//*[normalize-space(text()) and normalize-space(.)='59AB28O'])[2]/following::td[1]"))
selenium.click("link=K9B8")
selenium.click("link=>9B8")
selenium.click("name=username")
selenium.type("name=username", "test_company")
selenium.type("name=password", "test_pass")
selenium.click("xpath=//button[@type='submit']")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test_company'])[1]/following::button[1]")
softAssertion.assertEquals("5B >B?@02;5==KE ?@83;0H5=89", selenium.getText("xpath=(.//*[normalize-space(text()) and normalize-space(.)='59AB28O'])[1]/following::td[1]"))
softAssertion.assertEquals("@5?>4020B5;8 =5 =0945=K", selenium.getText("xpath=(.//*[normalize-space(text()) and normalize-space(.)='59AB28O'])[2]/following::td[1]"))
selenium.click("link=@5?>4020B5;8")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='bb bb bb'])[1]/following::span[1]")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test test test'])[1]/following::button[1]")
selenium.click("link=>O AB@0=8F0")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test_company'])[1]/following::button[1]")
softAssertion.assertEquals("test test test", selenium.getText("xpath=(.//*[normalize-space(text()) and normalize-space(.)='59AB28O'])[1]/following::span[1]"))
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test test test'])[1]/following::button[1]")
softAssertion.assertEquals("5B >B?@02;5==KE ?@83;0H5=89", selenium.getText("xpath=(.//*[normalize-space(text()) and normalize-space(.)='59AB28O'])[1]/following::td[1]"))
selenium.click("link=@5?>4020B5;8")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='bb bb bb'])[1]/following::span[1]")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test test test'])[1]/following::button[1]")
selenium.click("link=K9B8")
selenium.click("link=>9B8")
selenium.click("name=username")
selenium.type("name=username", "test_teacher")
selenium.type("name=password", "test_pass")
selenium.click("xpath=//button[@type='submit'")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test_teacher'])[1]/following::button[1]")
softAssertion.assertEquals("Test company", selenium.getText("xpath=(.//*[normalize-space(text()) and normalize-space(.)='59AB28O'])[1]/following::span[1]"))
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test address'])[1]/following::button[1]")
softAssertion.assertEquals("Test company", selenium.getText("xpath=(.//*[normalize-space(text()) and normalize-space(.)='59AB28O'])[2]/following::span[1]"))
selenium.click("link=K9B8")
selenium.click("link=>9B8")
selenium.click("name=username")
selenium.type("name=username", "test_company")
selenium.type("name=password", "test_pass")
selenium.click("xpath=//button[@type='submit']")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test_company'])[1]/following::button[1]")
softAssertion.assertEquals("test test test", selenium.getText("xpath=(.//*[normalize-space(text()) and normalize-space(.)='59AB28O'])[2]/following::span[1]"))
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test test test'])[1]/following::button[1]")
softAssertion.assertEquals("@5?>4020B5;8 =5 =0945=K", selenium.getText("xpath=(.//*[normalize-space(text()) and normalize-space(.)='59AB28O'])[2]/following::td[1]"))
selenium.click("link=@5?>4020B5;8")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='bb bb bb'])[1]/following::span[1]")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test test test'])[1]/following::button[1]")
selenium.click("link=K9B8")
selenium.click("link=>9B8")
selenium.click("name=username")
selenium.type("name=username", "test_teacher")
selenium.type("name=password", "test_pass")
selenium.click("xpath=//button[@type='submit']")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test_teacher'])[1]/following::button[1]")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test address'])[1]/following::button[1]")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test address'])[1]/following::button[1]")
softAssertion.assertEquals("><?0=88 =5 =0945=K", selenium.getText("xpath=(.//*[normalize-space(text()) and normalize-space(.)='59AB28O'])[2]/following::td[1]"))
selenium.click("link=K9B8")
selenium.click("link=>9B8")
selenium.click("name=username")
selenium.type("name=username", "test_company")
selenium.type("name=password", "test_pass")
selenium.click("xpath=//button[@type='submit']")
selenium.click("link=@5?>4020B5;8")
selenium.click("link=@5?>4020B5;8")
selenium.click("link=@5?>4020B5;8")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='bb bb bb'])[1]/following::span[1]")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test test test'])[1]/following::button[1]")
selenium.click("link=K9B8")
selenium.click("link=>9B8")
selenium.click("name=username")
selenium.type("name=username", "test_teacher")
selenium.type("name=password", "test_pass")
selenium.click("xpath=//button[@type='submit']")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test_teacher'])[1]/following::button[1]")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='@8=OBL'])[1]/following::button[1]")
softAssertion.assertEquals("0 40==K9 <><5=B ?@83;0H5=89 =5B", selenium.getText("xpath=(.//*[normalize-space(text()) and normalize-space(.)='59AB28O'])[1]/following::td[1]"))
selenium.click("link=K9B8")
selenium.click("link=>9B8")
selenium.click("name=username")
selenium.type("name=username", "test_company")
selenium.type("name=password", "test_pass")
selenium.click("xpath=//button[@type='submit']")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test_company'])[1]/following::button[1]")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='59AB28O'])[1]/following::td[1]")
softAssertion.assertEquals("5B >B?@02;5==KE ?@83;0H5=89", selenium.getText("xpath=(.//*[normalize-space(text()) and normalize-space(.)='59AB28O'])[1]/following::td[1]"))
selenium.click("link=K9B8")
