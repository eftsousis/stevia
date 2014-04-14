package com.persado.oss.quality.stevia.tests;

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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.testng.annotations.Test;

import com.persado.oss.quality.stevia.annotations.Postconditions;
import com.persado.oss.quality.stevia.annotations.Preconditions;
import com.persado.oss.quality.stevia.annotations.RunsWithController;
import com.persado.oss.quality.stevia.pageObjects.GoogleHomePage;
import com.persado.oss.quality.stevia.pageObjects.PersadoHomePage;
import com.persado.oss.quality.stevia.selenium.core.SteviaContext;
import com.persado.oss.quality.stevia.selenium.core.controllers.SeleniumWebController;
import com.persado.oss.quality.stevia.selenium.core.controllers.WebDriverWebController;
import com.persado.oss.quality.stevia.spring.SteviaTestBase;

@Component
public class TestGoogleSearch extends SteviaTestBase {

	private static Logger LOG = LoggerFactory.getLogger(TestGoogleSearch.class);
	@Autowired
	GoogleHomePage googleHome;

	@Autowired
	PersadoHomePage persadoHome;

	/**
	 * this test checks loading of sizzle css and verification of contains
	 */
	@Test
	public void searchPersadoInGoogle() {
		commonControllerTest();
	}

	
	
	@RunsWithController(SeleniumWebController.class) 
	@Preconditions(controller=WebDriverWebController.class, value = {"precondition2" })
	@Test
	public void testWithBothAnnotations() {
		LOG.info("Should run with SELENIUM");
		Assert.isTrue(SteviaContext.isWebDriver() != true, "this controller is not Selenium");

		commonControllerTest();
	}

	
	
	@Preconditions( { "precondition1", "precondition2" })
	@Postconditions( { "postCondition1" } )
	@Test(dependsOnMethods= {"testWithBothAnnotations"})
	public void testExecutionOfPreconditions() {	
		//this test should run with webdriver
		Assert.isTrue(SteviaContext.isWebDriver() == true, "this controller is not WebDriver");
		LOG.info("TEST METHOD CODE");
	}
	
	public void precondition1() {
		LOG.info("TEST precondition1 CODE");

	}
	
	public void precondition2() {
		LOG.info("TEST precondition2 CODE");
		commonControllerTest();
	}

	public void postCondition1() {
		LOG.info("TEST postCondition1 CODE");
		commonControllerTest();
	}
	
	@Preconditions(controller=SeleniumWebController.class, value = {"precondition2" })
	@Test(dependsOnMethods= {"searchPersadoInGoogle","testExecutionOfPreconditions"})
	public void lastTestWithOtherController() {
		LOG.info("Should run in WEB DRIVER, precondition in SELENIUM mode");
		Assert.isTrue(SteviaContext.isWebDriver() == true, "this controller is not WebDriver");

	}
	

	@RunsWithController(SeleniumWebController.class) 
	@Test(dependsOnMethods= {"lastTestWithOtherController"})
	public void lastMethodWithDifferentController() {
		LOG.info("Should run with SELENIUM");
		Assert.isTrue(SteviaContext.isWebDriver() != true, "this controller is not Selenium");

		commonControllerTest();
	}

	/**
	 * this method contains Stevia - API code that runs with any controller;
	 * in this test it runs with WebDriver in {@link TestGoogleSearch##searchPersadoInGoogle()} 
	 * and Selenium {@link TestGoogleSearch#lastMethodWithDifferentController()}
	 */
	private void commonControllerTest() {
		SteviaContext.getWebController().navigate("http://www.google.com");
		googleHome.inputSearchText("persado");
		googleHome.controller().pressLinkNameAndWaitForPageToLoad("Persado | Global leader in Marketing Language Engineering");
		persadoHome.checkPersadoTitle();
	}
	
}
