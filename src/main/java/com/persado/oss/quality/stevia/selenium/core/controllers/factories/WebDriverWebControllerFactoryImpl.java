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
import com.persado.oss.quality.stevia.selenium.core.controllers.SteviaWebControllerFactory;
import com.persado.oss.quality.stevia.selenium.core.controllers.WebDriverWebController;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WebDriverWebControllerFactoryImpl implements WebControllerFactory {
    private static final Logger LOG = LoggerFactory.getLogger(WebDriverWebControllerFactoryImpl.class);
    @Override
    public WebController initialize(ApplicationContext context, WebController controller) {
        WebDriverWebController wdController = (WebDriverWebController) controller;
        WebDriver driver = null;
        /**
         * Capabilities are now fetched from Stevia Context.
         * Capabilities are stored in Stevia Context inside qa-tests project
         * using the overriden testInitialisation in WorkableTestBase
         */

        Capabilities capabilities = SteviaContext.getCapabilities();

        // Handle type of webdriver based on "remote" param
        System.out.println("Initializing web driver with capabilities:" + SteviaContext.getCapabilities());
        if (SteviaContext.getParam("remote").compareTo(SteviaWebControllerFactory.TRUE) == 0) {
            final String wdHost = SteviaContext.getParam("rcUrl");
            CompletableFuture<WebDriver> wd = CompletableFuture.supplyAsync(() -> getRemoteWebDriver(wdHost, capabilities));
            try {
                driver = wd.get(Integer.valueOf(SteviaContext.getParam("nodeTimeout")), TimeUnit.MINUTES);
            } catch (InterruptedException | ExecutionException e) {
                LOG.error(e.getMessage());
            } catch (TimeoutException e) {
                throw new RuntimeException("Timeout of " + Integer.valueOf(SteviaContext.getParam("nodeTimeout")) + " minutes reached waiting for a hub node to receive the request");
            }
            ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
        } else {
            driver = getLocalDriver(capabilities);
        }

        //Navigate to the desired target host url
        if (SteviaContext.getParam(SteviaWebControllerFactory.TARGET_HOST_URL) != null) {
            driver.get(SteviaContext.getParam(SteviaWebControllerFactory.TARGET_HOST_URL));
        }

        wdController.setDriver(driver);

        // Enable web driver actions logging
        if (SteviaContext.getParam(SteviaWebControllerFactory.ACTIONS_LOGGING).compareTo(SteviaWebControllerFactory.TRUE) == 0) {
            wdController.enableActionsLogging();
        }
        return wdController;
    }

    @Override
    public String getBeanName() {
        return "webDriverController";
    }

    private WebDriver getRemoteWebDriver(String rcUrl, Capabilities desiredCapabilities) {
        WebDriver driver;
        Augmenter augmenter = new Augmenter(); // adds screenshot capability to a default webdriver.
        try {
            driver = augmenter.augment(new RemoteWebDriver(new URL(rcUrl), desiredCapabilities));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        return driver;
    }

    private WebDriver getLocalDriver(Capabilities capabilities) {
        WebDriver driver;
        String browser = capabilities.getBrowserName();
        switch (browser) {
            case "chrome":
                driver = new ChromeDriver(capabilities);
                break;
            case "firefox":
                driver = new FirefoxDriver(capabilities);
                break;
            default:
                throw new IllegalStateException("Browser requested is invalid");
        }
        return driver;
    }
}
