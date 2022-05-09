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
selenium.click("name=username")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='@5?>4020B5;8'])[1]/following::div[2]")
selenium.click("name=password")
selenium.type("name=password", "test_pass")
selenium.type("name=username", "test_company")
selenium.click("xpath=//button[@type='submit']")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test_course'])[1]/following::button[1]")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='@5?>4020B5;8'])[2]/following::button[1]")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test test test'])[1]/following::button[1]")
selenium.click("link=K9B8")
selenium.click("link=>9B8")
selenium.click("name=username")
selenium.click("name=username")
selenium.type("name=username", "test_teacher")
selenium.type("name=password", "test_pass")
selenium.click("name=password")
selenium.click("xpath=//button[@type='submit']")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='59AB28O'])[1]/following::span[1]")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test_course'])[1]/following::button[1]")
selenium.click("xpath=(.//*[normalize-space(text()) and normalize-space(.)='test_course'])[1]/following::button[1]")
selenium.click("id=teacher")
selenium.click("id=validation_passwd")
selenium.setText("id=validation_passwd", "2022-05-09T13:38")
selenium.click("id=validation_passwd_confirm")
selenium.setText("id=validation_passwd_confirm", "2022-05-09T14:38")
selenium.click("id=validationTextarea")
selenium.type("id=validationTextarea", ("---").toString())
selenium.click("xpath=//form[@action='/create_lesson']")
selenium.click("xpath=//button[@type='submit']")
selenium.click("link=K9B8")
