package com.web.service;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

public class TestData {
	public static Service montrealService;
	public static Service sherbrookService;
	public static Service quebecService;
	public static final String CONFERENCES = "Conferences";
	public static final String SEMINARS = "Seminars";
	public static final String TRADE_SHOWS = "Trade Shows";

	public static void main(String[] args) throws Exception {
//
		URL montrealURL = new URL("http://localhost:8080/montreal?wsdl");
		QName montrealQName = new QName("http://implementaion.service.web.com/", "EventManagementService");
		montrealService = Service.create(montrealURL, montrealQName);

		URL quebecURL = new URL("http://localhost:8080/quebec?wsdl");
		QName quebecQName = new QName("http://implementaion.service.web.com/", "EventManagementService");
		quebecService = Service.create(quebecURL, quebecQName);

		URL sherbrookURL = new URL("http://localhost:8080/sherbrook?wsdl");
		QName sherbrookQName = new QName("http://implementaion.service.web.com/", "EventManagementService");
		sherbrookService = Service.create(sherbrookURL, sherbrookQName);

		addTestData();
	}

	private synchronized static void addTestData() {
		WebInterface MTLobj = montrealService.getPort(WebInterface.class);
		WebInterface QUEobj = quebecService.getPort(WebInterface.class);
		WebInterface SHEobj = sherbrookService.getPort(WebInterface.class);

		System.out.println("PreDemo TestCases");
		System.out.println("*********************************************************");

		System.out.println("Logged in as MTLM3456 MANAGER:");
		System.out.println(MTLobj.addEvent("MTLA080820", CONFERENCES, 2));
		System.out.println(MTLobj.addEvent("MTLM110820", SEMINARS, 1));
		System.out.println(MTLobj.addEvent("MTLM120820", SEMINARS, 1));

		System.out.println("Logged in as SHEM9000 MANAGER:");
		System.out.println(SHEobj.addEvent("SHEE080820", TRADE_SHOWS, 1));

		System.out.println("Logged in as QUEM9000 MANAGER:");
		System.out.println(QUEobj.addEvent("QUEA250820", SEMINARS, 1));
		System.out.println(QUEobj.addEvent("QUEE150820", TRADE_SHOWS, 1));

		System.out.println("Logged in as SHEC1234 CUSTOMER:");
		System.out.println(SHEobj.bookEvent("SHEC1234", "MTLA080820", CONFERENCES));
		System.out.println(SHEobj.bookEvent("SHEC1234", "MTLM110820", SEMINARS));
		System.out.println(SHEobj.bookEvent("SHEC1234", "QUEA250820", SEMINARS));
		System.out.println(SHEobj.bookEvent("SHEC1234", "SHEE080820", TRADE_SHOWS));

	}
}
