package com.persado.oss.quality.stevia.selenium.core.controllers.factories;

/*
 * #%L
 * Stevia QA Framework - Core
 * %%
 * Copyright (C) 2013 - 2014 Persado
 * %%
 * Copyright (c) Persado Intellectual Property Limited. All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *  
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *  
 * * Neither the name of the Persado Intellectual Property Limited nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import com.persado.oss.quality.stevia.selenium.core.SteviaContext;
import com.persado.oss.quality.stevia.selenium.core.WebController;
import com.persado.oss.quality.stevia.selenium.core.controllers.WebDriverWebController;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WebDriverWebControllerFactoryImpl implements WebControllerFactory {
	  private static final Logger LOG = LoggerFactory.getLogger(WebDriverWebControllerFactoryImpl.class);

	  public WebController initialize(ApplicationContext context, WebController controller) {
	    WebDriverWebController wdController = (WebDriverWebController)controller;
	    WebDriver driver = null;
	    if (SteviaContext.getParam("debugging").compareTo("true") == 0) {
	      if (SteviaContext.getParam("browser") == null || SteviaContext.getParam("browser").compareTo("firefox") == 0 || SteviaContext.getParam("browser").isEmpty()) {
	        LOG.info("Debug enabled, using Firefox Driver");
	        FirefoxDriver firefoxDriver = new FirefoxDriver();
	      } else if (SteviaContext.getParam("browser").compareTo("chrome") == 0) {
	        LOG.info("Debug enabled, using ChromeDriver");
	        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
	        ChromeOptions options = new ChromeOptions();
	        options.addArguments(new String[] { "start-maximized" });
	        options.addArguments(new String[] { "test-type" });
	        if (SteviaContext.getParam("headless").compareTo("true") == 0) {
	          LOG.info("*** Chrome is running headless.. ***");
	          options.addArguments(new String[] { "headless" });
	          options.addArguments(new String[] { "silent" });
	          options.addArguments(new String[] { "window-size=1920,1200" });
	        } 
	        Map<String, Object> chromePrefs = new HashMap<>();
	        chromePrefs.put("download.default_directory", System.getProperty("user.dir") + File.separator + "target");
	        options.setExperimentalOption("prefs", chromePrefs);
	        capabilities.setCapability("goog:chromeOptions", options);
	        options.merge((Capabilities)capabilities);
	        ChromeDriver chromedriver = new ChromeDriver(options);
	        driver = chromedriver;
	      } else if (SteviaContext.getParam("browser").compareTo("iexplorer") == 0) {
	        LOG.info("Debug enabled, using InternetExplorerDriver");
	        InternetExplorerDriver internetExplorerDriver = new InternetExplorerDriver();
	      } else if (SteviaContext.getParam("browser").compareTo("safari") == 0) {
	        LOG.info("Debug enabled, using SafariDriver");
	        SafariDriver safariDriver = new SafariDriver();
	      } else {
	        throw new IllegalArgumentException("Wrong value for 'browser' parameter was defined");
	      } 
	    } else {
	      DesiredCapabilities capability = new DesiredCapabilities();
	      if (SteviaContext.getParam("browser") == null || SteviaContext.getParam("browser").compareTo("firefox") == 0 || SteviaContext.getParam("browser").isEmpty()) {
	        LOG.info("Debug OFF, using a RemoteWebDriver with Firefox capabilities");
	        capability = DesiredCapabilities.firefox();
	      } else if (SteviaContext.getParam("browser").compareTo("chrome") == 0) {
	        LOG.info("Debug OFF, using a RemoteWebDriver with Chrome capabilities");
	        capability = DesiredCapabilities.chrome();
	        ChromeOptions options = new ChromeOptions();
	        options.addArguments(new String[] { "start-maximized" });
	        options.addArguments(new String[] { "test-type" });
	        capability.setCapability("goog:chromeOptions", options);
	      } else if (SteviaContext.getParam("browser").compareTo("iexplorer") == 0) {
	        LOG.info("Debug OFF, using a RemoteWebDriver with Internet Explorer capabilities");
	        capability = DesiredCapabilities.internetExplorer();
	      } else if (SteviaContext.getParam("browser").compareTo("safari") == 0) {
	        LOG.info("Debug OFF, using a RemoteWebDriver with Safari capabilities");
	        capability = DesiredCapabilities.safari();
	      } else if (SteviaContext.getParam("browser").compareTo("opera") == 0) {
	        LOG.info("Debug OFF, using a RemoteWebDriver with Opera capabilities");
	        capability = DesiredCapabilities.opera();
	      } else {
	        throw new IllegalArgumentException("Wrong value for 'browser' parameter was defined");
	      } 
	      Augmenter augmenter = new Augmenter();
	      try {
	        driver = augmenter.augment((WebDriver)new RemoteWebDriver(new URL("http://" + SteviaContext.getParam("rcHost") + ":" + SteviaContext.getParam("rcPort") + "/wd/hub"), (Capabilities)capability));
	      } catch (MalformedURLException e) {
	        throw new IllegalArgumentException(e.getMessage(), e);
	      } 
	    } 
	    if (SteviaContext.getParam("targetHostUrl") != null)
	      driver.get(SteviaContext.getParam("targetHostUrl")); 
	    wdController.setDriver(driver);
	    if (SteviaContext.getParam("actionsLogging").compareTo("true") == 0)
	      wdController.enableActionsLogging(); 
	    return (WebController)wdController;
	  }

	  public String getBeanName() {
	    return "webDriverController";
	  }
	}